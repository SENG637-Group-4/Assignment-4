package org.jfree.data.rangetest;

import static org.junit.Assert.*;

import org.jfree.data.Range;
import org.junit.Test;

public class RangeHashCodeTest {

    // ------------------------------------------------------------------
    // Determinism: same range always produces same hash code
    // ------------------------------------------------------------------

    @Test
    public void testSameRangeProducesSameHashCode() {
        Range r1 = new Range(1.0, 10.0);
        Range r2 = new Range(1.0, 10.0);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    public void testHashCodeIsConsistentAcrossMultipleCalls() {
        Range range = new Range(1.0, 10.0);
        int first  = range.hashCode();
        int second = range.hashCode();
        int third  = range.hashCode();
        assertEquals(first, second);
        assertEquals(second, third);
    }

    // ------------------------------------------------------------------
    // Different ranges should (in practice) produce different hash codes
    // ------------------------------------------------------------------

    @Test
    public void testDifferentLowerProducesDifferentHashCode() {
        Range r1 = new Range(1.0, 10.0);
        Range r2 = new Range(2.0, 10.0);
        assertNotEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    public void testDifferentUpperProducesDifferentHashCode() {
        Range r1 = new Range(1.0, 10.0);
        Range r2 = new Range(1.0, 20.0);
        assertNotEquals(r1.hashCode(), r2.hashCode());
    }

    // ------------------------------------------------------------------
    // Negative range
    // ------------------------------------------------------------------

    @Test
    public void testNegativeRangeIsDeterministic() {
        Range r1 = new Range(-10.0, -1.0);
        Range r2 = new Range(-10.0, -1.0);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    // ------------------------------------------------------------------
    // Range spanning zero
    // ------------------------------------------------------------------

    @Test
    public void testRangeSpanningZeroIsDeterministic() {
        Range r1 = new Range(-5.0, 5.0);
        Range r2 = new Range(-5.0, 5.0);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    // ------------------------------------------------------------------
    // Zero-length range
    // ------------------------------------------------------------------

    @Test
    public void testZeroLengthRangeIsDeterministic() {
        Range r1 = new Range(5.0, 5.0);
        Range r2 = new Range(5.0, 5.0);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    public void testZeroLengthAtZeroIsDeterministic() {
        Range r1 = new Range(0.0, 0.0);
        Range r2 = new Range(0.0, 0.0);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    // ------------------------------------------------------------------
    // Special floating-point values
    // ------------------------------------------------------------------

    @Test
    public void testPositiveInfinityUpperIsDeterministic() {
        Range r1 = new Range(1.0, Double.POSITIVE_INFINITY);
        Range r2 = new Range(1.0, Double.POSITIVE_INFINITY);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    public void testNegativeInfinityLowerIsDeterministic() {
        Range r1 = new Range(Double.NEGATIVE_INFINITY, 1.0);
        Range r2 = new Range(Double.NEGATIVE_INFINITY, 1.0);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    public void testBothInfinityIsDeterministic() {
        Range r1 = new Range(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        Range r2 = new Range(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    public void testMaxValueIsDeterministic() {
        Range r1 = new Range(Double.MAX_VALUE, Double.MAX_VALUE);
        Range r2 = new Range(Double.MAX_VALUE, Double.MAX_VALUE);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    // ------------------------------------------------------------------
    // Hash code is an int (return type contract)
    // ------------------------------------------------------------------

    @Test
    public void testHashCodeReturnsInt() {
        Range range = new Range(1.0, 10.0);
        int hash = range.hashCode();
        assertTrue(hash >= Integer.MIN_VALUE && hash <= Integer.MAX_VALUE);
    }
    
    @Test
    public void testHashCodeConsistency() {
        Range r = new Range(1.0, 5.0);

        assertEquals(r.hashCode(), r.hashCode());
    }
    
    @Test
    public void testHashCodeEqualObjects() {
        Range r1 = new Range(1.0, 5.0);
        Range r2 = new Range(1.0, 5.0);

        assertEquals(r1.hashCode(), r2.hashCode());
    }
    
    @Test
    public void testHashCodeDifferentLower() {
        Range r1 = new Range(1.0, 5.0);
        Range r2 = new Range(2.0, 5.0);

        assertNotEquals(r1.hashCode(), r2.hashCode());
    }
    
    @Test
    public void testHashCodeDifferentUpper() {
        Range r1 = new Range(1.0, 5.0);
        Range r2 = new Range(1.0, 6.0);

        assertNotEquals(r1.hashCode(), r2.hashCode());
    }
    
    @Test
    public void testHashCodeNegativeValues() {
        Range r1 = new Range(-5.0, -1.0);
        Range r2 = new Range(-5.0, -1.0);

        assertEquals(r1.hashCode(), r2.hashCode());
    }
    
    @Test
    public void testHashCodeZeroVsNegativeZero() {
        Range r1 = new Range(0.0, 1.0);
        Range r2 = new Range(-0.0, 1.0);

        assertNotEquals(r1.hashCode(), r2.hashCode());
    }
}