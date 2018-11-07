package cs677.ComplexWord;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.map.InverseMapper;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

public class ComplexSpeakerJob {
    public static void main(String[] args) {
        try{
            Configuration conf = new Configuration();
            conf.set("subreddit", args[0]);
            Job job = Job.getInstance(conf,"Readability");
            job.setJarByClass(ComplexSpeakerJob.class);

            job.setMapperClass(ComplexSpeakerMapper.class);
            job.setCombinerClass(ComplexSpeakerReducer.class);
            job.setReducerClass(ComplexSpeakerReducer.class);

            job.setNumReduceTasks(12);

            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(DoubleWritable.class);

            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(DoubleWritable.class);
            job.setOutputFormatClass(SequenceFileOutputFormat.class);

            FileInputFormat.addInputPath(job, new Path(args[1]));
            FileOutputFormat.setOutputPath(job, new Path(args[2]  +"/temp"));

            job.waitForCompletion(true);

            Configuration conf2 = new Configuration();
            Job job2 = Job.getInstance(conf2,"Reverse Map");

            job2.setInputFormatClass(SequenceFileInputFormat.class);
            job2.setJarByClass(ComplexSpeakerJob.class);

            job2.setMapperClass(InverseMapper.class);

            job2.setMapOutputKeyClass(DoubleWritable.class);
            job2.setMapOutputValueClass(Text.class);

            FileInputFormat.addInputPath(job2, new Path(args[2] + "/temp"));
            FileOutputFormat.setOutputPath(job2, new Path(args[2] +"/final" ));

            job2.waitForCompletion(true);


        }catch (Exception e){
            System.err.println(e.getMessage());
        }
    }
}
