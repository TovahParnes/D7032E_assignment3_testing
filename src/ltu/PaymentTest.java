package ltu;

import static ltu.CalendarFactory.getCalendar;
import static org.junit.Assert.*;
import java.io.IOException;

import org.junit.Test;
import java.util.Calendar;
import org.junit.Test;
import ltu.PaymentImpl;
import ltu.VT2016CalendarImpl;

public class PaymentTest
{

    private final int FULL_LOAN = 7088;
    private final int HALF_LOAN = 3564;
    private final int ZERO_LOAN = 0;
    private final int FULL_SUBSIDY = 2816;
    private final int HALF_SUBSIDY = 1396;
    private final int ZERO_SUBSIDY = 0;
    private final int FULLTIME_INCOME = 85813;
    private final int PARTTIME_INCOME = 128722;


    //Age requirements
    @Test
    // [ID: 101] The student must be at least 20 years old to receive subsidiary and student loans.
    public void age_101() throws IOException {
    	PaymentImpl payment = new PaymentImpl(new VT2016CalendarImpl());
    	
    	// Student under 20
    	int under_20 = payment.getMonthlyAmount("20040101-0000", 0, 100, 100);
    	assertEquals(ZERO_LOAN + ZERO_SUBSIDY, under_20);

        // Student equal 20
    	int equal_20 = payment.getMonthlyAmount("19960101-0000", 0, 100, 100);
    	assertEquals(FULL_LOAN + FULL_SUBSIDY, equal_20);
    	
    	// Student over 20
    	int over_20 = payment.getMonthlyAmount("19910101-0000", 0, 100, 100);
    	assertEquals(FULL_LOAN + FULL_SUBSIDY, over_20);
    }

    @Test
    // [ID: 102] The student may receive subsidiary until the year they turn 56.
    public void age_102() throws IOException {
    	PaymentImpl payment = new PaymentImpl(new VT2016CalendarImpl());
    	
    	// Student under 56
    	int under_56 = payment.getMonthlyAmount("19601231-0000", 0, 100, 100);
    	assertEquals(FULL_SUBSIDY + ZERO_SUBSIDY, under_56);
    	
    	// Student over 56
    	int over_56 = payment.getMonthlyAmount("19591231-0000", 0, 100, 100);
    	assertEquals(ZERO_LOAN + ZERO_SUBSIDY, over_56);
    }

    @Test
    // [ID: 103] The student may not receive any student loans from the year they turn 47.
    public void age_103() throws IOException {
    	PaymentImpl payment = new PaymentImpl(new VT2016CalendarImpl());
    	
    	// Student under 47
    	int under_47 = payment.getMonthlyAmount("19701231-0000", 0, 100, 100);
    	assertEquals(FULL_LOAN + FULL_SUBSIDY, under_47);
    	
    	// Student over 47
    	int over_47 = payment.getMonthlyAmount("19691231-0000", 0, 100, 100);
    	assertEquals(ZERO_LOAN + FULL_SUBSIDY, over_47);

        // Student over 47
    	int equal_49 = payment.getMonthlyAmount("19681231-0000", 0, 100, 100);
    	assertEquals(ZERO_LOAN + FULL_SUBSIDY, equal_49);
    }

    // Study pace requirements
    @Test
    // [ID: 201] The student must be studying at least half time to receive any subsidiary.
    public void pace_201() throws IOException {
    	PaymentImpl payment = new PaymentImpl(getCalendar());
    	
    	// Study rate under 50
    	int under_50 = payment.getMonthlyAmount("20000001-0000", 0, 49, 100);
    	assertEquals(ZERO_LOAN + ZERO_SUBSIDY, under_50);
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
    	int equal_85813 = payment.getMonthlyAmount("20000001-0000", FULLTIME_INCOME, 100, 100);
    	assertEquals(FULL_LOAN + FULL_SUBSIDY, equal_85813);

        // Income over 85 813SEK, full study rate
    	int over_85813 = payment.getMonthlyAmount("20000001-0000", FULLTIME_INCOME + 1, 100, 100);
    	assertEquals(ZERO_LOAN + ZERO_SUBSIDY, over_85813);
    }

