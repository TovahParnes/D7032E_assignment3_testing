package ltu;

import static ltu.CalendarFactory.getCalendar;
import static org.junit.Assert.*;
import java.io.IOException;

import org.junit.Test;
import java.util.Calendar;
import org.junit.Test;
import ltu.PaymentImpl;

public class PaymentTest
{

    private int FULL_LOAN = 7088;
    private int HALF_LOAN = 3564;
    private int ZERO_LOAN = 0;
    private int FULL_SUBSIDY = 2816;
    private int HALF_SUBSIDY = 1396;
    private int ZERO_SUBSIDY = 1396;
    private int FULLTIME_INCOME = 85813;
    private int PARTTIME_INCOME = 128722;
    private ICalendar calendar;


    //Age requirements
    @Test
    // [ID: 101] The student must be at least 20 years old to receive subsidiary and student loans.
    public void age_101() throws IOException {
    	PaymentImpl payment = new PaymentImpl(getCalendar());
    	
    	// Student under 20
    	int under_20 = payment.getMonthlyAmount("20100101-0000", 0, 100, 100);
    	assertEquals(0, under_20);
    	
    	// Student over 20
    	int over_20 = payment.getMonthlyAmount("19970101-0000", 0, 100, 100);
    	assertEquals(FULL_LOAN + FULL_SUBSIDY, over_20);
    }

}
