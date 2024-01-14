package io.holixon.example.axon.event.transformation.application.port.`in`

/**
 * Use case of updating a series of measurements.
 */
interface UpdateMeasurementInPort {
  /**
   * Updates a sensor measurements.
   * @param sensorMeasurements a series of measurements carrying new values. Usually the dates target the future.
   */
  fun updateSensorMeasurements(sensorMeasurements: SensorMeasurements)
}
