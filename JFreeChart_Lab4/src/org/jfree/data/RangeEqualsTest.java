package org.jfree.data;

import static org.junit.Assert.*;
import org.junit.Test;

public class RangeEqualsTest {

    // ------------------------------------------------------------------
    // Equal ranges
    // ------------------------------------------------------------------

    @Test
    public void testEqualPositiveRanges() {
        Range r1 = new Range(1.0, 10.0);
        Range r2 = new Range(1.0, 10.0);
        assertTrue(r1.equals(r2));
    }

    @Test
    public void testEqualNegativeRanges() {
        Range r1 = new Range(-10.0, -1.0);
        Range r2 = new Range(-10.0, -1.0);
        assertTrue(r1.equals(r2));
    }

    @Test
    public void testEqualRangesSpanningZero() {
        Range r1 = new Range(-5.0, 5.0);
        Range r2 = new Range(-5.0, 5.0);
        assertTrue(r1.equals(r2));
    }

    @Test
    public void testEqualZeroLengthRanges() {
        Range r1 = new Range(0.0, 0.0);
        Range r2 = new Range(0.0, 0.0);
        assertTrue(r1.equals(r2));
    }

    @Test
    public void testEqualDecimalRanges() {
        Range r1 = new Range(1.5, 2.5);
        Range r2 = new Range(1.5, 2.5);
        assertTrue(r1.equals(r2));
    }

    // ------------------------------------------------------------------
    // Reflexivity: object equals itself
    // ------------------------------------------------------------------

    @Test
    public void testReflexive() {
        Range r1 = new Range(1.0, 10.0);
        assertTrue(r1.equals(r1));
    }

    // ------------------------------------------------------------------
    // Symmetry: r1.equals(r2) == r2.equals(r1)
    // ------------------------------------------------------------------

    @Test
    public void testSymmetricWhenEqual() {
        Range r1 = new Range(1.0, 10.0);
        Range r2 = new Range(1.0, 10.0);
        assertTrue(r1.equals(r2));
        assertTrue(r2.equals(r1));
    }

    @Test
    public void testSymmetricWhenNotEqual() {
        Range r1 = new Range(1.0, 10.0);
        Range r2 = new Range(2.0, 10.0);
        assertFalse(r1.equals(r2));
        assertFalse(r2.equals(r1));
    }

    // ------------------------------------------------------------------
    // Transitivity: r1==r2, r2==r3 → r1==r3
    // ------------------------------------------------------------------

    @Test
    public void testTransitive() {
        Range r1 = new Range(1.0, 10.0);
        Range r2 = new Range(1.0, 10.0);
        Range r3 = new Range(1.0, 10.0);
        assertTrue(r1.equals(r2));
        assertTrue(r2.equals(r3));
        assertTrue(r1.equals(r3));
    }

    // ------------------------------------------------------------------
    // Not equal — different lower bound
    // ------------------------------------------------------------------

    @Test
    public void testDifferentLowerBound() {
        Range r1 = new Range(1.0, 10.0);
        Range r2 = new Range(2.0, 10.0);
        assertFalse(r1.equals(r2));
    }

    @Test
    public void testDifferentLowerBoundNegative() {
        Range r1 = new Range(-10.0, 5.0);
        Range r2 = new Range(-9.0, 5.0);
        assertFalse(r1.equals(r2));
    }

    // ------------------------------------------------------------------
    // Not equal — different upper bound
    // ------------------------------------------------------------------

    @Test
    public void testDifferentUpperBound() {
        Range r1 = new Range(1.0, 10.0);
        Range r2 = new Range(1.0, 20.0);
        assertFalse(r1.equals(r2));
    }

    @Test
    public void testDifferentUpperBoundNegative() {
        Range r1 = new Range(-5.0, -1.0);
        Range r2 = new Range(-5.0, -2.0);
        assertFalse(r1.equals(r2));
    }

    // ------------------------------------------------------------------
    // Not equal — both bounds different
    // ------------------------------------------------------------------

    @Test
    public void testBothBoundsDifferent() {
        Range r1 = new Range(1.0, 10.0);
        Range r2 = new Range(2.0, 20.0);
        assertFalse(r1.equals(r2));
    }

    // ------------------------------------------------------------------
    // null input — must return false, not throw
    // ------------------------------------------------------------------

    @Test
    public void testEqualsNull() {
        Range r1 = new Range(1.0, 10.0);
        assertFalse(r1.equals(null));
    }

    // ------------------------------------------------------------------
    // Non-Range object types
    // ------------------------------------------------------------------

    @Test
    public void testEqualsString() {
        Range r1 = new Range(1.0, 10.0);
        assertFalse(r1.equals("Range[1.0,10.0]"));
    }

    @Test
    public void testEqualsInteger() {
        Range r1 = new Range(1.0, 10.0);
        assertFalse(r1.equals(42));
    }

    @Test
    public void testEqualsDouble() {
        Range r1 = new Range(1.0, 10.0);
        assertFalse(r1.equals(1.0));
    }

    @Test
    public void testEqualsObject() {
        Range r1 = new Range(1.0, 10.0);
        assertFalse(r1.equals(new Object()));
    }

    // ------------------------------------------------------------------
    // Special floating-point values
    // ------------------------------------------------------------------

    @Test
    public void testEqualPositiveInfinityUpper() {
        Range r1 = new Range(1.0, Double.POSITIVE_INFINITY);
        Range r2 = new Range(1.0, Double.POSITIVE_INFINITY);
        assertTrue(r1.equals(r2));
    }

    @Test
    public void testEqualNegativeInfinityLower() {
        Range r1 = new Range(Double.NEGATIVE_INFINITY, 1.0);
        Range r2 = new Range(Double.NEGATIVE_INFINITY, 1.0);
        assertTrue(r1.equals(r2));
    }

    @Test
    public void testEqualBothInfinity() {
        Range r1 = new Range(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        Range r2 = new Range(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        assertTrue(r1.equals(r2));
    }

    @Test
    public void testDifferentInfinityBounds() {
        Range r1 = new Range(1.0, Double.POSITIVE_INFINITY);
        Range r2 = new Range(Double.NEGATIVE_INFINITY, 1.0);
        assertFalse(r1.equals(r2));
    }
}