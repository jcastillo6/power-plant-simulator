package com.jcastillo.simulator.domain

import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class CalculatorService(
    private val totalNetworkOutputCalculator: TotalNetworkOutputCalculator,
    private val powerOutputCalculator: PowerOutputCalculator
) {
    fun calculatePowerForAge(age: Int): BigDecimal =
        powerOutputCalculator.calculatePowerAndGet(age)

    fun calculateTotalOutput(days: Int, stats: PowerPlantStats): BigDecimal =
        totalNetworkOutputCalculator.calculateAndGetTotalOutput(days, stats)
}