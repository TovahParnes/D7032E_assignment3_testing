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

    @Test
    // [ID: 102] The student may receive subsidiary until the year they turn 56.
    public void age_102() throws IOException {
    	PaymentImpl payment = new PaymentImpl(getCalendar());
    	
    	// Student under 56
    	int under_56 = payment.getMonthlyAmount("19661231-0000", 0, 100, 100);
    	assertEquals(FULL_SUBSIDY, under_56);
    	
    	// Student over 56
    	int over_56 = payment.getMonthlyAmount("19651231-0000", 0, 100, 100);
    	assertEquals(0, over_56);
    }

    @Test
    // [ID: 103] The student may not receive any student loans from the year they turn 47.
    public void age_103() throws IOException {
    	PaymentImpl payment = new PaymentImpl(getCalendar());
    	
    	// Student under 47
    	int under_47 = payment.getMonthlyAmount("19761231-0000", 0, 100, 100);
    	assertEquals(FULL_LOAN + FULL_SUBSIDY, under_47);
    	
    	// Student over 47
    	int over_47 = payment.getMonthlyAmount("19751231-0000", 0, 100, 100);
    	assertEquals(FULL_SUBSIDY, over_47);
    }

    // Study pace requirements
    @Test
    // [ID: 201] The student must be studying at least half time to receive any subsidiary.
    public void pace_201() throws IOException {
    	PaymentImpl payment = new PaymentImpl(getCalendar());
    	
    	// Study rate under 50
    	int under_50 = payment.getMonthlyAmount("20000001-0000", 0, 49, 100);
    	assertEquals(0, under_50);
    }

    @Test
    // [ID: 202] A student studying less than full time is entitled to 50% subsidiary.
    public void pace_202() throws IOException {
    	PaymentImpl payment = new PaymentImpl(getCalendar());
    	
    	// Study rate equals 50
    	int equal_50 = payment.getMonthlyAmount("20000001-0000", 0, 50, 100);
    	assertEquals(HALF_LOAN + HALF_SUBSIDY, equal_50);

        // Study rate under 100
    	int under_100 = payment.getMonthlyAmount("20000001-0000", 0, 99, 100);
    	assertEquals(HALF_LOAN + HALF_SUBSIDY, under_100);
    }

    @Test
    // [ID: 203] A student studying full time is entitled to 100% subsidiary.
    public void pace_203() throws IOException {
    	PaymentImpl payment = new PaymentImpl(getCalendar());
    	
    	// Study rate equals 100
    	int equal_50 = payment.getMonthlyAmount("20000001-0000", 0, 100, 100);
    	assertEquals(FULL_LOAN + FULL_SUBSIDY, equal_50);
    }


    // Income while studying requirements
    @Test
    // [ID: 301] A student who is studying full time or more is permitted to earn a maximum of 85 813SEK per year in order to receive any subsidiary or student loans.
    public void pace_301() throws IOException {
    	PaymentImpl payment = new PaymentImpl(getCalendar());

        // Income equal to 85 813SEK, full study rate
    	int equal_85813 = payment.getMonthlyAmount("20000001-0000", 85813, 100, 100);
    	assertEquals(FULL_LOAN + FULL_SUBSIDY, equal_85813);

        // Income over 85 813SEK, full study rate
    	int over_85813 = payment.getMonthlyAmount("20000001-0000", 85814, 100, 100);
    	assertEquals(0, over_85813);
    }

    @Test
    // [ID: 302] A student who is studying less than full time is allowed to earn a maximum of 128 722SEK per year in order to receive any subsidiary or student loans.
    public void pace_302() throws IOException {
    	PaymentImpl payment = new PaymentImpl(getCalendar());

        // Income equal to 128 722SEK, half study rate
    	int equal_128722 = payment.getMonthlyAmount("20000001-0000", 128722, 50, 100);
    	assertEquals(HALF_LOAN + HALF_SUBSIDY, equal_128722);

        // Income over 128 722SEK, half study rate
    	int over_128722 = payment.getMonthlyAmount("20000001-0000", 128723, 50, 100);
    	assertEquals(0, over_128722);
    }


    // Completion ratio requirement
    @Test
    // [ID: 401] A student must have completed at least 50% of previous studies in order to receive any subsidiary or student loans.
    public void pace_401() throws IOException {
    	PaymentImpl payment = new PaymentImpl(getCalendar());

        // Completed 50% of previous studies
    	int equal_50 = payment.getMonthlyAmount("20000001-0000", 0, 100, 50);
    	assertEquals(FULL_LOAN + FULL_SUBSIDY, equal_50);

        // Completed under 50% of previous studies
    	int under_49 = payment.getMonthlyAmount("20000001-0000", 0, 100, 49);
    	assertEquals(0, under_49);
    }
}
