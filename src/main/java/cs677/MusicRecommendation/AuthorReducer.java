package cs677.MusicRecommendation;

import cs677.Writables.TextCountWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class AuthorReducer extends Reducer<Text, TextCountWritable, Text, Text> {

  @Override
  protected void reduce(Text key, Iterable<TextCountWritable> values, Context context)
      throws IOException, InterruptedException {
    HashMap<String, Integer> map = new HashMap<>();

    // sum up value
    for (TextCountWritable val : values) {
      int num = map.getOrDefault(val.getText(), 0);
      num += val.getCount();
      map.put(val.getText(), num);
    }

    TextCountWritable[] array = new TextCountWritable[map.size()];
    int i = 0;
    for (String k : map.keySet()) {
      TextCountWritable w = new TextCountWritable(k, map.get(k));
      array[i++] = w;
    }

    Arrays.sort(array, (o1, o2) -> (int) (o2.getCount() - o1.getCount()));
    context.write(key, new Text(Arrays.toString(array)));
  }
}
