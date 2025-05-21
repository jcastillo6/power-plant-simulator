package com.jcastillo.simulator

import io.cucumber.java.Before
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.mongodb.core.MongoTemplate


class DatabaseCleanupHooks(val mongoTemplate: MongoTemplate) {
    @Value("\${spring.data.mongodb.database}")
    private lateinit var databaseName: String

    @Before
    fun cleanDatabase() {
        mongoTemplate.dropCollection(databaseName)
    }
}