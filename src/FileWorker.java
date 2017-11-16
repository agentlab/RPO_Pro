import java.io.File;
//import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.GregorianCalendar;

class FileWorker {
	public String read(String fileName) throws IOException {
		
		File log = new File(fileName);
		
		FileReader fr = null;
		try {
			fr = new FileReader(log);
			
		} catch(IOException ex) {
			System.out.println(ex.getMessage());
			return "NoFile";
		}
		
		String logLine = new String();
		
		int day, month, year;
		int hour, min, sec;
		
		int c;
		while((c = fr.read())!='\n') {
			logLine += (char)c;		
		}
		
		day = Integer.parseInt(logLine.substring(0, 2));
		month = Integer.parseInt(logLine.substring(3, 5));
		year = Integer.parseInt(logLine.substring(6, 10));
		hour = Integer.parseInt(logLine.substring(14, 16));
		min = Integer.parseInt(logLine.substring(17, 19));
		sec = Integer.parseInt(logLine.substring(20, 22));
		
		GregorianCalendar date = new GregorianCalendar(year, month-1, day, hour, min, sec);
		
		String type = logLine.substring(26, logLine.indexOf('>'));
		
		String message = logLine.substring(logLine.indexOf('>') + 4);
		
		LogStruct ls = new LogStruct(date, type, message);
		
		System.out.println(ls.getEventTime().getTime());
		System.out.println(ls.getType());
		System.out.println(ls.getMessage());
		
		return fileName;
		
	}
}