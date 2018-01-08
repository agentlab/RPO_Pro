package ru.bmstu.magapov.suricatalog.test;

import static org.junit.Assert.*;

import java.io.PrintWriter;
import java.net.URI;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.atLeast;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import ru.bmstu.magapov.suricatalog.FileWorker;


@RunWith(MockitoJUnitRunner.class)
public class FileWorkerTest {
	@Mock
	private Appender mockAppender;
	
	@Captor
	private ArgumentCaptor<LoggingEvent> captorLoggingEvent;
	
	@Before
	public void setup() throws InterruptedException {
        final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.setLevel(Level.INFO);
        logger.addAppender(mockAppender);
        captorLoggingEvent = ArgumentCaptor.forClass(LoggingEvent.class);
    }
	
	@After
    public void teardown() throws InterruptedException {
        final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.detachAppender(mockAppender);
    }
	
	@Test
	public void write1log() {
		FileWorker fw = new FileWorker("/var/log/suricata/1.json", "hdfs://localhost:9000");
		Thread th = new Thread(fw);
		th.setName("Write1LogThread");
		th.start();
		while(th.getState() != Thread.State.TIMED_WAITING);
		th.interrupt();
		while(th.isAlive());
		
		assertTrue(true);
	}
	
	@Test
	public void write10000log() {
		FileWorker fw = new FileWorker("/var/log/suricata/10000.json", "hdfs://localhost:9000");
		Thread th = new Thread(fw);
		th.setName("Write10000LogThread");
		th.start();
		while(th.getState() != Thread.State.TIMED_WAITING);
		th.interrupt();
		while(th.isAlive());
		
		assertTrue(true);
	}
	
	@Test
	public void write20000log() {
		FileWorker fw = new FileWorker("/var/log/suricata/20000.json", "hdfs://localhost:9000");
		Thread th = new Thread(fw);
		th.setName("Write20000LogThread");
		th.start();
		while(th.getState() != Thread.State.TIMED_WAITING);
		th.interrupt();
		while(th.isAlive());
		
		assertTrue(true);
	}
	
	@Test
	public void write30000log() {
		FileWorker fw = new FileWorker("/var/log/suricata/30000.json", "hdfs://localhost:9000");
		Thread th = new Thread(fw);
		th.setName("Write30000LogThread");
		th.start();
		while(th.getState() != Thread.State.TIMED_WAITING);
		th.interrupt();
		while(th.isAlive());
		
		assertTrue(true);
	}
	
	@Test
	public void write4000log() {
		FileWorker fw = new FileWorker("/var/log/suricata/40000.json", "hdfs://localhost:9000");
		Thread th = new Thread(fw);
		th.setName("Write40000LogThread");
		th.start();
		while(th.getState() != Thread.State.TIMED_WAITING);
		th.interrupt();
		while(th.isAlive());
		
		assertTrue(true);
	}
	
	@Test
	public void write50000log() {
		FileWorker fw = new FileWorker("/var/log/suricata/50000.json", "hdfs://localhost:9000");
		Thread th = new Thread(fw);
		th.setName("Write50000LogThread");
		th.start();
		while(th.getState() != Thread.State.TIMED_WAITING);
		th.interrupt();
		while(th.isAlive());
		
		assertTrue(true);
	}
	
	@Test
	public void write60000log() {
		FileWorker fw = new FileWorker("/var/log/suricata/60000.json", "hdfs://localhost:9000");
		Thread th = new Thread(fw);
		th.setName("Write60000LogThread");
		th.start();
		while(th.getState() != Thread.State.TIMED_WAITING);
		th.interrupt();
		while(th.isAlive());
		
		assertTrue(true);
	}
	
	@Test
	public void write70000log() {
		FileWorker fw = new FileWorker("/var/log/suricata/70000.json", "hdfs://localhost:9000");
		Thread th = new Thread(fw);
		th.setName("Write70000LogThread");
		th.start();
		while(th.getState() != Thread.State.TIMED_WAITING);
		th.interrupt();
		while(th.isAlive());
		
		assertTrue(true);
	}
	
