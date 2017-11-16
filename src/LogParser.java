import java.io.IOException;

class LogParser {
	public static void main(String args[]) throws IOException {
		System.out.println("My first Java app\n");
		
		FileWorker fw = new FileWorker();
		
		fw.read("/var/log/suricata/suricata.log");
		
		//LogStruct ls = new LogStruct();
		
		/*System.out.println(ls.getEventTime().getTime());
		System.out.println(ls.getType());
		System.out.println(ls.getMessage());*/
	}
}