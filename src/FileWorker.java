import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;


/*
 * TODO:
 * 1. Parse fast.log
 */

class FileWorker {
	public ArrayList<LogStruct> read(String fileName) throws IOException {
		
		JsonFactory factory = new JsonFactory();
		
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
		
		ArrayList<HashMap<String, Object>> logList = new ArrayList<HashMap<String, Object>>();
		
		while (scan.hasNext()) {
			
			logLine = scan.nextLine();
			
			JsonParser parser = factory.createParser(logLine);
			
			HashMap<String, Object> oneLogMap = parseDepth(parser);
			
			logList.add(oneLogMap);
		}
		
		return eventsList;
		
	}
	
	private HashMap<String, Object> parseDepth(JsonParser parser) throws IOException{
		String parseKey = new String();
		Object parseVal = new String();
		
		HashMap<String, Object> oneLogMap = new HashMap<String, Object>();
		
		while(!parser.isClosed()) {
			JsonToken token = parser.nextToken();
			if(JsonToken.FIELD_NAME.equals(token)) {
				parseKey = parser.getCurrentName();
			} else if(JsonToken.VALUE_STRING.equals(token) || JsonToken.VALUE_NUMBER_INT.equals(token)) {
				parseVal = parser.getValueAsString();
				oneLogMap.put(parseKey, parseVal);
			} else if(JsonToken.START_OBJECT.equals(token)) {
				parseVal = parseDepth(parser);
				oneLogMap.put(parseKey, parseVal);
			} else if(JsonToken.END_OBJECT.equals(token)) {
				return oneLogMap;
			}
		}
		
		return null;
	}
}