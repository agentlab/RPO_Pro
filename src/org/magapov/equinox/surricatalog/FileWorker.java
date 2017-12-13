package org.magapov.equinox.surricatalog;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
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
		
		File log = new File(fileName);
		
		Scanner scan = null;
		try {
			scan = new Scanner(log);
			
		} catch(IOException ex) {
			System.out.println(ex.getMessage());
			return null;
		}
		
		String logLine = new String();
		
		ArrayList<HashMap<String, Object>> logList = new ArrayList<HashMap<String, Object>>();
		//Experimental start
		
		String separator = new String("NEXTLOG");
		
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
	    
	    Path newFolderPath = new Path(path);
	    
	    if (!fs.exists(newFolderPath)) {
			fs.mkdirs(newFolderPath);
		}
	    
	    Path hdfswritepath = new Path(newFolderPath + "/" + hdfsFileName);
		FSDataOutputStream outputStream = fs.create(hdfswritepath);
	    //Experimental end
		
		while (this.isAlive()) {
			if (scan.hasNext()) {
				logLine = scan.nextLine();

				JsonParser parser = factory.createParser(logLine);

				parser.nextToken();

				HashMap<String, Object> oneLogMap = parseDepth(parser);
				// Experimental start
				byte[] serializedOneLog = SerializationUtils.serialize(oneLogMap);
				outputStream.write(serializedOneLog);
				outputStream.writeBytes(separator);
				
				System.out.println("newLog");
				// Experimental end
			} else {
				System.out.println("wait......");
				FileWorker.sleep(1000);
			}
		}
		// Experimental start
		scan.close();
		outputStream.close();
		this.interrupt();
		// Experimental end
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