package cs677.TScreamer;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class ScreamerCombiner extends Reducer<Text, DoubleWritable,Text, DoubleWritable> {
    @Override
    protected void reduce(Text key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {
        double count = 0;
        double upper = 0;
        for(DoubleWritable val : values)
        {
            count += 1;
            upper += val.get();
        }

        double screamerscore = upper/count;
        context.write(key,new DoubleWritable(screamerscore));
    }
}
