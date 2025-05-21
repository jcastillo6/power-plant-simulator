package com.jcastillo.simulator.service

import com.jcastillo.simulator.domain.PowerPlantStats
import com.jcastillo.simulator.infrastructure.persistence.TotalNetworkPowerEntity
import com.jcastillo.simulator.infrastructure.persistence.TotalNetworkPowerRepository
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Component

@Component
class TotalNetworkPowerPersistenceService(private val totalNetworkPowerRepository: TotalNetworkPowerRepository,
                                          private val mongoTemplate: MongoTemplate) {

    fun upsertNetworkPower(powerPlantStats: PowerPlantStats): TotalNetworkPowerEntity {
        val query = Query()
        val update = Update()
            .set("totalAges", powerPlantStats.sumOfTotalAges)
            .set("numberOfPowerPlants", powerPlantStats.numberOfPowerPlants)

        return mongoTemplate.findAndModify(
            query,
            update,
            TotalNetworkPowerEntity::class.java,
            "power_plant_summary"
        )  ?: TotalNetworkPowerEntity(
            totalAges = powerPlantStats.sumOfTotalAges,
            numberOfPowerPlants = powerPlantStats.numberOfPowerPlants
        )
    }

    fun getCurrentNetworkPower(): PowerPlantStats? =
        totalNetworkPowerRepository.findAll().firstOrNull().let { PowerPlantStats(it?.numberOfPowerPlants ?: 0, it?.totalAges ?: 0) }
}