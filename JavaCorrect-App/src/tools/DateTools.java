package tools;

import java.time.LocalDate;

public class DateTools {

	public static int checkMinimumDeadlineDays(LocalDate lc, int days){
		LocalDate now = LocalDate.now();
		LocalDate excpected = now.plusDays(days);
		return lc.compareTo(excpected);
	}

	
	
}
