import java.io.File;
//import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Scanner;

/*
 * TODO:
 * 1. Parse fast.log
 */

class FileWorker {
	public ArrayList<LogStruct> read(String fileName) throws IOException {
		
		File log = new File(fileName);
		
		Scanner scan = null;
		try {
			scan = new Scanner(log);
			
		} catch(IOException ex) {
			System.out.println(ex.getMessage());
			return null;
		}
		
		String logLine = new String();
		
		int day, month, year;
		int hour, min, sec;
		
		ArrayList<LogStruct> eventsList = new ArrayList<LogStruct>();
		
		int c;
		while (scan.hasNext()) {
			
			logLine = scan.nextLine();

			day = Integer.parseInt(logLine.substring(0, 2));
			month = Integer.parseInt(logLine.substring(3, 5));
			year = Integer.parseInt(logLine.substring(6, 10));
			hour = Integer.parseInt(logLine.substring(14, 16));
			min = Integer.parseInt(logLine.substring(17, 19));
			sec = Integer.parseInt(logLine.substring(20, 22));

			GregorianCalendar date = new GregorianCalendar(year, month - 1, day, hour, min, sec);

			String type = logLine.substring(26, logLine.indexOf('>'));

			String message = logLine.substring(logLine.indexOf('>') + 4);
			
			eventsList.add(new LogStruct(date, type, message));
		}
		
		return eventsList;
		
	}
}