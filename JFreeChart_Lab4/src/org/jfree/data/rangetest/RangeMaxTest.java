package org.jfree.data.rangetest;

import static org.junit.Assert.*;

import org.jfree.data.Range;
import org.junit.Test;

import java.lang.reflect.Method;

public class RangeMaxTest {

    /**
     * Helper method to invoke the private max() using reflection
     */
    private double invokeMax(double d1, double d2) throws Exception {
        Method method = Range.class.getDeclaredMethod("max", double.class, double.class);
        method.setAccessible(true);
        return (double) method.invoke(null, d1, d2);
    }

    /**
     * Test branch where d1 is NaN
     */
    @Test
    public void testMaxWhenFirstIsNaN() throws Exception {
        double result = invokeMax(Double.NaN, 5.0);

        assertEquals(5.0, result, 0.0000001);
    }

    /**
     * Test branch where d2 is NaN
     */
    @Test
    public void testMaxWhenSecondIsNaN() throws Exception {
        double result = invokeMax(3.0, Double.NaN);

        assertEquals(3.0, result, 0.0000001);
    }

    /**
     * Test normal branch where neither value is NaN
     */
    @Test
    public void testMaxNormalValues() throws Exception {
        double result = invokeMax(3.0, 7.0);

        assertEquals(7.0, result, 0.0000001);
    }
}
