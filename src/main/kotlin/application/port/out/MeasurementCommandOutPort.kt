package io.holixon.example.axon.event.transformation.application.port.out

import io.holixon.example.axon.event.transformation.application.port.`in`.SensorMeasurements

/**
 * Command port.
 */
interface MeasurementCommandOutPort {
  /**
   * Creates and dispatches the command about measurement update.
   */
  fun updateMeasurements(sensorMeasurements: SensorMeasurements)
}
