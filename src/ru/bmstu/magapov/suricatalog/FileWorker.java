package ru.bmstu.magapov.suricatalog;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class FileWorker implements Runnable {
	private String logFileName;
	private String hdfsURI;
	private static final Logger LOG = LoggerFactory.getLogger(FileWorker.class);

	public FileWorker(String logFN, String hdfsURINew) {
		logFileName = logFN;
		hdfsURI = hdfsURINew;
	}

	public FileWorker() {
		this("/var/log/suricata/eve.json", "hdfs://localhost:9000");
	}
	
	public FileWorker(String hdfsURINew) {
		this("/var/log/suricata/eve.json", hdfsURINew);
	}

	@Override
	public void run() {
		try {
			this.read(logFileName);
		} catch (FileNotFoundException fileEx) {
			LOG.info("FileNotFoundException throws");
		} catch (IOException ioEx) {
			LOG.info("IOException throws");
		} catch (InterruptedException intEx) {
			LOG.info("InterruptedException throws");
		} catch (Exception e) {
			LOG.info("Exception throws");
		}
	}

	public void read(String fileName) throws Exception {
		
		JsonFactory factory = new JsonFactory();
		
		InputStream logStream = new FileInputStream(fileName);

		BufferedReader scan = new BufferedReader(new InputStreamReader(logStream));

		String logLine = new String();

		String path = "/suricataLog";
		String hdfsFileName = "log.bin";

		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", hdfsURI);

		conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
		conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
		conf.setBoolean("dfs.support.append", true);

		System.setProperty("HADOOP_USER_NAME", "hduser");
		System.setProperty("hadoop.home.dir", "/");

		FileSystem fs = FileSystem.get(URI.create(hdfsURI), conf);

		Path newFolderPath = new Path(path + String.format("%d", System.currentTimeMillis() / 1000));

		if (!fs.exists(newFolderPath)) {
			fs.mkdirs(newFolderPath);
		}

		Path hdfswritepath = new Path(newFolderPath + "/" + hdfsFileName);
		Path hdfswritepathHeader = new Path(newFolderPath + "/" + hdfsFileName + ".head");
		FSDataOutputStream outputStream = fs.create(hdfswritepath);
		FSDataOutputStream outputStreamHeader = fs.create(hdfswritepathHeader);
		
		int headerPos = 0;
		
		ArrayList<String> header = new ArrayList<String>();
		try {
			while (!Thread.currentThread().isInterrupted()) {
				if (scan.ready()) {

					logLine = scan.readLine();
					
					JsonParser parser = factory.createParser(logLine);
					
					parser.nextToken();
					
					ArrayList<String> logs = new ArrayList<String>();
					
					parseDepth(parser, "", header, logs);
					
					for(; headerPos < header.size(); headerPos++) {
						outputStreamHeader.write((header.get(headerPos) + "|").getBytes());
					}
					
					for(int logPos = 0; logPos < logs.size(); logPos++) {
						outputStream.write((logs.get(logPos).isEmpty() ? "" : logs.get(logPos)).getBytes());
						if(logPos != logs.size() - 1)
							outputStream.write("|".getBytes());
						else
							outputStream.write("\n".getBytes());
					}
				} else {
					System.out.println("wait......" + Thread.currentThread().getName());
					Thread.sleep(1000);
				}
			}
		} catch (InterruptedException intEx) {
			scan.close();
			outputStream.close();
			outputStreamHeader.close();
			fs.close();
		}
	}
	
	private void parseDepth(JsonParser parser, String keyPrefix, ArrayList<String> header, ArrayList<String> logs) throws IOException{
		String parseKey = new String();		
		
		while (!parser.isClosed()) {
			JsonToken token = parser.nextToken();
			if (JsonToken.FIELD_NAME.equals(token)) {
				parseKey = keyPrefix + parser.getCurrentName();
			} else if (JsonToken.VALUE_STRING.equals(token) || JsonToken.VALUE_TRUE.equals(token)
					 || JsonToken.VALUE_FALSE.equals(token) || JsonToken.VALUE_NUMBER_INT.equals(token)) {
				if(!parseKey.isEmpty() && !header.contains(parseKey))
					header.add(parseKey);
				if (header.indexOf(parseKey) > logs.size() - 1) {
					for (int i = logs.size(); i <= header.indexOf(parseKey); i++) {
						logs.add("");
					}
				}
				logs.set(header.indexOf(parseKey), parser.getValueAsString());
				parseKey = "";
			} else if (JsonToken.START_OBJECT.equals(token)) {
				if(!parseKey.isEmpty())
					keyPrefix = parseKey + "-" + keyPrefix;
				parseDepth(parser, keyPrefix, header, logs);
				keyPrefix = keyPrefix.replace(parseKey + "-", "");
				parseKey = "";
			} else if (JsonToken.END_OBJECT.equals(token)) {
				return;
			}
		}
	}
}
