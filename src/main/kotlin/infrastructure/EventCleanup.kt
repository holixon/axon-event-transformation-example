package io.holixon.example.axon.event.transformation.infrastructure

import io.axoniq.axonserver.connector.AxonServerConnection
import io.axoniq.axonserver.connector.event.transformation.Appender
import io.axoniq.axonserver.connector.event.transformation.EventTransformation
import io.axoniq.axonserver.connector.event.transformation.event.EventSources
import io.axoniq.axonserver.connector.event.transformation.event.EventTransformer
import io.axoniq.axonserver.grpc.event.EventWithToken
import mu.KLogging
import org.axonframework.axonserver.connector.AxonServerConfiguration
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class EventCleanup(
  axonServerConfiguration: AxonServerConfiguration
) {

  private val connect = axonServerConfiguration.routingServers().first()
  private val context = axonServerConfiguration.context

  companion object : KLogging()

  fun deleteEventsUntil(eventFQDN: String, deleteUntil: Instant, firstToken: Long = 0, lastToken: Long = -1L) {
    AutoClosableAxonServerConnection
      .connect(connect.hostName, connect.grpcPort, this.javaClass.simpleName, this.context).use { connection ->

        // define the range of events
        val last = if (lastToken == -1L) {
          connection.eventChannel().lastToken.get()
        } else {
          lastToken
        }

        try {
          EventSources
            .range({ connection.eventChannel() }, firstToken, last)
            .filter { eventWithToken ->
              logger.debug { "Event: ${eventWithToken.event.payload.type}" }
              eventWithToken.event.payload.type == eventFQDN && eventWithToken.event.timestamp < deleteUntil.toEpochMilli()
            }
            .transform("Deleting events until $deleteUntil") { event, appender ->
              appender.deleteEvent(event.token)
            }
            .execute { connection.eventTransformationChannel() }
            .get()
        } catch (e: Exception) {
          logger.error(e) { "Error executing event transformation" }
        } finally {
          connection.ensureNoActiveTransformations()
        }
      }
  }

  fun compact() {
    AutoClosableAxonServerConnection
      .connect(connect.hostName, connect.grpcPort, this.javaClass.simpleName, this.context).use { connection ->
        connection.ensureNoActiveTransformations()
        connection
          .eventTransformationChannel()
          .startCompacting()
          .get()
      }
  }

  fun AxonServerConnection.ensureNoActiveTransformations() {
    this.eventTransformationChannel().transformations().get().forEach {
      logger.debug { "Found event transformation: ${it.id()}, ${it.description()}, ${it.state()}, ${it.lastSequence()}" }
      if (it.state() == EventTransformation.State.ACTIVE) {
        // cancelling the active one
        this.eventTransformationChannel().activeTransformation().get().cancel().get()
        logger.warn { "Cancelled active transformation ${it.id()}, ${it.description()}, ${it.state()}, ${it.lastSequence()}" }
      }
    }
  }

}


