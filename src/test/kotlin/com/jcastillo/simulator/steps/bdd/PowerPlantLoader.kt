package com.jcastillo.simulator.steps.bdd

import com.jcastillo.simulator.port.model.PowerPlant
import io.restassured.RestAssured
import io.restassured.response.Response
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