package cs677.UniqueWord;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class UniqueWordJob {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Args: <input> <output>");
            System.exit(-1);
        }

        Configuration conf = new Configuration();
        conf.setStrings("mapreduce.reduce.shuffle.memory.limit.percent", "0.15");
        termSubredditListJob(args, conf);
    }

    private static void termSubredditListJob(String[] args, Configuration conf) {
        try {
            Job job = Job.getInstance(conf, "Unique word job: term subreddit list");
            job.setJarByClass(UniqueWordJob.class);

            /* Mapper class */
            job.setMapperClass(UniqueWordMapper.class);

            /* Reducer class */
            job.setReducerClass(UniqueWordReducer.class);

            /* Outputs from the Mapper and Reducer. */
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(Text.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(Text.class);

            /* Job input path in HDFS */
            FileInputFormat.addInputPath(job, new Path(args[0]));

            /* Job output path in HDFS*/
            FileOutputFormat.setOutputPath(job, new Path(args[1]));

            /* Wait (block) for the job to complete... */
            job.waitForCompletion(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
