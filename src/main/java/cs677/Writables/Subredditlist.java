package cs677.Writables;

import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class Subredditlist extends ArrayWritable {

    public Subredditlist() {
        super(Subreddits.class);
    }

    public Subredditlist(Writable[] values) {
        super(Subreddits.class, values);
    }


    @Override
    public void write(DataOutput out) throws IOException {
        super.write(out);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        super.readFields(in);
    }

    @Override
    public Writable[] get() {
        return (super.get());
    }

    @Override
    public String toString() {
        Writable[] writables = super.get();
        Subreddits[] subreddits = new Subreddits[writables.length];
        for(int i = 0; i < writables.length; i++){
            subreddits[i] = (Subreddits) writables[i];
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Liked Subreddits: ");
        for (Subreddits writable : subreddits) {
            sb.append("\t" + writable.getSubreddit() + ":" + writable.getCount().toString() + "\n");
        }
        sb.append("}");
        return sb.toString();
    }
}
