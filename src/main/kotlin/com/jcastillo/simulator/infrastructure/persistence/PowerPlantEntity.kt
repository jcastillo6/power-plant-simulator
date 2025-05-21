package com.jcastillo.simulator.infrastructure.persistence

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal

@Document(collection = "power_plant")
data class PowerPlantEntity(
    @Id
    val id: String? = null,
    @Indexed(unique = true)
    val name: String,
    val age: Int,
    val output: BigDecimal
)

