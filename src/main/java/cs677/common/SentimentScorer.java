package cs677.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;

public class SentimentScorer {

  public SentimentScorer() {
    try {
      makeMap();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private HashMap<String, Integer> sentiment = new HashMap<>();

  public double sentimentScore(String text) {
    double sentScore = 0;
    double count = 0;
    StringTokenizer itr = new StringTokenizer(text);
    // emit word, count pairs.
    while (itr.hasMoreTokens()) {
      count += 1;
      sentScore += sentiment.getOrDefault(itr.nextToken(), 0);
    }
    return sentScore / count;
  }

  private void makeMap() throws IOException {
    String line;
    InputStream in = getClass().getResourceAsStream("/AFINN-111.txt");
    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    while ((line = reader.readLine()) != null) {
      String[] parts = line.split("\t");
      if (parts.length >= 2) {
        String key = parts[0];
        int value = Integer.parseInt(parts[1]);
        sentiment.put(key, value);
      } else {
        System.out.println("ignoring line: " + line);
      }
    }
  }
}
