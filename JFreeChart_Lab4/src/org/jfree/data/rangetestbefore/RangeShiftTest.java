package org.jfree.data.rangetestbefore;

import static org.junit.Assert.*;
import org.jfree.data.Range;
import org.junit.*;

public class RangeShiftTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    // Test Case 1
    // null base range: should throw InvalidParameterException
    // Expected: InvalidParameterException
    @Test(expected = IllegalArgumentException.class)
    public void shiftWithNullBaseThrowsException() {
        Range base = null;
        Range.shift(base, 5.0, true);
    }

    // Test Case 2
    // Range(-5,5) with delta=0, allowZeroCrossing=true: range unchanged
    // Expected: Range(-5,5)
    @Test
    public void shiftWithZeroDeltaReturnsUnchangedRange() {
        Range base = new Range(-5, 5);
        Range result = Range.shift(base, 0, true);
        assertEquals("Shifted range lower bound should be -5", 
                -5, result.getLowerBound(), .000000001d);
        assertEquals("Shifted range upper bound should be 5", 
                5, result.getUpperBound(), .000000001d);
    }

    // Test Case 3
    // Range(-5,5) with delta=4.9, allowZeroCrossing=false: lower just below 0 (BLB)
    // Expected: Range(-0.1,9.9)
    @Test
    public void shiftWithLowerBoundJustBelowZero() {
        Range base = new Range(-5, 5);
        Range result = Range.shift(base, 4.9, false);
        assertEquals("Shifted range lower bound should be -0.1", 
                -0.1, result.getLowerBound(), .000000001d);
        assertEquals("Shifted range upper bound should be 9.9", 
                9.9, result.getUpperBound(), .000000001d);
    }

    // Test Case 4
    // Range(-5,5) with delta=5, allowZeroCrossing=false: lower at 0 (LB boundary)
    // Expected: Range(0,10)
    @Test
    public void shiftWithLowerBoundAtZero() {
        Range base = new Range(-5, 5);
        Range result = Range.shift(base, 5, false);
        assertEquals("Shifted range lower bound should be 0", 
                0, result.getLowerBound(), .000000001d);
        assertEquals("Shifted range upper bound should be 10", 
                10, result.getUpperBound(), .000000001d);
    }

    // Test Case 5
    // Range(-5,5) with delta=5.1, allowZeroCrossing=false: lower crosses zero (ALB)
    // Expected: Range(0,10.1)
    @Test
    public void shiftWithLowerBoundCrossingZero() {
        Range base = new Range(-5, 5);
        Range result = Range.shift(base, 5.1, false);
        assertEquals("Shifted range lower bound should be 0 (clamped)", 
                0, result.getLowerBound(), .000000001d);
        assertEquals("Shifted range upper bound should be 10.1", 
                10.1, result.getUpperBound(), .000000001d);
    }

    // Test Case 6
    // Range(-6,-2) with delta=1.9, allowZeroCrossing=false: upper just below 0 (BUB)
    // Expected: Range(-4.1,-0.1)
    @Test
    public void shiftWithUpperBoundJustBelowZero() {
        Range base = new Range(-6, -2);
        Range result = Range.shift(base, 1.9, false);
        assertEquals("Shifted range lower bound should be -4.1", 
                -4.1, result.getLowerBound(), .000000001d);
        assertEquals("Shifted range upper bound should be -0.1", 
                -0.1, result.getUpperBound(), .000000001d);
    }

    // Test Case 7
    // Range(-6,-2) with delta=2, allowZeroCrossing=false: upper at 0 (UB boundary)
    // Expected: Range(-4,0)
    @Test
    public void shiftWithUpperBoundAtZero() {
        Range base = new Range(-6, -2);
        Range result = Range.shift(base, 2, false);
        assertEquals("Shifted range lower bound should be -4", 
                -4, result.getLowerBound(), .000000001d);
        assertEquals("Shifted range upper bound should be 0", 
                0, result.getUpperBound(), .000000001d);
    }

    // Test Case 8
    // Range(-6,-2) with delta=2.1, allowZeroCrossing=false: upper crosses zero (AUB)
    // Expected: Range(-3.9,0)
    @Test
    public void shiftWithUpperBoundCrossingZero() {
        Range base = new Range(-6, -2);
        Range result = Range.shift(base, 2.1, false);
        assertEquals("Shifted range lower bound should be -3.9", 
                -3.9, result.getLowerBound(), .000000001d);
        assertEquals("Shifted range upper bound should be 0 (clamped)", 
                0, result.getUpperBound(), .000000001d);
    }

    // Test Case 9
    // Range(-5,5) with delta=2, allowZeroCrossing=true: NOM
    // Expected: Range(-3,7)
    @Test
    public void shiftWithZeroCrossingAllowed() {
        Range base = new Range(-5, 5);
        Range result = Range.shift(base, 2, true);
        assertEquals("Shifted range lower bound should be -3", 
                -3, result.getLowerBound(), .000000001d);
        assertEquals("Shifted range upper bound should be 7", 
                7, result.getUpperBound(), .000000001d);
    }

    // Test Case 10
    // Range(-6,4) with delta=-2, allowZeroCrossing=true: negative delta NOM
    // Expected: Range(-8,2)
    @Test
    public void shiftWithNegativeDelta() {
        Range base = new Range(-6, 4);
        Range result = Range.shift(base, -2, true);
        assertEquals("Shifted range lower bound should be -8", 
                -8, result.getLowerBound(), .000000001d);
        assertEquals("Shifted range upper bound should be 2", 
                2, result.getUpperBound(), .000000001d);
    }

    // Test Case 11
    // Range(2,6) with delta=-5, allowZeroCrossing=true: larger negative delta NOM
    // Expected: Range(-3,1)
    @Test
    public void shiftPositiveRangeWithLargeNegativeDelta() {
        Range base = new Range(2, 6);
        Range result = Range.shift(base, -5, true);
        assertEquals("Shifted range lower bound should be -3", 
                -3, result.getLowerBound(), .000000001d);
        assertEquals("Shifted range upper bound should be 1", 
                1, result.getUpperBound(), .000000001d);
    }

    @After
    public void tearDown() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }
}