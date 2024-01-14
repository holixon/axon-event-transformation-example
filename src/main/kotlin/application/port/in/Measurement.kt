package io.holixon.example.axon.event.transformation.application.port.`in`

/**
 * Represents a single measurement with a unit.
 */
data class Measurement(
  /**
   * Value of the measurement.
   */
  val value: Float,
  /**
   * Unit.
   */
  val unit: String = "°C",
)
