package io.holixon.example.axon.event.transformation.adapter.`in`

import io.holixon.example.axon.event.transformation.adapter.out.event.MeasurementsUpdatedEvent
import io.holixon.example.axon.event.transformation.infrastructure.EventCleanup
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.noContent
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
@RequestMapping("/admin")
class AdminController(
  val eventCleanup: EventCleanup
) {

  @PostMapping(value = ["/cleanup"])
  fun cleanup(@RequestBody deleteUntil: Instant): ResponseEntity<Void> {
    eventCleanup.deleteEventsUntil(eventFQDN = MeasurementsUpdatedEvent::class.java.name, deleteUntil = deleteUntil)
    return noContent().build()
  }

  @PostMapping(value = ["/compact"])
  fun compact(): ResponseEntity<Void> {
    eventCleanup.compact()
    return noContent().build()
  }

}
