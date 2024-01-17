# Axon Event Transformation Example

[![incubating](https://img.shields.io/badge/lifecycle-INCUBATING-orange.svg)](https://github.com/holisticon#open-source-lifecycle)
[![Build Status](https://github.com/holixon/axon-event-transformation-example/workflows/Development%20branches/badge.svg)](https://github.com/holixon/axon-event-transformation-example/actions)
[![sponsored](https://img.shields.io/badge/sponsoredBy-Holisticon-RED.svg)](https://holisticon.de/)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.holixon.example.axon-event-transformation/axon-event-transformation-example/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.holixon.example.axon-event-transformation/axon-event-transformation-example)

## Experiment

### Setup

- Size of event file set to 1000000 bytes (977kb).
- We consider only event storage (no snapshot files, no nindex files, no global-index-00000.xref)
- The size of the snapshot file can be set-up via property
- The size of the index file seems to be 2097152 bytes (approx 2mb) for event segment files of default size (256mb) and for a drastically reduced event segment size of 977k.
- The size of global-index-00000.xref is 64mb

Event payload:

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
    another 363 values for dates
  }
}
```
A single JSON payload consumes 16415 bytes in total (netto)
Based on file names of event files (00000000000000000175.events, 00000000000000000233.events) 58 update events match into a 977kb file resulting in 16845 bytes (brutto).

356 event consume 16845 * 365 = 6148425 brutto (splitting), resulting in not quite full 7 segments per batch run.

### Empty event store 

977K Jan 17 22:40 00000000000000000000.events

### First batch run (1 create sensor event + 365 update events)

977K Jan 17 22:44 00000000000000000000.events
977K Jan 17 22:45 00000000000000000059.events
977K Jan 17 22:45 00000000000000000117.events
977K Jan 17 22:45 00000000000000000175.events
977K Jan 17 22:45 00000000000000000233.events
977K Jan 17 22:45 00000000000000000291.events
977K Jan 17 22:45 00000000000000000349.events

### Second batch run (further 365 update events starting from "2024-01-17T21:48:22.384553762Z")

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

### Cleanup (delete all update events created before "2024-01-17T21:48:22.384553762Z")

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

### Compacting

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

