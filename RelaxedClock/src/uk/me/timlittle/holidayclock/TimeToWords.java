package uk.me.timlittle.holidayclock;

import java.util.Calendar;

public class TimeToWords {
	private static final StringBuilder builder = 
	        new StringBuilder();
	private static final String[] hourNames = {"twelve", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten", "eleven"};
	private static final String[] minuteNames = {"", "five", "ten", "a quarter", "twenty", "twenty five", "half"};
	
	private static int nearestFive(int num) {
	    int temp = num%5;
	    if (temp<3)
	         return num-temp;
	    else
	         return num+5-temp;
	}
	
	public synchronized static String timeToWords( Calendar date ) {
		int minute;
		int roughMinute;
		int hour;
		String hourName;
		String minuteName;
		String direction = " past ";
		String roughWord = "about ";
		char firstLetter;
		
		builder.setLength( 0 );
		hour = date.get(Calendar.HOUR_OF_DAY);
		minute = date.get(Calendar.MINUTE);
		
		roughMinute = nearestFive(minute);
		if (minute == roughMinute)
			roughWord = "";
		else if (minute < roughMinute)
			roughWord = "almost ";
		else if (minute > roughMinute)
			roughWord = "about ";
		

		if (roughMinute > 30) {
			hour++;
			
			roughMinute = 60 - roughMinute;
			direction = " to ";
		}
		
		if ((hour != 0) && (hour !=24))
			hourName = hourNames[hour % 12];
		else
			hourName = "midnight";
		
		minuteName = minuteNames[roughMinute / 5];
				
		if (roughMinute == 0) {
			
			
			if ((hour != 0) && (hour != 24)){
				builder.append(hourName);
				
				if (minute != 0)
					builder.append("-ish");
				else
					builder.append(" o'clock");
				
			} else {
				if (minute < roughMinute)
					builder.append("almost ");
				
				builder.append(hourName);
				
			}
		} else {
			builder.append(roughWord);
			builder.append(minuteName);
			builder.append(direction);
			builder.append(hourName);
		}

		firstLetter = builder.charAt(0);
		firstLetter = Character.toUpperCase(firstLetter);
		builder.setCharAt(0, firstLetter);
		
		
		return builder.toString();
	}
	
	public synchronized static String timeToWordsQuarters( 
	        Calendar date )
	    {
			int minute;
			int hour;
			String hourName;
			
			builder.setLength( 0 );
			hour = date.get(Calendar.HOUR);
			minute = date.get(Calendar.MINUTE);

			if (minute > 38){
				hour++;
			}
			
			if (hour > 11)
				hour = hour % 12;
			
			hourName = hourNames[hour];
			
			if ((minute >55) || (minute <=8)){
				builder.append(hourName);
				
				builder.append("-ish");
			}
				
			if ((minute >8) && (minute <=23)){			
				builder.append("about a quarter past ");
				builder.append(hourName);
			}

			if ((minute >23) && (minute <=38)){			
				builder.append("roughly half past ");
				builder.append(hourName);				
			}
			
			if ((minute >38) && (minute <=55)){			
				
				hourName = hourNames[hour];

				builder.append("about a quarter to ");
				builder.append(hourName);
				
			}
			
			if (builder.length()==0) {
				builder.append("who cares?");
			}
			
			
	        return builder.toString();
	    }	        
}
