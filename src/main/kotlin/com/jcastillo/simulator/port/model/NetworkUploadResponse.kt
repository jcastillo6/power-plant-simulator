package com.jcastillo.simulator.port.model

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid

/**
 * 
 * @param producedKwh Total power produced in kilowatt-hours
 * @param network 
 */
data class NetworkUploadResponse(

    @Schema(example = "null", description = "Total power produced in kilowatt-hours")
    @get:JsonProperty("produced-kwh") val producedKwh: java.math.BigDecimal? = null,

    @field:Valid
    @Schema(example = "null", description = "")
    @get:JsonProperty("network") val network: List<PowerPlant>? = null
) {

}

