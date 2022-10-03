package ltu;

import java.util.Calendar;
import java.util.Date;

public class VT2016SaturdayCalendarImpl implements ICalendar
{
	@Override
	public Date getDate() {
		Calendar cal = Calendar.getInstance();
		cal.set(2016, 4, 1);
		return cal.getTime();
	}

}