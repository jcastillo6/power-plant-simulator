package com.jcastillo.simulator.domain

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.math.BigDecimal
import java.math.RoundingMode

class TotalNetworkOutputCalculatorTest {

    private lateinit var totalNetworkOutputCalculator: TotalNetworkOutputCalculator

    @BeforeEach
    fun setUp() {
        totalNetworkOutputCalculator = TotalNetworkOutputCalculator(SimulationProperties())
    }

    @ParameterizedTest
    @CsvSource(
        "0, 4.524707",
        "50, 4.521580"
    )
    fun `calculateAndGetTotalOutput should return correct output for different days`(
        days: Int,
        expectedOutput: String
    ) {
        val numberOfPowerPlants = 2L
        val sumOfTotalAges = 1327L

        val output = totalNetworkOutputCalculator.calculateAndGetTotalOutput(
            days,
            PowerPlantStats(numberOfPowerPlants, sumOfTotalAges)
        )

        assertEquals(
            BigDecimal(expectedOutput),
            output.setScale(6, RoundingMode.DOWN)
        )
    }
}