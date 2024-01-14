package io.holixon.example.axon.event.transformation.infrastructure

import io.axoniq.axonserver.connector.AxonServerConnection
import io.axoniq.axonserver.connector.AxonServerConnectionFactory
import io.axoniq.axonserver.connector.impl.ServerAddress
import mu.KLogging
import java.io.Closeable

/**
 * Auto-closable version of AxonServer Connection.
 */
class AutoClosableAxonServerConnection(
  private val connection: AxonServerConnection
) : Closeable, AxonServerConnection by connection {

  companion object : KLogging() {
    fun connect(axonServerHost: String, axonServerPort: Int = 8124, componentName: String, context: String): AutoClosableAxonServerConnection {
      val factory = AxonServerConnectionFactory.forClient(componentName)
        .routingServers(ServerAddress(axonServerHost, axonServerPort))
        .build()
      val connection = factory.connect(context)
      logger.info { "Connected to $axonServerHost:$axonServerPort@$context" }
      return AutoClosableAxonServerConnection(connection)
    }
  }

  override fun close() {
    if (connection.isConnected) {
      connection.disconnect()
    }
  }
}
