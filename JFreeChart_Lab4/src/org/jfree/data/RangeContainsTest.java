package org.jfree.data;

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
	
	// Test Case 15
	// Range(5, 95) with input Double.NaN: NaN fails all comparisons
	// Forces (value >= this.lower) to evaluate FALSE in the final return statement
	// Expected: false (covers FALSE branch of >= in final return)
	@Test
	public void doesNotContainNaNValue() {
	    Range range = new Range(5, 95);
	    assertFalse("Range(5, 95) should not contain Double.NaN",
	            range.contains(Double.NaN));
	}

	// Test Case 16
	// Range(Double.NaN, Double.NaN) with input 50: NaN bounds fail all comparisons
	// Forces (value <= this.upper) to evaluate FALSE in the final return statement
	// Expected: false (covers FALSE branch of <= in final return)
	@Test
	public void doesNotContainValueInNaNBoundedRange() {
	    Range range = new Range(Double.NaN, Double.NaN);
	    assertFalse("Range(NaN, NaN) should not contain value 50",
	            range.contains(50));
	}
	
	// Test Case 15
	// Range(5, 95) with input Double.NaN: NaN fails >= comparison in final return
	// Forces (value >= this.lower) to evaluate FALSE, short-circuits && 
	// Covers FALSE branch of (value >= this.lower) in final return
	// Expected: false
	@Test
	public void doesNotContainNaNAsValue() {
	    Range range = new Range(5, 95);
	    assertFalse("Range(5, 95) should not contain Double.NaN",
	            range.contains(Double.NaN));
	}

	// Test Case 16
	// Range(5, Double.NaN) with input 50: NaN upper bound fails <= comparison in final return
	// value=50 passes both guards (50 < 5 = false, 50 > NaN = false)
	// then in final return: (50 >= 5) = TRUE, (50 <= NaN) = FALSE
	// Covers FALSE branch of (value <= this.upper) in final return
	// Expected: false
	@Test
	public void doesNotContainValueWhenUpperBoundIsNaN() {
	    Range range = new Range(5, Double.NaN);
	    assertFalse("Range(5, NaN) should not contain value 50",
	            range.contains(50));
	}

    @After
    public void tearDown() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }
}