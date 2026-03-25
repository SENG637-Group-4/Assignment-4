package org.jfree.data.rangetestbefore;

import static org.junit.Assert.*;

import org.jfree.data.Range;
import org.junit.Test;

public class RangeToStringTest {

    // ------------------------------------------------------------------
    // Basic positive range
    // ------------------------------------------------------------------

    @Test
    public void testPositiveRange() {
        Range range = new Range(1.0, 10.0);
        assertEquals("Range[1.0,10.0]", range.toString());
    }

    // ------------------------------------------------------------------
    // Negative range
    // ------------------------------------------------------------------

    @Test
    public void testNegativeRange() {
        Range range = new Range(-10.0, -1.0);
        assertEquals("Range[-10.0,-1.0]", range.toString());
    }

    // ------------------------------------------------------------------
    // Range spanning zero
    // ------------------------------------------------------------------

    @Test
    public void testRangeSpanningZero() {
        Range range = new Range(-5.0, 5.0);
        assertEquals("Range[-5.0,5.0]", range.toString());
    }

    // ------------------------------------------------------------------
    // Zero-length range (lower == upper)
    // ------------------------------------------------------------------

    @Test
    public void testZeroLengthRange() {
        Range range = new Range(0.0, 0.0);
        assertEquals("Range[0.0,0.0]", range.toString());
    }

    // ------------------------------------------------------------------
    // Bounds at zero
    // ------------------------------------------------------------------

    @Test
    public void testLowerBoundZero() {
        Range range = new Range(0.0, 5.0);
        assertEquals("Range[0.0,5.0]", range.toString());
    }

    @Test
    public void testUpperBoundZero() {
        Range range = new Range(-5.0, 0.0);
        assertEquals("Range[-5.0,0.0]", range.toString());
    }

    // ------------------------------------------------------------------
    // Decimal values
    // ------------------------------------------------------------------

    @Test
    public void testDecimalValues() {
        Range range = new Range(1.5, 2.5);
        assertEquals("Range[1.5,2.5]", range.toString());
    }

    @Test
    public void testNegativeDecimalValues() {
        Range range = new Range(-2.5, -1.5);
        assertEquals("Range[-2.5,-1.5]", range.toString());
    }

    // ------------------------------------------------------------------
    // Large values
    // ------------------------------------------------------------------

    @Test
    public void testLargeValues() {
        Range range = new Range(1000000.0, 9999999.0);
        assertEquals("Range[1000000.0,9999999.0]", range.toString());
    }

    // ------------------------------------------------------------------
    // Special floating-point values
    // ------------------------------------------------------------------

    @Test
    public void testPositiveInfinityUpperBound() {
        Range range = new Range(1.0, Double.POSITIVE_INFINITY);
        assertEquals("Range[1.0,Infinity]", range.toString());
    }

    @Test
    public void testNegativeInfinityLowerBound() {
        Range range = new Range(Double.NEGATIVE_INFINITY, 1.0);
        assertEquals("Range[-Infinity,1.0]", range.toString());
    }

    @Test
    public void testBothInfinity() {
        Range range = new Range(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        assertEquals("Range[-Infinity,Infinity]", range.toString());
    }
}