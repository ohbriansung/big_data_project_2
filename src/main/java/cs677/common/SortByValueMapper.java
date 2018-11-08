package cs677.common;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class SortByValueMapper extends Mapper<LongWritable, Text, IntWritable, Text> {

    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        String line = value.toString();
        String[] tokens = line.split("\\s+");
        String keypart = tokens[0];
        int valuePart = Integer.parseInt(tokens[1]);

        context.write(new IntWritable(valuePart), new Text(keypart));
    }
}
