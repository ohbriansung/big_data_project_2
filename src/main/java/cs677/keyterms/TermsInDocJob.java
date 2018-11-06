package cs677.keyterms;

import cs677.common.StringLongReducer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.json.JSONObject;

import java.io.IOException;

public class TermsInDocJob {
  public static void main(String[] args) {
    if (args.length != 2) {
      System.out.println("args required: <input> <output>");
      System.exit(-1);
    }
    try {
      Configuration conf = new Configuration();
      Job job = new Job(conf, "terms in doc");

      job.setJarByClass(TermsInDocJob.class);

      job.setInputFormatClass(KeyValueTextInputFormat.class);
      job.setMapOutputKeyClass(Text.class);
      job.setMapOutputValueClass(LongWritable.class);

      job.setOutputKeyClass(Text.class);
      job.setOutputValueClass(LongWritable.class);

      job.setMapperClass(TermsInDocMapper.class);
      job.setReducerClass(StringLongReducer.class);

      boolean completed = job.waitForCompletion(true);



    } catch (IOException | InterruptedException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  private static class TermsInDocMapper extends Mapper<Text, Text, Text, LongWritable> {
    @Override
    protected void map(Text key, Text value, Context context)
        throws IOException, InterruptedException {
      JSONObject jsonObject = new JSONObject(value.toString());
      for (String document : jsonObject.keySet()) {
        context.write(new Text(document), new LongWritable(jsonObject.getLong(document)));
      }
    }
  }
}
