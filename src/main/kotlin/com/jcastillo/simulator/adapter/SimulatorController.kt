package com.jcastillo.simulator.adapter

import com.fasterxml.jackson.databind.ObjectMapper
import com.jcastillo.simulator.domain.CreatePowerPlantCommand
import com.jcastillo.simulator.domain.SimulationAggregate
import com.jcastillo.simulator.service.FileValidationResult
import com.jcastillo.simulator.service.FileValidator
import org.openapitools.api.SimulatorApi
import org.openapitools.model.NetworkUploadResponse
import org.openapitools.model.PowerPlantOutput
import org.openapitools.model.TotalOutputResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.time.OffsetDateTime

private const val FILE_IS_EMPTY = "File is empty"
private const val INVALID_DAY = "Invalid number of days. Must be at least 1 and less than 9125"
private const val NO_POWER_PLANTS_TO_LOAD = "No power plants to load"


@RestController
class SimulatorController(
    private val simulatorAggregate: SimulationAggregate,
    private val fileValidator: FileValidator,
    val mapper: ObjectMapper
) : SimulatorApi {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(SimulatorApi::class.java)
    }

     override fun uploadPowerPlantFile(days: Int, file: Resource): ResponseEntity<NetworkUploadResponse> {
        if (days < 0) {
            throw NegativeNumberOfDaysException(INVALID_DAY)
        }
        val isValid = fileValidator.validate(file)
        return when (isValid) {
            is FileValidationResult.Invalid -> throw InvalidFileFormatException(isValid.message)
            is FileValidationResult.Valid -> processFileAndGetResponse(days, file)
        }
    }

    override fun loadPowerPlants(powerPlants: List<org.openapitools.model.PowerPlant>): ResponseEntity<Unit> {
        logger.info("Loading power plants: ${powerPlants.size}")
        if (powerPlants.isEmpty()) {
            throw EmptyBodyException(NO_POWER_PLANTS_TO_LOAD)
        }
        powerPlants.map { CreatePowerPlantCommand(it.name, it.age) }.let { simulatorAggregate.addAllPowerPlants(it) }
        return ResponseEntity.status(HttpStatus.RESET_CONTENT).build()
    }

    // TODO implement caching
    override fun getNetworkStatus(
        days: Int,
        ifNoneMatch: String?,
        ifModifiedSince: OffsetDateTime?
    ): ResponseEntity<List<PowerPlantOutput>> {
        logger.info("Getting network status for days: $days")
        val simulation = simulatorAggregate.executeSimulation(days)
        val powerPlantsOutput =
            simulation.powerPlants.map { (name, age, output) -> PowerPlantOutput(name, age, output) }
        return ResponseEntity.status(HttpStatus.OK).body(powerPlantsOutput)
    }

    //TODO this can be retrieved in O(1) persisting value the memory cache or db
    override fun getTotalOutput(
        days: Int,
        ifNoneMatch: String?,
        ifModifiedSince: OffsetDateTime?
    ): ResponseEntity<TotalOutputResponse> {
        logger.info("Getting total output for days: $days")
        val simulation = simulatorAggregate.executeSimulation(days)
        return ResponseEntity.status(HttpStatus.OK).body(TotalOutputResponse(simulation.kwhOutput))
    }

    /**
     * TODO validate negative ages
     */
    private fun processFileAndGetResponse(days: Int, file: Resource): ResponseEntity<NetworkUploadResponse> {
        logger.info("Processing file: ${file.filename} with days: $days")
        val powerPlantsInput = mapper.readValue(file.inputStream, Array<org.openapitools.model.PowerPlant>::class.java)
        if (powerPlantsInput.isEmpty()) {
            throw EmptyFileException(FILE_IS_EMPTY)
        }
        val simulation = powerPlantsInput.map { CreatePowerPlantCommand(it.name, it.age) }
            .let { simulatorAggregate.addAndExecuteSimulation(it, days) }
        val powerPlantsOutput = simulation.powerPlants.map { (name, age) -> org.openapitools.model.PowerPlant(name, age) }
        val networkUploadResponse = NetworkUploadResponse(producedKwh = simulation.kwhOutput, powerPlantsOutput)
        return ResponseEntity.status(HttpStatus.CREATED).body(networkUploadResponse)
    }
}