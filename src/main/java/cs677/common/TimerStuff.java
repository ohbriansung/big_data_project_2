package cs677.common;

import java.time.Duration;
import java.time.Instant;

public class TimerStuff {
  Instant t0;
  Instant t1;

  public void start() {
    t0 = Instant.now();
  }

  public void stop() {
    t1 = Instant.now();
  }

  public String toString() {
    if (t0 == null || t1 == null) return "Timer needs a start and a stop";
    Duration duration = Duration.between(t0, t1);
    return formatDuration(duration);
  }

  public static String formatDuration(Duration duration) {
    return String.format(
        "%d:%02d:%02d.%03d",
        duration.toHours(),
        duration.toMinutes() % 60,
        duration.getSeconds() % 60,
        duration.getNano() % 1000);
  }
}
