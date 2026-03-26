package org.jfree.data.rangetest;

import static org.junit.Assert.*;

import org.jfree.data.Range;
import org.junit.*;

/**
 * White-box tests for Range.constrain(double value).
 *
 * Source code:
 *   public double constrain(double value) {
 *       double result = value;
 *       if (!contains(value)) {          // Branch A: value outside range?
 *           if (value > this.upper) {    // Branch B: above upper bound?
 *               result = this.upper;
 *           }
 *           else if (value < this.lower) { // Branch C: below lower bound?
 *               result = this.lower;
 *           }
 *           // Branch D (implicit): NaN — neither B nor C is true; result stays NaN
 *       }
 *       return result;
 *   }
 *
 * Dependency — Range.contains(double value):
 *   if (value < this.lower) return false;
 *   if (value > this.upper) return false;
 *   return (value >= this.lower && value <= this.upper);
 *
 * Branch coverage map (all 6 branch outcomes are exercised):
 *   A=false (value inside range)           → tests 1, 2, 3
 *   A=true + B=true  (value > upper)       → tests 4, 5
 *   A=true + B=false + C=true (< lower)    → tests 6, 7
 *   A=true + B=false + C=false (NaN)       → test 8
 *
 * Spec: "Returns the value within the range that is closest to the specified value."
 */
public class RangeConstrainTest {

    private Range range; // base: Range(-5.0, 5.0)

    @BeforeClass public static void setUpBeforeClass() throws Exception {}

    @Before
    public void setUp() {
        range = new Range(-5.0, 5.0);
    }

    @After       public void tearDown()           throws Exception {}
    @AfterClass  public static void tearDownAfterClass() throws Exception {}

    // Branch A=false: value inside range → return value unchanged
    // EC NOM: central value (zero)
    @Test
    public void constrainReturnsValueWhenInsideRange() {
        assertEquals("Value 0.0 is inside Range(-5,5) — constrain should return 0.0",
                0.0, range.constrain(0.0), 1e-9);
    }

    // Branch A=false: value inside range → return value unchanged
    // BVA: value exactly at lower bound (inclusive boundary)
    @Test
    public void constrainReturnsLowerBoundWhenValueEqualsLower() {
        assertEquals("Value -5.0 equals lower bound — constrain should return -5.0",
                -5.0, range.constrain(-5.0), 1e-9);
    }

    // Branch A=false: value inside range → return value unchanged
    // BVA: value exactly at upper bound (inclusive boundary)
    @Test
    public void constrainReturnsUpperBoundWhenValueEqualsUpper() {
        assertEquals("Value 5.0 equals upper bound — constrain should return 5.0",
                5.0, range.constrain(5.0), 1e-9);
    }

    // Branch A=true, Branch B=true: value just above upper → clamp to upper
    // BVA: value = upper + epsilon
    @Test
    public void constrainClampsToUpperWhenValueJustAboveUpper() {
        assertEquals("Value 5.0001 is just above upper 5.0 — constrain should clamp to 5.0",
                5.0, range.constrain(5.0001), 1e-9);
    }

    // Branch A=true, Branch B=true: value well above upper → clamp to upper
    // EC: large positive value outside range
    @Test
    public void constrainClampsToUpperWhenValueWellAboveUpper() {
        assertEquals("Value 100.0 >> upper 5.0 — constrain should clamp to 5.0",
                5.0, range.constrain(100.0), 1e-9);
    }

    // Branch A=true, B=false, Branch C=true: value just below lower → clamp to lower
    // BVA: value = lower - epsilon
    @Test
    public void constrainClampsToLowerWhenValueJustBelowLower() {
        assertEquals("Value -5.0001 is just below lower -5.0 — constrain should clamp to -5.0",
                -5.0, range.constrain(-5.0001), 1e-9);
    }

