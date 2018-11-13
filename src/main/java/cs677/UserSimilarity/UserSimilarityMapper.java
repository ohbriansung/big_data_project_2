package cs677.UserSimilarity;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import cs677.Writables.AuthorWordsWritable;
import cs677.common.Constants;
import org.apache.curator.shaded.com.google.common.hash.BloomFilter;
import org.apache.curator.shaded.com.google.common.hash.Funnels;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class UserSimilarityMapper extends Mapper<LongWritable, Text, DoubleWritable, AuthorWordsWritable> {
    BloomFilter<CharSequence> filter;
    String author;
    int size;

    public UserSimilarityMapper() {}

    @Override
    protected void setup(Context context) {
        Configuration conf = context.getConfiguration();
        author = conf.get(Constants.AUTHOR, "");
        size = 0;

        String directory = conf.get(Constants.ARCHIVED, "out-usersimilarity");
        try {
            Path pt = new Path("hdfs:/user/csung4/"+ directory + "/temp/part-r-00000");
            FileSystem fs = FileSystem.get(new Configuration());
            BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(pt)));

            for (String line; (line = br.readLine()) != null;) {
                if (line.startsWith(author)) {
                    makeBloomFilter(line);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void makeBloomFilter(String line) {
        String[] split = line.split("\t");
        String textCountArray = split[1];
        JsonParser parser = new JsonParser();
        JsonArray array = (JsonArray) parser.parse(textCountArray);

        // taking 20% of the inputs as size of bloom filter
        int paretoPrincipleSize = array.size() * 20 / 100;
        filter = BloomFilter.create(Funnels.stringFunnel(Charset.defaultCharset()), paretoPrincipleSize, 0.01);

        for (JsonElement element : array) {
            String text = element.getAsString();
            filter.put(text);
            size++;
        }
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] split = value.toString().split("\t");
        String _author = split[0];
        String textCountArray = split[1];

        if (_author.equals(author) || _author.equals("[deleted]")) {
            return;  // not matching self
        }

        JsonParser parser = new JsonParser();
        JsonArray array = (JsonArray) parser.parse(textCountArray);

        List<String> matchList = new ArrayList<>();
        double matched = 0;
        for (JsonElement element : array) {
            if (element.isJsonNull()) {
                continue;
            }

            String text = element.getAsString();
            if (filter.mightContain(text)) {
                matchList.add(text);
                matched++;
            }
        }

        int _size = Math.max(size, array.size());
        double similarity = matched / _size * 100;
        context.write(new DoubleWritable(similarity), new AuthorWordsWritable(_author, matchList.toString()));
    }
}
