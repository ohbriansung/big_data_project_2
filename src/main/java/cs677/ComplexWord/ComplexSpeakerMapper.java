package cs677.ComplexWord;

import cs677.Writables.Readablity;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.json.JSONObject;

import java.io.IOException;
import java.util.StringTokenizer;

public class ComplexSpeakerMapper extends Mapper<LongWritable, Text, Text, Readablity> {

  @Override
  protected void map(LongWritable key, Text value, Context context)
      throws IOException, InterruptedException {
    JSONObject obj = new JSONObject(value.toString());
    Configuration configuration = context.getConfiguration();

    String subreddit = obj.getString("subreddit");
    String author = obj.getString("author");
    String body = obj.getString("body");

    if ("all".equals(configuration.get("subreddit"))) {
      //      context.write(new Text(subreddit), new Readablity(fscore, gscore, subreddit));
      context.write(new Text(subreddit), score(body, subreddit));
    }

    if (subreddit.equals(configuration.get("subreddit"))) {
      //      context.write(new Text(author), new Readablity(fscore, gscore, author));
      context.write(new Text(author), score(body, author));
    }
  }

  private Readablity score(String s, String key) {

    StringTokenizer itr = new StringTokenizer(s);
    int words = 0;
    int complexWords = 0;
    int sentences = 0;
    int syllables = 0;
    while (itr.hasMoreTokens()) {
      String word = itr.nextToken();
      words += 1;
      if (word.length() > 6) complexWords += 1;
      if (word.endsWith(".") || word.endsWith("?") || word.endsWith("!")) {
        sentences += 1;
      }
      syllables += syllableCount(word);
    }
    if (sentences == 0) sentences = 1;
    if (words == 0) words = 1;

    double fEase = fleschEase(sentences, words, syllables);
    double fGrade = fleschGrade(sentences, words, syllables);
    double gFog = gunnFog(sentences, words, complexWords);

    if (Double.isNaN(fEase)) fEase = 0;
    if (Double.isNaN(fGrade)) fGrade = 0;
    if (Double.isNaN(gFog)) gFog = 0;

    return new Readablity(fEase, fGrade, gFog, key);
  }

  private double gunnFog(int sentences, int words, int complexwords) {
    return 0.4 * ((((double) words) / sentences) + (100.0 * (double) complexwords) / words);
  }

  private double fleschEase(int sentences, int words, int syllables) {
    return 206.835 - (1.015 * ((double) words) / sentences) - (84.6 * ((double) syllables) / words);
  }

  private double fleschGrade(int sentences, int words, int syllables) {
    return (0.39 * ((double) words) / sentences) + (11.8 * ((double) syllables) / words) - 15.59;
  }

  private int syllableCount(String s) {
    int sylCount = 0;
    char[] chars = s.toCharArray();
    for (int i = 0; i < chars.length; i++) {
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
    return sylCount;
  }
}
