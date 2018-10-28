package utility;

import java.util.GregorianCalendar;
import java.util.TimeZone;

public class OraUtility {
	public static long convertToGMT(GregorianCalendar dataOriginaria){
		TimeZone tz = TimeZone.getDefault();
		long tempoMillisecondi = dataOriginaria.getTimeInMillis() - tz.getRawOffset();
		
		if(tz.inDaylightTime(dataOriginaria.getTime()))
			tempoMillisecondi = tempoMillisecondi - tz.getDSTSavings();
		
		return tempoMillisecondi;
	}
}
