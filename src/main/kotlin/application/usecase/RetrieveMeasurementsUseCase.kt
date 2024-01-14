package io.holixon.example.axon.event.transformation.application.usecase

import io.holixon.example.axon.event.transformation.application.port.`in`.Measurement
import io.holixon.example.axon.event.transformation.application.port.out.MeasurementQueryOutPort
import io.holixon.example.axon.event.transformation.application.port.`in`.RetrieveMeasurementsInPort
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class RetrieveMeasurementsUseCase(
  private val measurementQueryOutPort: MeasurementQueryOutPort
) : RetrieveMeasurementsInPort {

  override fun retrieveMeasurements(sensorId: String): Map<LocalDate, Measurement> {
    return measurementQueryOutPort.loadMeasurementForSensor(sensorId = sensorId)
  }
}

