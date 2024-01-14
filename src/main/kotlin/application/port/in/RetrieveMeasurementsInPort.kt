package io.holixon.example.axon.event.transformation.application.port.`in`

import io.holixon.example.axon.event.transformation.application.port.`in`.Measurement
import java.time.LocalDate

interface RetrieveMeasurementsInPort {
  fun retrieveMeasurements(sensorId: String): Map<LocalDate, Measurement>
}
