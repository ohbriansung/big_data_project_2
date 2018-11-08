package cs677.MusicRecommendation;

import cs677.common.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * This is the main class. Hadoop will invoke the main method of this class.
 */
public class MusicRecommendationJob {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Args: <input> <output> <user>");
            System.exit(-1);
        }

        Configuration conf = new Configuration();

        conf.setStrings(Constants.AUTHOR, args[2]);
        System.out.println("Author: " + args[2]);

        /* Job 1: get word count for author */
        authorJob(args, conf);

        /* Job 2: sort word count by value */
        sortingJob(args, conf);
    }

    private static void authorJob(String[] args, Configuration conf) {
        try {
            Job job = Job.getInstance(conf, "author word count job");
            job.setJarByClass(MusicRecommendationJob.class);

            /* Mapper class */
            job.setMapperClass(AuthorMapper.class);

            /* Reducer class */
            job.setReducerClass(StringIntReducer.class);

            /* Outputs from the Mapper and Reducer. */
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(IntWritable.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(IntWritable.class);

            /* Job input path in HDFS */
            FileInputFormat.addInputPath(job, new Path(args[0]));

            /* Job output path in HDFS*/
            FileOutputFormat.setOutputPath(job, new Path(args[1] + "/word-" + args[2]));

            /* Wait (block) for the job to complete... */
            job.waitForCompletion(true);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private static void sortingJob(String[] args, Configuration conf) {
        try {
            Job job = Job.getInstance(conf, "author word count sorting job");
            job.setJarByClass(MusicRecommendationJob.class);

            /* Mapper class */
            job.setMapperClass(SortByValueMapper.class);

            /* Reducer class */
            job.setReducerClass(SortByValueReducer.class);

            /* Outputs from the Mapper and Reducer. */
            job.setMapOutputKeyClass(IntWritable.class);
            job.setMapOutputValueClass(Text.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(IntWritable.class);

            /* Descending sorting */
            job.setSortComparatorClass(IntComparator.class);

            /* Job input path in HDFS */
            FileInputFormat.addInputPath(job, new Path(args[1] + "/word-" + args[2]));

            /* Job output path in HDFS */
            FileOutputFormat.setOutputPath(job, new Path(args[1] + "/sorted-" + args[2]));

            /* Wait (block) for the job to complete... */
            job.waitForCompletion(true);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private static void musicRecJob(String[] args, Configuration conf) {
        try {
            Job job = Job.getInstance(conf, "author word count sorting job");
            job.setJarByClass(MusicRecommendationJob.class);

            /* Mapper class */
            job.setMapperClass(MusicRecommendationMapper.class);

            /* Reducer class */
            job.setReducerClass(MusicRecommendationReducer.class);

            /* Outputs from the Mapper and Reducer. */
            job.setMapOutputKeyClass(IntWritable.class);
            job.setMapOutputValueClass(Text.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(IntWritable.class);

            /* Descending sorting */
            job.setSortComparatorClass(IntComparator.class);

            /* Job input path in HDFS */
            FileInputFormat.addInputPath(job, new Path(args[1] + "/word-" + args[2]));

            /* Job output path in HDFS */
            FileOutputFormat.setOutputPath(job, new Path(args[1] + "/sorted-" + args[2]));

            /* Wait (block) for the job to complete... */
            job.waitForCompletion(true);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
