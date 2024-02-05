package org.example.util;

import java.time.Duration;
import java.time.Instant;

public record Snapshot(String name, Instant lastExecutionDate, Duration lastRunElapsedTime) {


  @Override
  public String toString() {
    return name + "," + lastExecutionDate.toString() + "," + lastRunElapsedTime.toString();
  }
}

