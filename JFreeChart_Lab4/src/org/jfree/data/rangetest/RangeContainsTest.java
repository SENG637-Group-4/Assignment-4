package org.jfree.data.rangetest;

import static org.junit.Assert.*;
import org.jfree.data.Range;
import org.junit.*;

public class RangeContainsTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    // Test Case 1
    // Range(5, 95) with input 54: NOM (nominal value within range)
    // Expected: true
    @Test
    public void containsNominalValueWithinRange() {
        Range range = new Range(5, 95);
        assertTrue("Range(5, 95) should contain nominal value 54",
                range.contains(54));
    }

    // Test Case 2
    // Range(5, 95) with input 5: LB (value on the lower boundary)
    // Expected: true
    @Test
    public void containsValueAtLowerBoundary() {
        Range range = new Range(5, 95);
        assertTrue("Range(5, 95) should contain lower boundary value 5",
                range.contains(5));
    }

    // Test Case 3
    // Range(5, 95) with input 95: UB (value on the upper boundary)
    // Expected: true
    @Test
    public void containsValueAtUpperBoundary() {
        Range range = new Range(5, 95);
        assertTrue("Range(5, 95) should contain upper boundary value 95",
                range.contains(95));
    }

    // Test Case 4
    // Range(5, 95) with input 94: BUB (value just below the upper bound)
    // Expected: true
    @Test
    public void containsValueJustBelowUpperBound() {
        Range range = new Range(5, 95);
        assertTrue("Range(5, 95) should contain value 94 just below upper bound",
                range.contains(94));
    }

    // Test Case 5
    // Range(5, 95) with input 4: BLB (value just below the lower bound)
    // Expected: false
    @Test
    public void doesNotContainValueJustBelowLowerBound() {
        Range range = new Range(5, 95);
        assertFalse("Range(5, 95) should not contain value 4 just below lower bound",
                range.contains(4));
    }

    // Test Case 6
    // Range(5, 95) with input 96: AUB (value just above the upper bound)
    // Expected: false
    @Test
    public void doesNotContainValueJustAboveUpperBound() {
        Range range = new Range(5, 95);
        assertFalse("Range(5, 95) should not contain value 96 just above upper bound",
                range.contains(96));
    }

    // Test Case 7
    // Range(5, 95) with input 6: ALB (value just above the lower boundary)
    // Expected: true
    @Test
    public void containsValueJustAboveLowerBoundary() {
        Range range = new Range(5, 95);
        assertTrue("Range(5, 95) should contain value 6 just above lower boundary",
                range.contains(6));
    }

    // Test Case 8
    // Range(5, 95) with input Double.MAX_VALUE: extreme upper value
    // Expected: false
    @Test
    public void doesNotContainDoubleMaxValue() {
        Range range = new Range(5, 95);
        assertFalse("Range(5, 95) should not contain Double.MAX_VALUE",
                range.contains(Double.MAX_VALUE));
    }

    // Test Case 9
    // Range(5, 95) with input -Double.MAX_VALUE: extreme lower value
    // Expected: false
    @Test
    public void doesNotContainDoubleMinValue() {
        Range range = new Range(5, 95);
        assertFalse("Range(5, 95) should not contain -Double.MAX_VALUE",
                range.contains(-Double.MAX_VALUE));
    }
    
	 // Test Case 10
	 // Range(5, 95) with input 50: internal value exercising final boolean condition
	 // Expected: true
	 @Test
	 public void containsInternalValueUsingFinalBooleanCheck() {
	     // Arrange
	     Range range = new Range(5, 95);
	
	     // Act
	     boolean result = range.contains(50);
	
	     // Assert
	     assertTrue("Range(5, 95) should contain value 50 evaluated by final boolean condition",
	             result);
	 }
	
	 // Test Case 11
	 // Range(5, 95) with input 10: another internal value
	 // Expected: true
	 @Test
	 public void containsAnotherInternalValueUsingFinalCheck() {
	     // Arrange
	     Range range = new Range(5, 95);
	
	     // Act
	     boolean result = range.contains(10);
	
	     // Assert
	     assertTrue("Range(5, 95) should contain value 10 evaluated by final boolean condition",
	             result);
	 }
	
	 // Test Case 12
	 // Range(5, 95) with input 90: internal value near upper bound
	 // Expected: true
	 @Test
	 public void containsValueNearUpperBoundUsingFinalCheck() {
	     // Arrange
	     Range range = new Range(5, 95);
	
	     // Act
	     boolean result = range.contains(90);
	
	     // Assert
	     assertTrue("Range(5, 95) should contain value 90 evaluated by final boolean condition",
	             result);
	 }
	 
	// Test Case 13
	// Range(5, 95) with input 5.0: value exactly equal to lower bound
	// Forces (value >= this.lower) to evaluate as true at the boundary in the final return
	// Expected: true
	@Test
	public void containsValueExactlyAtLowerBoundFinalReturn() {
	    // Arrange
	    Range range = new Range(5, 95);

	    // Act
	    boolean result = range.contains(5.0);

	    // Assert
	    assertTrue(
	        "Range(5, 95) should return true for 5.0 — exercises value >= lower branch in final return",
	        result);
	}

	// Test Case 14
	// Range(5, 95) with input 95.0: value exactly equal to upper bound
	// Forces (value <= this.upper) to evaluate as true at the boundary in the final return
	// Expected: true
	@Test
	public void containsValueExactlyAtUpperBoundFinalReturn() {
	    // Arrange
	    Range range = new Range(5, 95);

	    // Act
	    boolean result = range.contains(95.0);

	    // Assert
	    assertTrue(
	        "Range(5, 95) should return true for 95.0 — exercises value <= upper branch in final return",
	        result);
	}
	
	// Test Case 10
    // Range(-10, 10) with input -10: Tests lower boundary with negative range
    // Targets mutant 19 on line 159 (condition mutations)
    // Expected: true
    @Test
    public void containsNegativeLowerBoundary() {
        Range range = new Range(-10, 10);
        assertTrue("Range(-10, 10) should contain lower boundary -10",
                range.contains(-10));
    }

    // Test Case 11
    // Range(-10, 10) with input -10.0000001: Just below negative lower boundary
    // Targets mutant 19 on line 159 and mutant 7 on line 160
    // Expected: false
    @Test
    public void doesNotContainValueJustBelowNegativeLowerBound() {
        Range range = new Range(-10, 10);
        assertFalse("Range(-10, 10) should not contain -10.0000001",
                range.contains(-10.0000001));
    }

    // Test Case 12
    // Range(-10, 10) with input 10.0000001: Just above upper boundary
    // Targets mutant 19 on line 162 and mutant 7 on line 163
    // Expected: false
    @Test
    public void doesNotContainValueJustAboveUpperBoundPrecise() {
        Range range = new Range(-10, 10);
        assertFalse("Range(-10, 10) should not contain 10.0000001",
                range.contains(10.0000001));
    }

    // Test Case 13
    // Range(0, 0) with input 0: Single point range
    // Tests all boundaries collapse to one point
    // Expected: true
    @Test
    public void containsValueInSinglePointRange() {
        Range range = new Range(0, 0);
        assertTrue("Range(0, 0) should contain value 0",
                range.contains(0));
    }

    // Test Case 14
    // Range(0, 0) with input 0.0000001: Just outside single point range
    // Expected: false
    @Test
    public void doesNotContainValueOutsideSinglePointRange() {
        Range range = new Range(0, 0);
        assertFalse("Range(0, 0) should not contain 0.0000001",
                range.contains(0.0000001));
    }

    // Test Case 15
    // Range(0, 0) with input -0.0000001: Just below single point range
    // Expected: false
    @Test
    public void doesNotContainValueBelowSinglePointRange() {
        Range range = new Range(0, 0);
        assertFalse("Range(0, 0) should not contain -0.0000001",
                range.contains(-0.0000001));
    }

    // Test Case 19
    // Range(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY) with input 0
    // Tests infinite range contains zero
    // Expected: true
    @Test
    public void containsZeroInInfiniteRange() {
        Range range = new Range(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        assertTrue("Range(-∞, +∞) should contain 0",
                range.contains(0));
    }

    // Test Case 20
    // Range(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY) with input Double.MAX_VALUE
    // Tests infinite range contains extreme value
    // Expected: true
    @Test
    public void containsMaxValueInInfiniteRange() {
        Range range = new Range(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        assertTrue("Range(-∞, +∞) should contain Double.MAX_VALUE",
                range.contains(Double.MAX_VALUE));
    }

    // Test Case 21
    // Range(Double.NEGATIVE_INFINITY, 0) with input -1
    // Tests semi-infinite range (negative side)
    // Expected: true
    @Test
    public void containsNegativeValueInNegativeInfiniteRange() {
        Range range = new Range(Double.NEGATIVE_INFINITY, 0);
        assertTrue("Range(-∞, 0) should contain -1",
                range.contains(-1));
    }

    // Test Case 22
    // Range(0, Double.POSITIVE_INFINITY) with input 1
    // Tests semi-infinite range (positive side)
    // Expected: true
    @Test
    public void containsPositiveValueInPositiveInfiniteRange() {
        Range range = new Range(0, Double.POSITIVE_INFINITY);
        assertTrue("Range(0, +∞) should contain 1",
                range.contains(1));
    }

    // Test Case 23
    // Range(0, Double.POSITIVE_INFINITY) with input -1
    // Value below semi-infinite range
    // Expected: false
    @Test
    public void doesNotContainNegativeValueInPositiveInfiniteRange() {
        Range range = new Range(0, Double.POSITIVE_INFINITY);
        assertFalse("Range(0, +∞) should not contain -1",
                range.contains(-1));
    }

    // Test Case 24
    // Range(Double.NEGATIVE_INFINITY, 0) with input 1
    // Value above semi-infinite range
    // Expected: false
    @Test
    public void doesNotContainPositiveValueInNegativeInfiniteRange() {
        Range range = new Range(Double.NEGATIVE_INFINITY, 0);
        assertFalse("Range(-∞, 0) should not contain 1",
                range.contains(1));
    }

    // Test Case 25
    // Range(-100, -50) with input -75: Entirely negative range
    // Tests nominal value in negative range
    // Expected: true
    @Test
    public void containsNominalValueInNegativeRange() {
        Range range = new Range(-100, -50);
        assertTrue("Range(-100, -50) should contain -75",
                range.contains(-75));
    }

    // Test Case 26
    // Range(100, 200) with input 150: Entirely positive range
    // Tests nominal value in positive range
    // Expected: true
    @Test
    public void containsNominalValueInPositiveRange() {
        Range range = new Range(100, 200);
        assertTrue("Range(100, 200) should contain 150",
                range.contains(150));
    }

    // Test Case 27
    // Range(-1, 1) with input 0: Zero in range crossing zero
    // Expected: true
    @Test
    public void containsZeroInRangeCrossingZero() {
        Range range = new Range(-1, 1);
        assertTrue("Range(-1, 1) should contain 0",
                range.contains(0));
    }

    @After
    public void tearDown() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }
}