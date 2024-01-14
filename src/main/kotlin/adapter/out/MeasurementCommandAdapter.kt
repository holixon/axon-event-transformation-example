package io.holixon.example.axon.event.transformation.adapter.out

import io.holixon.example.axon.event.transformation.adapter.out.command.api.UpdateMeasurementsCommand
import io.holixon.example.axon.event.transformation.application.port.out.MeasurementCommandOutPort
import io.holixon.example.axon.event.transformation.application.port.`in`.SensorMeasurements
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.stereotype.Component

@Component
class MeasurementCommandAdapter(
  val commandGateway: CommandGateway
) : MeasurementCommandOutPort {
  override fun updateMeasurements(sensorMeasurements: SensorMeasurements) {
    commandGateway.sendAndWait<Void>(
      UpdateMeasurementsCommand(
        sensorId = sensorMeasurements.sensorId,
        values = sensorMeasurements.values
      )
    )
  }
}
