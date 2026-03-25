package org.jfree.data.rangetest;

import static org.junit.Assert.*;

import org.jfree.data.Range;
import org.junit.Test;

import java.lang.reflect.Field;

public class RangeGetLowerBoundTest {

    /**
     * Test the normal case where lower <= upper.
     * The method should return the lower bound.
     */
    @Test
    public void testGetLowerBoundNormalCase() {
        Range range = new Range(2.0, 8.0);

        double result = range.getLowerBound();

        assertEquals(2.0, result, 0.0000001);
    }

    /**
     * Test the branch where lower > upper.
     * Reflection is used to force an invalid state
     * because the constructor prevents this condition.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetLowerBoundThrowsException() throws Exception {

        Range range = new Range(1.0, 5.0);

        // Access private fields
        Field lowerField = Range.class.getDeclaredField("lower");
        Field upperField = Range.class.getDeclaredField("upper");

        lowerField.setAccessible(true);
        upperField.setAccessible(true);

        // Force lower > upper
        lowerField.set(range, 10.0);
        upperField.set(range, 5.0);

        // This should trigger the exception branch
        range.getLowerBound();
    }
}
