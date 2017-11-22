import java.util.Calendar;
import java.util.GregorianCalendar;

class LogStruct{
	private GregorianCalendar eventTime;
	private String type;
	private String message;
	private String someTextBetweenBrackets;
	private int priority;
	private String protocol;
	private String sender;
	private String receiver;
	
	public LogStruct(GregorianCalendar evDate, String evType, String evMessage, 
					 String evSTBB, int evPriority, String evProt,
					 String evSender, String evReceiver) {
		eventTime = evDate;
		type = evType;
		message = evMessage;
		someTextBetweenBrackets = evSTBB;
		priority = evPriority;
		protocol = evProt;
		sender = evSender;
		receiver = evReceiver;
	}
	
	public LogStruct() {
		eventTime = new GregorianCalendar(GregorianCalendar.YEAR,
										  GregorianCalendar.JANUARY,
										  GregorianCalendar.DAY_OF_MONTH,
										  0, 0, 0);
		type = "BaseTime";
		message = "BaseMessage";
		someTextBetweenBrackets = "BaseSomeTextBetweenBrackets";
		priority = 0;
		protocol = "BaseProtocol";
		sender = "BaseSender";
		receiver = "BaseReceiver";
	}
	
	public GregorianCalendar getEventTime() {
		return eventTime;
	}
	
	public String getType() {
		return type;
	}
	
	public String getSomeTextBetweenBrackets() {
		return someTextBetweenBrackets;
	}
	
	public String getMessage() {
		return message;
	}
	
	public int getPriority() {
		return priority;
	}
	
	public String getProtocol() {
		return protocol;
	}
	
	public String getSender() {
		return sender;
	}
	
	public String getReceiver() {
		return receiver;
	}
}