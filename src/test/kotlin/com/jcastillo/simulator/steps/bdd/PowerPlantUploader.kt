package com.jcastillo.simulator.steps.bdd

import io.restassured.RestAssured
import io.restassured.response.Response
import org.springframework.http.MediaType

private const val FILE_NAME = "powerplants.json"

class PowerPlantUploader {

    fun upload(url: String, days: Int, fileAsByteArray: ByteArray): Response {
        return RestAssured.given()
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
            .header("Accept", "application/json")
            .formParam("days", days)
            .multiPart("file", FILE_NAME, fileAsByteArray, MediaType.APPLICATION_JSON_VALUE)
            .`when`()
            .post(url)
            .then()
            .log().status()
            .log().body()
            .extract()
            .response()
    }
}
