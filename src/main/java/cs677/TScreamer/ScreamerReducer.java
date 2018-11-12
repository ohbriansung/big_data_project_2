package cs677.TScreamer;


import cs677.Writables.UpperCount;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class ScreamerReducer extends Reducer<Text, UpperCount,Text, UpperCount> {
    @Override
    protected void reduce(Text key, Iterable<UpperCount> values, Context context) throws IOException, InterruptedException {
        double count = 0;
        double upper = 0;
        UpperCount counter = new UpperCount(count,upper);
        for(UpperCount val : values)
        {
            counter.addcounts(val.getCount().get(),val.getUppercount().get());
            count += 1;
        }
        if (count > 10)
            context.write(key,counter);
    }
}
