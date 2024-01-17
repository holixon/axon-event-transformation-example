package io.holixon.example.axon.event.transformation.adapter.`in`

import io.holixon.example.axon.event.transformation.application.port.`in`.*
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.noContent
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.*
import java.time.Instant
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneOffset

@RestController
@RequestMapping("/measurements")
class MeasurementController(
  val updateMeasurementInPort: UpdateMeasurementInPort,
  val retrieveMeasurementsInPort: RetrieveMeasurementsInPort,
  val generateRandomBatchUpdateInPort: GenerateRandomBatchUpdateInPort
) {

  @GetMapping(value = ["/sensor/{sensorId}"], produces = [MediaType.APPLICATION_JSON_VALUE])
  fun load(
    @PathVariable("sensorId") sensorId: String,
  ): ResponseEntity<Map<OffsetDateTime, MeasurementDto>> {
    return ok(
      retrieveMeasurementsInPort.retrieveMeasurements(sensorId).map { (date, measurement) ->
        OffsetDateTime.of(date, LocalTime.NOON, ZoneOffset.UTC) to MeasurementDto(
          value = measurement.value,
          unit = measurement.unit
        )
      }.toMap()
    )
  }


  @PostMapping(value = ["/sensor/{sensorId}"], consumes = [MediaType.APPLICATION_JSON_VALUE])
  fun update(
    @PathVariable("sensorId") sensorId: String,
    @RequestBody values: Map<OffsetDateTime, MeasurementDto>
  ): ResponseEntity<Void> {

    updateMeasurementInPort.updateSensorMeasurements(
      SensorMeasurements(
        sensorId = sensorId,
        values = values.map { (date, measurement) ->
          date.toLocalDate() to Measurement(
            value = measurement.value,
            unit = measurement.unit
          )
        }.toMap()
      )
    )

    return noContent().build()
  }

  @PostMapping(value = ["/batch"])
  fun batchUpdate(): ResponseEntity<ExecutionTimeDto> {
    val before = Instant.now()
    generateRandomBatchUpdateInPort.runBatchUpdate()
    val after = Instant.now()
    return ok(ExecutionTimeDto(start = before, end = after))
  }

  data class ExecutionTimeDto(
    val start: Instant,
    val end: Instant
  )
}
