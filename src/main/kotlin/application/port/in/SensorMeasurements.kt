package io.holixon.example.axon.event.transformation.application.port.`in`

import java.time.LocalDate

/**
 * Represents a series of sensor measurements.
 */
data class SensorMeasurements(
  /**
   * Sensor id, for identification of the location.
   */
  val sensorId: String,
  /**
   * Series of measurements.
   */
  val values: Map<LocalDate, Measurement> = mapOf()
)
