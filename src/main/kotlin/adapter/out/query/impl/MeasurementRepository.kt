package io.holixon.example.axon.event.transformation.adapter.out.query.impl

import io.holixon.example.axon.event.transformation.application.port.`in`.Measurement
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.concurrent.ConcurrentHashMap

interface MeasurementRepository {

  /**
   * Finds measurements available for the sensor id.
   * @param sensorId id.
   * @return a map of measurements (keyed by dates) or null if not found.
   */
  fun findForSensorId(sensorId: String): Map<LocalDate, Measurement>?

  /**
   * Saves measurement on a particular date for a sensor.
   * @param sensorId id.
   * @param date date to save for.
   * @param measurement measurement.
   */
  fun save(sensorId: String, date: LocalDate, measurement: Measurement)

  /**
   * Simple in-memory implementation.
   */
  @Component
  class InMemoryMeasurementRepositoryImpl(
    private val storage: MutableMap<String, MutableMap<LocalDate, Measurement>> = ConcurrentHashMap()
  ) : MeasurementRepository {
    override fun findForSensorId(sensorId: String): Map<LocalDate, Measurement>? = storage[sensorId]
    override fun save(sensorId: String, date: LocalDate, measurement: Measurement) {
      storage.getOrPut(sensorId) { ConcurrentHashMap() }[date] = measurement
    }
  }
}
