package com.jcastillo.simulator.service

import com.jcastillo.simulator.domain.PowerPlant
import com.jcastillo.simulator.domain.PowerPlantStats
import com.jcastillo.simulator.infrastructure.persistence.PowerPlantEntity
import com.jcastillo.simulator.infrastructure.persistence.PowerPlantRepository
import org.springframework.data.mongodb.core.BulkOperations
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Component

private const val NAME = "name"
private const val AGE = "age"
private const val OUTPUT = "output"

@Component
class PowerPlantPersistenceService(
    private val powerPlantRepository: PowerPlantRepository,
    private val mongoTemplate: MongoTemplate
) {

    fun upsertAll(powerPlants: List<PowerPlant>) {
        if (powerPlants.isEmpty()) return

        val bulkOps = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, PowerPlantEntity::class.java)

        powerPlants.forEach { powerPlant ->
            val query = Query(Criteria.where(NAME).`is`(powerPlant.name))
            val update = Update()
                .set(AGE, powerPlant.age)
                .set(OUTPUT, powerPlant.output)

            bulkOps.upsert(query, update)
        }

        bulkOps.execute()
    }

    fun getAllPowerPlants(): List<PowerPlant> = powerPlantRepository.findAll()
        .map { PowerPlant(name = it.name, age = it.age, output = it.output) }
        .toList()

    fun getPowerPlantsStats(): PowerPlantStats? =
        powerPlantRepository.getPowerPlantsStats()?.let { PowerPlantStats(it.numberOfPowerPlants, it.sumOfTotalAges) }

}