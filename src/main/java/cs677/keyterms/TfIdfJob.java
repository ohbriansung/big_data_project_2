package cs677.keyterms;

import cs677.common.FileCreator;
import cs677.common.TimerStuff;
import cs677.readability.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.time.Duration;
import java.time.Instant;

// yarn jar P2-1.0.jar cs677.keyterms.TfIdfJob /out/termcount_subs/* /out/tfidf  nosleep 41683 33714
// yarn jar P2-1.0.jar cs677.keyterms.TfIdfJob /test/termcount_01/* /test/tfidf AskReddit 12 3
// yarn jar P2-1.0.jar cs677.keyterms.TfIdfJob /out/termcount_subs_5per_00/* /out/tfidf_5per rarepuppers 23508 178519
// hdfs dfs -cat /out/termindoc_subs_5per_00/part* | grep rarepuppers
// hdfs dfs -cat /out/termindoc_subs_5per_00/part* | wc -l

// wordcloud
// https://github.com/amueller/word_cloud/blob/b79b3d69a65643dbd421a027e66760a4398e91b3/wordcloud/wordcloud.py#L363
public class TfIdfJob {
  static final String DOCUMENT_KEY = "doc_key";
  static final String TOTAL_NUM_TERMS = "tot_terms";
  static final String TOTAL_NUM_DOCUMENTS = "tot_docs";

  public static void main(String[] args) {
    if (args.length != 5) {
      System.out.println(
          "required args: <input_path> <output_path> <document_key> <# of terms for document> <# of documents>");
      System.exit(-1);
    }
    String input = args[0];
    String output = args[1];
    String docKey = args[2];
    long numTerms = Long.parseLong(args[3]);
    long numDocs = Long.parseLong(args[4]);

    try {
      Instant t1 = Instant.now();

      Configuration conf = new Configuration();

      conf.set(DOCUMENT_KEY, docKey);
      conf.setLong(TOTAL_NUM_TERMS, numTerms);
      conf.setLong(TOTAL_NUM_DOCUMENTS, numDocs);

      /* Setup */
      Job job = Job.getInstance(conf, "TF-IDF " + docKey);
      job.setJarByClass(ReadabilityJob.class);

      /* Input path */
      FileInputFormat.addInputPath(job, new Path(input));
      System.out.println("Input path: " + input);
      job.setInputFormatClass(KeyValueTextInputFormat.class);


      /* Output path */
      Path outPath = FileCreator.findEmptyPath(conf, output);
      System.out.println("Output path: " + outPath.toString());
      FileOutputFormat.setOutputPath(job, outPath);

      /* Mapper */
      job.setMapperClass(TfIdfMapper.class);
      job.setMapOutputKeyClass(DoubleWritable.class);
      job.setMapOutputValueClass(Text.class);

      /* Reducer */
      job.setReducerClass(TfIdfReducer.class);
      job.setOutputKeyClass(Text.class);
      job.setOutputValueClass(DoubleWritable.class);

      /* Wait job to complete */
      boolean completed = job.waitForCompletion(true);
      System.out.println(Instant.now());
      System.out.println("Input path: " + input);
      System.out.println("Output path: " + outPath.toString());

      Instant t2 = Instant.now();
      System.out.println("Time Taken: " + TimerStuff.formatDuration(Duration.between(t1, t2)));

      System.exit(completed ? 0 : 1);

    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }
}
