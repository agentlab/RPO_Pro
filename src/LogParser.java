import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

class LogParser {
	public static void main(String args[]) throws IOException {
		System.out.println("My first Java app\n");
		
		FileWorker fw = new FileWorker();
		
		ArrayList<HashMap> eventsList = fw.read("/var/log/suricata/eve.json");
		
		String hdfsPath = "/usr/local/hadoop/tmp/hdfs/";
		
		String fileName = "suricataLog.bin";
		
		String nameNode = "hdfs://localhost:8020";
		
		String content = "SoneTextInFile";
		
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", nameNode);
		conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
		conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
		
		System.setProperty("HADOOP_USER_NAME", "hduser");
		System.setProperty("hadoop.home.dir", "/");
		
		FileSystem fs = FileSystem.get(URI.create(nameNode), conf);
		Path workingDir = fs.getWorkingDirectory();
		Path newFolderPath = new Path(hdfsPath);
		
		if(!fs.exists(newFolderPath)) {
			fs.mkdirs(newFolderPath);
		}
		
		Path hdfsWritePath = new Path(newFolderPath + "/" +fileName);
		
		FSDataOutputStream ofStream = fs.create(hdfsWritePath);
		
		ofStream.writeBytes(content);
		ofStream.close();
		
		//End of writing
		//Begin of reading
		Path hdfsReadPath = new Path(newFolderPath + "/" +fileName);
		
		FSDataInputStream ifStream = fs.open(hdfsReadPath);
		String readStr = IOUtils.toString(ifStream, "UTF-8");
		ifStream.close();
		fs.close();
		
		System.out.println(readStr);
	}
}







