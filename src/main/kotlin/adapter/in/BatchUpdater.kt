package io.holixon.example.axon.event.transformation.adapter.`in`

import io.holixon.example.axon.event.transformation.application.port.`in`.Measurement
import io.holixon.example.axon.event.transformation.application.port.`in`.SensorMeasurements
import io.holixon.example.axon.event.transformation.application.port.`in`.UpdateMeasurementInPort
import org.springframework.stereotype.Component
import java.time.LocalDate
import kotlin.random.Random

@Component
class BatchUpdater(
  val updateMeasurementInPort: UpdateMeasurementInPort
) {

  fun runBatchUpdate(sensorId: String = "temp1") {
    (0L..365L).forEach { offset ->
      val updateDate = LocalDate.now().plusDays(offset)
      generateForecast(sensorId, updateDate)
    }
  }

  fun generateForecast(sensorId: String, updateDate: LocalDate) {
    updateMeasurementInPort.updateSensorMeasurements(
      SensorMeasurements(
        sensorId = sensorId,
        values = (0L..365L).associate { forecastOffset ->
          updateDate.plusDays(forecastOffset) to Measurement(randomFloat(), "°C")
        }
      )
    )

  }

  private fun randomFloat(): Float = Random.nextFloat() * 30
}
