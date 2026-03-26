package org.jfree.data.rangetest;

import static org.junit.Assert.*;

import org.jfree.data.Range;
import org.junit.Test;

public class RangeIsNaNRangeTest {

    /**
     * Both bounds are NaN → should return true
     */
    @Test
    public void testBothNaN() {
        Range r = new Range(Double.NaN, Double.NaN);

        assertTrue(r.isNaNRange());
    }

    /**
     * Only lower is NaN → should return false
     */
    @Test
    public void testLowerNaNOnly() {
        Range r = new Range(Double.NaN, 5.0);

        assertFalse(r.isNaNRange());
    }

    /**
     * Only upper is NaN → should return false
     */
    @Test
    public void testUpperNaNOnly() {
        Range r = new Range(3.0, Double.NaN);

        assertFalse(r.isNaNRange());
    }

    /**
     * Neither bound is NaN → should return false
     */
    @Test
    public void testNoNaN() {
        Range r = new Range(2.0, 6.0);

        assertFalse(r.isNaNRange());
    }
    
}
