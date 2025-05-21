package com.jcastillo.simulator.port.model

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid

/**
 * 
 * @param code 
 * @param message 
 * @param details 
 */
data class Error(

    @Schema(example = "null", description = "")
    @get:JsonProperty("code") val code: Error.Code? = null,

    @Schema(example = "null", description = "")
    @get:JsonProperty("message") val message: kotlin.String? = null,

    @field:Valid
    @Schema(example = "null", description = "")
    @get:JsonProperty("details") val details: kotlin.collections.Map<kotlin.String, kotlin.Any>? = null
) {

    /**
    * 
    * Values: vALIDATIONERROR,rATELIMITEXCEEDED,iNVALIDAPIKEY,rESOURCENOTFOUND,iNTERNALERROR
    */
    enum class Code(val value: kotlin.String) {

        @JsonProperty("VALIDATION_ERROR") vALIDATIONERROR("VALIDATION_ERROR"),
        @JsonProperty("RATE_LIMIT_EXCEEDED") rATELIMITEXCEEDED("RATE_LIMIT_EXCEEDED"),
        @JsonProperty("INVALID_API_KEY") iNVALIDAPIKEY("INVALID_API_KEY"),
        @JsonProperty("RESOURCE_NOT_FOUND") rESOURCENOTFOUND("RESOURCE_NOT_FOUND"),
        @JsonProperty("INTERNAL_ERROR") iNTERNALERROR("INTERNAL_ERROR")
    }

}

