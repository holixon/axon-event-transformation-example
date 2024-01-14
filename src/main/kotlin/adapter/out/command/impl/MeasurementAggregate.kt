package io.holixon.example.axon.event.transformation.adapter.out.command.impl

import io.holixon.example.axon.event.transformation.adapter.out.command.api.UpdateMeasurementsCommand
import io.holixon.example.axon.event.transformation.adapter.out.event.MeasurementsUpdatedEvent
import io.holixon.example.axon.event.transformation.adapter.out.event.SensorCreatedEvent
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateCreationPolicy
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.modelling.command.CreationPolicy
import org.axonframework.spring.stereotype.Aggregate
import org.springframework.context.annotation.Scope
import org.springframework.core.annotation.AliasFor

@Aggregate
class MeasurementAggregate {
  @AggregateIdentifier
  private lateinit var sensorId: String

  @CreationPolicy(value = AggregateCreationPolicy.CREATE_IF_MISSING)
  @CommandHandler
  fun handle(cmd: UpdateMeasurementsCommand) {

    if (!this::sensorId.isInitialized) {
      // just created a new aggregate
      AggregateLifecycle.apply(
        SensorCreatedEvent(
          sensorId = cmd.sensorId
        )
      )
    }

    AggregateLifecycle.apply(
      MeasurementsUpdatedEvent(
        sensorId = cmd.sensorId,
        values = cmd.values,
      )
    )
  }

  @EventSourcingHandler
  fun on(evt: SensorCreatedEvent) {
    this.sensorId = evt.sensorId
  }

}