    // Branch A=true, B=false, Branch C=true: value well below lower → clamp to lower
    // EC: large negative value outside range
    @Test
    public void constrainClampsToLowerWhenValueWellBelowLower() {
        assertEquals("Value -100.0 << lower -5.0 — constrain should clamp to -5.0",
                -5.0, range.constrain(-100.0), 1e-9);
    }

    // Branch A=true, B=false, C=false: NaN input
    // NaN comparisons are always false (IEEE 754), so neither branch B nor C fires.
    // result was assigned NaN at entry; it stays NaN and is returned.
    @Test
    public void constrainWithNaNReturnsNaN() {
        double result = range.constrain(Double.NaN);
        assertTrue("constrain(NaN): NaN comparisons are always false, so result stays NaN",
                Double.isNaN(result));
    }
    
    @Test
    public void constrainValueJustAboveLowerBound_MustReturnItself() {
        // value = -4.9999 is strictly inside (-5, 5); must return -4.9999 unchanged.
        // "not equal to less than" mutant would misroute this into the if-block.
        assertEquals("Value just above lower bound must be returned unchanged",
                -4.9999, range.constrain(-4.9999), 1e-9);
    }

    @Test
    public void constrainValueJustBelowUpperBound_MustReturnItself() {
        // value = 4.9999 is strictly inside (-5, 5); must return 4.9999 unchanged.
        // "not equal to greater than" mutant would misroute this into the if-block.
        assertEquals("Value just below upper bound must be returned unchanged",
                4.9999, range.constrain(4.9999), 1e-9);
    }

    @Test
    public void constrainWithDegenerateRange_ValueAbovePoint_ClampsToPoint() {
        // Range [3, 3]: only value 3.0 is "inside". Value 3.0001 is above.
        // Line 212: (3.0001 > 3.0) → true → result = 3.0.
        // "Less or equal to less than" mutant: (3.0001 >= 3.0) → also true → same result.
        // "changed conditional boundary" → (3.0001 >= 3.0) → same.
        // Use value = 3.0 + epsilon where epsilon is so small that boundary matters:
        Range point = new Range(3.0, 3.0);
        assertEquals("Value above degenerate range must clamp to 3.0",
                3.0, point.constrain(3.5), 1e-9);
    }

    @Test
    public void constrainWithDegenerateRange_ValueBelowPoint_ClampsToPoint() {
        Range point = new Range(3.0, 3.0);
        assertEquals("Value below degenerate range must clamp to 3.0",
                3.0, point.constrain(2.5), 1e-9);
    }

    @Test
    public void constrainWithDegenerateRange_ValueAtPoint_ReturnsPoint() {
        Range point = new Range(3.0, 3.0);
        assertEquals("Value exactly at degenerate range point must return that point",
                3.0, point.constrain(3.0), 1e-9);
    }

    @Test
    public void constrainUpperNotCorruptedAfterClamping_PostIncrementMutant() {
        // First call: value=10 > upper=5 → result = upper = 5.
        // If a++ mutant: upper becomes 6 after first call.
        // Second call: value=10 > upper=6 → result = 6 (wrong!).
        assertEquals("First clamp to upper must return 5.0", 5.0, range.constrain(10.0), 1e-9);
        assertEquals("Second clamp to upper must still return 5.0 (field not corrupted)",
                5.0, range.constrain(10.0), 1e-9);
    }

    @Test
    public void constrainUpperNotCorruptedAfterClamping_PostDecrementMutant() {
        // If a-- mutant: upper becomes 4 after first call.
        // Second call: value=10 > upper=4 → result = 4 (wrong!).
        assertEquals("First clamp to upper must return 5.0", 5.0, range.constrain(10.0), 1e-9);
        assertEquals("Second clamp to upper must still return 5.0 (field not decremented)",
                5.0, range.constrain(10.0), 1e-9);
        // Also verify that a value that WAS inside the range is still inside
        assertEquals("After clamping, interior value must still be returned unchanged",
                3.0, range.constrain(3.0), 1e-9);
    }

