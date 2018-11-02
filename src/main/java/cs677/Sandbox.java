package cs677;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.Instant;
import java.util.PriorityQueue;

public class Sandbox {
  public static void main(String[] args) {
    //    itemizeString();
//    testPriorityQueue();
//    jsonArrTest();
    instantNow();
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

}
