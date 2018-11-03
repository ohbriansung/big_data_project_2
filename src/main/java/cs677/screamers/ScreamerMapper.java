package cs677.screamers;

import cs677.Writables.CountTotalWritable;
import cs677.common.Constants;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class ScreamerMapper extends Mapper<LongWritable, Text, Text, CountTotalWritable> {

  private static final String screamString = "ABCDEFGHJIKLMNOPQRSTUVWXYZ!";
  private static final String normalString = "abcdefghijklmnopqrstuvwxyz.";

  private static final HashSet<Character> screamCharSet =
      new HashSet<>(toCharacterList(screamString));
  private static final HashSet<Character> normalCharSet =
      new HashSet<>(toCharacterList(normalString));

  private static List<Character> toCharacterList(String str) {
    if (str == null) {
      return new ArrayList<>();
    }
    char[] chars = str.toCharArray();
    Character[] characters = new Character[chars.length];
    for (int i = 0; i < chars.length; i++) {
      characters[i] = chars[i];
    }
    return Arrays.asList(characters);
  }

  @Override
  protected void map(LongWritable key, Text value, Context context)
      throws IOException, InterruptedException {
    JSONObject obj = new JSONObject(value.toString());
    String author = obj.getString(Constants.AUTHOR);
    String body = obj.getString(Constants.BODY);
    Text out_key = new Text(author);
    long count = 0;
    long total = 0;
    for (Character chr : body.toCharArray()) {
      if (screamCharSet.contains(chr)) {
        count += 1;
        total += 1;
      }
      if (normalCharSet.contains(chr)) {
        total += 1;
      }
    }
    context.write(out_key, new CountTotalWritable(count, total));
  }
}
