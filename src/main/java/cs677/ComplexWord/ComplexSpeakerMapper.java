package cs677.ComplexWord;


import com.kennycason.fleschkincaid.FleschKincaid;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.json.JSONObject;

import java.io.IOException;

public class ComplexSpeakerMapper extends Mapper<LongWritable, Text, Text, DoubleWritable> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        JSONObject obj = new JSONObject(value.toString());
        Configuration configuration = context.getConfiguration();

        String subreddit = obj.getString("subreddit");
        String author = obj.getString("author");
        String body = obj.getString("body");
        FleschKincaid fleschKincaid = new FleschKincaid();
        if(subreddit.equals(configuration.get("subreddit"))){
            context.write(new Text(author), new DoubleWritable(fleschKincaid.calculate(body)));
        }

        if("all".equals(configuration.get("subreddit"))){
            context.write(new Text(subreddit), new DoubleWritable(fleschKincaid.calculate(body)));
        }

    }
}
