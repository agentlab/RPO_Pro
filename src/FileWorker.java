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


class FileWorker {
	public ArrayList read(String fileName) throws IOException {
		
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
		
		ArrayList<HashMap<String, Object>> logList = new ArrayList<HashMap<String, Object>>();
		
		while (scan.hasNext()) {
			
			logLine = scan.nextLine();
			
			JsonParser parser = factory.createParser(logLine);
			
			parser.nextToken();
			
			HashMap<String, Object> oneLogMap = parseDepth(parser);
			
			logList.add(oneLogMap);
		}
		
		return logList;
		
	}
	
	private HashMap<String, Object> parseDepth(JsonParser parser) throws IOException{
		String parseKey = new String();
		Object parseVal = new String();
		
		HashMap<String, Object> oneLogMap = new HashMap<String, Object>();
		
		while(!parser.isClosed()) {
			JsonToken token = parser.nextToken();
			if(JsonToken.FIELD_NAME.equals(token)) {
				parseKey = parser.getCurrentName();
			} else if(JsonToken.VALUE_STRING.equals(token)) {
				parseVal = parser.getValueAsString();
				oneLogMap.put(parseKey, parseVal);
			} else if(JsonToken.VALUE_NUMBER_INT.equals(token)){
				if (!parseKey.equals("flow_id")) {
					parseVal = parser.getValueAsInt();
				} else {
					parseVal = parser.getValueAsString();
				}
				oneLogMap.put(parseKey, parseVal);
			} else if(JsonToken.VALUE_TRUE.equals(token) || JsonToken.VALUE_FALSE.equals(token)){
				parseVal = parser.getValueAsBoolean();
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