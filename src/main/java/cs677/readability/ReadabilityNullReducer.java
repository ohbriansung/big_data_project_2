package cs677.readability;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class ReadabilityNullReducer
    extends Reducer<Text, SenWorSylWritable, FleschWritable, NullWritable> {
  private static final NullWritable outVal = NullWritable.get();

  @Override
  protected void reduce(Text key, Iterable<SenWorSylWritable> values, Context context)
      throws InterruptedException, IOException {
    for (SenWorSylWritable value : values) {
      long sent = value.getSentences();
      long word = value.getWords();
      long syll = value.getSyllables();
      context.write(new FleschWritable(sent, word, syll), outVal);
    }
  }
}
