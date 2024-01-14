package io.holixon.example.axon.event.transformation.application.port.out

import io.holixon.example.axon.event.transformation.application.port.`in`.Measurement
import java.time.LocalDate

interface MeasurementQueryOutPort {
  fun loadMeasurementForSensor(sensorId: String): Map<LocalDate, Measurement>
}
