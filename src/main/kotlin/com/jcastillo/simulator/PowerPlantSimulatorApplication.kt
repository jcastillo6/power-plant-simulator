package com.jcastillo.simulator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PowerPlantSimulatorApplication

fun main(args: Array<String>) {
    runApplication<PowerPlantSimulatorApplication>(*args)
}
