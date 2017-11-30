import java.io.IOException;
import java.util.ArrayList;

class LogParser {
	public static void main(String args[]) throws IOException {
		System.out.println("My first Java app\n");
		
		FileWorker fw = new FileWorker();
		
		ArrayList eventsList = fw.read("/var/log/suricata/eve.json");
		
		for (Object log : eventsList) {
			
		}
	}
}