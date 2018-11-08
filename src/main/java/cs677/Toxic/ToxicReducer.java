package cs677.Toxic;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class ToxicReducer extends Reducer<Text, DoubleWritable,Text,DoubleWritable> {

    @Override
    protected void reduce(Text key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {
        double sentscore = 0;
        double sentencecount = 0;
        for(DoubleWritable val : values){
            sentencecount += 1;
            sentscore += val.get();
        }
        //sentscore = sentscore/sentencecount;
        context.write(key,new DoubleWritable(sentscore));
    }
}
