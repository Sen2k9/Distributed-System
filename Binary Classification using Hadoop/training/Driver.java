import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Driver {
	public static int total_num_of_features; // provided from cmd
	public static float learningRate; // provided from cmd
	public static long total_input = (long) 0;
	public static class Mapping extends Mapper<LongWritable, Text, Text, FloatWritable> {
		public static int counting = 0;
		public static float learningRate = 0.0f;
		public static Float[] Input_i = null;
		public static ArrayList<Float> current_theta = new ArrayList<Float>();
		@Override
		public void setup(Context context) throws IOException, InterruptedException {
			total_input++;
			learningRate = context.getConfiguration().getFloat("learningRate", 0);
		}
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			++counting;
			String[] input_samples = value.toString().split(","); // taking the input values
			if (counting == 1) {// when all the theta values are zero
				for (int i = 0; i < input_samples.length; i++) {
					current_theta.add(context.getConfiguration().getFloat("theta".concat(String.valueOf(i)), 0));
				}
				Input_i = new Float[input_samples.length];// creating input array
			}
			for (int i = 0; i < Input_i.length; i++) {
				if (i == 0) {
					Input_i[0] = (float) 1;// setting bias input as 1
				} else {// storing input until last sample value
					Input_i[i] = Float.parseFloat(input_samples[i - 1]);
				}
			}
			float exponential = 0;
			float hypothesis = 0;
			for (int i = 0; i < Input_i.length; i++) {
				exponential += (Input_i[i] * current_theta.get(i));
				if (i == (Input_i.length - 1)) {
					hypothesis = (float) (1 / (1 + (Math.exp(-(exponential)))));// output prediction
				}
			}
			float class_value = Float.parseFloat(input_samples[input_samples.length - 1]);// class value
			for (int i = 0; i < Input_i.length; i++) {
				float temporary = current_theta.get(i);
				current_theta.remove(i);
				// updating the theta value
				current_theta.add(i,
						(float) (temporary + (learningRate / total_input) * (class_value - hypothesis) * (Input_i[i])));
			}
		}

		@Override
		public void cleanup(Context context) throws IOException, InterruptedException {
			for (int i = 0; i < current_theta.size(); i++) {
				context.write(new Text("theta" + i), new FloatWritable(current_theta.get(i)));
			}
		}
	}

	public static class Reducing extends Reducer<Text, FloatWritable, Text, FloatWritable> {
		public void reduce(Text key, Iterable<FloatWritable> values, Context context)
				throws IOException, InterruptedException {
			float total = 0;
			int values_count = 0;
			for (FloatWritable value : values) {
				total += value.get(); //summing total theta values
				values_count++;
			}
			context.write(key, new FloatWritable(total / values_count)); //averaging the theta value
		}
	}

	public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
		// args[0] for feature numbers
		total_num_of_features = Integer.parseInt(args[0]);
		++total_num_of_features;
		// args[1] is for learning rate
		learningRate = Float.parseFloat(args[1]);
		Float[] theta_value = new Float[total_num_of_features];
		Configuration config = new Configuration();
		for (int j = 0; j < total_num_of_features; j++) {
			theta_value[j] = (float) 0.0; //setting initial wight value as zero
		}
		config.setFloat("learningRate", learningRate);
		for (int j = 0; j < total_num_of_features; j++) {
			config.setFloat("theta".concat(String.valueOf(j)), theta_value[j]);
		}
		Job task = new Job(config, "Taining Process: Calculating theata value");
		task.setJarByClass(Driver.class);
		FileInputFormat.setInputPaths(task, new Path(args[2]));//input path
		FileOutputFormat.setOutputPath(task, new Path(args[3]));//output fish
		task.setMapperClass(Mapping.class); //setting mapper
		task.setReducerClass(Reducing.class);//setting reducer
		task.setOutputKeyClass(Text.class);
		task.setOutputValueClass(FloatWritable.class);
		task.waitForCompletion(true);

	}
}
