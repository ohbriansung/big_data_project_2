package cs677.recordcount;

import cs677.misc.FileCreator;
import cs677.misc.JsonOutputFormat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/** This is the main class. Hadoop will invoke the main method of this class. */
public class RecordCountJob {
  public static void main(String[] args) {
    try {
      Configuration conf = new Configuration();

      /* Job Name. You'll see this in the YARN webapp */
      Job job = Job.getInstance(conf, "record count job");
      /* Current class */
      job.setJarByClass(RecordCountJob.class);

      /* Mapper class */
      job.setMapperClass(RecordCountMapper.class);

      /* Combiner class */
      job.setCombinerClass(RecordCountReducer.class);

      /* Reducer class */
      job.setReducerClass(RecordCountReducer.class);

      /* Outputs from the Mapper. */
      job.setMapOutputKeyClass(Text.class);
      job.setMapOutputValueClass(IntWritable.class);

      /* Outputs from the Reducer */
      job.setOutputKeyClass(Text.class);
      job.setOutputValueClass(IntWritable.class);
      job.setOutputFormatClass(JsonOutputFormat.class);

      /* Job input path in HDFS */
      FileInputFormat.addInputPath(job, new Path(args[0]));

      /* Job output path in HDFS. */
      Path outPath = FileCreator.findEmptyPath(conf, args[1]);
      FileOutputFormat.setOutputPath(job, outPath);

      /* Wait (block) for the job to complete... */
      boolean completed = job.waitForCompletion(true);
      if (completed) {
        parseOutput(conf, outPath);
      }
      System.exit(completed ? 0 : 1);

    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  private static void parseOutput(Configuration conf, Path path) throws IOException {
    FileSystem fs = path.getFileSystem(conf);
    RemoteIterator<LocatedFileStatus> fileStatusListIterator = fs.listFiles(path, false);
    int total_count = 0;
    int subreddit_count = 0;
    while (fileStatusListIterator.hasNext()) {
      LocatedFileStatus fileStatus = fileStatusListIterator.next();
      if (fileStatus.isFile()) {
        BufferedReader br =
            new BufferedReader(new InputStreamReader(fs.open(fileStatus.getPath())));
        String line = br.readLine();
        while (line != null) {
          JSONObject obj = new JSONObject(line);
          int post_count = Integer.parseInt(obj.getString("value"));
          total_count += post_count;
          subreddit_count += 1;
          line = br.readLine();
        }
      }
    }
    System.out.println(String.format("Number of subreddits: %d", subreddit_count));
    System.out.println(String.format("Total number of entries: %d", total_count));
  }
}
