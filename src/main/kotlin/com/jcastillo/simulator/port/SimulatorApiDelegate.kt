package com.jcastillo.simulator.port

import com.jcastillo.simulator.port.model.Error
import com.jcastillo.simulator.port.model.NetworkUploadResponse
import com.jcastillo.simulator.port.model.PowerPlant
import com.jcastillo.simulator.port.model.PowerPlantOutput
import com.jcastillo.simulator.port.model.TotalOutputResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.core.io.Resource

import java.util.Optional

/**
 * A delegate to be called by the {@link SimulatorApiController}}.
 * Implement this interface with a {@link org.springframework.stereotype.Service} annotated class.
 */
@jakarta.annotation.Generated(value = ["org.openapitools.codegen.languages.KotlinSpringServerCodegen"])
interface SimulatorApiDelegate {

    fun getRequest(): Optional<NativeWebRequest> = Optional.empty()

    /**
     * @see SimulatorApi#getNetworkStatus
     */
    fun getNetworkStatus(days: kotlin.Int,
        ifNoneMatch: kotlin.String?,
        ifModifiedSince: java.time.OffsetDateTime?): ResponseEntity<List<PowerPlantOutput>> {
        getRequest().ifPresent { request ->
            for (mediaType in MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    ApiUtil.setExampleResponse(request, "application/json", "[ {  \"name\" : \"name\",  \"output-in-kwh\" : 6.027456183070403,  \"age\" : 8008}, {  \"name\" : \"name\",  \"output-in-kwh\" : 6.027456183070403,  \"age\" : 8008} ]")
                    break
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    ApiUtil.setExampleResponse(request, "application/json", "{  \"code\" : \"VALIDATION_ERROR\",  \"details\" : {    \"key\" : \"\"  },  \"message\" : \"message\"}")
                    break
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    ApiUtil.setExampleResponse(request, "application/json", "{  \"code\" : \"VALIDATION_ERROR\",  \"details\" : {    \"key\" : \"\"  },  \"message\" : \"message\"}")
                    break
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    ApiUtil.setExampleResponse(request, "application/json", "{  \"code\" : \"VALIDATION_ERROR\",  \"details\" : {    \"key\" : \"\"  },  \"message\" : \"message\"}")
                    break
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    ApiUtil.setExampleResponse(request, "application/json", "{  \"code\" : \"VALIDATION_ERROR\",  \"details\" : {    \"key\" : \"\"  },  \"message\" : \"message\"}")
                    break
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    ApiUtil.setExampleResponse(request, "application/json", "{  \"code\" : \"VALIDATION_ERROR\",  \"details\" : {    \"key\" : \"\"  },  \"message\" : \"message\"}")
                    break
                }
            }
        }
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)

    }


    /**
     * @see SimulatorApi#getTotalOutput
     */
    fun getTotalOutput(days: kotlin.Int,
        ifNoneMatch: kotlin.String?,
        ifModifiedSince: java.time.OffsetDateTime?): ResponseEntity<TotalOutputResponse> {
        getRequest().ifPresent { request ->
            for (mediaType in MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    ApiUtil.setExampleResponse(request, "application/json", "{  \"total-output-in-kwh\" : 0.8008281904610115}")
                    break
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    ApiUtil.setExampleResponse(request, "application/json", "{  \"code\" : \"VALIDATION_ERROR\",  \"details\" : {    \"key\" : \"\"  },  \"message\" : \"message\"}")
                    break
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    ApiUtil.setExampleResponse(request, "application/json", "{  \"code\" : \"VALIDATION_ERROR\",  \"details\" : {    \"key\" : \"\"  },  \"message\" : \"message\"}")
                    break
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    ApiUtil.setExampleResponse(request, "application/json", "{  \"code\" : \"VALIDATION_ERROR\",  \"details\" : {    \"key\" : \"\"  },  \"message\" : \"message\"}")
                    break
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    ApiUtil.setExampleResponse(request, "application/json", "{  \"code\" : \"VALIDATION_ERROR\",  \"details\" : {    \"key\" : \"\"  },  \"message\" : \"message\"}")
                    break
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    ApiUtil.setExampleResponse(request, "application/json", "{  \"code\" : \"VALIDATION_ERROR\",  \"details\" : {    \"key\" : \"\"  },  \"message\" : \"message\"}")
                    break
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    ApiUtil.setExampleResponse(request, "application/json", "{  \"code\" : \"VALIDATION_ERROR\",  \"details\" : {    \"key\" : \"\"  },  \"message\" : \"message\"}")
                    break
                }
            }
        }
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)

    }


    /**
     * @see SimulatorApi#loadPowerPlants
     */
    fun loadPowerPlants(powerPlant: kotlin.collections.List<PowerPlant>): ResponseEntity<Unit> {
        getRequest().ifPresent { request ->
            for (mediaType in MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    ApiUtil.setExampleResponse(request, "application/json", "{  \"code\" : \"VALIDATION_ERROR\",  \"details\" : {    \"key\" : \"\"  },  \"message\" : \"message\"}")
                    break
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    ApiUtil.setExampleResponse(request, "application/json", "{  \"code\" : \"VALIDATION_ERROR\",  \"details\" : {    \"key\" : \"\"  },  \"message\" : \"message\"}")
                    break
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    ApiUtil.setExampleResponse(request, "application/json", "{  \"code\" : \"VALIDATION_ERROR\",  \"details\" : {    \"key\" : \"\"  },  \"message\" : \"message\"}")
                    break
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    ApiUtil.setExampleResponse(request, "application/json", "{  \"code\" : \"VALIDATION_ERROR\",  \"details\" : {    \"key\" : \"\"  },  \"message\" : \"message\"}")
                    break
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    ApiUtil.setExampleResponse(request, "application/json", "{  \"code\" : \"VALIDATION_ERROR\",  \"details\" : {    \"key\" : \"\"  },  \"message\" : \"message\"}")
                    break
                }
            }
        }
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)

    }


    /**
     * @see SimulatorApi#uploadPowerPlantFile
     */
    fun uploadPowerPlantFile(days: kotlin.Int,
        file: Resource?): ResponseEntity<NetworkUploadResponse> {
        getRequest().ifPresent { request ->
            for (mediaType in MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    ApiUtil.setExampleResponse(request, "application/json", "{  \"produced-kwh\" : 1234.56,  \"network\" : [ {    \"name\" : \"Power plant 1\",    \"age\" : 854  }, {    \"name\" : \"Power plant 2\",    \"age\" : 473  } ]}")
                    break
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    ApiUtil.setExampleResponse(request, "application/json", "{  \"code\" : \"VALIDATION_ERROR\",  \"details\" : {    \"key\" : \"\"  },  \"message\" : \"message\"}")
                    break
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    ApiUtil.setExampleResponse(request, "application/json", "{  \"code\" : \"VALIDATION_ERROR\",  \"details\" : {    \"key\" : \"\"  },  \"message\" : \"message\"}")
                    break
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    ApiUtil.setExampleResponse(request, "application/json", "{  \"code\" : \"VALIDATION_ERROR\",  \"details\" : {    \"key\" : \"\"  },  \"message\" : \"message\"}")
                    break
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    ApiUtil.setExampleResponse(request, "application/json", "{  \"code\" : \"VALIDATION_ERROR\",  \"details\" : {    \"key\" : \"\"  },  \"message\" : \"message\"}")
                    break
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    ApiUtil.setExampleResponse(request, "application/json", "{  \"code\" : \"VALIDATION_ERROR\",  \"details\" : {    \"key\" : \"\"  },  \"message\" : \"message\"}")
                    break
                }
            }
        }
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)

    }

}
