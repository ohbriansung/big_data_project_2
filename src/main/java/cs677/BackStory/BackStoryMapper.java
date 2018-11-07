package cs677.BackStory;


import com.kennycason.fleschkincaid.FleschKincaid;
import cs677.Writables.BackGroundWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.json.JSONObject;

import java.io.IOException;

public class BackStoryMapper extends Mapper<LongWritable, Text, Text, BackGroundWritable> {

    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        // tokenize into words.
        JSONObject obj = new JSONObject(value.toString());

        String[] subreddits = new String[10];
        subreddits[0] = obj.getString("subreddit");

        int ups = obj.getInt("ups");
        FleschKincaid fleschKincaid = new FleschKincaid();
        double readscore = fleschKincaid.calculate(obj.getString("body"));
        String location = getLocation(obj.getString("body"));
        int commentcount = 1;

        BackGroundWritable background = new BackGroundWritable(ups,readscore,location,commentcount);

        String user = obj.getString("author");
        context.write(new Text(user),background);
    }

    private String getLocation(String body){
        return "no location";
    }
}
