package cs677.Writables;

import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.json.JSONObject;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class AuthorWordsWritable implements Writable {
    private final Text author;
    private final ArrayWritable wordList;

    // disable empty constructor
    private AuthorWordsWritable() {
        this.author = new Text();
        this.wordList = new ArrayWritable(new String[0]);
    }

    public AuthorWordsWritable(String author, String[] wordList) {
        this.author = new Text(author);
        this.wordList = new ArrayWritable(wordList);
    }

    public String getAuthor() {
        return this.author.toString();
    }

    public String[] getWordList() {
        Writable[] w = this.wordList.get();
        String[] s = new String[w.length];

        for (int i = 0; i < w.length; i++) {
            s[i] = w[i].toString();
        }

        return s;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        this.author.write(dataOutput);
        this.wordList.write(dataOutput);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.author.readFields(dataInput);
        this.wordList.readFields(dataInput);
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("author", this.author);
        jsonObject.put("wordList", Arrays.asList(this.getWordList()));
        return jsonObject.toString();
    }
}
