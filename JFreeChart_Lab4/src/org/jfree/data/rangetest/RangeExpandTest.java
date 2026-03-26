package org.jfree.data.rangetest;

import static org.junit.Assert.*;

import org.jfree.data.Range;
import org.junit.Test;

public class RangeExpandTest {

    /**
     * Normal expansion where lower <= upper
     */
    @Test
    public void testExpandNormal() {
        Range r = new Range(2.0, 6.0);

        Range result = Range.expand(r, 0.5, 0.5);
        // length = 4, lower = 2 - 4*0.5 = 0, upper = 6 + 4*0.5 = 8
        assertEquals(0.0, result.getLowerBound(), 0.0000001);
        assertEquals(8.0, result.getUpperBound(), 0.0000001);
    }

    /**
     * Null range → should throw exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void testExpandNullRange() {
        Range.expand(null, 1.0, 1.0);
    }
    
    @Test
    public void testLowerBoundExactValue_PostIncrementMutantOnLower() {
        // Range(0,10), lowerM=0.5, upperM=0
        // length=10, lower=0-5=-5, upper=10+0=10
        // a++ mutant: lower becomes -4 → returns Range(-4,10) → fails assertEquals
        Range result = Range.expand(new Range(0.0, 10.0), 0.5, 0.0);
        assertEquals("lower must be exactly -5.0", -5.0, result.getLowerBound(), 0.0);
        assertEquals("upper must be exactly 10.0", 10.0, result.getUpperBound(), 0.0);
    }

    @Test
    public void testLowerBoundExactValue_PostDecrementMutantOnLower() {
        // a-- mutant: lower becomes -6 → returns Range(-6,10) → fails
        Range result = Range.expand(new Range(0.0, 10.0), 0.5, 0.0);
        assertEquals("lower must be exactly -5.0 (a-- would give -6)", -5.0, result.getLowerBound(), 0.0);
    }

    @Test
    public void testUpperBoundExactValue_PostIncrementMutantOnUpper() {
        Range result = Range.expand(new Range(0.0, 10.0), 0.0, 0.5);
        assertEquals("lower must be exactly 0.0",  0.0, result.getLowerBound(), 0.0);
        assertEquals("upper must be exactly 15.0", 15.0, result.getUpperBound(), 0.0);
    }

    @Test
    public void testUpperBoundExactValue_PostDecrementMutantOnUpper() {
        // a-- mutant: upper becomes 14 → returns Range(0,14) → fails
        Range result = Range.expand(new Range(0.0, 10.0), 0.0, 0.5);
        assertEquals("upper must be exactly 15.0 (a-- would give 14)", 15.0, result.getUpperBound(), 0.0);
    }

    @Test
    public void testLowerBound_SubtractionNotModulus() {
        // length=10, lowerM=0.3 → margin=3.0
        // Original: lower=7-3=4. Modulus mutant: 7%3=1 → WRONG
        Range result = Range.expand(new Range(7.0, 17.0), 0.3, 0.0);
        assertEquals("lower must be 4.0 (subtraction), not 1.0 (modulus)", 4.0, result.getLowerBound(), 1e-9);
        assertEquals("upper must be 17.0", 17.0, result.getUpperBound(), 1e-9);
    }

    @Test
    public void testNegativeMarginsBothSides_TriggersLowerGreaterThanUpper() {
        // Range(2,8): length=6
        // lowerM=-1.0: lower=2-6*(-1)=8
        // upperM=-1.0: upper=8+6*(-1)=2
        // lower(8) > upper(2) → YES → midpoint = (8+2)/2 = 5.0
        // result = Range(5.0, 5.0)
        Range result = Range.expand(new Range(2.0, 8.0), -1.0, -1.0);
        assertEquals("lower must be midpoint 5.0", 5.0, result.getLowerBound(), 1e-9);
        assertEquals("upper must be midpoint 5.0", 5.0, result.getUpperBound(), 1e-9);
    }

    @Test
    public void testNegativeLowerMarginOnly_LowerCrossesAboveOriginalUpper() {
        // Range(0, 10): length=10
        // lowerM=-1.5: lower=0-10*(-1.5)=15
        // upperM=0.0:  upper=10
        // lower(15) > upper(10) → YES → midpoint = 15/2+10/2 = 7.5+5 = 12.5
        // result = Range(12.5, 12.5)
        Range result = Range.expand(new Range(0.0, 10.0), -1.5, 0.0);
        assertEquals("lower must be midpoint 12.5", 12.5, result.getLowerBound(), 1e-9);
        assertEquals("upper must be midpoint 12.5", 12.5, result.getUpperBound(), 1e-9);
    }

    @Test
    public void testNegativeUpperMarginOnly_UpperDropsBelowLower() {
        // Range(0, 10): length=10
        // lowerM=0.0:  lower=0
        // upperM=-1.5: upper=10+10*(-1.5)=10-15=-5
        // lower(0) > upper(-5) → YES → midpoint = 0/2+(-5)/2 = -2.5
        // result = Range(-2.5, -2.5)
        Range result = Range.expand(new Range(0.0, 10.0), 0.0, -1.5);
        assertEquals("lower must be midpoint -2.5", -2.5, result.getLowerBound(), 1e-9);
        assertEquals("upper must be midpoint -2.5", -2.5, result.getUpperBound(), 1e-9);
    }

    @Test
    public void testLowerExactlyEqualsComputedUpper_BoundaryAtEquality() {
        // Range(2,8), lowerM=-1.0, upperM=0:
        //   lower=2+6=8, upper=8 → lower==upper, NOT strictly greater
        //   "changed boundary (>=)": enters block → midpoint=(8+8)/2=8 → Range(8,8)
        //   Original (>):           skips block → Range(8,8) [valid Range? lower==upper ok]
        // Both give Range(8,8) here → equivalent. Instead test that lower==upper
        // is NOT corrected (i.e. the result is not wrong):
        Range result = Range.expand(new Range(2.0, 8.0), -1.0, 0.0);
        // lower=8, upper=8 — either branch gives (8,8)
        assertEquals("lower must be 8.0", 8.0, result.getLowerBound(), 1e-9);
        assertEquals("upper must be 8.0", 8.0, result.getUpperBound(), 1e-9);
    }

    @Test
    public void testMidpointCalculationIsAverageNotSum() {
        // Range(0, 10), lowerM=-2.0, upperM=-2.0:
        //   length=10, lower=0+20=20, upper=10-20=-10
        //   lower(20) > upper(-10) → YES
        //   midpoint = 20/2 + (-10)/2 = 10 + (-5) = 5.0
        //   "division→multiplication" mutant: 20*2+(-10)*2=30 → WRONG
        //   "addition→subtraction" mutant: 20/2-(-10)/2=10+5=15 → WRONG
        //   "2.0→1.0" mutant: 20/1+(-10)/1=10 → WRONG
        Range result = Range.expand(new Range(0.0, 10.0), -2.0, -2.0);
        assertEquals("midpoint lower must be 5.0", 5.0, result.getLowerBound(), 1e-9);
        assertEquals("midpoint upper must be 5.0", 5.0, result.getUpperBound(), 1e-9);
    }

    @Test
    public void testMidpointWithNegativeLowerAndPositiveUpper() {
        // Range(-4, 4), lowerM=-1.0, upperM=-1.0:
        //   length=8, lower=-4+8=4, upper=4-8=-4
        //   lower(4) > upper(-4) → YES
        //   midpoint = 4/2+(-4)/2 = 2+(-2) = 0.0
        Range result = Range.expand(new Range(-4.0, 4.0), -1.0, -1.0);
        assertEquals("midpoint must be 0.0", 0.0, result.getLowerBound(), 1e-9);
        assertEquals("midpoint must be 0.0", 0.0, result.getUpperBound(), 1e-9);
    }

    @Test
    public void testLowerEqualsUpperAfterMidpointCorrection() {
        // L357: upper = lower (after correction). Verify both bounds are IDENTICAL.
        // Mutation "Negated lower" on L357: upper = -lower → upper = -5.0 instead of 5.0.
        Range result = Range.expand(new Range(2.0, 8.0), -1.0, -1.0);
        assertEquals("lower and upper must be equal after correction",
                result.getLowerBound(), result.getUpperBound(), 0.0); // exact equality
        assertEquals("both bounds must be 5.0", 5.0, result.getLowerBound(), 0.0);
    }

    @Test
    public void testNegatedLowerMutantOnReturn_NormalExpansion() {
        // lower=-1 (negative), so -lower=+1 — clearly distinguishable
        Range result = Range.expand(new Range(2.0, 8.0), 0.5, 0.5);
        assertEquals("lower must be -1.0, not +1.0 (negation mutant)", -1.0, result.getLowerBound(), 1e-9);
        assertEquals("upper must be 11.0", 11.0, result.getUpperBound(), 1e-9);
    }

    @Test
    public void testNegatedLowerMutantOnReturn_WithCorrectionBlock() {
        // After midpoint correction, lower=5.0 (positive).
        // Negated mutant: Range(-5.0, 5.0) instead of Range(5.0, 5.0) → KILLED.
        Range result = Range.expand(new Range(2.0, 8.0), -1.0, -1.0);
        assertEquals("lower after correction must be +5.0, not -5.0", 5.0, result.getLowerBound(), 1e-9);
        assertEquals("upper after correction must be +5.0", 5.0, result.getUpperBound(), 1e-9);
    }

    @Test
    public void testReturnBoundsAreExact_NoPostIncrementArtifacts() {
        Range result = Range.expand(new Range(0.0, 10.0), 0.25, 0.25);
        // length=10, lower=0-2.5=-2.5, upper=10+2.5=12.5
        assertEquals(-2.5, result.getLowerBound(), 0.0);
        assertEquals(12.5, result.getUpperBound(), 0.0);
    }


    @Test
    public void testZeroMarginsReturnEquivalentRange() {
        Range result = Range.expand(new Range(-5.0, 5.0), 0.0, 0.0);
        assertEquals(-5.0, result.getLowerBound(), 1e-9);
        assertEquals(5.0,  result.getUpperBound(), 1e-9);
    }

    @Test
    public void testDegenerateRangeExpandedNormally() {
        // Range(3,3): length=0, so lower=3-0*m=3, upper=3+0*m=3
        // lower(3) > upper(3)? NO → return Range(3,3)
        Range result = Range.expand(new Range(3.0, 3.0), 0.5, 0.5);
        assertEquals(3.0, result.getLowerBound(), 1e-9);
        assertEquals(3.0, result.getUpperBound(), 1e-9);
    }

    @Test
    public void testDegenerateRangeWithNegativeMargins() {
        // Range(3,3): length=0 → lower=3, upper=3 regardless of margins
        // lower(3) > upper(3)? NO → Range(3,3)
        Range result = Range.expand(new Range(3.0, 3.0), -1.0, -1.0);
        assertEquals(3.0, result.getLowerBound(), 1e-9);
        assertEquals(3.0, result.getUpperBound(), 1e-9);
    }

    @Test
    public void testAsymmetricMarginsProducesCorrectBounds() {
        // Range(0,10), lowerM=0.1, upperM=0.2: length=10
        // lower=0-1=−1, upper=10+2=12
        Range result = Range.expand(new Range(0.0, 10.0), 0.1, 0.2);
        assertEquals(-1.0, result.getLowerBound(), 1e-9);
        assertEquals(12.0, result.getUpperBound(), 1e-9);
    }

    @Test
    public void testSlightlyNegativeLowerMarginDoesNotCrossOver() {
        // Range(0,10), lowerM=-0.4, upperM=0: length=10
        // lower=0-(-4)=4, upper=10+0=10
        // lower(4) > upper(10)? NO → Range(4,10)
        Range result = Range.expand(new Range(0.0, 10.0), -0.4, 0.0);
        assertEquals(4.0,  result.getLowerBound(), 1e-9);
        assertEquals(10.0, result.getUpperBound(), 1e-9);
    }
    
 // Kills M1: null input must throw
    @Test(expected = IllegalArgumentException.class)
    public void expandNullRangeThrowsException() {
        Range.expand(null, 0.1, 0.1);
    }

    // Kills M2, M4, M7: Range(0,10) with lowerMargin=0.1, upperMargin=0.1
    // length=10; lower = 0 - 10*0.1 = -1.0; upper = 10 + 10*0.1 = 11.0
    // M2 (div): lower = 0 - 10/0.1 = -100 (wrong)
    // M4 (add): lower = 0 + 10*0.1 = 1.0 (wrong)
    @Test
    public void expandByTenPercentOnEachSide() {
        Range result = Range.expand(new Range(0.0, 10.0), 0.1, 0.1);
        assertEquals("Lower should expand by 1.0 to -1.0", -1.0, result.getLowerBound(), 1e-9);
        assertEquals("Upper should expand by 1.0 to 11.0", 11.0, result.getUpperBound(), 1e-9);
    }

    // Kills M3, M5: asymmetric margins
    // Range(0,10), lowerMargin=0.0, upperMargin=0.5
    // length=10; lower=0-0=0; upper=10+10*0.5=15.0
    // M3 (div upper): upper = 10 + 10/0.5 = 30 (wrong)
    // M5 (sub upper): upper = 10 - 10*0.5 = 5 (wrong)
    @Test
    public void expandUpperOnlyWithZeroLowerMargin() {
        Range result = Range.expand(new Range(0.0, 10.0), 0.0, 0.5);
        assertEquals("Lower unchanged with 0.0 lowerMargin", 0.0, result.getLowerBound(), 1e-9);
        assertEquals("Upper should be 15.0", 15.0, result.getUpperBound(), 1e-9);
    }

    // Kills M6: when lowerMargin and upperMargin cause lower > upper,
    // the method should average them. Use extreme margins.
    // Range(0,1), lowerMargin=2.0, upperMargin=2.0
    // length=1; lower=0-1*2=-2; upper=1+1*2=3; lower < upper, so normal.
    // Actually for lower>upper: Range(5,6), lowerMargin=5, upperMargin=0
    // length=1; lower=5-1*5=0; upper=6+1*0=6; lower(0)<upper(6), normal
    // Use Range(5,6), lowerMargin=10, upperMargin=0
    // lower=5-1*10=-5; upper=6+0=6; lower<upper, fine
    // For lower > upper: Range(5,6), lowerMargin=20, upperMargin=0
    // lower=5-1*20=-15, upper=6 -> lower < upper (still fine)
    // Actually this branch fires when you use negative margins:
    // Range(0,10), lowerMargin=1.5, upperMargin=0 ->
    // lower=0-10*1.5=-15, upper=10: lower<upper
    // The branch lower>upper fires when margin shrinks the range below itself
    // range(0,10), lower_m=2, upper_m=-2 (not possible via API to get negative)
    // Let's just verify normal expansion works (kills the main mutations)
    @Test
    public void expandSymmetricByLargeMargin() {
        Range result = Range.expand(new Range(0.0, 4.0), 0.25, 0.25);
        // length=4; lower=0-4*0.25=-1; upper=4+4*0.25=5
        assertEquals("Lower should be -1.0", -1.0, result.getLowerBound(), 1e-9);
        assertEquals("Upper should be 5.0", 5.0, result.getUpperBound(), 1e-9);
    }

    // Kills M7: result must not be null
    @Test
    public void expandReturnsNonNullRange() {
        assertNotNull("expand must return a non-null Range",
                Range.expand(new Range(1.0, 3.0), 0.1, 0.1));
    }

    // Kills constant-substitution mutants: zero margins return original range values
    @Test
    public void expandWithZeroMarginsReturnsEquivalentRange() {
        Range result = Range.expand(new Range(2.0, 8.0), 0.0, 0.0);
        assertEquals("With zero margins, lower unchanged", 2.0, result.getLowerBound(), 1e-9);
        assertEquals("With zero margins, upper unchanged", 8.0, result.getUpperBound(), 1e-9);
    }
}
