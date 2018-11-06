package cs677.keyterms;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.json.JSONObject;

import java.io.IOException;

public class TfIdfMapper extends Mapper<Text, Text, DoubleWritable, Text> {
  @Override
  protected void map(Text key, Text value, Context context)
      throws IOException, InterruptedException {

    Configuration conf = context.getConfiguration();

    String keyToUse = conf.get(TfIdfJob.DOCUMENT_KEY, "");
    long totalTerms = conf.getLong(TfIdfJob.TOTAL_NUM_TERMS, 0L);
    long totalDocs = conf.getLong(TfIdfJob.TOTAL_NUM_DOCUMENTS, 0L);
    JSONObject obj = new JSONObject(value.toString());

    if (obj.length() == 0 || totalDocs == 0 || totalTerms == 0 || !obj.has(keyToUse)) {
      return;
    }

    double tf = obj.getDouble(keyToUse) / totalTerms;
    double idf = Math.log(((double) totalDocs) / obj.length());

    context.write(new DoubleWritable(tf * idf), key);
  }
}
