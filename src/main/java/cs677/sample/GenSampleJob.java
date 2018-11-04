package cs677.sample;

import cs677.common.FileCreator;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.time.Instant;
import java.util.Random;

/**
 * Use
 * https://hadoop.apache.org/docs/current/hadoop-project-dist/hadoop-common/FileSystemShell.html#getmerge
 * to merge the outputs
 *
 * <p>getmerge example hadoop fs -getmerge [-nl] <src> <localdst>
 */
public class GenSampleJob {
  public static void main(String[] args) {
    try {
      Configuration conf = new Configuration();

      /* Setup */
      Job job = Job.getInstance(conf, "0.0001 sample job");
      job.setJarByClass(GenSampleJob.class);

      /* Mapper */
      job.setMapperClass(GenSampleMapper.class);
      job.setMapOutputKeyClass(Text.class);
      job.setMapOutputValueClass(NullWritable.class);

      /* Reducer */
      job.setNumReduceTasks(0);
      job.setOutputKeyClass(Text.class);
      job.setOutputValueClass(NullWritable.class);

      /* Input path */
      FileInputFormat.addInputPath(job, new Path(args[0]));
      System.out.println("Input path: " + args[0]);

      /* Output path */
      Path outPath = FileCreator.findEmptyPath(conf, args[1]);
      System.out.println("Output path: " + outPath.toString());
      FileOutputFormat.setOutputPath(job, outPath);

      /* Wait job to complete */
      boolean completed = job.waitForCompletion(true);
      System.out.println(Instant.now());
      System.out.println("Input path: " + args[0]);
      System.out.println("Output path: " + outPath.toString());

      System.exit(completed ? 0 : 1);

    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  private static class GenSampleMapper extends Mapper<LongWritable, Text, Text, NullWritable> {
    private NullWritable out = NullWritable.get();
    private Random rand = new Random();

    public GenSampleMapper() {}

    @Override
    protected void map(LongWritable key, Text value, Context context)
        throws IOException, InterruptedException {
      float chance = rand.nextFloat();

      if (chance >= 0.0001) {
        return;
      }

      context.write(value, out);
    }
  }
}
