import java.util.Calendar;
import java.util.GregorianCalendar;

class LogStruct{
	private GregorianCalendar eventTime;
	private String type;
	private String message;
	
	public LogStruct(GregorianCalendar evDate, String evType, String evMessage) {
		eventTime = evDate;
		type = evType;
		message = evMessage;
	}
	
	public LogStruct() {
		eventTime = new GregorianCalendar(GregorianCalendar.YEAR,
										  GregorianCalendar.JANUARY,
										  GregorianCalendar.DAY_OF_MONTH,
										  0, 0, 0);
		type = "BaseTime";
		message = "BaseMessage";
	}
	
	public GregorianCalendar getEventTime() {
		return eventTime;
	}
	
	public String getType() {
		return type;
	}
	
	public String getMessage() {
		return message;
	}
}