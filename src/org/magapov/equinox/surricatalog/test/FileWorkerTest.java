package org.magapov.equinox.surricatalog.test;

import org.magapov.equinox.surricatalog.FileWorker;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;
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


@RunWith(MockitoJUnitRunner.class)
public class FileWorkerTest {
	@Mock
	private Appender mockAppender;
	
	@Captor
	private ArgumentCaptor<LoggingEvent> captorLoggingEvent;
	
	@Before
	public void setup() {
        final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.addAppender(mockAppender);
        captorLoggingEvent = ArgumentCaptor.forClass(LoggingEvent.class);
    }
	
	@After
    public void teardown() {
        final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.detachAppender(mockAppender);
    }
	
	@Test
	public void emptyTest() {
		assertTrue(true);
	}
	
	@Test
	public void expectedWrongFile() throws Exception{
		FileWorker fw = new FileWorker("/home/dts/123123123.dat");
		Thread th = new Thread(fw);
		th.start();
		
		Thread.currentThread().sleep(10);
		verify(mockAppender, atLeast(1)).doAppend(captorLoggingEvent.capture());
        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        assertThat(loggingEvent.getLevel(), is(Level.INFO));
        assertThat(loggingEvent.getMessage(), is("FileNotFoundException throws"));
	}
	
	@Test
	public void expectedWrongConnect() throws Exception {
		FileWorker fw = new FileWorker();
		Thread th = new Thread(fw);
		th.start();
		
		Thread.currentThread().sleep(10000);
		verify(mockAppender, atLeast(1)).doAppend(captorLoggingEvent.capture());
        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        assertThat(loggingEvent.getLevel(), is(Level.INFO));
        assertThat(loggingEvent.getMessage(), is("IOException throws"));
	}
	
	
	
}
