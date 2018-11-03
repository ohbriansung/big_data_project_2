package cs677;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.PriorityQueue;
import java.util.Random;

public class Sandbox {
  public static void main(String[] args) {
    //    itemizeString();
    //    testPriorityQueue();
    //    jsonArrTest();
    //    instantNow();
    //    randTest();
        localDateTimeTest();
//    durationTest();
  }

  private static void itemizeString() {
    String location = "abc";
    for (int i = 0; i < 10; i++) {
      String numbered_location = String.format("%s_%04d", location, i);
      System.out.println(numbered_location);
    }
  }

  private static void testPriorityQueue() {
    PriorityQueue<Integer> priorityQueue =
        new PriorityQueue<>(10, (Integer o1, Integer o2) -> o1 - o2);
    for (int i = 25; i > 0; i--) {
      if (priorityQueue.size() >= 10 && i > priorityQueue.peek()) {
        priorityQueue.poll();
        priorityQueue.add(i);
      }
      if (priorityQueue.size() < 10) {
        priorityQueue.add(i);
      }
    }
    for (Integer i : priorityQueue) {
      System.out.println(i);
    }
  }

  private static void jsonArrTest() {
    String arr = "{\"arr\": [1, 2, 3, 4]}";
    JSONObject object = new JSONObject(arr);
    JSONArray jsonArray = object.getJSONArray("arr");
    System.out.println(jsonArray.toString());
  }

  private static void instantNow() {
    System.out.println(Instant.now());
  }

  private static void randTest() {
    Random rand = new Random();
    float r;
    int count = 0;
    for (int i = 0; i < 1000; i++) {
      r = rand.nextFloat();
      if (r >= 0.05) {
        continue;
      }
      System.out.println(r);
      count += 1;
    }
    System.out.println(count);
  }

  private static void localDateTimeTest() {
    long seconds = 1397805829;
    LocalDateTime dateTime = LocalDateTime.ofEpochSecond(seconds, 0, ZoneOffset.UTC);
    //            LocalDateTime.ofInstant(Instant.ofEpochMilli(seconds), ZoneId.of("UTC"));
    System.out.println(dateTime);
    System.out.println(dateTime.getMonthValue());
    System.out.println(dateTime.getYear());

    System.out.println(String.format("%d-%02d", dateTime.getYear(), dateTime.getMonthValue()));
  }

  private static void durationTest() {
    Instant t0 = Instant.ofEpochSecond(1397805829);
    Instant t1 = Instant.now();
    Duration diff = Duration.between(t0, t1);
    System.out.println(diff);
    System.out.println(
        String.format("%d:%02d:%02d.%03d", diff.toHours(), diff.toMinutes() % 60, diff.getSeconds() % 60, diff.getNano() % 1000));
    System.out.println((t1.getEpochSecond() - t0.getEpochSecond()) / 60);
  }
}
