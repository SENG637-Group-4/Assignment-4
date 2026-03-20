package org.jfree.data;

import static org.junit.Assert.*;
import org.junit.Test;

import java.lang.reflect.Field;

public class RangeGetUpperBoundTest {

    /**
     * Test normal branch where lower <= upper.
     */
    @Test
    public void testGetUpperBoundNormalCase() {
        Range range = new Range(2.0, 8.0);

        double result = range.getUpperBound();

        assertEquals(8.0, result, 0.0000001);
    }

    /**
     * Test exception branch where lower > upper.
     * Reflection is used to force an invalid state.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetUpperBoundThrowsException() throws Exception {

        Range range = new Range(1.0, 5.0);

        Field lowerField = Range.class.getDeclaredField("lower");
        Field upperField = Range.class.getDeclaredField("upper");

        lowerField.setAccessible(true);
        upperField.setAccessible(true);

        // Force invalid state
        lowerField.set(range, 10.0);
        upperField.set(range, 5.0);

        range.getUpperBound();
    }
}
