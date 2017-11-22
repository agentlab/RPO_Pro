import java.io.IOException;
import java.util.ArrayList;

class LogParser {
	public static void main(String args[]) throws IOException {
		System.out.println("My first Java app\n");
		
		FileWorker fw = new FileWorker();
		
		ArrayList<LogStruct> eventsList = fw.read("/var/log/suricata/fast.log");
		
		for (LogStruct log : eventsList) {
			System.out.println(log.getEventTime().getTime());
			System.out.println(log.getType());
			System.out.println(log.getMessage() + '\n');
			System.out.println(log.getSomeTextBetweenBrackets() + '\n');
			System.out.println("Priority: " + log.getPriority());
			System.out.println(log.getProtocol() + '\n');
			System.out.println("Sender: " + log.getSender() + '\n');
			System.out.println("Receiver: " + log.getReceiver() + '\n');
			
			System.out.println("-------------------------------------------------------------------");
		}
	}
}