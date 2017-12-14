package org.magapov.equinox.surricatalog;

import java.io.IOException;
import java.lang.InterruptedException;

class LogParser {
	public static void main(String args[]) throws IOException, InterruptedException {
		System.out.println("My first Java app\n");
		
		FileWorker fw = new FileWorker();
		Thread myThread = new Thread(fw);
		myThread.start();
	}
}
