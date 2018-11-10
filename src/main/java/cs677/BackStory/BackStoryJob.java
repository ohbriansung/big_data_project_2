package cs677.BackStory;


import cs677.Writables.BackGroundKey;
import cs677.Writables.BackGroundWritable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.map.InverseMapper;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

public class BackStoryJob {

    public static void main(String[] args) {
        try{
            Configuration conf = new Configuration();
            Job job = Job.getInstance(conf,"BackStory Generation");
            job.setJarByClass(BackStoryJob.class);

            job.setMapperClass(BackStoryMapper.class);
            job.setReducerClass(BackStoryReducer.class);

            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(BackGroundWritable.class);

            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(BackGroundWritable.class);
            job.setOutputFormatClass(SequenceFileOutputFormat.class);

            FileInputFormat.addInputPath(job, new Path(args[0]));
            FileOutputFormat.setOutputPath(job, new Path(args[1] +"/temp"));

            job.waitForCompletion(true);

            Configuration conf2 = new Configuration();
            Job job2 = Job.getInstance(conf2,"Invert");
            job2.setInputFormatClass(SequenceFileInputFormat.class);
            job2.setJarByClass(BackStoryJob.class);


            job2.setMapperClass(InverseMapper.class);

            job2.setMapOutputKeyClass(BackGroundWritable.class);
            job2.setMapOutputValueClass(Text.class);

            FileInputFormat.addInputPath(job2, new Path(args[1] + "/temp"));
            FileOutputFormat.setOutputPath(job2, new Path(args[1] + "/final"));
            job2.waitForCompletion(true);

        }catch (Exception e){
            System.err.println(e.getMessage());
        }
    }

}


