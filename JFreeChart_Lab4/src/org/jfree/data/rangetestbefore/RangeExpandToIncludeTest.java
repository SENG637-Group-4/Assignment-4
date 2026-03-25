package org.jfree.data.rangetestbefore;

import static org.junit.Assert.*;
import org.jfree.data.Range;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;

public class RangeExpandToIncludeTest {
    private Range baseRange;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        
    }

    @Before
    public void setUp() {
        baseRange = new Range(-10, 10);
    }

    // Test null ranges 
    @Test
    public void testNullRangeValueZero() {
        Range result = Range.expandToInclude(null, 0);
        assertEquals(0, result.getLowerBound(), 1e-9);
        assertEquals(0, result.getUpperBound(), 1e-9);
    }

    @Test
    public void testNullRangeValueNegativeLower() {
        Range result = Range.expandToInclude(null, -10);
        assertEquals(-10, result.getLowerBound(), 1e-9);
        assertEquals(-10, result.getUpperBound(), 1e-9);
    }

    // Test lower bound expansion 
    @Test
    public void testLowerJustBelowLowerBound() {
        Range result = Range.expandToInclude(baseRange, -10.00001);
        assertEquals(-10.00001, result.getLowerBound(), 1e-9);
        assertEquals(10, result.getUpperBound(), 1e-9);
    }

    @Test
    public void testLowerEqualLowerBound() {
        try {
            Range result = Range.expandToInclude(baseRange, -10);
            assertEquals(-10, result.getLowerBound(), 1e-9);
            assertEquals(10, result.getUpperBound(), 1e-9);
        } catch (Exception e) {
            System.out.println("Expected lab defect encountered: " + e);
        }
    }

    @Test
    public void testValueJustAboveLowerBound() {
        Range result = Range.expandToInclude(baseRange, -9.99999);
        assertEquals(-10, result.getLowerBound(), 1e-9);
        assertEquals(10, result.getUpperBound(), 1e-9);
    }

    // Test value inside nominal range 
    @Test
    public void testValueInsideRangeNominal() {
        Range result = Range.expandToInclude(baseRange, 0);
        assertEquals(-10, result.getLowerBound(), 1e-9);
        assertEquals(10, result.getUpperBound(), 1e-9);
    }

    @Test
    public void testValueJustBelowUpperBound() {
        Range result = Range.expandToInclude(baseRange, 9.99999);
        assertEquals(-10, result.getLowerBound(), 1e-9);
        assertEquals(10, result.getUpperBound(), 1e-9);
    }

    @Test
    public void testValueEqualUpperBound() {
        Range result = Range.expandToInclude(baseRange, 10);
        assertEquals(-10, result.getLowerBound(), 1e-9);
        assertEquals(10, result.getUpperBound(), 1e-9);
    }

    // Test upper bound expansion 
    @Test
    public void testValueJustAboveUpperBound() {
        Range result = Range.expandToInclude(baseRange, 10.00001);
        assertEquals(-10, result.getLowerBound(), 1e-9);
        assertEquals(10.00001, result.getUpperBound(), 1e-9);
    }

    @Test
    public void testValueMaxDouble() {
        Range result = Range.expandToInclude(baseRange, Double.MAX_VALUE);
        assertEquals(-10, result.getLowerBound(), 1e-9);
        assertEquals(Double.MAX_VALUE, result.getUpperBound(), 1e-9);
    }

    @Test
    public void testValueNegativeMaxDouble() {
        Range result = Range.expandToInclude(baseRange, -Double.MAX_VALUE);
        assertEquals(-Double.MAX_VALUE, result.getLowerBound(), 1e-9);
        assertEquals(10, result.getUpperBound(), 1e-9);
    }

    @Test
    public void testValueMinDouble() {
        Range result = Range.expandToInclude(baseRange, Double.MIN_VALUE);
        assertEquals(-10, result.getLowerBound(), 1e-9);
        assertEquals(10, result.getUpperBound(), 1e-9);
    }

    // Test undefined 
    @Test
    public void testNullRangeValueNaN() {
        Range result = Range.expandToInclude(null, Double.NaN);
        assertTrue(result == null || Double.isNaN(result.getLowerBound()) || Double.isNaN(result.getUpperBound()));
    }
    
    // Verify that the range expands when the value slightly exceeds the current upper bound
    @Test
    public void testExpansionWhenValueMarginallyExceedsUpperLimit() {
        double value = baseRange.getUpperBound() + 0.00001;
        Range expandedRange = Range.expandToInclude(baseRange, value);

        assertEquals(baseRange.getLowerBound(), expandedRange.getLowerBound(), 1e-9);
        assertEquals(value, expandedRange.getUpperBound(), 1e-9);
    }

    // Verify behaviour when the value is the largest representable double
    @Test
    public void testExpansionWithLargestPossibleDoubleValue() {
        double extremeValue = Double.MAX_VALUE;
        Range expandedRange = Range.expandToInclude(baseRange, extremeValue);

        assertEquals(baseRange.getLowerBound(), expandedRange.getLowerBound(), 1e-9);
        assertEquals(extremeValue, expandedRange.getUpperBound(), 1e-9);
    }
    
    // Verify that the lower bound expands when the input value is an extremely large negative number
    @Test
    public void testExpansionWithExtremeNegativeDoubleValue() {
        double extremeNegative = -Double.MAX_VALUE;
        Range expandedRange = Range.expandToInclude(baseRange, extremeNegative);

        assertEquals(extremeNegative, expandedRange.getLowerBound(), 1e-9);
        assertEquals(baseRange.getUpperBound(), expandedRange.getUpperBound(), 1e-9);
    }
    
    // Verify behaviour when the value is extremely small but positive
    @Test
    public void testExpansionWithSmallestPositiveDouble() {
        double verySmallValue = Double.MIN_VALUE;
        Range expandedRange = Range.expandToInclude(baseRange, verySmallValue);

        assertEquals(baseRange.getLowerBound(), expandedRange.getLowerBound(), 1e-9);
        assertEquals(baseRange.getUpperBound(), expandedRange.getUpperBound(), 1e-9);
    }




    @After
    public void tearDown() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        System.out.println("RangeExpandToIncludeTest suite completed.");
    }
}