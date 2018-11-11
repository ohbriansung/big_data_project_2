package cs677.MusicRecommendation;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MusicRecommendationReducer extends Reducer<Text, Text, Text, Text> {
    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context)
            throws IOException, InterruptedException {
        boolean checkTry = false;
        boolean checkRec = false;
        List<String> array = new ArrayList<>();
        for (Text val : values) {
            if (checkRec && checkTry) {
                break;
            }

            if (val.toString().startsWith("try") && !checkTry) {
                array.add(val.toString());
                checkTry = true;
            }
            else if (val.toString().startsWith("rec") && !checkRec) {
                array.add(val.toString());
                checkRec = true;
            }
        }
        context.write(key, new Text(array.toString()));
    }
}
