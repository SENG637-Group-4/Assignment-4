package org.jfree.data;

import static org.junit.Assert.*;
import org.junit.*;

/**
 * White-box tests for Range.combine(Range range1, Range range2).
 *
 * Source code:
 *   public static Range combine(Range range1, Range range2) {
 *       if (range1 == null) {          // Branch A: range1 null?
 *           return range2;
 *       }
 *       if (range2 == null) {          // Branch B: range2 null?
 *           return range1;
 *       }
 *       double l = Math.min(range1.getLowerBound(), range2.getLowerBound());
 *       double u = Math.max(range1.getUpperBound(), range2.getUpperBound());
 *       return new Range(l, u);        // Branch C: both non-null → new combined range
 *   }
 *
 * Branch coverage (all 4 branch outcomes covered):
 *   Branch A=true  (range1 null)           → tests 1, 2
 *   Branch A=false, B=true  (range2 null)  → test 3
 *   Branch A=false, B=false (both non-null)→ tests 4, 5, 6, 7
 *
 * Spec: "Creates a new range by combining two existing ranges.
 *        Either range can be null, in which case the other range is returned.
 *        If both ranges are null the return value is null."
 */
public class RangeCombineTest {

    @BeforeClass public static void setUpBeforeClass() throws Exception {}
    @Before      public void setUp()              throws Exception {}
    @After       public void tearDown()           throws Exception {}
    @AfterClass  public static void tearDownAfterClass() throws Exception {}

    // Branch A=true: range1 == null, range2 non-null → return range2
    @Test
    public void combineWithNullRange1ReturnsRange2() {
        Range r2 = new Range(1.0, 5.0);
        assertEquals("combine(null, r2) should return r2", r2, Range.combine(null, r2));
    }

    // Branch A=true: range1 == null, range2 also null → return null (range2)
    @Test
    public void combineWithBothNullReturnsNull() {
        assertNull("combine(null, null) should return null",
                Range.combine(null, null));
    }

    // Branch A=false, Branch B=true: range1 non-null, range2 null → return range1
    @Test
    public void combineWithNullRange2ReturnsRange1() {
        Range r1 = new Range(1.0, 5.0);
        assertEquals("combine(r1, null) should return r1", r1, Range.combine(r1, null));
    }

    // Branch A=false, B=false → compute combined range
    // EC: non-overlapping ranges (gap between them)
    @Test
    public void combineTwoNonOverlappingRanges() {
        Range result = Range.combine(new Range(1.0, 3.0), new Range(5.0, 7.0));
        assertNotNull("Combined range of non-overlapping ranges should not be null", result);
        assertEquals("Combined lower should be min(1.0, 5.0) = 1.0",
                1.0, result.getLowerBound(), 1e-9);
        assertEquals("Combined upper should be max(3.0, 7.0) = 7.0",
                7.0, result.getUpperBound(), 1e-9);
    }

    // Branch A=false, B=false → compute combined range
    // EC: overlapping ranges
    @Test
    public void combineTwoOverlappingRanges() {
        Range result = Range.combine(new Range(1.0, 5.0), new Range(3.0, 8.0));
        assertNotNull("Combined range of overlapping ranges should not be null", result);
        assertEquals("Combined lower should be min(1.0, 3.0) = 1.0",
                1.0, result.getLowerBound(), 1e-9);
        assertEquals("Combined upper should be max(5.0, 8.0) = 8.0",
                8.0, result.getUpperBound(), 1e-9);
    }

    // Branch A=false, B=false → compute combined range
    // EC: one range fully contains the other
    @Test
    public void combineWhenOneRangeContainsTheOther() {
        Range result = Range.combine(new Range(-10.0, 10.0), new Range(-3.0, 3.0));
        assertNotNull("Combined range should not be null", result);
        assertEquals("Combined lower should be min(-10, -3) = -10.0",
                -10.0, result.getLowerBound(), 1e-9);
        assertEquals("Combined upper should be max(10, 3) = 10.0",
                10.0, result.getUpperBound(), 1e-9);
    }

    // Branch A=false, B=false → compute combined range
    // BVA: adjacent ranges (touching at one point)
    @Test
    public void combineAdjacentRanges() {
        Range result = Range.combine(new Range(1.0, 3.0), new Range(3.0, 5.0));
        assertNotNull("Combined adjacent ranges should not be null", result);
        assertEquals("Combined lower should be 1.0", 1.0, result.getLowerBound(), 1e-9);
        assertEquals("Combined upper should be 5.0", 5.0, result.getUpperBound(), 1e-9);
    }
}
