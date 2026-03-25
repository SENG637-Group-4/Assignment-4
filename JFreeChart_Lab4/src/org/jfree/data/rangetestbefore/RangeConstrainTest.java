package org.jfree.data.rangetestbefore;

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
}
