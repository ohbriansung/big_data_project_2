package cs677.Writables;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.json.JSONObject;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class AuthorWordsWritable implements Writable {
    private final Text author;
    private final Text wordList;

    // disable empty constructor
    private AuthorWordsWritable() {
        this.author = new Text();
        this.wordList = new Text();
    }

    public AuthorWordsWritable(String author, String wordList) {
        this.author = new Text(author);
        this.wordList = new Text(wordList);
    }

    public String getAuthor() {
        return this.author.toString();
    }

    public JsonArray getWordList() {
        JsonParser parser = new JsonParser();
        JsonArray array = (JsonArray) parser.parse(this.wordList.toString());

        return array;
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
        jsonObject.put("wordList", this.getWordList());
        return jsonObject.toString();
    }
}
