package org.jfree.data.rangetest;

import static org.junit.Assert.*;
import org.jfree.data.Range;
import org.junit.*;

/**
 * Mutation-killing tests for Range.scale(Range base, double factor).
 *
 * Source:
 *   ParamChecks.nullNotPermitted(base, "base");
 *   if (factor < 0) { throw new IllegalArgumentException(...); }
 *   return new Range(base.getLowerBound() * factor, base.getUpperBound() * factor);
 *
 * Key mutants targeted:
 *   M1 (L409): removed call to nullNotPermitted → null base no longer throws
 *   M2 (L410): changed boundary < 0 → <= 0 → factor=0.0 now throws (incorrectly)
 *   M3 (L410): Substituted 0.0 with 1.0 → factor < 1.0 throws (very wrong)
 *   M4 (L413): Replaced multiplication with division → wrong result
 *   M5 (L414): Replaced multiplication with division → wrong upper
 *   M6 (L413): removed call to getUpperBound/getLowerBound
 */
public class RangeScaleTest {

    @BeforeClass public static void setUpBeforeClass() throws Exception {}
    @Before      public void setUp()              throws Exception {}
    @After       public void tearDown()           throws Exception {}
    @AfterClass  public static void tearDownAfterClass() throws Exception {}

 // Kills M1: null base must throw IllegalArgumentException
    @Test(expected = IllegalArgumentException.class)
    public void scaleWithNullBaseThrowsException() {
        Range.scale(null, 2.0);
    }

    // Kills M2: factor=0 must NOT throw (0 >= 0 is valid by spec)
    @Test
    public void scaleWithZeroFactorProducesZeroRange() {
        Range result = Range.scale(new Range(1.0, 5.0), 0.0);
        assertNotNull("scale with factor=0 must not throw and return non-null range", result);
        assertEquals("Lower bound should be 0.0", 0.0, result.getLowerBound(), 1e-9);
        assertEquals("Upper bound should be 0.0", 0.0, result.getUpperBound(), 1e-9);
    }

    // Kills M3: factor=0.5 must not throw (0.5 >= 0, valid)
    @Test
    public void scaleWithFractionalFactorIsValid() {
        Range result = Range.scale(new Range(2.0, 8.0), 0.5);
        assertNotNull("factor 0.5 is valid", result);
        assertEquals("Lower should be 1.0", 1.0, result.getLowerBound(), 1e-9);
        assertEquals("Upper should be 4.0", 4.0, result.getUpperBound(), 1e-9);
    }

    // Kills M4 + M5: multiplication is the correct operation
    // Range(2,6) * 3 = Range(6,18); if division: Range(0.67, 2) — very different
    @Test
    public void scaleMultipliesCorrectly() {
        Range result = Range.scale(new Range(2.0, 6.0), 3.0);
        assertEquals("Lower should be 6.0", 6.0, result.getLowerBound(), 1e-9);
        assertEquals("Upper should be 18.0", 18.0, result.getUpperBound(), 1e-9);
    }

    // Kills M4 + M5: negative lower bound scaled correctly
    @Test
    public void scaleWithNegativeLowerBound() {
        Range result = Range.scale(new Range(-4.0, 2.0), 2.0);
        assertEquals("Lower should be -8.0", -8.0, result.getLowerBound(), 1e-9);
        assertEquals("Upper should be 4.0", 4.0, result.getUpperBound(), 1e-9);
    }

    // Kills M2: negative factor must throw
    @Test(expected = IllegalArgumentException.class)
    public void scaleWithNegativeFactorThrowsException() {
        Range.scale(new Range(1.0, 5.0), -1.0);
    }

    // Boundary: factor = 1.0 should return the same values
    @Test
    public void scaleWithUnitFactorReturnsEquivalentRange() {
        Range base = new Range(-3.0, 7.0);
        Range result = Range.scale(base, 1.0);
        assertEquals("Lower unchanged at factor=1.0", -3.0, result.getLowerBound(), 1e-9);
        assertEquals("Upper unchanged at factor=1.0", 7.0, result.getUpperBound(), 1e-9);
    }
}
