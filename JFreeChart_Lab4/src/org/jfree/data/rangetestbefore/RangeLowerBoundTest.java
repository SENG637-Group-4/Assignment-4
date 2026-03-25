package org.jfree.data.rangetestbefore;

import static org.junit.Assert.*;

import org.jfree.data.Range;
import org.junit.*;

/**
 * White-box tests for Range.getLowerBound().
 *
 * Source code:
 *   public double getLowerBound() {
 *       if (lower > upper) {          // Branch A: INFEASIBLE via public API
 *           throw new IllegalArgumentException(...); // constructor already prevents this
 *       }
 *       return this.lower;            // Branch B: always taken
 *   }
 *
 * Branch coverage:
 *   Branch A (true)  = INFEASIBLE: Range constructor throws if lower > upper,
 *                      so this guard can never be triggered through the public API.
 *                      This is identified as a dead-code branch in the SUT.
 *   Branch B (false) = covered by every test below.
 *
 * Reachable statement coverage: 100% (2 of 2 reachable statements).
 * Branch coverage: 50% (1 reachable branch; 1 infeasible).
 *
 * Spec (Javadoc): "Returns the lower bound for the range."
 */
public class RangeLowerBoundTest {

    @BeforeClass public static void setUpBeforeClass() throws Exception {}
    @Before      public void setUp()              throws Exception {}
    @After       public void tearDown()           throws Exception {}
    @AfterClass  public static void tearDownAfterClass() throws Exception {}

    // Branch B: lower <= upper → return lower
    // EC: positive lower bound (NOM)
    @Test
    public void getLowerBoundWithPositiveLower() {
        assertEquals("Lower bound of Range(2.0, 6.0) should be 2.0",
                2.0, new Range(2.0, 6.0).getLowerBound(), 1e-9);
    }

    // Branch B: lower <= upper → return lower
    // EC: negative lower bound (NOM)
    @Test
    public void getLowerBoundWithNegativeLower() {
        assertEquals("Lower bound of Range(-5.0, 3.0) should be -5.0",
                -5.0, new Range(-5.0, 3.0).getLowerBound(), 1e-9);
    }

    // Branch B: lower <= upper → return lower
    // BVA: lower = 0.0 (zero boundary)
    @Test
    public void getLowerBoundWhenLowerIsZero() {
        assertEquals("Lower bound of Range(0.0, 10.0) should be 0.0",
                0.0, new Range(0.0, 10.0).getLowerBound(), 1e-9);
    }

    // Branch B: lower <= upper → return lower
    // BVA: lower = upper (zero-length range)
    @Test
    public void getLowerBoundWhenLowerEqualsUpper() {
        assertEquals("Lower bound of Range(5.0, 5.0) should be 5.0",
                5.0, new Range(5.0, 5.0).getLowerBound(), 1e-9);
    }

    // Branch B: lower <= upper → return lower
    // EC: very large negative lower bound (extreme value)
    @Test
    public void getLowerBoundWithLargeNegativeValue() {
        assertEquals("Lower bound of Range(-1e15, 0.0) should be -1e15",
                -1e15, new Range(-1e15, 0.0).getLowerBound(), 1.0);
    }

    // Branch B: lower <= upper → return lower
    // Correctness: verify getLowerBound does NOT return upper bound
    @Test
    public void getLowerBoundDoesNotReturnUpperBound() {
        Range r = new Range(-3.0, 7.0);
        assertFalse("getLowerBound() must not return the upper bound 7.0",
                r.getLowerBound() == 7.0);
        assertEquals("getLowerBound() must return -3.0", -3.0, r.getLowerBound(), 1e-9);
    }
}
