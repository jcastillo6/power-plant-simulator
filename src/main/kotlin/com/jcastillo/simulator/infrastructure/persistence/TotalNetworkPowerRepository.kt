package com.jcastillo.simulator.infrastructure.persistence

import org.springframework.data.mongodb.repository.MongoRepository

interface TotalNetworkPowerRepository: MongoRepository<TotalNetworkPowerEntity, String>