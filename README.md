# Axon Event Transformation Example

[![incubating](https://img.shields.io/badge/lifecycle-INCUBATING-orange.svg)](https://github.com/holisticon#open-source-lifecycle)
[![Build Status](https://github.com/holixon/axon-event-transformation-example/workflows/Development%20branches/badge.svg)](https://github.com/holixon/axon-event-transformation-example/actions)
[![sponsored](https://img.shields.io/badge/sponsoredBy-Holisticon-RED.svg)](https://holisticon.de/)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.holixon.example.axon-event-transformation/axon-event-transformation-example/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.holixon.example.axon-event-transformation/axon-event-transformation-example)

## Into

The example demonstrates the usage of the [event store transformation API](https://docs.axoniq.io/reference-guide/axon-server/administration/event-transformation) and focuses on deletion of "stale" events.

## Use case

Consider a system storing temperature forecast delivered by some external system. The forecast is generated periodically and the system is representing the actual forecast.
Apart from the historical information (which can be for example used to tune the forecast model), the system holds current (= latest) forecast and can be queried for the 
weather on a certain date. After the delivery of the new forecast, the previous forecast is considered as "stale" and doesn't need to be stored.

## The API

The system allows to insert a new temperature value at a single date and query temperature on certain date. For *simulation purposes* the system provides a fake endpoint
for generation of an entire forecast for the next year (taking random values for the temperature).

## The mission

Try to use event store transformation API to delete stale events and reduce the size of the event store - both for purposes of disk usage and the amount of events used on replays.

## Experiment

- A sensor is created (1 event)
- The first batch of 365 updates (starting from today for the next year) for 365 days from update date to the future is sent
- The second batch of 365 updates (starting from today for the next year) for 365 days from update date to the future is sent
- All 365 events from the first batch are deleted 

### Preparation

- Axon server is configured to use small segments. Size of event file set to 1000000 bytes (977kb).

### Setup

- We consider only event storage (no snapshot files, no nindex files, no global-index-00000.xref)
- The size of the snapshot file can be set-up via property separately.
- The size of the index file seems to be 2097152 bytes (approx 2mb) for event segment files of default size (256mb) and for a drastically reduced event segment size of 977k.
- The size of global-index-00000.xref is 64mb.
- Events use Jackson serializer storing payload as JSON.
- One event contains a forecast for the next year (a map keyed with date and valued with measurement)
- Event payload is then represented like this:

```json
{
  "sensorId": "temp1",
  "values": {
    "2025-01-16": {
      "value": 23.363865,
      "unit": "°C"
    },
    "2025-01-17": {
      "value": 23.188648,
      "unit": "°C"
    },
    ...
    another 363 values for dates
  }
}
```
- A single JSON payload consumes 16415 bytes in total (netto).
- Based on file names of event files (00000000000000000175.events, 00000000000000000233.events) 58 update events match into a 977kb file resulting in 16845 bytes (brutto).
- 356 event consume 16845 * 365 = 6148425 brutto (splitting), resulting in not quite full 7 segments per batch run.

### Empty event store 

```
977K Jan 17 22:40 00000000000000000000.events
```

### First batch run (1 create sensor event + 365 update events)

```
977K Jan 17 22:44 00000000000000000000.events
977K Jan 17 22:45 00000000000000000059.events
977K Jan 17 22:45 00000000000000000117.events
977K Jan 17 22:45 00000000000000000175.events
977K Jan 17 22:45 00000000000000000233.events
977K Jan 17 22:45 00000000000000000291.events
977K Jan 17 22:45 00000000000000000349.events
```

### Second batch run (further 365 update events starting from "2024-01-17T21:48:22.384553762Z")

```
977K Jan 17 22:44 00000000000000000000.events
977K Jan 17 22:45 00000000000000000059.events
977K Jan 17 22:45 00000000000000000117.events
977K Jan 17 22:45 00000000000000000175.events
977K Jan 17 22:45 00000000000000000233.events
977K Jan 17 22:45 00000000000000000291.events
977K Jan 17 22:48 00000000000000000349.events
977K Jan 17 22:48 00000000000000000407.events
977K Jan 17 22:48 00000000000000000465.events
977K Jan 17 22:48 00000000000000000523.events
977K Jan 17 22:48 00000000000000000581.events
977K Jan 17 22:48 00000000000000000639.events
977K Jan 17 22:48 00000000000000000697.events
```

### Cleanup (delete all update events created before "2024-01-17T21:48:22.384553762Z")

```
3,9K Jan 17 22:50 00000000000000000000_00003.events
977K Jan 17 22:44 00000000000000000000.events
3,6K Jan 17 22:50 00000000000000000059_00003.events
977K Jan 17 22:45 00000000000000000059.events
3,6K Jan 17 22:50 00000000000000000117_00003.events
977K Jan 17 22:45 00000000000000000117.events
3,6K Jan 17 22:50 00000000000000000175_00003.events
977K Jan 17 22:45 00000000000000000175.events
3,6K Jan 17 22:50 00000000000000000233_00003.events
977K Jan 17 22:45 00000000000000000233.events
3,6K Jan 17 22:50 00000000000000000291_00003.events
977K Jan 17 22:45 00000000000000000291.events
668K Jan 17 22:50 00000000000000000349_00003.events
977K Jan 17 22:48 00000000000000000349.events
977K Jan 17 22:48 00000000000000000407.events
977K Jan 17 22:48 00000000000000000465.events
977K Jan 17 22:48 00000000000000000523.events
977K Jan 17 22:48 00000000000000000581.events
977K Jan 17 22:48 00000000000000000639.events
977K Jan 17 22:48 00000000000000000697.events
```

### Compacting

```
3,9K Jan 17 22:50 00000000000000000000_00003.events
3,6K Jan 17 22:50 00000000000000000059_00003.events
3,6K Jan 17 22:50 00000000000000000117_00003.events
3,6K Jan 17 22:50 00000000000000000175_00003.events
3,6K Jan 17 22:50 00000000000000000233_00003.events
3,6K Jan 17 22:50 00000000000000000291_00003.events
668K Jan 17 22:50 00000000000000000349_00003.events
977K Jan 17 22:48 00000000000000000407.events
977K Jan 17 22:48 00000000000000000465.events
977K Jan 17 22:48 00000000000000000523.events
977K Jan 17 22:48 00000000000000000581.events
977K Jan 17 22:48 00000000000000000639.events
977K Jan 17 22:48 00000000000000000697.events
```

## How to run

- Start Axon Server via `docker-compose up`
- Build application with mvn (`./mvnw clean package`)
- Start application `java -jar target/axon-event-transformation-example-0.0.1-SNAPSHOT.jar` and check the console [Admin console](http://localhost:8024)
- Open browser and navigate to [Swagger UI](http://localhost:8080/swagger-ui/index.html)
- Run a batch update (HTTP POST to `/measurements/batch`) and check files in `.docker/events/default`  
- Run a batch update for the second time (HTTP POST to `/measurements/batch`) and check files in `.docker/events/default`
- Copy the `start` value of from the HTTP response of the second batch run
- Run cleanup, by providing the value from the previous step as a string payload (HTTP POST to `/admin/cleanup`) and check files in `.docker/events/default`
- Run compact (HTTP POST to `/admin/compact`) and check files in `.docker/events/default`
