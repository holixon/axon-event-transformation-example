package io.holixon.example.axon.event.transformation

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

fun main(args: Array<String>) {
  runApplication<SensorApplication>(*args).let { Unit }
}

@SpringBootApplication
class SensorApplication
