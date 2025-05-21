package com.jcastillo.simulator.domain

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import java.math.BigDecimal
import java.math.RoundingMode

@Configuration
@ConfigurationProperties(prefix = "simulation")
class SimulationProperties {
    var setupTimeInDays: Int = 60
    var yearsAfterSetupUntilBreakdown: Int = 25
    var daysInYear: Int = 365
    var degradationFactor: Double = 0.005
    var baseKilowatts: Int = 20
    var calculationScale: Int = 10
    var resultScale: Int = 6
    var roundingMode: String = "HALF_UP"
    var sunLightPerHour: Double = 0.11415525
    var daysUtilBreakdown: Int = 9185

    fun getRoundingMode(): RoundingMode = RoundingMode.valueOf(roundingMode)

    // Converted values
    fun getYearsAfterSetupUntilBreakdown(): BigDecimal = yearsAfterSetupUntilBreakdown.toBigDecimal()
    fun getDegradationFactor(): BigDecimal = degradationFactor.toBigDecimal()
    fun getBaseKilowatts(): BigDecimal = baseKilowatts.toBigDecimal()
    fun getSunLightPerHour(): BigDecimal = sunLightPerHour.toBigDecimal()
}