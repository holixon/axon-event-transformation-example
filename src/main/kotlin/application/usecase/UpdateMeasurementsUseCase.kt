package io.holixon.example.axon.event.transformation.application.usecase

import io.holixon.example.axon.event.transformation.application.port.`in`.UpdateMeasurementInPort
import io.holixon.example.axon.event.transformation.application.port.out.MeasurementCommandOutPort
import io.holixon.example.axon.event.transformation.application.port.`in`.SensorMeasurements
import org.springframework.stereotype.Component

@Component
class UpdateMeasurementsUseCase(
  private val measurementCommandOutPort: MeasurementCommandOutPort
) : UpdateMeasurementInPort {

  override fun updateSensorMeasurements(sensorMeasurements: SensorMeasurements) {
    measurementCommandOutPort.updateMeasurements(sensorMeasurements)
  }
}
