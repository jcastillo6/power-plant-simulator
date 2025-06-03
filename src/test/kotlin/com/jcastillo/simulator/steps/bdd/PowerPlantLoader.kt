package com.jcastillo.simulator.steps.bdd

import io.restassured.RestAssured
import io.restassured.response.Response
import org.openapitools.model.PowerPlant
import org.springframework.http.MediaType

class PowerPlantLoader {

    fun load(url: String, powerPlants: List<PowerPlant>): Response {
        return RestAssured.given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .header("Accept", MediaType.APPLICATION_JSON_VALUE)
            .body(powerPlants)
            .`when`()
            .post(url)
            .then()
            .log().status()
            .log().body()
            .extract()
            .response()
    }
}