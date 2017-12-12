import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.commons.lang.SerializationUtils;

class LogParser {
	public static void main(String args[]) throws IOException {
		System.out.println("My first Java app\n");
		
		FileWorker fw = new FileWorker();
		
		ArrayList<HashMap> eventsList = fw.read("/var/log/suricata/eve.json");
		
		String hdfsURI = "hdfs://localhost:9000";
		String path = "/suricataLog";
		String fileName = "log.bin";
		
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", hdfsURI);
		
		conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
	    conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
	    
	    System.setProperty("HADOOP_USER_NAME", "hduser");
	    System.setProperty("hadoop.home.dir", "/");
	    
	    FileSystem fs = FileSystem.get(URI.create(hdfsURI), conf);
	    
		Path newFolderPath = new Path(path);
		if (fs.exists(newFolderPath)) {
			fs.delete(newFolderPath, true);
			fs.mkdirs(newFolderPath);
		}
		
		Path hdfswritepath = new Path(newFolderPath + "/" + fileName);
		FSDataOutputStream outputStream = fs.create(hdfswritepath);
		
		SerializationUtils.serialize(eventsList, outputStream);
		outputStream.close();
	}
}







