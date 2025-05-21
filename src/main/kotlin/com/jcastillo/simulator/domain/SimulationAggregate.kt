package com.jcastillo.simulator.domain

import com.jcastillo.simulator.service.PowerPlantPersistenceService
import com.jcastillo.simulator.service.TotalNetworkPowerPersistenceService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

/**
 * This class is the simulation aggregate root. It's in charge of executing the simulation and
 * persisting the results.
 *
 * It's also in charge of calculating the power output for each power plant based on its age.
 *
 * The simulation is based on the following assumptions:
 * - The plant will produce a certain amount of power output per day based on its efficiency.
 */
@Component
class SimulationAggregate(
    private val powerPlantPersistenceService: PowerPlantPersistenceService,
    private val totalNetworkPowerPersistenceService: TotalNetworkPowerPersistenceService,
    private val calculatorService: CalculatorService,
) {
    private val log = LoggerFactory.getLogger(this::class.java)


    // TODO Change to queue to avoid blocking the service, or use reactive programming.
    @Transactional
    fun addAllPowerPlants(powerPlantCommands: List<CreatePowerPlantCommand>) {
        require(powerPlantCommands.isNotEmpty()) {
            "Nothing to add, empty list provided."
        }
        log.info("Adding ${powerPlantCommands.size} power plants")
        val powerByAge = mutableMapOf<Int, BigDecimal>()
        val powerPlants = powerPlantCommands.map { command ->
            val power = if (powerByAge.containsKey(command.age)) {
                powerByAge[command.age] ?: BigDecimal.ZERO
            } else {
                val power = calculatorService.calculatePowerForAge(command.age)
                powerByAge[command.age] = power
                power
            }
            PowerPlant(name = command.name, age = command.age, power)
        }

        publishPersistenceEvent(powerPlants)
    }

    //TODO the statistics need to be move to redis or other cache for faster access
    @Synchronized
    private fun publishPersistenceEvent(powerPlants: List<PowerPlant>) {
        powerPlantPersistenceService.upsertAll(powerPlants)
        powerPlantPersistenceService.getPowerPlantsStats()?.let { stats ->
            totalNetworkPowerPersistenceService.upsertNetworkPower(stats)
            log.info("Added ${powerPlants.size} power plants and updated network stats")
            log.debug("Network stats: {}", stats)
        } ?: {
            log.error("Error getting power plants stats")
        }
    }

    //TODO locking mechanism need to be improved dirty reads can happen
    @Synchronized
    @Transactional
    fun addAndExecuteSimulation(powerPlantCommands: List<CreatePowerPlantCommand>, days: Int): Simulation {
        addAllPowerPlants(powerPlantCommands)
        return executeSimulation(days)
    }

    /**
     * Executes the simulation for the given number of days
     * @param days Number of days to simulate exclusive the calculation will be made days -1
     * @return Simulation with the total output in kWh and a list of power plants with their respective output in kWh
     * TODO use pagination or streaming to avoid loading everything in memory
     */
    fun executeSimulation(days: Int): Simulation {
        var totalOutput = BigDecimal.ZERO
        val updatedPowerPlants = mutableListOf<PowerPlant>()
        val powerPlants: List<PowerPlant>
        log.info("Executing simulation for $days days")
        synchronized(this) {
            powerPlants = powerPlantPersistenceService.getAllPowerPlants()
        }
        log.debug("Found ${powerPlants.size} power plants")
        for (powerPlant in powerPlants) {
            val calculatedPower = if (days != 0) {
                calculatorService.calculatePowerForAge(days - 1 + powerPlant.age)
            } else {
                powerPlant.output
            }
            val powerPlantSimulation =
                PowerPlant(powerPlant.name, powerPlant.age + days, calculatedPower)
            updatedPowerPlants.add(powerPlantSimulation)
            totalOutput += powerPlantSimulation.output
        }
        return Simulation(totalOutput, updatedPowerPlants)
    }

    fun getTotalNetworkPower(days: Int) =
        totalNetworkPowerPersistenceService.getCurrentNetworkPower()?.let {
            calculatorService.calculateTotalOutput(days, it)
        } ?: BigDecimal.ZERO

}
