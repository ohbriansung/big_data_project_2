package cs677;

import cs677.Writables.TextCountArrayWritable;
import cs677.Writables.TextCountWritable;
import org.apache.hadoop.io.Text;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.function.IntConsumer;

public class Sandbox {
  public static void main(String[] args) {
    new Sandbox();
  }

  Sandbox() {
    //    itemizeString();
    //    testPriorityQueue();
    //    jsonArrTest();
    //    instantNow();
    //    randTest();
    //        localDateTimeTest();
    //    durationTest();
    //    String str = createBigString();
    //    System.out.println("big string made");
    //    charSequenceStream(str);
    //    senWorSylCount();
    //    textIsEqual();
    jsonReadKeys();
  }

  private void itemizeString() {
    String location = "abc";
    for (int i = 0; i < 10; i++) {
      String numbered_location = String.format("%s_%04d", location, i);
      System.out.println(numbered_location);
    }
  }

  private void testPriorityQueue() {
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

  private void jsonArrTest() {
    String arr = "{\"arr\": [1, 2, 3, 4]}";
    JSONObject object = new JSONObject(arr);
    JSONArray jsonArray = object.getJSONArray("arr");
    System.out.println(jsonArray.toString());
  }

  private void instantNow() {
    System.out.println(Instant.now());
  }

  private void randTest() {
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

  private void localDateTimeTest() {
    long seconds = 1397805829;
    LocalDateTime dateTime = LocalDateTime.ofEpochSecond(seconds, 0, ZoneOffset.UTC);
    //            LocalDateTime.ofInstant(Instant.ofEpochMilli(seconds), ZoneId.of("UTC"));
    System.out.println(dateTime);
    System.out.println(dateTime.getMonthValue());
    System.out.println(dateTime.getYear());

    System.out.println(String.format("%d-%02d", dateTime.getYear(), dateTime.getMonthValue()));
  }

  private void durationTest() {
    Instant t0 = Instant.ofEpochSecond(1397805829);
    Instant t1 = Instant.now();
    Duration diff = Duration.between(t0, t1);
    System.out.println(diff);
    System.out.println(
        String.format(
            "%d:%02d:%02d.%03d",
            diff.toHours(), diff.toMinutes() % 60, diff.getSeconds() % 60, diff.getNano() % 1000));
    System.out.println((t1.getEpochSecond() - t0.getEpochSecond()) / 60);
  }

  private void charSequenceStream(String str) {
    Instant start = Instant.now();
    for (int i = 0; i < 500; i++) {
      //      long low = str.chars().filter(c -> c >= 'a' && c <= 'z').reduce(0, (a, b) -> a + 1);
      Averager avg =
          str.chars()
              .filter(c -> (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))
              .collect(Averager::new, Averager::accept, Averager::combine);
    }
    System.out.println(Duration.between(start, Instant.now()).toMillis());

    start = Instant.now();
    for (int i = 0; i < 500; i++) {
      long low = 0;
      long up = 0;
      for (char c : str.toCharArray()) {
        if (c >= 'a' && c <= 'z') low += 1;
        else if (c >= 'A' && c <= 'Z') up += 1;
      }
    }
    System.out.println(Duration.between(start, Instant.now()).toMillis());
  }

  private String createBigString() {
    String str = "abcdABCDE!";
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 1000000; i++) {
      sb.append(str);
    }
    return sb.toString();
  }

  private void senWorSylCount() {
    String body =
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent tincidunt lorem non convallis varius. Nulla facilisi. Nulla nec elementum velit, aliquet viverra neque. Fusce non mauris tempus, pretium orci ac, ullamcorper ex. Pellentesque in volutpat velit. Vestibulum pharetra, sapien id consectetur tincidunt, dui leo varius justo, ac varius orci risus et nibh. Morbi in leo ac elit vehicula lacinia. Integer viverra sapien nunc, vel bibendum sem faucibus a. Nullam at varius leo. Proin ante eros, volutpat volutpat venenatis ac, placerat pretium ante. Aliquam consectetur egestas metus. Phasellus placerat diam et lorem lobortis auctor.\n"
            + "\n"
            + "Mauris ante quam, tempus at accumsan vel, tempus viverra nibh. Quisque euismod at eros id finibus. Fusce quis tristique felis, in convallis dolor. Suspendisse a diam vel orci mollis faucibus. Quisque eleifend, odio ut hendrerit molestie, nibh lacus faucibus lorem, non mollis.";

    // https://www.programiz.com/java-programming/examples/alphabet
    char[] chars = body.toCharArray();
    long sylCount = 0;
    long worCount = 0;
    long senCount = 0;
    boolean inWord = false;
    for (int i = 0; i < chars.length; i++) {
      if (i == chars.length - 1) {
        switch (chars[i]) {
          case 'a':
            sylCount += 1;
            break;
          case 'e':
            sylCount += 1;
            break;
          case 'i':
            sylCount += 1;
            break;
          case 'o':
            sylCount += 1;
            break;
          case 'u':
            sylCount += 1;
            break;
          case 'y':
            sylCount += 1;
            break;
          default:
            break;
        }
        worCount += 1;
        senCount += 1;
      } else {
        if (isPunct(chars[i])) {
          if (inWord) worCount += 1;
          inWord = false;
          if (!isPunct(chars[i + 1])) senCount += 1;
        }
        if (Character.isWhitespace(chars[i])) {
          if (inWord) worCount += 1;
          inWord = false;
        }
        if (Character.isAlphabetic(chars[i])) {
          inWord = true;
          switch (chars[i]) {
            case 'a':
              if (chars[i + 1] != 'i' || chars[i + 1] != 'y') sylCount += 1;
              break;
            case 'e':
              if (chars[i + 1] != 'i' || chars[i + 1] != 'y') sylCount += 1;
              break;
            case 'i':
              sylCount += 1;
              break;
            case 'o':
              if (chars[i + 1] != 'i' || chars[i + 1] != 'u' || chars[i + 1] != 'y') sylCount += 1;
              break;
            case 'u':
              sylCount += 1;
              break;
            case 'y':
              sylCount += 1;
              break;
            default:
              break;
          }
        }
      }
    }
    System.out.println(senCount);
    System.out.println(worCount);
    System.out.println(sylCount);
  }

  private boolean isPunct(char chr) {
    return chr == '.' || chr == '!' || chr == '?' || chr == '~';
  }

  private void textIsEqual() {
    HashSet<Text> hashSet = new HashSet<>();

    hashSet.add(new Text("a"));
    hashSet.add(new Text("a"));
    hashSet.add(new Text("a"));
    hashSet.add(new Text());
    hashSet.add(new Text());

    for (Text text : hashSet) {
      System.out.println("\"" + text + "\"");
    }
  }

  private void jsonReadKeys() {
    String jsonString = "{\"a\": 1, \"b\": 2}";
    JSONObject jsonObject = new JSONObject(jsonString);
    for (String key : jsonObject.keySet()) {
      System.out.println(jsonObject.get(key));
    }
  }

  class Averager implements IntConsumer {
    private int count = 0;
    private int total = 0;

    Averager() {}

    public double average() {
      return total > 0 ? ((double) count) / total : 0;
    }

    public void accept(int c) {
      if (c >= 'A' && c <= 'Z') {
        count += 1;
      }
      total++;
    }

    public void combine(Averager other) {
      count += other.count;
      total += other.total;
    }
  }
}