	@Test
	public void write80000log() {
		FileWorker fw = new FileWorker("/var/log/suricata/80000.json", "hdfs://localhost:9000");
		Thread th = new Thread(fw);
		th.setName("Write80000LogThread");
		th.start();
		while(th.getState() != Thread.State.TIMED_WAITING);
		th.interrupt();
		while(th.isAlive());
		
		assertTrue(true);
	}
	
	@Test
	public void write90000log() {
		FileWorker fw = new FileWorker("/var/log/suricata/90000.json", "hdfs://localhost:9000");
		Thread th = new Thread(fw);
		th.setName("Write90000LogThread");
		th.start();
		while(th.getState() != Thread.State.TIMED_WAITING);
		th.interrupt();
		while(th.isAlive());
		
		assertTrue(true);
	}
	
	@Test
	public void write100000log() {
		FileWorker fw = new FileWorker("/var/log/suricata/100000.json", "hdfs://localhost:9000");
		Thread th = new Thread(fw);
		th.setName("Write100000LogThread");
		th.start();
		while(th.getState() != Thread.State.TIMED_WAITING);
		th.interrupt();
		while(th.isAlive());
		
		assertTrue(true);
	}
	
	@Test
	public void expectedWrongFile() throws Exception{
		FileWorker fw = new FileWorker("/home/dts/123123123.dat", "hdfs://localhost:9000");
		Thread th = new Thread(fw);
		th.setName("WrongFileThread");
		th.start();
		
		Thread.currentThread().sleep(10);
		verify(mockAppender, atLeast(1)).doAppend(captorLoggingEvent.capture());
        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        assertThat(loggingEvent.getLevel(), is(Level.INFO));
        assertThat(loggingEvent.getMessage(), is("FileNotFoundException throws"));
        
        th.interrupt();
        while(th.isAlive());
        assertTrue(true);
	}
	
	@Test
	public void expectedWrongConnect() throws Exception {
		Thread.currentThread().sleep(1000);
		FileWorker fw = new FileWorker("hdfs://localhost:1234");
		Thread th = new Thread(fw);
		th.setName("WrongConnectThread");
		th.start();
		
		Thread.currentThread().sleep(7000);
		verify(mockAppender, atLeast(1)).doAppend(captorLoggingEvent.capture());
        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        assertThat(loggingEvent.getLevel(), is(Level.INFO));
        assertThat(loggingEvent.getMessage(), is("IOException throws"));
        
        th.interrupt();
        while(th.isAlive());
	}
	
	@Test
	public void writeAndRead() throws Exception {
		String writeStr = "{\"FirstField\": \"FirstValue\", \"SecondField\": \"SecondValue\"}";
		PrintWriter writer = new PrintWriter("test.json", "UTF-8");
		writer.println(writeStr);
		writer.close();
		
		FileWorker fw = new FileWorker("test.json", "hdfs://localhost:9000");
		Thread th = new Thread(fw);
		th.setName("LogParserThread");
		th.start();
		
		Thread.currentThread().sleep(5000);
		th.interrupt();		
		Thread.currentThread().sleep(1000);
		
		String hdfsURI = "hdfs://localhost:9000";
		String hdfsFileName = "log.bin";
		
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", hdfsURI);
		
		conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
	    conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
	    conf.setBoolean("dfs.support.append", true);
	    
	    System.setProperty("HADOOP_USER_NAME", "hduser");
	    System.setProperty("hadoop.home.dir", "/");
	    
	    FileSystem fs = FileSystem.get(URI.create(hdfsURI), conf);
	    FileStatus[] fileStatus = fs.listStatus(new Path(hdfsURI + "/"));
	    Path[] paths = FileUtil.stat2Paths(fileStatus);
	    
	    Path newFolderPath = new Path(paths[paths.length - 1].toString());
	    
	    assertTrue(fs.exists(newFolderPath));
	    
	    Path hdfsReadPath = new Path(newFolderPath + "/" + hdfsFileName);
	    
	    assertTrue(fs.exists(hdfsReadPath));
	    
	    FSDataInputStream inStream = fs.open(hdfsReadPath);
	    
	    String inStr = IOUtils.toString(inStream, "UTF-8");
	    
	    inStream.close();
	    fs.close();
	    
	    assertEquals(inStr, "FirstField,SecondField\n" + 
	    		"FirstValue,SecondValue\n");
	}
	
}
