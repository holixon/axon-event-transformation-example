package io.holixon.example.axon.event.transformation.adapter.out.query.impl

import io.holixon.example.axon.event.transformation.adapter.out.event.MeasurementsUpdatedEvent
import mu.KLogging
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.axonframework.eventhandling.Timestamp
import org.springframework.stereotype.Component
import java.time.Instant

@Component
@ProcessingGroup(value = "measurements")
class MeasurementProjector(
  private val measurementRepository: MeasurementRepository
) {
  companion object : KLogging()

  @EventHandler(payloadType = MeasurementsUpdatedEvent::class)
  fun on(event: MeasurementsUpdatedEvent, @Timestamp time: Instant) {
    logger.trace { "Received measurement updated event at $time" }
    event.values.forEach { (date, measurement) ->
      measurementRepository.save(
        sensorId = event.sensorId,
        date = date,
        measurement = measurement
      )
    }
  }
}
