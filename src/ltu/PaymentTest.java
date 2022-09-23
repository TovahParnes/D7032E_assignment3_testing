package ltu;

import static org.junit.Assert.*;

import org.junit.Test;

public class PaymentTest
{
    //Age requirements
    @Test
    public void above20()
    {
        assertTrue("At least 20 years",getAge() >= 20);
    }

}
