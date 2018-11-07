package cs677.BackStory;

import Writables.BackGroundWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class BackStoryReducer extends Reducer<Text, BackGroundWritable, Text,BackGroundWritable> {


    @Override
    protected void reduce(Text key, Iterable<BackGroundWritable> values, Context context) throws IOException, InterruptedException {

        String[] likes = new String[10];
        BackGroundWritable backGroundWritable = new BackGroundWritable(0,0,"none",0);
        for(BackGroundWritable value : values){
            backGroundWritable.append(value.getUpvotes().get(),value.getReadability_score().get(),value.getLocation().toString(),value.getCommentcount().get());
        }

        context.write(key, backGroundWritable);



    }
}
