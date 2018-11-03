package cs677.screamers;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class TextDoubleFlipMapper extends Mapper<Text, Text, DoubleWritable, Text> {
  @Override
  public void map(Text key, Text value, Context context) throws IOException, InterruptedException {
    double newVal = Double.parseDouble(value.toString());
    context.write(new DoubleWritable(newVal), key);
  }
}
