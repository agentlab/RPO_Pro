import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class LogParser {
	public static void main(String args[]) throws IOException {
		System.out.println("My first Java app\n");
		
		FileWorker fw = new FileWorker();
		
		ArrayList<HashMap> eventsList = fw.read("/var/log/suricata/eve.json");
		
		System.out.println("exit");
		
		//System.out.println(eventsList.get();
		
		/*for (HashMap<String, Object> log : eventsList) {
			for(Map.Entry<String, Object> note : log.entrySet()) {
				System.out.print("key: " + note.getKey());
				if(note.getValue().getClass() != ) {
					
				}
			}
		}*/
	}
}