    @Test
    // [ID: 302] A student who is studying less than full time is allowed to earn a maximum of 128 722SEK per year in order to receive any subsidiary or student loans.
    public void pace_302() throws IOException {
    	PaymentImpl payment = new PaymentImpl(getCalendar());

        // Income equal to 128 722SEK, half study rate
    	int equal_128722 = payment.getMonthlyAmount("20000001-0000", PARTTIME_INCOME, 50, 100);
    	assertEquals(HALF_LOAN + HALF_SUBSIDY, equal_128722);

        // Income over 128 722SEK, half study rate
    	int over_128722 = payment.getMonthlyAmount("20000001-0000", PARTTIME_INCOME + 1, 50, 100);
    	assertEquals(ZERO_LOAN + ZERO_SUBSIDY, over_128722);
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

    @Test
    // Full time students are entitled to:
    // [ID: 501] Student loan: 7088 SEK / month
    public void pace_501() throws IOException {
    	PaymentImpl payment = new PaymentImpl(getCalendar());

        // full time, check loan
    	int full_time = payment.getMonthlyAmount("20000001-0000", 0, 100, 100);
    	assertEquals(FULL_LOAN, full_time - FULL_SUBSIDY);
    }

    @Test
    // Full time students are entitled to:
    // [ID: 502] Subsidiary: 2816 SEK / month
    public void pace_502() throws IOException {
    	PaymentImpl payment = new PaymentImpl(getCalendar());

        // full time, check subsiday
    	int full_time = payment.getMonthlyAmount("20000001-0000", 0, 100, 100);
    	assertEquals(FULL_SUBSIDY, full_time - FULL_LOAN);
    }

    @Test
    // Less than full time students are entitled to:
    // [ID: 503] Student loan: 3564 SEK / month
    public void pace_503() throws IOException {
    	PaymentImpl payment = new PaymentImpl(getCalendar());

        // not full time, check loan
    	int not_full_time = payment.getMonthlyAmount("20000001-0000", 0, 99, 100);
    	assertEquals(HALF_LOAN, not_full_time - HALF_SUBSIDY);
    }

    @Test
    // Less than full time students are entitled to:
    // [ID: 504] Subsidiary: 1396 SEK / month
    public void pace_504() throws IOException {
    	PaymentImpl payment = new PaymentImpl(getCalendar());

        // not full time, check subsidary
    	int not_full_time = payment.getMonthlyAmount("20000001-0000", 0, 99, 100);
    	assertEquals(HALF_SUBSIDY, not_full_time - HALF_LOAN);
    }

    @Test
    // [ID: 505] A person who is entitled to receive a student loan will always receive the full amount.
    public void full_amount505() throws IOException {
    	PaymentImpl payment = new PaymentImpl(getCalendar());
    	
    	int full_amount_full_time = payment.getMonthlyAmount("19970101-0000", 0, 100, 100);
    	assertEquals(FULL_LOAN + FULL_SUBSIDY, full_amount_full_time);
    	
    	int full_amount_half_time = payment.getMonthlyAmount("19970101-0000", 0, 50, 100);
    	assertEquals(HALF_LOAN + HALF_SUBSIDY, full_amount_half_time);
    }

    @Test
    // [ID: 506] Student loans and subsidiary is paid on the last weekday (Monday to Friday) every month.
    public void pace_506Sunday() throws IOException {
    	PaymentImpl payment = new PaymentImpl(new VT2016CalendarImpl());

        // payment day is a sunday
    	String payment_day = payment.getNextPaymentDay();
    	assertEquals("20160129", payment_day);
    }

    public void pace_506Saturday() throws IOException {
    	PaymentImpl payment = new PaymentImpl(new VT2016SaturdayCalendarImpl());

        // payment day is a sunday
    	String payment_day = payment.getNextPaymentDay();
    	assertEquals("20160429", payment_day);
    }
}
