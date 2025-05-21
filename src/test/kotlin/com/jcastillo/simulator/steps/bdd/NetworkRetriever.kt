package com.jcastillo.simulator.steps.bdd

import com.jcastillo.simulator.port.model.PowerPlant
import io.restassured.RestAssured
import io.restassured.response.Response
import org.springframework.http.MediaType

class NetworkRetriever {

    fun get(url: String): Response {
        return RestAssured.given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .header("Accept", "application/json")
            .`when`()
            .get(url)
            .then()
            .log().status()
            .log().body()
            .extract()
            .response()
    }
}