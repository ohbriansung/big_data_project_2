package cs677.keyterms;

import cs677.common.Constants;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.json.JSONObject;

import java.io.IOException;
import java.util.StringTokenizer;

public class IdfMapper extends Mapper<LongWritable, Text, Text, Text> {
    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {

        JSONObject obj = new JSONObject(value.toString());

        String subreddit = obj.getString(Constants.SUBREDDIT);
        Text outValue = new Text(subreddit);

        StringTokenizer itr = new StringTokenizer(value.toString());
        while (itr.hasMoreTokens()) {
            context.write(new Text(itr.nextToken()), outValue);
        }
    }
}