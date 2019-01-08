import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Test {
	public static int total_num_of_features; //total number of features 
	public static class Mapping extends Mapper<LongWritable, Text, Text, FloatWritable> {
		public static ArrayList<Float> current_theta = new ArrayList<Float>();		
		public static Float[] input_i = null; //initializing input array
		public static int counting = 0; //count the number of test input
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {		
			String[] sample_input = value.toString().split("\\,"); //split input values from csv
			for (int i = 0; i < sample_input.length; i++) {
				current_theta.add(context.getConfiguration().getFloat("theta".concat(String.valueOf(i)), 0));// getting the weights value from the configuration
			}
			input_i = new Float[sample_input.length];
			for (int i = 0; i < input_i.length; i++) {
				if (i == 0) {
					input_i[0] = (float) 1;// bias input
				} else {
					input_i[i] = Float.parseFloat(sample_input[i - 1]); //input values
				}
			}
			float prediction = 0;
			float exponential = 0;
			for (int i = 0; i < input_i.length; i++) {
				exponential += (input_i[i] * current_theta.get(i));
				if (i == (input_i.length - 1)) {
					prediction = (float) (1 / (1 + (Math.exp(-(exponential)))));//predicting the output
				}
			}
			++counting;
			float output = Float.parseFloat(sample_input[sample_input.length - 1]);
			context.write(new Text("real_value_for_test_" + counting + " : " + output), new FloatWritable(prediction));
		}
	}
	public static class Reducing extends Reducer<Text, FloatWritable, Text, FloatWritable> {
		public void reduce(Text key, Iterable<FloatWritable> values, Context context)
				throws IOException, InterruptedException {
			float total = 0;
			for (FloatWritable value : values) {
				total += value.get();
			}
			if (total > 0) {
				total = (float) 1.0;
			} else {
				total = (float) 0.0;
			}
			context.write(key, new FloatWritable(total));
		}
	}
	public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
		total_num_of_features = Integer.parseInt(args[0]);// taking input for features
		++total_num_of_features;
		Float[] theta_values = new Float[total_num_of_features];
		Configuration config = new Configuration(); //setting configuration
		FileSystem filesystem = FileSystem.get(config); //defining filesystem
		//
		BufferedReader read_from_file = new BufferedReader(new InputStreamReader(filesystem.open(new Path(args[1]))));
		int iteration = 0;
		String stored_theta = null;
		while ((stored_theta = read_from_file.readLine()) != null) {
			String[] theta_line = stored_theta.toString().split("\t");
			theta_values[iteration] = Float.parseFloat(theta_line[1]);
			iteration++;
		}
		read_from_file.close();
		for (int j = 0; j < total_num_of_features; j++) {
			config.setFloat("theta".concat(String.valueOf(j)), theta_values[j]);//setting theta value in the configuration
		}		
		Job task = new Job(config, "Theta Calculation");
		task.setJarByClass(Test.class);
		FileInputFormat.setInputPaths(task, new Path(args[2]));
		FileOutputFormat.setOutputPath(task, new Path(args[3]));
		task.setMapperClass(Mapping.class);
		task.setReducerClass(Reducing.class);
		task.setOutputKeyClass(Text.class);
		task.setOutputValueClass(FloatWritable.class);
		task.waitForCompletion(true);
	}
}
