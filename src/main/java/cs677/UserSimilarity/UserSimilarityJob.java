package cs677.UserSimilarity;

import cs677.Writables.AuthorWordsWritable;
import cs677.common.Constants;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class UserSimilarityJob {
    public static void main(String[] args) {
        if (args.length < 4) {
            System.out.println("Args: <input> <output> <subreddit> <user>");
            System.exit(-1);
        }

        Configuration conf = new Configuration();

        conf.setStrings(Constants.ARCHIVED, args[1]);
        conf.setStrings(Constants.SUBREDDIT, args[2]);
        conf.setStrings(Constants.AUTHOR, args[3]);

        /* job 1: author word count in subreddit */
        subredditWordCountJob(args, conf);

        /* job 2: bloom filter to match users' words */
        bloomFilterJob(args, conf);
    }

    private static void subredditWordCountJob(String[] args, Configuration conf) {
        try {
            Job job = Job.getInstance(conf, "User similarity job: subreddit author word list");
            job.setJarByClass(UserSimilarityJob.class);

            /* Mapper class */
            job.setMapperClass(SubredditAuthorWordListMapper.class);

            /* Reducer class */
            job.setReducerClass(SubredditAuthorWordListReducer.class);

            /* Outputs from the Mapper and Reducer. */
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(Text.class);
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

    private static void bloomFilterJob(String[] args, Configuration conf) {
        try {
            Job job = Job.getInstance(conf, "User similarity job: bloom filter");
            job.setJarByClass(UserSimilarityJob.class);

            /* Mapper class */
            job.setMapperClass(UserSimilarityMapper.class);

            /* Reducer class */
            job.setReducerClass(UserSimilarityReducer.class);

            /* Outputs from the Mapper and Reducer. */
            job.setMapOutputKeyClass(DoubleWritable.class);
            job.setMapOutputValueClass(AuthorWordsWritable.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(Text.class);

            /* Job input path in HDFS */
            FileInputFormat.addInputPath(job, new Path(args[1] + "/temp/part-r-00000"));

            /* Job output path in HDFS*/
            FileOutputFormat.setOutputPath(job, new Path(args[1] + "/final"));

            /* Wait (block) for the job to complete... */
            job.waitForCompletion(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
