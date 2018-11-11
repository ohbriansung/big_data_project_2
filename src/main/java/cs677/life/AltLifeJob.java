package cs677.life;

import cs677.BackStory.BackStoryJob;
import cs677.BackStory.BackStoryMapper;
import cs677.BackStory.BackStoryReducer;
import cs677.Writables.BackGroundWritable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.map.InverseMapper;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

public class AltLifeJob {
    public static void main(String[] args) {


        try {
            Configuration conf = new Configuration();
            conf.set("user",args[2]);
            Job job = Job.getInstance(conf, "Life Tracer");
            job.setJarByClass(LifeJob.class);

            job.setMapperClass(Lifemapper.class);
            job.setReducerClass(LifeReducer.class);

            job.setMapOutputKeyClass(IntWritable.class);
            job.setMapOutputValueClass(BackGroundWritable.class);

            job.setOutputKeyClass(IntWritable.class);
            job.setOutputValueClass(BackGroundWritable.class);

            FileInputFormat.addInputPath(job, new Path(args[0]));
            FileOutputFormat.setOutputPath(job, new Path(args[1]));

            job.waitForCompletion(true);



        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
