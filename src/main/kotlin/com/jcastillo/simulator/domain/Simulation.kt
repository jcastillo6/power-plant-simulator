package com.jcastillo.simulator.domain

import java.math.BigDecimal

data class Simulation(val kwhOutput: BigDecimal, val powerPlants: List<PowerPlant>)