    @Test
    public void constrainLowerNotCorruptedAfterClamping_PostIncrementMutant() {
        // If a++ mutant on lower: lower becomes -4 after first call.
        // Second call: value=-10 enters outer-if; (-10 > upper=5)? No.
        // (-10 < lower=-4)? Yes → result = -4 (wrong, should be -5).
        assertEquals("First clamp to lower must return -5.0", -5.0, range.constrain(-10.0), 1e-9);
        assertEquals("Second clamp to lower must still return -5.0 (field not corrupted)",
                -5.0, range.constrain(-10.0), 1e-9);
    }

    @Test
    public void constrainLowerNotCorruptedAfterClamping_PostDecrementMutant() {
        // If a-- mutant on lower: lower becomes -6 after first call.
        // Second call: value=-5.5 → contains(-5.5)? (-5.5 < lower=-6)? No. (-5.5 > 5)? No.
        // So contains = true (wrong because lower shifted). result = -5.5 (wrong).
        assertEquals("First clamp to lower must return -5.0", -5.0, range.constrain(-10.0), 1e-9);
        assertEquals("Second clamp to lower must still return -5.0 (field not decremented)",
                -5.0, range.constrain(-10.0), 1e-9);
        // Verify interior value still works after clamping
        assertEquals("After clamping, interior value must still be returned unchanged",
                -3.0, range.constrain(-3.0), 1e-9);
    }

    @Test
    public void constrainWithIntegerRange_ValueOneAboveUpper_ClampsExactly() {
        Range r = new Range(0.0, 10.0);
        // value=11: exactly 1 above upper=10.
        // a++ mutant on upper: result = 10 (original), upper becomes 11.
        // Next call with value=10.5: (10.5 > 11)? NO → then (10.5 < 0)? No → result=10.5 (WRONG).
        assertEquals(10.0, r.constrain(11.0), 1e-9);
        assertEquals("Upper field must not have been incremented",
                10.0, r.constrain(10.5), 1e-9);
    }

    @Test
    public void constrainWithIntegerRange_ValueOneBelowLower_ClampsExactly() {
        Range r = new Range(0.0, 10.0);
        // value=-1: exactly 1 below lower=0.
        assertEquals(0.0, r.constrain(-1.0), 1e-9);
        assertEquals("Lower field must not have been incremented",
                0.0, r.constrain(-0.5), 1e-9);
    }

    @Test
    public void constrainMidpointAndOutsideValue_BothCorrect() {
        // Inside: midpoint returns itself
        assertEquals(0.0, range.constrain(0.0), 1e-9);
        // Outside above: clamps to upper
        assertEquals(5.0, range.constrain(7.0), 1e-9);
        // Outside below: clamps to lower
        assertEquals(-5.0, range.constrain(-7.0), 1e-9);
    }

    @Test
    public void constrainPositiveRange_InsideAndOutside() {
        Range pos = new Range(2.0, 8.0);
        // Inside
        assertEquals(5.0, pos.constrain(5.0), 1e-9);
        // Above upper
        assertEquals(8.0, pos.constrain(10.0), 1e-9);
        // Below lower
        assertEquals(2.0, pos.constrain(0.0), 1e-9);
    }

    @Test
    public void constrainRemoveContainsMutant_ExteriorValueMustClamp() {
        // If contains() call is removed and replaced with false (always-false),
        // the outer if is never entered → exterior values not clamped → WRONG.
        assertEquals("Above upper must clamp even if contains() removed",
                5.0, range.constrain(6.0), 1e-9);
        assertEquals("Below lower must clamp even if contains() removed",
                -5.0, range.constrain(-6.0), 1e-9);
        // This pair combined with an interior test kills the "always false" variant.
        assertEquals("Interior must return itself",
                1.0, range.constrain(1.0), 1e-9);
    }
}
