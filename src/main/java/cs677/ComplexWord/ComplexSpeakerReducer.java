package cs677.ComplexWord;

import cs677.Writables.Readablity;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class ComplexSpeakerReducer extends Reducer<Text, Readablity,Text,Readablity> {

    @Override
    protected void reduce(Text key, Iterable<Readablity> values, Context context) throws IOException, InterruptedException {
        double count = 0;
        double fsum = 0;
        double gsum = 0;
        for(Readablity val : values){
            count += 1;
            if ( val.getFletch().get() > 100)
                fsum += 100;
            else if (val.getFletch().get() < 0)
                fsum += 0;
            else
                fsum += val.getFletch().get();


            if ( val.getGunn().get() > 20)
                gsum += 20;
            else if (val.getGunn().get() < 0)
                gsum += 0;
            else
                gsum += val.getFletch().get();


            gsum += val.getGunn().get();
        }

        if(count > 50)
            context.write(key,new Readablity((fsum/count), (gsum/count)));
    }
}
