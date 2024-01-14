package io.holixon.example.axon.event.transformation.application.port.`in`

interface GenerateRandomBatchUpdateInPort {
  fun runBatchUpdate(sensorId: String = "temp1")
}
