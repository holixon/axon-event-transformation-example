package io.holixon.example.axon.event.transformation.adapter.`in`

import com.fasterxml.jackson.annotation.JsonProperty

data class MeasurementDto(
  @JsonProperty("value")
  val value: Float,
  @JsonProperty("unit")
  val unit: String
)
