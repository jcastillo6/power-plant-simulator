package com.jcastillo.simulator.domain

import com.jcastillo.simulator.infrastructure.persistence.TotalNetworkPowerEntity
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class SimulationAggregateTest {
    private val powerPlantPersistenceService = mockk<com.jcastillo.simulator.service.PowerPlantPersistenceService>()
    private val totalNetworkPowerPersistenceService = mockk<com.jcastillo.simulator.service.TotalNetworkPowerPersistenceService>()
    private val calculatorService: CalculatorService = mockk()
    private val curatorFramework: org.apache.curator.framework.CuratorFramework = mockk()
    private lateinit var simulationAggregate: SimulationAggregate

    @BeforeEach
    fun setUp() {
        simulationAggregate = SimulationAggregate(powerPlantPersistenceService, totalNetworkPowerPersistenceService, calculatorService, curatorFramework)
    }

    @Test
    fun `addAllPowerPlants when the input is valid persist plants`() {
        val agePowerPlant1 = 60
        val expectedOutputPlant1 = BigDecimal("10.00")
        val commands = listOf(
            CreatePowerPlantCommand("Plant1", agePowerPlant1)
        )
        val powerPlantStats = PowerPlantStats(3L, 1327L)
        val plantsSlot = slot<List<PowerPlant>>()
        every { powerPlantPersistenceService.upsertAll(capture(plantsSlot)) } returns Unit
        every { calculatorService.calculatePowerForAge(agePowerPlant1) } returns expectedOutputPlant1
        every { powerPlantPersistenceService.getPowerPlantsStats() } returns powerPlantStats
        every { totalNetworkPowerPersistenceService.upsertNetworkPower(powerPlantStats) } returns TotalNetworkPowerEntity(totalAges = 1L, numberOfPowerPlants = 1L)


        simulationAggregate.addAllPowerPlants(commands)

        verify(exactly = 1) { powerPlantPersistenceService.upsertAll(any()) }
        verify(exactly = 1) { totalNetworkPowerPersistenceService.upsertNetworkPower(powerPlantStats) }
        verify(exactly = 1) { calculatorService.calculatePowerForAge(agePowerPlant1) }
        // Verify the captured plant
        val capturedPlants = plantsSlot.captured
        assertEquals(1, capturedPlants.size)

        // Verify Plant1
        val plant1 = capturedPlants.find { it.name == "Plant1" }
        assertNotNull(plant1)
        assertEquals(agePowerPlant1, plant1.age)
        assertEquals(expectedOutputPlant1, plant1.output.setScale(2, RoundingMode.DOWN))
    }

    @Test
    fun `executeSimulation should calculate correct power output`() {
        val days = 5
        val expectedAgeNotInclusive = 60 + days - 1
        val initialPlants = listOf(
            PowerPlant("Plant1", 60, BigDecimal("10.00")),
            PowerPlant("Plant2", 60, BigDecimal("9.50"))
        )
        every { powerPlantPersistenceService.getAllPowerPlants() } returns initialPlants
        every { calculatorService.calculatePowerForAge(expectedAgeNotInclusive) } returns BigDecimal("2.5")

        val simulation = simulationAggregate.executeSimulation(days)

        verify(exactly = 1) { powerPlantPersistenceService.getAllPowerPlants() }
        verify(exactly = 2) { calculatorService.calculatePowerForAge(expectedAgeNotInclusive) }

        assertNotNull(simulation)
        assertEquals(2, simulation.powerPlants.size)
        assertEquals(BigDecimal.valueOf(5.0), simulation.kwhOutput)
        assertEquals(simulation.kwhOutput, simulation.powerPlants.sumOf { it.output })
    }

    @Test
    fun `executeSimulation should skip calculation if days is 0`() {
        val days = 0
        val expectedAgeNotInclusive = 60 + days - 1
        val initialPlants = listOf(
            PowerPlant("Plant1", 60, BigDecimal("2.5")),
            PowerPlant("Plant2", 60, BigDecimal("2.5"))
        )
        every { powerPlantPersistenceService.getAllPowerPlants() } returns initialPlants

        val simulation = simulationAggregate.executeSimulation(days)

        verify(exactly = 1) { powerPlantPersistenceService.getAllPowerPlants() }
        verify(exactly = 0) { calculatorService.calculatePowerForAge(expectedAgeNotInclusive) }

        assertNotNull(simulation)
        assertEquals(2, simulation.powerPlants.size)
        assertEquals(BigDecimal.valueOf(5.0), simulation.kwhOutput)
        assertEquals(simulation.kwhOutput, simulation.powerPlants.sumOf { it.output })
    }

    @Test
    fun `executeSimulation with empty plant list should return zero output`() {
        every { powerPlantPersistenceService.getAllPowerPlants() } returns emptyList()

        val simulation = simulationAggregate.executeSimulation(5)

        assertEquals(BigDecimal.ZERO, simulation.kwhOutput)
        assertTrue(simulation.powerPlants.isEmpty())
    }

    @Test
    fun `addAndExecuteSimulation should add plants and return simulation results`() {
        val powerPlantStats = PowerPlantStats(3L, 1327L)
        val commands = listOf(
            CreatePowerPlantCommand("Plant1", 80),
            CreatePowerPlantCommand("Plant2", 80)
        )
        every { powerPlantPersistenceService.upsertAll(any()) } returns Unit
        every { powerPlantPersistenceService.getAllPowerPlants() } returns listOf(
            PowerPlant("Plant1", 80, BigDecimal("10.00")),
            PowerPlant("Plant2", 80, BigDecimal("9.50"))
        )
        every { calculatorService.calculatePowerForAge(80) } returns BigDecimal("2.1")
        every { calculatorService.calculatePowerForAge(84) } returns BigDecimal("2.5")
        every { powerPlantPersistenceService.getPowerPlantsStats() } returns powerPlantStats
        every { totalNetworkPowerPersistenceService.upsertNetworkPower(powerPlantStats) } returns TotalNetworkPowerEntity(totalAges = 1L, numberOfPowerPlants = 1L)


        val simulation = simulationAggregate.addAndExecuteSimulation(commands, 5)

        assertNotNull(simulation)
        assertEquals(2, simulation.powerPlants.size)
        assertTrue(simulation.kwhOutput > BigDecimal.ZERO)
    }

    @Test
    fun `addAllPowerPlants should handle empty command list`() {
        assertThrows<IllegalArgumentException> {
            simulationAggregate.addAllPowerPlants(emptyList())
        }
    }

    @Test
    fun `getTotalNetworkPower when called should retrieve the stats and call the calculatorService`() {
        val days = 5
        val numberOfPowerPlants = 2L
        val sumOfTotalAges = 1327L
        val powerPlantStats = PowerPlantStats(numberOfPowerPlants, sumOfTotalAges)
        every { totalNetworkPowerPersistenceService.getCurrentNetworkPower() } returns powerPlantStats
        every { calculatorService.calculateTotalOutput(days, powerPlantStats) } returns BigDecimal("10.00")

        simulationAggregate.getTotalNetworkPower(days)

        verify(exactly = 1) { totalNetworkPowerPersistenceService.getCurrentNetworkPower() }
        verify(exactly = 1) { calculatorService.calculateTotalOutput(days, powerPlantStats) }
    }

    @Test
    fun `getTotalNetworkPower when called with empty stats should return zero`() {
        val days = 5
        every { totalNetworkPowerPersistenceService.getCurrentNetworkPower() } returns null

        val totalOutput = simulationAggregate.getTotalNetworkPower(days)

        assertEquals(BigDecimal.ZERO, totalOutput)
        verify(exactly = 1) { totalNetworkPowerPersistenceService.getCurrentNetworkPower() }
        verify(exactly = 0) { calculatorService.calculateTotalOutput(days, any()) }
    }
}