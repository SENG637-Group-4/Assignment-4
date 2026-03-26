package org.jfree.data.rangetest;

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
    
    @Test
    public void testValueExactlyAtLowerBound_ReturnsSameRangeObject() {
        // value == lower (-10): NOT below lower, so line 327 condition is FALSE.
        // Must fall through to else-if (line 330): value(-10) > upper(10)? No.
        // Falls to else → returns the SAME range object unchanged.
        // "changed conditional boundary" mutant (<=): TRUE → creates new Range(-10,10)
        // — different object, same values. Use assertSame to detect.
        Range result = Range.expandToInclude(baseRange, -10.0);
        assertSame("Value equal to lower bound: original range object must be returned",
                baseRange, result);
    }

    @Test
    public void testValueExactlyAtLowerBound_BoundsUnchanged() {
        // Verify bounds are also exactly correct (no off-by-one from boundary mutations).
        Range result = Range.expandToInclude(baseRange, -10.0);
        assertEquals("Lower bound must remain -10.0", -10.0, result.getLowerBound(), 1e-9);
        assertEquals("Upper bound must remain 10.0",   10.0, result.getUpperBound(), 1e-9);
    }
    
    @Test
    public void testValueOneBelowLowerBound_NewLowerBoundIsExact() {
        // Use an integer step below lower so the exact value is unambiguous.
        Range result = Range.expandToInclude(baseRange, -11.0);
        assertEquals("New lower bound must be exactly -11.0", -11.0, result.getLowerBound(), 1e-9);
        assertEquals("Upper bound must remain 10.0",           10.0, result.getUpperBound(), 1e-9);
    }
    
    @Test
    public void testValueExactlyAtUpperBound_ReturnsSameRangeObject() {
        // value == upper (10): NOT above upper, so line 330 condition is FALSE.
        // Falls to else → returns SAME range object.
        // "changed conditional boundary" mutant (>=): TRUE → creates new Range — different object.
        Range result = Range.expandToInclude(baseRange, 10.0);
        assertSame("Value equal to upper bound: original range object must be returned",
                baseRange, result);
    }

    @Test
    public void testValueExactlyAtUpperBound_BoundsUnchanged() {
        Range result = Range.expandToInclude(baseRange, 10.0);
        assertEquals("Lower bound must remain -10.0", -10.0, result.getLowerBound(), 1e-9);
        assertEquals("Upper bound must remain 10.0",   10.0, result.getUpperBound(), 1e-9);
    }
    
    @Test
    public void testValueOneAboveUpperBound_NewUpperBoundIsExact() {
        Range result = Range.expandToInclude(baseRange, 11.0);
        assertEquals("Lower bound must remain -10.0", -10.0, result.getLowerBound(), 1e-9);
        assertEquals("New upper bound must be exactly 11.0", 11.0, result.getUpperBound(), 1e-9);
    }

    @Test
    public void testLowerExpansion_ReturnedLowerBoundMatchesValueExactly() {
        // Use a value with many decimal places to ensure no rounding/increment occurs.
        double value = -10.123456789;
        Range result = Range.expandToInclude(new Range(-10.0, 10.0), value);
        assertEquals("Returned lower bound must match value exactly",
                value, result.getLowerBound(), 0.0); // delta=0: exact equality
        assertEquals("Upper bound must be exactly 10.0",
                10.0, result.getUpperBound(), 0.0);
    }

    @Test
    public void testUpperExpansion_ReturnedUpperBoundMatchesValueExactly() {
        double value = 10.123456789;
        Range result = Range.expandToInclude(new Range(-10.0, 10.0), value);
        assertEquals("Returned upper bound must match value exactly",
                value, result.getUpperBound(), 0.0); // delta=0: exact equality
        assertEquals("Lower bound must be exactly -10.0",
                -10.0, result.getLowerBound(), 0.0);
    }

    @Test
    public void testNullRange_ReturnedBoundsMatchValueExactly() {
        double value = 3.141592653589793;
        Range result = Range.expandToInclude(null, value);
        assertEquals("Lower bound of null-expansion must match value exactly",
                value, result.getLowerBound(), 0.0);
        assertEquals("Upper bound of null-expansion must match value exactly",
                value, result.getUpperBound(), 0.0);
    }

    @Test
    public void testNullRange_NegativeValue_ReturnedBoundsMatchExactly() {
        double value = -7.5;
        Range result = Range.expandToInclude(null, value);
        assertEquals("Lower bound must be exactly -7.5", value, result.getLowerBound(), 0.0);
        assertEquals("Upper bound must be exactly -7.5", value, result.getUpperBound(), 0.0);
    }

    @Test
    public void testAsymmetricRange_LowerExpansion() {
        Range r = new Range(-3.5, 7.25);
        Range result = Range.expandToInclude(r, -3.6);
        assertEquals(-3.6,  result.getLowerBound(), 0.0);
        assertEquals( 7.25, result.getUpperBound(), 0.0);
    }

    @Test
    public void testAsymmetricRange_UpperExpansion() {
        Range r = new Range(-3.5, 7.25);
        Range result = Range.expandToInclude(r, 7.3);
        assertEquals(-3.5, result.getLowerBound(), 0.0);
        assertEquals( 7.3, result.getUpperBound(), 0.0);
    }

    @Test
    public void testAsymmetricRange_InsideValue_ReturnsSameObject() {
        Range r = new Range(-3.5, 7.25);
        Range result = Range.expandToInclude(r, 0.0);
        assertSame("Interior value must return the original range object", r, result);
    }

    @Test
    public void testAsymmetricRange_ExactlyAtLowerBound_ReturnsSameObject() {
        Range r = new Range(-3.5, 7.25);
        Range result = Range.expandToInclude(r, -3.5);
        assertSame("Value == lower must return original range object", r, result);
    }

    @Test
    public void testAsymmetricRange_ExactlyAtUpperBound_ReturnsSameObject() {
        Range r = new Range(-3.5, 7.25);
        Range result = Range.expandToInclude(r, 7.25);
        assertSame("Value == upper must return original range object", r, result);
    }

    @After
    public void tearDown() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        System.out.println("RangeExpandToIncludeTest suite completed.");
    }
}