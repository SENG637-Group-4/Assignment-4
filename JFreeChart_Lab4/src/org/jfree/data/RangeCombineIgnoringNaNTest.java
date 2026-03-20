package org.jfree.data;

import static org.junit.Assert.*;
import org.junit.Test;

public class RangeCombineIgnoringNaNTest {

    /**
     * range1 null, range2 normal
     */
    @Test
    public void testRange1Null() {
        Range r2 = new Range(2.0, 5.0);

        Range result = Range.combineIgnoringNaN(null, r2);

        assertEquals(r2, result);
    }

    /**
     * range1 null, range2 NaN range → return null
     */
    @Test
    public void testRange1NullRange2NaN() {
        Range r2 = new Range(Double.NaN, Double.NaN);

        Range result = Range.combineIgnoringNaN(null, r2);

        assertNull(result);
    }

    /**
     * range2 null, range1 normal
     */
    @Test
    public void testRange2Null() {
        Range r1 = new Range(1.0, 4.0);

        Range result = Range.combineIgnoringNaN(r1, null);

        assertEquals(r1, result);
    }

    /**
     * range2 null, range1 NaN range
     */
    @Test
    public void testRange2NullRange1NaN() {
        Range r1 = new Range(Double.NaN, Double.NaN);

        Range result = Range.combineIgnoringNaN(r1, null);

        assertNull(result);
    }

    /**
     * both ranges valid → combine normally
     */
    @Test
    public void testCombineNormalRanges() {
        Range r1 = new Range(1.0, 5.0);
        Range r2 = new Range(3.0, 8.0);

        Range result = Range.combineIgnoringNaN(r1, r2);

        assertEquals(1.0, result.getLowerBound(), 0.0000001);
        assertEquals(8.0, result.getUpperBound(), 0.0000001);
    }

    /**
     * both ranges NaN → result should be null
     */
    @Test
    public void testBothRangesNaN() {
        Range r1 = new Range(Double.NaN, Double.NaN);
        Range r2 = new Range(Double.NaN, Double.NaN);

        Range result = Range.combineIgnoringNaN(r1, r2);

        assertNull(result);
    }
}
