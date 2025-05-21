package com.jcastillo.simulator.domain

import org.springframework.stereotype.Component
import java.math.BigDecimal

private const val YEAR_DAYS = 365

@Component
class TotalNetworkOutputCalculator(private val simulationProperties: SimulationProperties) {

    /**
     * Calculates the total power output based on the formula:
     * baseKW * (plants - (degradationFactor/365) * (currentAges + plants * days)) * sunlightHours
     *
     * @param days Number of simulation days
     * @param powerPlantStats Current statistics of power plants
     * @return The calculated total output in kilowatts
     */
    fun calculateAndGetTotalOutput(days: Int, powerPlantStats: PowerPlantStats): BigDecimal {
        // (degradationFactor/365)
        val degradationPerDay = simulationProperties.getDegradationFactor()
            .divide(
                YEAR_DAYS.toBigDecimal(),
                simulationProperties.calculationScale,
                simulationProperties.getRoundingMode()
            )
        // (totalAges + numberOfPlants * days)
        val agingPeriod = powerPlantStats.numberOfPowerPlants.toBigDecimal()
            .multiply(days.toBigDecimal())
            .add(powerPlantStats.sumOfTotalAges.toBigDecimal())
        //  (degradationPerDay * agingPeriod)
        val degradationEffect = degradationPerDay.multiply(agingPeriod)

        // n - degradationEffect
        val plantsEfficiency = powerPlantStats.numberOfPowerPlants.toBigDecimal()
            .subtract(degradationEffect)

        // Calculate final output (baseKW * plantsEfficiency * sunlightHours)
        return simulationProperties.getBaseKilowatts()
            .multiply(plantsEfficiency)
            .multiply(simulationProperties.getSunLightPerHour())
    }
}