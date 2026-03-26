package org.jfree.data.rangetest;

import static org.junit.Assert.*;
import org.jfree.data.Range;
import org.junit.Before;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class RangeIntersectsTest {

    private Range baseRange;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() {
        baseRange = new Range(-10, 10);
    }

    // Lower slightly below range, touching boundary (no overlap)
    @Test
    public void testLowerJustBelowLowerBoundTouchesLower() {
        assertFalse(baseRange.intersects(-10.00001, -10));
    }

    // Lower slightly below range but overlapping slightly
    @Test
    public void testLowerJustBelowLowerBoundOverlapsSlightly() {
        assertTrue(baseRange.intersects(-10.00001, -9.99999));
    }

    // Interval spans entire range
    @Test
    public void testLowerJustBelowLowerBoundSpansAll() {
        assertTrue(baseRange.intersects(-10.00001, 10.00001));
    }

    // Lower exactly at range lower bound, overlapping
    @Test
    public void testLowerAtLowerBoundOverlapsSlightly() {
        assertTrue(baseRange.intersects(-10, -9.99999));
    }

    // Exact match with range
    @Test
    public void testLowerAtLowerBoundMatchesRange() {
        assertTrue(baseRange.intersects(-10, 10));
    }

    // Interval fully inside range
    @Test
    public void testLowerInsideRangeNormalValues() {
        assertTrue(baseRange.intersects(-1, 1));
    }

    // Ends exactly at upper bound
    @Test
    public void testLowerInsideRangeEndsAtUpper() {
        assertTrue(baseRange.intersects(9.99999, 10));
    }

    // Starts exactly at upper bound (no overlap)
    @Test
    public void testLowerAtUpperBoundOverlapsSlightly() {
        assertFalse(baseRange.intersects(10, 10.00001));
    }

    // Very small positive number inside range
    @Test
    public void testLowerMinValueUpperBeyondRange() {
        assertTrue(baseRange.intersects(Double.MIN_VALUE, 10.00001));
    }

    // Lower slightly below range, very large upper
    @Test
    public void testLowerJustBelowLowerUpperMaxValue() {
        assertTrue(baseRange.intersects(-10.00001, Double.MAX_VALUE));
    }

    // Single point inside range
    @Test
    public void testSinglePointInsideRange() {
        assertTrue(baseRange.intersects(0, 0));
    }

    // NaN tests
    @Test
    public void testCase12_NaNLower() {
        assertFalse(baseRange.intersects(Double.NaN, 1));
    }

    @Test
    public void testLowerInsideUpperNaN() {
        assertFalse(baseRange.intersects(1, Double.NaN));
    }

    // Single point exactly at lower bound (no overlap)
    @Test
    public void testSinglePointAtLowerBound() {
        assertFalse(baseRange.intersects(-10, -10));
    }

    // Single point exactly at upper bound (no overlap)
    @Test
    public void testSinglePointAtUpperBound() {
        assertFalse(baseRange.intersects(10, 10));
    }
    
    // Test behaviour when the lower bound is NaN using computed values
    @Test
    public void testIntersectWithComputedNaNLowerBound() {

        double nanValue = Math.sqrt(-1);   // produces NaN
        double upperBound = 0.5 + 0.5;

        boolean result = baseRange.intersects(nanValue, upperBound);

        assertFalse("Intersection should be false when lower bound is NaN", result);
    }
    
    // Test behaviour when the upper bound becomes NaN during computation
    @Test
    public void testIntersectWithComputedNaNUpperBound() {

        double lowerBound = baseRange.getLowerBound() + 1;
        double nanValue = Double.NaN * 5;

        boolean result = baseRange.intersects(lowerBound, nanValue);

        assertFalse("Intersection should be false when upper bound is NaN", result);
    }
    
    /**
     * Line 179: "Less or equal to equal" survival.
     * b0 = -11 (strictly LESS than lower=-10, not equal).
     * With <= the condition is true → return (b1 > lower).
     * With == the condition is false → falls into else branch → wrong result.
     * b1 = -9 > -10, so assertTrue distinguishes the branches.
     */
    @Test
    public void testB0StrictlyBelowLower_MustTakeIfBranch() {
        // b0=-11 < lower=-10 (not equal), b1=-9 which is > lower
        assertTrue("b0 strictly below lower with b1 inside range must intersect",
                baseRange.intersects(-11, -9));
    }

    /**
     * Line 179: "Less or equal to equal" survival — false path.
     * b0=-11 strictly below lower, b1=-10 which is NOT > lower.
     * Ensures the if-branch is taken (not else) AND return value is false.
     */
    @Test
    public void testB0StrictlyBelowLower_B1AtLowerBound_NoIntersect() {
        // b0=-11 < lower=-10, b1=-10 → b1 > lower is false → no intersection
        assertFalse("b0 strictly below lower, b1 exactly at lower must NOT intersect",
                baseRange.intersects(-11, -10));
    }

    /**
     * Line 180: Targets "Substituted 1 with -1" survivors.
     * The return expression is (b1 > this.lower). The mutation substitutes
     * the literal 1 used internally. Using a b1 value very close to but
     * above lower forces the true result to be checked precisely.
     * b0=-10.5, b1=-9.9999 (just above lower=-10) → must be true.
     */
    @Test
    public void testIfBranch_B1JustAboveLower_MustReturnTrue() {
        assertTrue("b0 below lower, b1 just above lower must intersect",
                baseRange.intersects(-10.5, -9.9999));
    }

    /**
     * Line 180: Complementary — b1 exactly equals lower → false.
     * Tightens the > lower boundary check.
     */
    @Test
    public void testIfBranch_B1ExactlyAtLower_MustReturnFalse() {
        assertFalse("b0 below lower, b1 exactly at lower must NOT intersect",
                baseRange.intersects(-15, -10));
    }

    /**
     * Line 183: "Negated double local variable number 1" (b0 negated) survival.
     * b0=5, upper=10: condition is (b0 < upper) → true; negating b0 gives
     * (-5 < 10) which is also true, so we need b0 close to upper so that
     * negating it would flip the outcome.
     * b0=9 (positive, close to upper=10): negated b0 = -9 → (-9 < 10) still true.
     * Use b0=9, b1=9 (b1 >= b0 is true) → must intersect.
     * Also add a case where negated b0 would make b1 >= b0 fail:
     * b0=5, b1=5 → b1 >= b0 → true; negated b0=-5, b1=5 → b1 >= b0 (-5) → still true.
     * Best approach: use b0=5, b1=3 → b1 < b0 → should be FALSE.
     * Negated b0=-5, b1=3 → b1 >= -5 → true → wrong. This kills the mutant.
     */
    @Test
    public void testElseBranch_B1LessThanB0_MustReturnFalse() {
        // b0=5 > lower=-10 → else branch; b0=5 < upper=10 → true;
        // but b1=3 < b0=5 → b1 >= b0 is false → no intersection
        assertFalse("b0 inside range but b1 < b0 must NOT intersect",
                baseRange.intersects(5, 3));
    }

    /**
     * Line 183: Negated b0 survival — b0 negative inside range.
     * b0=-5, b1=-5 → b1 >= b0 → true; b0 < upper=10 → true → intersects.
     * Negated b0=5: b0 < upper → true; b1=-5 >= 5 → false → wrong.
     * This directly kills the "negate b0" mutant.
     */
    @Test
    public void testElseBranch_NegativeB0_B1EqualsB0_MustIntersect() {
        // b0=-5 > lower=-10 → else; b0=-5 < upper=10 → true; b1=-5 >= b0=-5 → true
        assertTrue("b0 negative inside range with b1==b0 must intersect",
                baseRange.intersects(-5, -5));
    }

    /**
     * Line 183: Post-increment (a++) on b0 survival.
     * b0 post-incremented: the comparison uses original b0, but return
     * uses b0+1. If b0=9 (just below upper=10), b1=9:
     * b0 < upper=10 → true; b1 >= b0 → true → intersects.
     * With a++ on b0 in the return: b1=9 >= (b0+1)=10 → false → wrong.
     */
    @Test
    public void testElseBranch_B0JustBelowUpper_B1EqualsB0_MustIntersect() {
        // b0=9 < upper=10 → true; b1=9 >= b0=9 → true → intersects
        assertTrue("b0 just below upper, b1==b0 must intersect",
                baseRange.intersects(9, 9));
    }

    /**
     * Line 183: Post-increment (a++) on b1 survival.
     * b0=5, b1=5: b1 >= b0 → true → intersects.
     * Mutant b1+1=6 >= b0=5 → still true. Need b1 exactly at b0 with
     * b1-1 failing: b0=5, b1=5 with a-- on b1 → b1=4 < b0=5 → false → kills it.
     * But we can't directly test a--; instead use b1=b0 (boundary) to distinguish.
     * For a++: b0=5, b1=4 is already false; we need b1=5 (== b0) to be true
     * while (b1++)=6 also true — not distinguishable here.
     * Better: b0=9.5, b1=9.5 → true; a++ on b1: b1=10.5 >= 9.5 → still true.
     * Use b0=9.9, b1=9.9 → true; a-- on b1: b1=8.9 >= 9.9? no → false → KILLS a--.
     */
    @Test
    public void testElseBranch_B0AndB1NearUpperBound_MustIntersect() {
        // b0=9.9 < upper=10; b1=9.9 >= b0=9.9 → true
        assertTrue("b0 and b1 both near upper bound must intersect",
                baseRange.intersects(9.9, 9.9));
    }

    /**
     * Line 183: Post-increment (a++) on upper survival.
     * Condition (b0 < this.upper): b0=9.9, upper=10 → true.
     * Mutant: b0 < (upper++) = b0 < 10 (post-inc uses original) → same result.
     * Use b0 just at boundary: b0=9.9999999 very close to upper=10.
     * With upper-- in return path: doesn't apply. 
     * Better target: b0=upper exactly → b0 < upper is FALSE → assertFalse.
     * Mutant upper++: b0 < (upper+1) → true → returns wrong true.
     */
    @Test
    public void testElseBranch_B0ExactlyAtUpper_MustReturnFalse() {
        // b0=10 == upper=10 → b0 < upper is false → no intersection
        assertFalse("b0 exactly at upper bound must NOT intersect",
                baseRange.intersects(10, 15));
    }

    /**
     * Line 183: Decremented (--a) on b0 at line 183 survival (mutation 53).
     * --b0 means b0 is decremented BEFORE use. If b0=0.5, --b0 = -0.5.
     * b1=-0.5 >= b0=0.5 → false normally → assertFalse.
     * With --b0: b1=-0.5 >= -0.5 → true → assertFalse would fail → KILLS it.
     * Need: original b0 gives false, pre-decremented b0 gives true.
     * b0=0.5, b1=-0.6: b1 >= b0=0.5? No → false. --b0=-0.5: b1=-0.6 >= -0.5? No → still false.
     * Try b0=0.5, b1=0: b1 >= b0 → false. --b0=-0.5: b1=0 >= -0.5 → TRUE → KILLS it!
     */
    @Test
    public void testElseBranch_B1BetweenDecrementedAndOriginalB0_MustReturnFalse() {
        // b0=0.5 > lower=-10 → else; b0=0.5 < upper=10 → true;
        // b1=0: b1 >= b0=0.5? NO → false. (--b0 mutant: b1=0 >= -0.5 → true → fails assert)
        assertFalse("b0=0.5 inside range, b1=0 which is below b0, must NOT intersect",
                baseRange.intersects(0.5, 0));
    }

    /**
     * Line 183: Incremented (++a) on b0 (mutation 48) survival.
     * ++b0 increments b0 before use in b1 >= b0.
     * b0=5, b1=5: b1 >= b0 → true. ++b0=6: b1=5 >= 6? false → KILLS it!
     * (This also helps with the negated b0 mutant.)
     */
    @Test
    public void testElseBranch_B1ExactlyEqualsB0_MustIntersect() {
        // b0=5 > lower=-10 → else; b0=5 < upper=10; b1=5 >= b0=5 → true
        assertTrue("b0 and b1 equal and inside range must intersect",
                baseRange.intersects(5, 5));
    }

    /**
     * Line 183: "Incremented (++a) double local variable number 3" (b1, mutation 48) survival.
     * Target: ++b1 used in b1 >= b0. If b1 is incremented before comparison,
     * a case where b1 < b0 should return false but ++b1 might push it to >= b0.
     * b0=5, b1=4: b1 >= b0? No → false. ++b1=5: 5 >= 5 → true → KILLS it.
     */
    @Test
    public void testElseBranch_B1OneUnitBelowB0_MustReturnFalse() {
        // b0=5 > lower=-10 → else; b0=5 < upper=10; b1=4 < b0=5 → false
        assertFalse("b1 just below b0 inside range must NOT intersect",
                baseRange.intersects(5, 4));
    }

    /**
     * Tighter boundary: b0=-10 (exactly at lower), b1=-10.
     * Ensures == path for b0=lower is covered: b0 <= lower → if branch.
     * b1=-10 → b1 > lower (-10)? NO → false.
     */
    @Test
    public void testB0AtLowerBound_B1AtLowerBound_NoIntersect() {
        assertFalse("b0==lower, b1==lower must NOT intersect",
                baseRange.intersects(-10.0, -10.0));
    }

    /**
     * b0=-10, b1=-10+epsilon: b0<=lower → if-branch; b1 > lower → true.
     * Tightens post-increment mutations on lower in the return expression.
     */
    @Test
    public void testB0AtLowerBound_B1JustAboveLower_MustIntersect() {
        assertTrue("b0==lower, b1 just above lower must intersect",
                baseRange.intersects(-10.0, -10.0 + 1e-9));
    }
    
    /**
     * Test when two ranges intersect.
     */
    @Test
    public void testIntersectsTrue() {
        Range r1 = new Range(1.0, 5.0);
        Range r2 = new Range(4.0, 10.0);

        boolean result = r1.intersects(r2);

        assertTrue(result);
    }

    /**
     * Test when two ranges do NOT intersect.
     */
    @Test
    public void testIntersectsFalse() {
        Range r1 = new Range(1.0, 3.0);
        Range r2 = new Range(5.0, 8.0);

        boolean result = r1.intersects(r2);

        assertFalse(result);
    }
    

    @After
    public void tearDown() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        System.out.println("RangeIntersectsTest suite completed.");
    }
}