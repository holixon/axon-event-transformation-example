package io.holixon.example.axon.event.transformation.adapter.out.command.api

import io.holixon.example.axon.event.transformation.application.port.`in`.Measurement
import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.time.LocalDate

data class UpdateMeasurementsCommand(
  @TargetAggregateIdentifier
  val sensorId: String,
  val values: Map<LocalDate, Measurement>
  )
