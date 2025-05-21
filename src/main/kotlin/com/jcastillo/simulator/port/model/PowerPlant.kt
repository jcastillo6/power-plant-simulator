package com.jcastillo.simulator.port.model

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

/**
 * 
 * @param name 
 * @param age 
 */
data class PowerPlant(

    @get:Pattern(regexp="^[a-zA-Z0-9- ]+$")
    @get:Size(min=1,max=100)
    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("name", required = true) val name: kotlin.String,

    @get:Min(0)
    @get:Max(100000)
    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("age", required = true) val age: kotlin.Int
) {

}

