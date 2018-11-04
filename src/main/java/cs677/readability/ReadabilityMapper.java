package cs677.readability;

import cs677.common.Constants;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;

public class ReadabilityMapper extends Mapper<LongWritable, Text, Text, SenWorSylWritable> {

  private Random random = new Random();

  @Override
  protected void map(LongWritable key, Text value, Context context)
      throws IOException, InterruptedException {

    if (random.nextFloat() > 0.05) return;

    JSONObject obj = new JSONObject(value.toString());

    Configuration conf = context.getConfiguration();
    boolean allSubs = conf.getBoolean(ReadabilityJob.ALL_SUBS_KEY, false);
    String subToParse = conf.get(ReadabilityJob.SUB_PARSING_KEY);
    String subreddit = obj.getString(Constants.SUBREDDIT);

    if (allSubs || !subToParse.equals(subreddit)) return;

    String body = obj.getString(Constants.BODY).toLowerCase();

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

    context.write(new Text(subreddit), new SenWorSylWritable(senCount, worCount, sylCount));
  }

  private boolean isPunct(char chr) {
    return chr == '.' || chr == '!' || chr == '?' || chr == '~';
  }
}
