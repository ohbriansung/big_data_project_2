package cs677.ComplexWord;


import com.kennycason.fleschkincaid.FleschKincaid;
import cs677.Writables.Readablity;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.json.JSONObject;

import java.io.IOException;
import java.util.StringTokenizer;

public class ComplexSpeakerMapper extends Mapper<LongWritable, Text, Text, Readablity> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        JSONObject obj = new JSONObject(value.toString());
        Configuration configuration = context.getConfiguration();

        String subreddit = obj.getString("subreddit");
        String author = obj.getString("author");
        String body = obj.getString("body");
        FleschKincaid fleschKincaid = new FleschKincaid();



        if("all".equals(configuration.get("subreddit"))){
            context.write(new Text(subreddit), new Readablity(fleschKincaid.calculate(body),gunnfog(body)));
        }

        if(subreddit.equals(configuration.get("subreddit"))){
            context.write(new Text(author), new Readablity(fleschKincaid.calculate(body),gunnfog(body)));
        }


    }

    private double gunnfog(String s){

        StringTokenizer itr = new StringTokenizer(s);
        int wordcount = 0;
        int complexcount = 0;
        int sentences = 0;
        while (itr.hasMoreTokens()) {
            String word = itr.nextToken();
            wordcount += 1;
            if(itr.nextToken().length() > 6)
                complexcount += 1;
            if(word.endsWith(".") || word.endsWith("?") || word.endsWith("!")){
                sentences += 1;
            }
        }
        if( sentences == 0)
            sentences = 1;



        return 0.4 * ((((double) wordcount)/(double)sentences) + 100* ((double)complexcount)/ ((double)wordcount));
    }
}
