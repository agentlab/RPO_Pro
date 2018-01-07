package org.magapov.equinox.surricatalog;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import org.jsontocsv.parser.JsonFlattener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		FSDataOutputStream outputStream = fs.create(hdfswritepath);
		try {
			while (!Thread.currentThread().isInterrupted()) {
				if (scan.ready()) {

					logLine = scan.readLine();

					JsonFlattener jsonToCsvParser = new JsonFlattener();

					List<Map<String, String>> flatJson = jsonToCsvParser.parseJson(logLine);

					Set<String> headers = collectHeaders(flatJson);
					String output = StringUtils.join(headers.toArray(), ",") + "\n";
					for (Map<String, String> map : flatJson) {
						output = output + getCommaSeperatedRow(headers, map) + "\n";
					}
					byte[] logCSV = output.getBytes();
					outputStream.write(logCSV);

					//System.out.println("newLog");
				} else {
					System.out.println("wait......" + Thread.currentThread().getName());
					Thread.sleep(1000);
				}
			}
		} catch (InterruptedException intEx) {
			scan.close();
			outputStream.close();
			fs.close();
		}
	}

	private Set<String> collectHeaders(List<Map<String, String>> flatJson) {
		Set<String> headers = new TreeSet<String>();
		for (Map<String, String> map : flatJson) {
			headers.addAll(map.keySet());
		}
		return headers;
	}

	private String getCommaSeperatedRow(Set<String> headers, Map<String, String> map) {
		List<String> items = new ArrayList<String>();
		for (String header : headers) {
			String value = map.get(header) == null ? "" : map.get(header).replace(",", "");
			items.add(value);
		}
		return StringUtils.join(items.toArray(), ",");
	}
}
