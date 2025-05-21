package com.jcastillo.simulator.port.model

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

/**
 * 
 * @param totalOutputInKwh Total power output in kilowatt-hours
 */
data class TotalOutputResponse(

    @Schema(example = "null", required = true, description = "Total power output in kilowatt-hours")
    @get:JsonProperty("total-output-in-kwh", required = true) val totalOutputInKwh: java.math.BigDecimal
) {

}

