package com.jcastillo.simulator.infrastructure.persistence

import org.springframework.data.mongodb.repository.Aggregation
import org.springframework.data.mongodb.repository.MongoRepository

interface PowerPlantRepository : MongoRepository<PowerPlantEntity, String>
{
    @Aggregation(pipeline = [
        "{ \$group: { " +
                "_id: null, " +
                "numberOfPowerPlants: { \$sum: 1 }, " +
                "sumOfTotalAges: { \$sum: '\$age' } " +
                "} }"
    ])
    fun getPowerPlantsStats(): PowerPlantStatsEntity?
}
