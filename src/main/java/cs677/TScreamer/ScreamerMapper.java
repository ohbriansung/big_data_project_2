package cs677.TScreamer;

import cs677.Writables.UpperCount;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;
import java.util.StringTokenizer;

public class ScreamerMapper extends Mapper<LongWritable,Text, Text, UpperCount> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        JSONObject obj = new JSONObject(value.toString());
        String user = obj.getString("author");
        String subreddit= obj.getString("subreddit");
        String body = obj.getString("body");

        double count = 0;
        double uppercount = 0;
        StringTokenizer itr = new StringTokenizer(body);
        while(itr.hasMoreTokens()){
            String token = itr.nextToken();
            String upper = token.toUpperCase();
            count += 1;
            if(upper.equals(token)){
                uppercount += 1;
            }
        }


        if(count > 0){
            Double uppercent = uppercount/count;
            context.write(new Text(subreddit), new UpperCount(count,uppercount));
        }



    }
}
