package cs677.BackStory;


import com.kennycason.fleschkincaid.FleschKincaid;
import cs677.Writables.BackGroundKey;
import cs677.Writables.BackGroundWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BackStoryMapper extends Mapper<LongWritable, Text, BackGroundKey, BackGroundWritable> {

    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        // tokenize into words.
        JSONObject obj = new JSONObject(value.toString());

        String subreddits = obj.getString("subreddit");

        int ups = obj.getInt("ups");
        FleschKincaid fleschKincaid = new FleschKincaid();
        double readscore = fleschKincaid.calculate(obj.getString("body"));
        String location = getLocation(obj.getString("body"));
        int commentcount = 1;

        BackGroundWritable background = new BackGroundWritable(ups,readscore,location,subreddits,commentcount);

        String user = obj.getString("author");
        context.write(new BackGroundKey(readscore,1,user),background);
    }

    private String getLocation(String body){
        String regex = "((i|we)('m|\\s*am|\\s*are)\\s*from\\s*([\\w\\d\\s]+))";
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(body);

        if (m.find()) {
            return m.group(4);
        }

        return "no location";
    }
}
