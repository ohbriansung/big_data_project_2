package cs677.ComplexWord;

import cs677.Writables.Readablity;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class ComplexSpeakerReducer extends Reducer<Text, Readablity, NullWritable, Readablity> {

  private static final BigDecimal HUNDRED = new BigDecimal(100);
  private static final BigDecimal TWENTY = new BigDecimal(20);

  @Override
  protected void reduce(Text key, Iterable<Readablity> values, Context context)
      throws IOException, InterruptedException {
    double count = 0;
    BigDecimal fEaseSum = BigDecimal.ZERO;
    BigDecimal fGradeSum = BigDecimal.ZERO;
    BigDecimal gunnSum = BigDecimal.ZERO;
    for (Readablity val : values) {
      count += 1;

      if (val.getFleschEase() > 100) fEaseSum = fEaseSum.add(HUNDRED);
      else if (val.getFleschEase() > 0)
        fEaseSum = fEaseSum.add(new BigDecimal(val.getFleschEase()));

      if (val.getFleschGrade() > 20) fGradeSum = fGradeSum.add(TWENTY);
      else if (val.getFleschGrade() > 0)
        fGradeSum = fGradeSum.add(new BigDecimal(val.getFleschGrade()));

      if (val.getGunn() > 20) gunnSum = gunnSum.add(TWENTY);
      else if (val.getGunn() > 0) gunnSum = gunnSum.add(new BigDecimal(val.getGunn()));
    }

    BigDecimal bigCount = new BigDecimal(count);

    if (count > 50) {
      context.write(
          NullWritable.get(),
          new Readablity(
              fEaseSum.divide(bigCount, RoundingMode.HALF_UP).doubleValue(),
              fGradeSum.divide(bigCount, RoundingMode.HALF_UP).doubleValue(),
              gunnSum.divide(bigCount, RoundingMode.HALF_UP).doubleValue(),
              key.toString()));
    }
  }
}
