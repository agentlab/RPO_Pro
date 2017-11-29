import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Scanner;
import com.fasterxml.jackson.dataformat.xml.*;

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

			day = Integer.parseInt(logLine.substring(3, 5));
			month = Integer.parseInt(logLine.substring(0, 2));
			year = Integer.parseInt(logLine.substring(6, 10));
			hour = Integer.parseInt(logLine.substring(11, 13));
			min = Integer.parseInt(logLine.substring(14, 16));
			sec = Integer.parseInt(logLine.substring(17, 19));

			GregorianCalendar date = new GregorianCalendar(year, month - 1, day, hour, min, sec);
			
			logLine = logLine.substring(logLine.indexOf(']') + 2);
			
			String sTBB = logLine.substring(1, logLine.indexOf(']'));
			
			logLine = logLine.substring(logLine.indexOf(']') + 2);
			
			String message = logLine.substring(0, logLine.indexOf('[') - 1);
			
			logLine = logLine.substring(logLine.indexOf(']') + 2);

			String type = logLine.substring(logLine.indexOf(':') + 2, logLine.indexOf(']'));

			logLine = logLine.substring(logLine.indexOf(']') + 2);
			
			int priority = Integer.parseInt(logLine.substring(11, 12));
			
			logLine = logLine.substring(logLine.indexOf(']') + 2);
			
			String protocol = logLine.substring(1, 4);
			
			logLine = logLine.substring(logLine.indexOf('}') + 2);
			
			String sender = logLine.substring(0, logLine.indexOf(' '));
			String receiver = logLine.substring(logLine.indexOf('>') + 2);
			
			eventsList.add(new LogStruct(date, type, message, sTBB, priority, protocol, sender, receiver));
		}
		
		return eventsList;
		
	}
}