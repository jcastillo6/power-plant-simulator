package com.jcastillo.simulator.steps

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.jcastillo.simulator.steps.bdd.NetworkRetriever
import com.jcastillo.simulator.steps.bdd.PowerPlantLoader
import com.jcastillo.simulator.steps.bdd.PowerPlantUploader
import io.cucumber.datatable.DataTable
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.restassured.response.Response
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasSize
import org.openapitools.model.PowerPlant
import org.openapitools.model.PowerPlantOutput
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.testcontainers.junit.jupiter.Testcontainers
import kotlin.test.assertNotNull


private const val HOST = "http://localhost:"
private const val BASE_URL = "/solar-simulator/"
private const val UPLOAD = "upload"
private const val LOAD = "load"
private const val NETWORK = "network"
private const val OUTPUT = "output"

@Testcontainers
class SimulatorSteps(@LocalServerPort val localPort: Int) {

    private val powerPlantUploader = PowerPlantUploader()
    private val powerPlantLoader = PowerPlantLoader()
    private val networkRetriever = NetworkRetriever()
    private var days: Int = 0
    private lateinit var powerPlantsInputs: List<PowerPlant>
    private lateinit var powerPlantsOutput: List<PowerPlant>
    private lateinit var expectedOutput: String
    private lateinit var response: Response

    @Given("a new simulation")
    fun startNewSimulation() {
    }

    @When("upload a file with the following content:")
    fun uploadFileWithContent(dataTable: DataTable?) {
        powerPlantsInputs = getPowerPlantFrom(dataTable)
    }

    @When("a span of {int} days")
    fun setDaysForSimulation(days: Int?) {
        assertNotNull(days)
        this.days = days
    }

    @Then("the response provide the produced-kwh of {string}")
    fun validateProduceKwhResult(producedKwh: String?) {
        assertNotNull(producedKwh)
        expectedOutput = producedKwh
        response.then()
            .statusCode(HttpStatus.CREATED.value())
            .body(org.hamcrest.Matchers.containsString("\"produced-kwh\":$expectedOutput"))
    }

    @Then("the following age for the power plants")
    fun validatePowerPlantsResult(dataTable: DataTable?) {
        powerPlantsOutput = getPowerPlantFrom(dataTable)
        val file = jacksonObjectMapper().writeValueAsString(powerPlantsInputs).toByteArray()
        response = powerPlantUploader.upload("$HOST$localPort${BASE_URL}$UPLOAD", days, file)
        response.then()
            .statusCode(HttpStatus.CREATED.value())
            .body("network", hasSize<Int>(powerPlantsOutput.size))

        // Validate each power plant in the network array
        powerPlantsOutput.forEachIndexed { index, plant ->
            response.then()
                .body("network[$index].name", equalTo(plant.name))
                .body("network[$index].age", equalTo(plant.age))
        }
    }

    @When("upload a file with the following content and negative days:")
    fun uploadPowerPlantsWithNegativeDays(dataTable: DataTable?) {
        powerPlantsInputs = getPowerPlantFrom(dataTable)
        val file = jacksonObjectMapper().writeValueAsString(powerPlantsInputs).toByteArray()
        response = powerPlantUploader.upload("$HOST$localPort${BASE_URL}$UPLOAD", -1, file)
    }

    private fun getPowerPlantFrom(dataTable: DataTable?): List<PowerPlant> =
        dataTable?.asLists()?.drop(1)?.map { row -> PowerPlant(row[0], row[1].toInt()) }
            ?: throw IllegalArgumentException("Power plants output cannot be null")

    private fun getPowerPlantOutputFrom(dataTable: DataTable?): List<PowerPlantOutput> =
        dataTable?.asLists()?.drop(1)?.map { row -> PowerPlantOutput(row[0], row[1].toInt(), row[2].toBigDecimal()) }
            ?: throw IllegalArgumentException("Power plants output cannot be null")

    @When("upload an empty file")
    fun uploadEmptyFile() {
        val file = "[]".toByteArray()
        response = powerPlantUploader.upload("$HOST$localPort${BASE_URL}$UPLOAD", days, file)
    }

    @When("upload a file with invalid JSON")
    fun uploadInvalidJson() {
        val file = "invalid json".toByteArray()
        response = powerPlantUploader.upload("$HOST$localPort${BASE_URL}$UPLOAD", days, file)
    }

    @Then("the response should contain a validation error with message {string}")
    fun validateErrorResponse(errorMessage: String) {
        response.then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("code", equalTo("VALIDATION_ERROR"))
            .body("message", equalTo(errorMessage))
    }

    @When("adding this power plants:")
    fun addPowerPlants(dataTable: DataTable?) {
        powerPlantsOutput = getPowerPlantFrom(dataTable)
        powerPlantLoader.load("$HOST$localPort${BASE_URL}$LOAD", powerPlantsOutput).then()
            .statusCode(205)
    }

    @When("Retrieving the network at {int} days")
    fun getPowerPlants(days: Int?) {
        assertNotNull(days)
        response = networkRetriever.get("$HOST$localPort${BASE_URL}$NETWORK/$days")
    }

    @Then("The response should contain the following data:")
    fun validateResponseOfGetPowerPlantNetwork(dataTable: DataTable?) {
        val powerPlants = getPowerPlantOutputFrom(dataTable)
        response.then()
            .statusCode(HttpStatus.OK.value())
            .body("size()", equalTo(powerPlants.size))

        powerPlants.forEachIndexed { index, plant ->
            response.then()
                .body("[$index].name", equalTo(plant.name))
                .body("[$index].age", equalTo(plant.age))
                .body("[$index].output-in-kwh", equalTo(plant.outputInKwh.toFloat()))
        }
    }

    @When("checking the output off all power plants at {int} days")
    fun getTotalOutput(days: Int?) {
        response = networkRetriever.get("$HOST$localPort${BASE_URL}$OUTPUT/$days")
    }

    @Then("The total output is {double}")
    fun validateTotalOutputResponse(output: Double?) {
        assertNotNull(output)
        response.then().statusCode(HttpStatus.OK.value()).body("total-output-in-kwh", equalTo(output.toFloat()))
    }
}