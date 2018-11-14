package cs677.MusicRecommendation;

import cs677.Writables.TextCountWritable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * This is the main class. Hadoop will invoke the main method of this class.
 */
public class MusicRecommendationJob {
    public static void main(String[] args) {
        Configuration conf = new Configuration();
        conf.setStrings("mapreduce.reduce.shuffle.memory.limit.percent", "0.15");

        /* Job 1: get word count for author */
        authorJob(args, conf);

        /* Job 2: sort word count by value */
        recommendJob(args, conf);
    }

    private static void authorJob(String[] args, Configuration conf) {
        try {
            Job job = Job.getInstance(conf, "author word count job");
            job.setJarByClass(MusicRecommendationJob.class);

            /* Mapper class */
            job.setMapperClass(AuthorMapper.class);

            /* Reducer class */
            job.setReducerClass(AuthorReducer.class);

            /* Outputs from the Mapper and Reducer. */
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(TextCountWritable.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(Text.class);

            /* Job input path in HDFS */
            FileInputFormat.addInputPath(job, new Path(args[0]));

            /* Job output path in HDFS*/
            FileOutputFormat.setOutputPath(job, new Path(args[1] + "/temp"));

            /* Wait (block) for the job to complete... */
            job.waitForCompletion(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void recommendJob(String[] args, Configuration conf) {
        try {
            Job job = Job.getInstance(conf, "music recommendation job");
            job.setJarByClass(MusicRecommendationJob.class);

            /* Mapper class */
            job.setMapperClass(MusicRecommendationMapper.class);

            /* Reducer class */
            job.setReducerClass(MusicRecommendationReducer.class);

            /* Outputs from the Mapper and Reducer. */
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(Text.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(Text.class);

            /* Job input path in HDFS */
            FileInputFormat.addInputPath(job, new Path(args[1] + "/temp/part-r-00000"));

            /* Job output path in HDFS */
            FileOutputFormat.setOutputPath(job, new Path(args[1] + "/final"));

            /* Wait (block) for the job to complete... */
            job.waitForCompletion(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
