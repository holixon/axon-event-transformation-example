package io.holixon.example.axon.event.transformation.adapter.out

import io.holixon.example.axon.event.transformation.adapter.out.query.api.MeasurementQueryProtocol
import io.holixon.example.axon.event.transformation.adapter.out.query.api.MeasurementQueryProtocol.Companion.FOR_SENSOR
import io.holixon.example.axon.event.transformation.application.port.`in`.Measurement
import io.holixon.example.axon.event.transformation.application.port.out.MeasurementQueryOutPort
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class MeasurementQueryAdapter(
  val queryGateway: QueryGateway
) : MeasurementQueryOutPort {

  override fun loadMeasurementForSensor(sensorId: String): Map<LocalDate, Measurement> {
    return queryGateway.query(
      FOR_SENSOR,
      MeasurementQueryProtocol.ForSensor(sensorId),
      ResponseTypes.instanceOf(MeasurementQueryProtocol.MeasurementQueryResult::class.java)
    ).join().values
  }
}
