package cs677.readability;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.math.BigDecimal;

public class ReadabilityAvgReducer extends Reducer<Text, SenWorSylWritable, Text, FleschWritable> {
  @Override
  protected void reduce(Text key, Iterable<SenWorSylWritable> values, Context context)
      throws InterruptedException, IOException {
    long count = 0;
    BigDecimal grade = BigDecimal.ZERO;
    BigDecimal ease = BigDecimal.ZERO;
    for (SenWorSylWritable value : values) {
      long sent = value.getSentences();
      long word = value.getWords();
      long syll = value.getSyllables();
      grade = grade.add(new BigDecimal(FleschWritable.gradeScore(sent, word, syll)));
      ease = ease.add(new BigDecimal(FleschWritable.easeScore(sent, word, syll)));
      count += 1;
    }
    grade = grade.divide(new BigDecimal(count), BigDecimal.ROUND_HALF_EVEN);
    ease = ease.divide(new BigDecimal(count), BigDecimal.ROUND_HALF_EVEN);
    FleschWritable fleschWritable = new FleschWritable(ease.doubleValue(), grade.doubleValue());
    context.write(key, fleschWritable);
  }
}
