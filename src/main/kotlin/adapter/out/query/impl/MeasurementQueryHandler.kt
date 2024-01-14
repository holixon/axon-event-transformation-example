package io.holixon.example.axon.event.transformation.adapter.out.query.impl

import io.holixon.example.axon.event.transformation.adapter.out.query.api.MeasurementQueryProtocol
import org.springframework.stereotype.Component

@Component
class MeasurementQueryHandler(
  private val measurementRepository: MeasurementRepository
) : MeasurementQueryProtocol {

  override fun query(param: MeasurementQueryProtocol.ForSensor): MeasurementQueryProtocol.MeasurementQueryResult {
    return MeasurementQueryProtocol.MeasurementQueryResult(measurementRepository.findForSensorId(param.sensorId) ?: mapOf())
  }
}
