package io.holixon.example.axon.event.transformation.adapter.out.event

/**
 * Domain event indicating creation / registration of a new sensor.
 */
data class SensorCreatedEvent(
  /**
   * Sensor id.
   */
  val sensorId: String
)
