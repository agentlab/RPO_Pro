package org.magapov.equinox.surricatalog;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.apache.commons.lang.SerializationUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;


class FileWorker extends Thread{
	FileWorker(){
		this.start();
	}
	
	@Override
	public void run() {
		try {
			this.read("/var/log/suricata/eve.json");
		}
		catch (IOException ioEx) {
			System.err.println("IOException" + ioEx);
		}
		catch (InterruptedException intEx) {
			System.err.println("InterruptException" + intEx);
		}		
	}
	
	public ArrayList read(String fileName) throws IOException, InterruptedException {
		
		JsonFactory factory = new JsonFactory();
		
		InputStream logStream = new FileInputStream(fileName);
		
		Scanner scan = new Scanner(logStream);
				
		String logLine = new String();
		
		ArrayList<HashMap<String, Object>> logList = new ArrayList<HashMap<String, Object>>();
		
		String hdfsURI = "hdfs://localhost:9000";
		String path = "/suricataLog";
		String hdfsFileName = "log.bin";
		
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", hdfsURI);
		
		conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
	    conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
	    
	    System.setProperty("HADOOP_USER_NAME", "hduser");
	    System.setProperty("hadoop.home.dir", "/");
	    
	    FileSystem fs = FileSystem.get(URI.create(hdfsURI), conf);
	    
	    Path newFolderPath = new Path(path + String.format("%d", System.currentTimeMillis()/1000));
	    
	    if (!fs.exists(newFolderPath)) {
			fs.mkdirs(newFolderPath);
		}
	    
	    Path hdfswritepath = new Path(newFolderPath + "/" + hdfsFileName);
		FSDataOutputStream outputStream = fs.create(hdfswritepath);
		
		while (this.isAlive()) {
			if (scan.hasNextLine()) {
				logLine = scan.nextLine();

				JsonParser parser = factory.createParser(logLine);

				parser.nextToken();

				HashMap<String, Object> oneLogMap = parseDepth(parser);
				byte[] serializedOneLog = SerializationUtils.serialize(oneLogMap);
				outputStream.write(serializedOneLog);
				outputStream.writeBytes("NEXTLOG");
				
				System.out.println("newLog, available: " + logStream.available());
			} else {
				System.out.println("wait......");
				FileWorker.sleep(1000);
			}
		}
		
		scan.close();
		outputStream.close();
		this.interrupt();
		return logList;
	}
	
	private HashMap<String, Object> parseDepth(JsonParser parser) throws IOException{
		String parseKey = new String();
		Object parseVal = new String();
		
		HashMap<String, Object> oneLogMap = new HashMap<String, Object>();
		
		while(!parser.isClosed()) {
			JsonToken token = parser.nextToken();
			if(JsonToken.FIELD_NAME.equals(token)) {
				parseKey = parser.getCurrentName();
			} else if(JsonToken.VALUE_STRING.equals(token)) {
				parseVal = parser.getValueAsString();
				oneLogMap.put(parseKey, parseVal);
			} else if(JsonToken.VALUE_NUMBER_INT.equals(token)){
				if (!parseKey.equals("flow_id")) {
					parseVal = parser.getValueAsInt();
				} else {
					parseVal = parser.getValueAsString();
				}
				oneLogMap.put(parseKey, parseVal);
			} else if(JsonToken.VALUE_TRUE.equals(token) || JsonToken.VALUE_FALSE.equals(token)){
				parseVal = parser.getValueAsBoolean();
				oneLogMap.put(parseKey, parseVal);
			} else if(JsonToken.START_OBJECT.equals(token)) {
				parseVal = parseDepth(parser);
				oneLogMap.put(parseKey, parseVal);
			} else if(JsonToken.END_OBJECT.equals(token)) {
				return oneLogMap;
			}
		}
		
		return null;
	}
}