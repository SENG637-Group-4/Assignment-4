package org.jfree.data.rangetestafter;

import static org.junit.Assert.*;

import org.jfree.data.Range;
import org.junit.Test;

public class ConstrainTest {

    // ------------------------------------------------------------------
    // Value is WITHIN the range (contains() returns true)
    // → method returns the value unchanged
    // ------------------------------------------------------------------

    @Test
    public void testValueAtLowerBound() {
        Range range = new Range(1.0, 10.0);
        assertEquals(1.0, range.constrain(1.0), 1e-9);
    }

    @Test
    public void testValueAtUpperBound() {
        Range range = new Range(1.0, 10.0);
        assertEquals(10.0, range.constrain(10.0), 1e-9);
    }

    @Test
    public void testValueInsideRange() {
        Range range = new Range(1.0, 10.0);
        assertEquals(5.0, range.constrain(5.0), 1e-9);
    }

    @Test
    public void testValueJustAboveLowerBound() {
        Range range = new Range(1.0, 10.0);
        assertEquals(1.0001, range.constrain(1.0001), 1e-9);
    }

    @Test
    public void testValueJustBelowUpperBound() {
        Range range = new Range(1.0, 10.0);
        assertEquals(9.9999, range.constrain(9.9999), 1e-9);
    }

    // ------------------------------------------------------------------
    // Value is ABOVE the range (value > upper)
    // → method returns upper
    // ------------------------------------------------------------------

    @Test
    public void testValueAboveUpperBound() {
        Range range = new Range(1.0, 10.0);
        assertEquals(10.0, range.constrain(15.0), 1e-9);
    }

    @Test
    public void testValueJustAboveUpperBound() {
        Range range = new Range(1.0, 10.0);
        assertEquals(10.0, range.constrain(10.0001), 1e-9);
    }

    @Test
    public void testValueFarAboveUpperBound() {
        Range range = new Range(1.0, 10.0);
        assertEquals(10.0, range.constrain(1000.0), 1e-9);
    }

    // ------------------------------------------------------------------
    // Value is BELOW the range (value < lower)
    // → method returns lower
    // ------------------------------------------------------------------

    @Test
    public void testValueBelowLowerBound() {
        Range range = new Range(1.0, 10.0);
        assertEquals(1.0, range.constrain(-5.0), 1e-9);
    }

    @Test
    public void testValueJustBelowLowerBound() {
        Range range = new Range(1.0, 10.0);
        assertEquals(1.0, range.constrain(0.9999), 1e-9);
    }

    @Test
    public void testValueFarBelowLowerBound() {
        Range range = new Range(1.0, 10.0);
        assertEquals(1.0, range.constrain(-1000.0), 1e-9);
    }

    // ------------------------------------------------------------------
    // Negative range
    // ------------------------------------------------------------------

    @Test
    public void testNegativeRangeValueInside() {
        Range range = new Range(-10.0, -1.0);
        assertEquals(-5.0, range.constrain(-5.0), 1e-9);
    }

    @Test
    public void testNegativeRangeValueAbove() {
        Range range = new Range(-10.0, -1.0);
        assertEquals(-1.0, range.constrain(0.0), 1e-9);
    }

    @Test
    public void testNegativeRangeValueBelow() {
        Range range = new Range(-10.0, -1.0);
        assertEquals(-10.0, range.constrain(-20.0), 1e-9);
    }

    // ------------------------------------------------------------------
    // Range spanning zero
    // ------------------------------------------------------------------

    @Test
    public void testRangeSpanningZeroValueInside() {
        Range range = new Range(-5.0, 5.0);
        assertEquals(0.0, range.constrain(0.0), 1e-9);
    }

    @Test
    public void testRangeSpanningZeroValueAbove() {
        Range range = new Range(-5.0, 5.0);
        assertEquals(5.0, range.constrain(10.0), 1e-9);
    }

    @Test
    public void testRangeSpanningZeroValueBelow() {
        Range range = new Range(-5.0, 5.0);
        assertEquals(-5.0, range.constrain(-10.0), 1e-9);
    }

    // ------------------------------------------------------------------
    // Zero-length range (lower == upper)
    // ------------------------------------------------------------------

    @Test
    public void testZeroLengthRangeValueAtBound() {
        Range range = new Range(5.0, 5.0);
        assertEquals(5.0, range.constrain(5.0), 1e-9);
    }

    @Test
    public void testZeroLengthRangeValueAbove() {
        Range range = new Range(5.0, 5.0);
        assertEquals(5.0, range.constrain(10.0), 1e-9);
    }

    @Test
    public void testZeroLengthRangeValueBelow() {
        Range range = new Range(5.0, 5.0);
        assertEquals(5.0, range.constrain(1.0), 1e-9);
    }

    // ------------------------------------------------------------------
    // Special floating-point values
    // ------------------------------------------------------------------

    @Test
    public void testPositiveInfinityAboveRange() {
        Range range = new Range(1.0, 10.0);
        assertEquals(10.0, range.constrain(Double.POSITIVE_INFINITY), 1e-9);
    }

    @Test
    public void testNegativeInfinityBelowRange() {
        Range range = new Range(1.0, 10.0);
        assertEquals(1.0, range.constrain(Double.NEGATIVE_INFINITY), 1e-9);
    }

    @Test
    public void testMaxValueAboveRange() {
        Range range = new Range(1.0, 10.0);
        assertEquals(10.0, range.constrain(Double.MAX_VALUE), 1e-9);
    }

    @Test
    public void testMinValueBelowRange() {
        Range range = new Range(1.0, 10.0);
        assertEquals(1.0, range.constrain(-Double.MAX_VALUE), 1e-9);
    }
}