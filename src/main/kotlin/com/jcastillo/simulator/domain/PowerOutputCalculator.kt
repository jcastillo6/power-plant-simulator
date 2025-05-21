package com.jcastillo.simulator.domain

import org.springframework.stereotype.Component
import java.math.BigDecimal

private const val YEAR_DAYS = 365

@Component
class PowerOutputCalculator(private val simulationProperties: SimulationProperties) {

    /**
     * Calculates the power output based on the formula:
     * 20 * (1 - (D/365 * 0.005)) * 0.11415525, or baseKW * (plants - (degradationFactor/365) * (currentAges + plants * days)) * sunlightHours
     * @param age Power plant age in years
     * @return The calculated power output in kilowatts
     */
    fun calculatePowerAndGet(age: Int): BigDecimal {
        if (!isAgeInValidRange(age, simulationProperties.setupTimeInDays, simulationProperties.daysUtilBreakdown)) {
            return BigDecimal.ZERO
        }
        // Calculate instantaneous power in kW: 20 * (1 - (D/365 * 0.005))
        val ageInYears = age.toBigDecimal()
            .divide(
                YEAR_DAYS.toBigDecimal(),
                simulationProperties.calculationScale,
                simulationProperties.getRoundingMode()
            )
        val degradation = ageInYears.multiply(simulationProperties.getDegradationFactor())
        val efficiencyFactor = BigDecimal.ONE.subtract(degradation)
        val instantaneousPower = simulationProperties.getBaseKilowatts().multiply(efficiencyFactor)

        return instantaneousPower.multiply(simulationProperties.getSunLightPerHour())
            .setScale(
                simulationProperties.resultScale,
                simulationProperties.getRoundingMode()
            )
    }

    private fun isAgeInValidRange(
        age: Int,
        setupTimeInDays: Int,
        maxValidAgeInDays: Int
    ): Boolean = age >= setupTimeInDays &&
            age < maxValidAgeInDays
}