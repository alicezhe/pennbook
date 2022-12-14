package edu.upenn.cis.nets212.storage;

import java.io.File;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;

import edu.upenn.cis.nets212.config.Config;

public class SparkConnector {
	static SparkSession spark = null;
	static JavaSparkContext context = null;
	
	public static SparkSession getSparkConnection() {
		return getSparkConnection(null);
	}
	
	public static synchronized SparkSession getSparkConnection(String host) {
		if (spark == null) {
			if (System.getenv("HADOOP_HOME") == null) {
				File workaround = new File(".");
				
				System.setProperty("hadoop.home.dir", workaround.getAbsolutePath() + "/native-libs");
			}
			
			if (host != null && !host.startsWith("spark://"))
				host = "spark://" + host + ":7077";
			
		    spark = SparkSession
		            .builder()
		            //.config("fs.s3a.access.key", Config.AWS_ACCESS_KEY)
		            //.config("fs.s3a.secret.key", Config.AWS_SECRET_KEY)
		            //.appName("Recommender")
		            //.master((host == null) ? Config.LOCAL_SPARK : host)
		            .getOrCreate();
		}
		
	    return spark;
	}

	public static synchronized JavaSparkContext getSparkContext() {
		if (context == null)
			context = new JavaSparkContext(getSparkConnection().sparkContext());
		
		return context;
	}
}
