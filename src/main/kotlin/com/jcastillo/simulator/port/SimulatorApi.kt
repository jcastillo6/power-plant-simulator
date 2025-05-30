/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (7.3.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
*/
package com.jcastillo.simulator.port

import com.jcastillo.simulator.port.model.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Validated
@RequestMapping("\${api.base-path:/solar-simulator}")
interface SimulatorApi {

    fun getDelegate(): SimulatorApiDelegate = object: SimulatorApiDelegate {}

    @Operation(
        tags = ["Simulator",],
        summary = "Get network status at specific days",
        operationId = "getNetworkStatus",
        description = """Returns the state of all power plants in the network after specified number of days""",
        responses = [
            ApiResponse(responseCode = "200", description = "Successful operation", content = [Content(array = ArraySchema(schema = Schema(implementation = PowerPlantOutput::class)))]),
            ApiResponse(responseCode = "400", description = "Invalid JSON format or content", content = [Content(schema = Schema(implementation = Error::class))]),
            ApiResponse(responseCode = "401", description = "Missing API key or invalid authentication credentials", content = [Content(schema = Schema(implementation = Error::class))]),
            ApiResponse(responseCode = "403", description = "API key doesn't have sufficient permissions", content = [Content(schema = Schema(implementation = Error::class))]),
            ApiResponse(responseCode = "413", description = "Request payload too large", content = [Content(schema = Schema(implementation = Error::class))]),
            ApiResponse(responseCode = "429", description = "Too Many Requests"),
            ApiResponse(responseCode = "200", description = "Unexpected error", content = [Content(schema = Schema(implementation = Error::class))])
        ],
        security = [ SecurityRequirement(name = "ApiKeyAuth") ]
    )
    @RequestMapping(
            method = [RequestMethod.GET],
            value = ["/network/{days}"],
            produces = ["application/json"]
    )
    fun getNetworkStatus(@Min(0)@Parameter(description = "Number of days to calculate network state", required = true) @PathVariable("days") days: kotlin.Int,@Parameter(description = "ETag from a previous request. A 304 will be returned if resource hasn't changed", `in` = ParameterIn.HEADER) @RequestHeader(value = "If-None-Match", required = false) ifNoneMatch: kotlin.String?,@Parameter(description = "Timestamp of the last retrieved data", `in` = ParameterIn.HEADER) @RequestHeader(value = "If-Modified-Since", required = false) ifModifiedSince: java.time.OffsetDateTime?): ResponseEntity<List<PowerPlantOutput>> {
        return getDelegate().getNetworkStatus(days, ifNoneMatch, ifModifiedSince)
    }

    @Operation(
        tags = ["Simulator",],
        summary = "Get total power output",
        operationId = "getTotalOutput",
        description = """Calculate total power output for specified number of days""",
        responses = [
            ApiResponse(responseCode = "200", description = "Successful operation", content = [Content(schema = Schema(implementation = TotalOutputResponse::class))]),
            ApiResponse(responseCode = "304", description = "Not Modified - Resource hasn't changed since the ETag specified in If-None-Match", content = [Content(schema = Schema(implementation = Error::class))]),
            ApiResponse(responseCode = "400", description = "Invalid JSON format or content", content = [Content(schema = Schema(implementation = Error::class))]),
            ApiResponse(responseCode = "401", description = "Missing API key or invalid authentication credentials", content = [Content(schema = Schema(implementation = Error::class))]),
            ApiResponse(responseCode = "403", description = "API key doesn't have sufficient permissions", content = [Content(schema = Schema(implementation = Error::class))]),
            ApiResponse(responseCode = "413", description = "Request payload too large", content = [Content(schema = Schema(implementation = Error::class))]),
            ApiResponse(responseCode = "429", description = "Too Many Requests"),
            ApiResponse(responseCode = "200", description = "Unexpected error", content = [Content(schema = Schema(implementation = Error::class))])
        ],
        security = [ SecurityRequirement(name = "ApiKeyAuth") ]
    )
    @RequestMapping(
            method = [RequestMethod.GET],
            value = ["/output/{days}"],
            produces = ["application/json"]
    )
    fun getTotalOutput(@Min(0)@Parameter(description = "Number of days to calculate output", required = true) @PathVariable("days") days: kotlin.Int,@Parameter(description = "ETag from a previous request. A 304 will be returned if resource hasn't changed", `in` = ParameterIn.HEADER) @RequestHeader(value = "If-None-Match", required = false) ifNoneMatch: kotlin.String?,@Parameter(description = "Timestamp of the last retrieved data", `in` = ParameterIn.HEADER) @RequestHeader(value = "If-Modified-Since", required = false) ifModifiedSince: java.time.OffsetDateTime?): ResponseEntity<TotalOutputResponse> {
        return getDelegate().getTotalOutput(days, ifNoneMatch, ifModifiedSince)
    }

    @Operation(
        tags = ["Simulator",],
        summary = "Add a new power plants",
        operationId = "loadPowerPlants",
        description = """Add a new power plants as a json array""",
        responses = [
            ApiResponse(responseCode = "205", description = "Successful operation"),
            ApiResponse(responseCode = "400", description = "Invalid JSON format or content", content = [Content(schema = Schema(implementation = Error::class))]),
            ApiResponse(responseCode = "401", description = "Missing API key or invalid authentication credentials", content = [Content(schema = Schema(implementation = Error::class))]),
            ApiResponse(responseCode = "403", description = "API key doesn't have sufficient permissions", content = [Content(schema = Schema(implementation = Error::class))]),
            ApiResponse(responseCode = "413", description = "Request payload too large", content = [Content(schema = Schema(implementation = Error::class))]),
            ApiResponse(responseCode = "429", description = "Too Many Requests"),
            ApiResponse(responseCode = "200", description = "Unexpected error", content = [Content(schema = Schema(implementation = Error::class))])
        ],
        security = [ SecurityRequirement(name = "ApiKeyAuth") ]
    )
    @RequestMapping(
            method = [RequestMethod.POST],
            value = ["/load"],
            produces = ["application/json"],
            consumes = ["application/json"]
    )
    fun loadPowerPlants(@Parameter(description = "Create a new power plants request should be an array", required = true) @Valid @RequestBody powerPlant: kotlin.collections.List<PowerPlant>): ResponseEntity<Unit> {
        return getDelegate().loadPowerPlants(powerPlant)
    }

    @Operation(
        tags = ["Simulator",],
        summary = "Upload power plants",
        operationId = "uploadPowerPlantFile",
        description = """Upload power plant file""",
        responses = [
            ApiResponse(responseCode = "200", description = "Successful operation", content = [Content(schema = Schema(implementation = NetworkUploadResponse::class))]),
            ApiResponse(responseCode = "400", description = "Invalid JSON format or content", content = [Content(schema = Schema(implementation = Error::class))]),
            ApiResponse(responseCode = "401", description = "Missing API key or invalid authentication credentials", content = [Content(schema = Schema(implementation = Error::class))]),
            ApiResponse(responseCode = "403", description = "API key doesn't have sufficient permissions", content = [Content(schema = Schema(implementation = Error::class))]),
            ApiResponse(responseCode = "413", description = "Request payload too large", content = [Content(schema = Schema(implementation = Error::class))]),
            ApiResponse(responseCode = "429", description = "Too Many Requests"),
            ApiResponse(responseCode = "200", description = "Unexpected error", content = [Content(schema = Schema(implementation = Error::class))])
        ],
        security = [ SecurityRequirement(name = "ApiKeyAuth") ]
    )
    @RequestMapping(
            method = [RequestMethod.POST],
            value = ["/upload"],
            produces = ["application/json"],
            consumes = ["multipart/form-data"]
    )
    fun uploadPowerPlantFile(@Parameter(description = "number of days to perform the calculation", required = true) @RequestParam(value = "days", required = true) days: kotlin.Int ,@Parameter(description = "file detail") @Valid @RequestPart("file") file: org.springframework.core.io.Resource): ResponseEntity<NetworkUploadResponse> {
        return getDelegate().uploadPowerPlantFile(days, file)
    }
}
