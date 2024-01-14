package io.holixon.example.axon.event.transformation.application.usecase

import io.holixon.example.axon.event.transformation.application.port.`in`.GenerateRandomBatchUpdateInPort
import io.holixon.example.axon.event.transformation.application.port.`in`.Measurement
import io.holixon.example.axon.event.transformation.application.port.`in`.SensorMeasurements
import io.holixon.example.axon.event.transformation.application.port.`in`.UpdateMeasurementInPort
import org.springframework.stereotype.Component
import java.time.LocalDate
import kotlin.random.Random

@Component
class GenerateRandomBatchUpdateUseCase(
  val updateMeasurementInPort: UpdateMeasurementInPort
) : GenerateRandomBatchUpdateInPort {

  override fun runBatchUpdate(sensorId: String) {
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
          updateDate.plusDays(forecastOffset) to Measurement(Random.nextFloat() * 30, "°C")
        }
      )
    )
  }
}
