package org.jfree.data.rangetest;

import static org.junit.Assert.*;

import org.jfree.data.Range;
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
    
    @Test
    public void testRangeWithNaNLowerBound() {
        Range r1 = new Range(Double.NaN, 5.0);
        Range r2 = new Range(1.0, 4.0);

        Range result = Range.combineIgnoringNaN(r1, r2);

        assertEquals(1.0, result.getLowerBound(), 0.0000001);
        assertEquals(5.0, result.getUpperBound(), 0.0000001);
    }
    
    @Test
    public void testRangeWithNaNUpperBound() {
        Range r1 = new Range(1.0, Double.NaN);
        Range r2 = new Range(2.0, 6.0);

        Range result = Range.combineIgnoringNaN(r1, r2);

        assertEquals(1.0, result.getLowerBound(), 0.0000001);
        assertEquals(6.0, result.getUpperBound(), 0.0000001);
    }
    
    @Test
    public void testOneBoundNaNResultNotNull() {
        Range r1 = new Range(Double.NaN, 5.0);
        Range r2 = new Range(Double.NaN, 10.0);

        Range result = Range.combineIgnoringNaN(r1, r2);

        assertNotNull(result);
        assertTrue(Double.isNaN(result.getLowerBound()));
        assertEquals(10.0, result.getUpperBound(), 0.0000001);
    }
    
    @Test
    public void testEqualLowerBounds() {
        Range r1 = new Range(2.0, 5.0);
        Range r2 = new Range(2.0, 8.0);

        Range result = Range.combineIgnoringNaN(r1, r2);

        assertEquals(2.0, result.getLowerBound(), 0.0000001);
        assertEquals(8.0, result.getUpperBound(), 0.0000001);
    }
    
    @Test
    public void testEqualUpperBounds() {
        Range r1 = new Range(1.0, 6.0);
        Range r2 = new Range(3.0, 6.0);

        Range result = Range.combineIgnoringNaN(r1, r2);

        assertEquals(1.0, result.getLowerBound(), 0.0000001);
        assertEquals(6.0, result.getUpperBound(), 0.0000001);
    }
    
    @Test
    public void testVeryCloseValues() {
        Range r1 = new Range(1.0000001, 5.0000001);
        Range r2 = new Range(1.0000002, 5.0000002);

        Range result = Range.combineIgnoringNaN(r1, r2);

        assertEquals(1.0000001, result.getLowerBound(), 0.0000001);
        assertEquals(5.0000002, result.getUpperBound(), 0.0000001);
    }
    
    @Test
    public void testNegativeRanges() {
        Range r1 = new Range(-10.0, -2.0);
        Range r2 = new Range(-5.0, -1.0);

        Range result = Range.combineIgnoringNaN(r1, r2);

        assertEquals(-10.0, result.getLowerBound(), 0.0000001);
        assertEquals(-1.0, result.getUpperBound(), 0.0000001);
    }
    
    @Test
    public void testContainedRange() {
        Range r1 = new Range(1.0, 10.0);
        Range r2 = new Range(3.0, 5.0);

        Range result = Range.combineIgnoringNaN(r1, r2);

        assertEquals(1.0, result.getLowerBound(), 0.0000001);
        assertEquals(10.0, result.getUpperBound(), 0.0000001);
    }
}
