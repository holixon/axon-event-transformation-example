package io.holixon.example.axon.event.transformation.adapter.out.query.api

import io.holixon.example.axon.event.transformation.application.port.`in`.Measurement
import org.axonframework.queryhandling.QueryHandler
import java.time.LocalDate

interface MeasurementQueryProtocol {

  companion object {
    const val FOR_SENSOR = "measurementForSensor"
  }

  @QueryHandler(queryName = FOR_SENSOR)
  fun query(param: ForSensor): MeasurementQueryResult

  data class ForSensor(val sensorId: String)
  data class MeasurementQueryResult(val values: Map<LocalDate, Measurement>)
}
