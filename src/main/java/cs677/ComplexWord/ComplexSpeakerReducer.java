package cs677.ComplexWord;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class ComplexSpeakerReducer extends Reducer<Text, DoubleWritable,Text,DoubleWritable> {

    @Override
    protected void reduce(Text key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {
        double count = 0;
        double sum = 0;
        for(DoubleWritable val : values){
            count += 1;
            sum += val.get();
        }
        if(count > 0)
            context.write(key,new DoubleWritable(sum/count));
    }
}
