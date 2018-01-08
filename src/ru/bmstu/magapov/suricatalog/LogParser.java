package ru.bmstu.magapov.suricatalog;

import java.io.IOException;
import java.lang.InterruptedException;

class LogParser {
	public static void main(String args[]) throws IOException, InterruptedException {
		FileWorker fw = new FileWorker();
		Thread myThread = new Thread(fw);
		myThread.start();
	}
}
