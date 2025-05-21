package com.jcastillo.simulator.domain

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.test.assertEquals

class PowerOutputCalculatorTest {
    private lateinit var powerOutputCalculator: PowerOutputCalculator
    private val simulationProperties = SimulationProperties()

    @BeforeEach
    fun setUp() {
        powerOutputCalculator = PowerOutputCalculator(simulationProperties)
    }

    @ParameterizedTest
    @CsvSource(
        "523, 2.266748",
        "904, 2.254832"
    )
    fun `calculatePowerAndGet when age is valid should return the power calculation`(
        age: Int,
        expectedOutput: String
    ) {

        val power = powerOutputCalculator.calculatePowerAndGet(age)

        assertEquals(BigDecimal(expectedOutput), power.setScale(6, RoundingMode.DOWN))
    }

    @Test
    fun `calculatePowerAndGet should return zero when age is less than setup time`() {
        simulationProperties.setupTimeInDays = 100
        val power = powerOutputCalculator.calculatePowerAndGet(99)

        assertEquals(BigDecimal.ZERO, power)
    }

    @Test
    fun `calculatePowerAndGet should return zero when age equals or exceeds breakdown days`() {
        simulationProperties.daysUtilBreakdown = 9125
        val power = powerOutputCalculator.calculatePowerAndGet(9125)

        assertEquals(BigDecimal.ZERO, power)
    }

    @Test
    fun `calculatePowerAndGet should handle edge case at setup time exactly`() {
        simulationProperties.setupTimeInDays = 100
        val power = powerOutputCalculator.calculatePowerAndGet(100)

        assertTrue(power > BigDecimal.ZERO)
    }

    @Test
    fun `calculatePowerAndGet should handle edge case just before breakdown`() {
        simulationProperties.daysUtilBreakdown = 9125
        val power = powerOutputCalculator.calculatePowerAndGet(9124)

        assertTrue(power > BigDecimal.ZERO)
    }
}