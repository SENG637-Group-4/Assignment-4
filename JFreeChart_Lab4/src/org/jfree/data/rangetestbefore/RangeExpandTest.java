package org.jfree.data.rangetestbefore;

import static org.junit.Assert.*;

import org.jfree.data.Range;
import org.junit.Test;

public class RangeExpandTest {

    /**
     * Normal expansion where lower <= upper
     */
    @Test
    public void testExpandNormal() {
        Range r = new Range(2.0, 6.0);

        Range result = Range.expand(r, 0.5, 0.5);
        // length = 4, lower = 2 - 4*0.5 = 0, upper = 6 + 4*0.5 = 8
        assertEquals(0.0, result.getLowerBound(), 0.0000001);
        assertEquals(8.0, result.getUpperBound(), 0.0000001);
    }

    /**
     * Expansion where lower > upper → triggers adjustment
     */
//    @Test
//    public void testExpandLowerGreaterThanUpper() {
//        Range r = new Range(2.0, 6.0);
//
//        // Use margins that reverse the bounds
//        Range result = Range.expand(r, 2.0, -2.0);
//        // length = 4, lower = 2 - 4*2 = -6, upper = 6 + 4*(-2) = -2 → lower > upper triggers adjustment
//        // adjusted lower = (-6 + -2)/2 = -4, upper = -4
//        assertEquals(-4.0, result.getLowerBound(), 0.0000001);
//        assertEquals(-4.0, result.getUpperBound(), 0.0000001);
//    }

    /**
     * Null range → should throw exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void testExpandNullRange() {
        Range.expand(null, 1.0, 1.0);
    }
}
