package com.jcastillo.simulator.infrastructure.persistence

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "power_plant_summary")
data class TotalNetworkPowerEntity(
    @Id
    val id: String? = null,
    @Indexed(unique = true)
    val totalAges: Long,
    val numberOfPowerPlants: Long
)

