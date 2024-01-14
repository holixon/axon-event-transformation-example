package io.holixon.example.axon.event.transformation.adapter.out.event

import io.holixon.example.axon.event.transformation.application.port.`in`.Measurement
import java.time.LocalDate

/**
 * Domain event indicating that the measurement have been updated.
 */
data class MeasurementsUpdatedEvent(
  /**
   * Id of the sensor.
   */
  val sensorId: String,
  /**
   * Series of measurements.
   */
  val values: Map<LocalDate, Measurement>
)
