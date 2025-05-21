package com.jcastillo.simulator.adapter

import com.jcastillo.simulator.domain.CreatePowerPlantCommand
import com.jcastillo.simulator.domain.PowerPlant
import com.jcastillo.simulator.domain.Simulation
import com.jcastillo.simulator.domain.SimulationAggregate
import com.jcastillo.simulator.service.FileValidationResult
import com.jcastillo.simulator.service.FileValidator
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import kotlin.test.assertEquals

class SimulatorControllerTest {
    val fileValidator: FileValidator = mockk()
    val fileResource: org.springframework.core.io.Resource = mockk()
    val mapper: com.fasterxml.jackson.databind.ObjectMapper = mockk()
    val simulationAggregate: SimulationAggregate = mockk()

    val simulatorController = SimulatorController(simulationAggregate, fileValidator, mapper)

    @Test
    fun `uploadPowerPlantFile When File Is Not Valid Should throw InvalidFileFormatException`() {
        every { fileValidator.validate(fileResource) } returns FileValidationResult.Invalid("File is not valid")

        assertThrows<InvalidFileFormatException> { simulatorController.uploadPowerPlantFile(1, fileResource)  }

        verify(exactly = 1) { fileValidator.validate(fileResource) }
        verify { simulationAggregate wasNot Called }
    }

    @Test
    fun `uploadPowerPlantFile When File Is Valid Should Return Ok`() {
        val daysForSimulation = 1
        val powerPlantInput1 = com.jcastillo.simulator.port.model.PowerPlant("name1", 1)
        val powerPlantInput2 = com.jcastillo.simulator.port.model.PowerPlant("name2", 2)
        val powerPlant1 = PowerPlant("name1", 1, BigDecimal.ONE)
        val powerPlant2 = PowerPlant("name2", 2, BigDecimal.ONE)
        val createPlantCommands = listOf(CreatePowerPlantCommand(powerPlantInput1.name, powerPlantInput1.age),
            CreatePowerPlantCommand(powerPlantInput2.name, powerPlantInput2.age)
        )
        val inputStream = mockk<java.io.InputStream>()
        val totalPowerGeneration = BigDecimal.TWO
        every { fileResource.inputStream } returns inputStream
        every {
            mapper.readValue(
                any<java.io.InputStream>(),
                Array<com.jcastillo.simulator.port.model.PowerPlant>::class.java
            )
        } returns arrayOf (powerPlantInput1, powerPlantInput2)
        every { fileValidator.validate(fileResource) } returns FileValidationResult.Valid
        every { simulationAggregate.addAndExecuteSimulation(createPlantCommands, daysForSimulation ) } returns Simulation(totalPowerGeneration, listOf(powerPlant1, powerPlant2))

        val response = simulatorController.uploadPowerPlantFile(daysForSimulation, fileResource)

        assertTrue { response.statusCode.is2xxSuccessful }
        assertTrue { response.body != null }
        val body = response.body!!
        assertTrue { body.producedKwh == totalPowerGeneration }
        assertTrue { body.network == listOf(powerPlantInput1, powerPlantInput2) }
    }

    @Test
    fun `uploadPowerPlantFile When File Is Empty Should Return EmptyFileException`() {
        val inputStream = mockk<java.io.InputStream>()
        every { fileResource.inputStream } returns inputStream
        every { fileValidator.validate(fileResource) } returns FileValidationResult.Valid
        every {
            mapper.readValue(
                any<java.io.InputStream>(),
                Array<com.jcastillo.simulator.port.model.PowerPlant>::class.java
            )
        } returns arrayOf()

        assertThrows<EmptyFileException> {
            simulatorController.uploadPowerPlantFile(1, fileResource)
        }

        verify(exactly = 1) { fileValidator.validate(fileResource) }
        verify { simulationAggregate wasNot Called }
    }

    @Test
    fun `getPowerPlantSimulation When Days Is Valid Should Return Simulation`() {
        val days = 5
        val powerPlant1 = PowerPlant("name1", 1, BigDecimal.ONE)
        val powerPlant2 = PowerPlant("name2", 2, BigDecimal.ONE)
        val totalPowerGeneration = BigDecimal.TWO
        val simulation = Simulation(totalPowerGeneration, listOf(powerPlant1, powerPlant2))

        every { simulationAggregate.executeSimulation(days) } returns simulation

        val response = simulatorController.getNetworkStatus(days = days, null, null)

        assertTrue { response.statusCode.is2xxSuccessful }
        assertTrue { response.body != null }
        val body = response.body!!
        assertEquals( 2,body.size)
        assertTrue {
            body == listOf(
                com.jcastillo.simulator.port.model.PowerPlantOutput("name1", 1, BigDecimal.ONE),
                com.jcastillo.simulator.port.model.PowerPlantOutput("name2", 2, BigDecimal.ONE)
            )
        }
    }

    @Test
    fun `addPowerPlants When Plants List Is Valid Should Return Created`() {
        val powerPlantInput1 = com.jcastillo.simulator.port.model.PowerPlant("name1", 1)
        val powerPlantInput2 = com.jcastillo.simulator.port.model.PowerPlant("name2", 2)
        val createPlantCommands = listOf(
            CreatePowerPlantCommand(powerPlantInput1.name, powerPlantInput1.age),
            CreatePowerPlantCommand(powerPlantInput2.name, powerPlantInput2.age)
        )

        every { simulationAggregate.addAllPowerPlants(createPlantCommands) } returns Unit

        val response = simulatorController.loadPowerPlants(listOf(powerPlantInput1, powerPlantInput2))

        assertTrue { response.statusCode.is2xxSuccessful }
        verify(exactly = 1) { simulationAggregate.addAllPowerPlants(createPlantCommands) }
    }

    @Test
    fun `addPowerPlants When Plants List Is Empty Should Throw EmptyFileException`() {
        assertThrows<EmptyBodyException> {
            simulatorController.loadPowerPlants(emptyList())
        }

        verify { simulationAggregate wasNot Called }
    }

    @Test
    fun `getTotalOutput When Days Is Valid Should Return Total Power Output`() {
        val days = 5
        val powerPlant1 = PowerPlant("name1", 1, BigDecimal.ONE)
        val powerPlant2 = PowerPlant("name2", 2, BigDecimal.ONE)
        val totalPowerGeneration = BigDecimal.TWO
        val simulation = Simulation(totalPowerGeneration, listOf(powerPlant1, powerPlant2))

        every { simulationAggregate.executeSimulation(days) } returns simulation

        val response = simulatorController.getTotalOutput(days, null, null)

        assertTrue { response.statusCode.is2xxSuccessful }
        assertTrue { response.body != null }
        val body = response.body!!
        assertEquals(totalPowerGeneration, body.totalOutputInKwh)
    }

    @Test
    fun `getTotalOutput When Simulation Returns Zero Should Return Zero Total Output`() {
        val days = 5
        val simulation = Simulation(BigDecimal.ZERO, emptyList())

        every { simulationAggregate.executeSimulation(days) } returns simulation

        val response = simulatorController.getTotalOutput(days, null, null)

        assertTrue { response.statusCode.is2xxSuccessful }
        assertTrue { response.body != null }
        val body = response.body!!
        assertEquals(BigDecimal.ZERO, body.totalOutputInKwh)
    }
}