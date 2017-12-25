package org.magapov.equinox.surricatalog.test;

import org.magapov.equinox.surricatalog.FileWorker;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.Suite;
import org.junit.runners.MethodSorters;


public class FileWorkerTest {
	@Test(expected = FileNotFoundException.class)
	public void FileNotFoundTest() {
		FileWorker fw = new FileWorker("/home/dts/123123123.dat");
		Thread th = new Thread(fw);
		th.start();
	}
}