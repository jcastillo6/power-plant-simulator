package com.jcastillo.simulator

import io.cucumber.java.Before
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.mongodb.core.MongoTemplate
import org.slf4j.Logger

class DatabaseCleanupHooks(val mongoTemplate: MongoTemplate) {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(DatabaseCleanupHooks::class.java)
    }

    @Value("\${spring.data.mongodb.database}")
    private lateinit var databaseName: String

    @Before
    fun cleanDatabase() {
        logger.info("Cleaning up database: $databaseName")
        mongoTemplate.dropCollection(databaseName)
    }
}