package org.jfree.data;

import static org.junit.Assert.*;
import org.junit.*;

/**
 * White-box tests for Range.getUpperBound().
 *
 * Source code:
 *   public double getUpperBound() {
 *       if (lower > upper) {          // Branch A: INFEASIBLE via public API
 *           throw new IllegalArgumentException(...);
 *       }
 *       return this.upper;            // Branch B: always taken
 *   }
 *
 * Branch coverage:
 *   Branch A (true)  = INFEASIBLE: same reason as getLowerBound — constructor
 *                      enforces lower <= upper, making this guard permanently false.
 *   Branch B (false) = covered by every test below.
 *
 * Reachable statement coverage: 100%.
 * Branch coverage: 50% (1 reachable branch; 1 infeasible).
 *
 * Spec (Javadoc): "Returns the upper bound for the range."
 */
public class RangeUpperBoundTest {

    @BeforeClass public static void setUpBeforeClass() throws Exception {}
    @Before      public void setUp()              throws Exception {}
    @After       public void tearDown()           throws Exception {}
    @AfterClass  public static void tearDownAfterClass() throws Exception {}

    // Branch B: lower <= upper → return upper
    // EC: positive upper bound (NOM)
    @Test
    public void getUpperBoundWithPositiveUpper() {
        assertEquals("Upper bound of Range(2.0, 6.0) should be 6.0",
                6.0, new Range(2.0, 6.0).getUpperBound(), 1e-9);
    }

    // Branch B: lower <= upper → return upper
    // EC: negative upper bound (NOM)
    @Test
    public void getUpperBoundWithNegativeUpper() {
        assertEquals("Upper bound of Range(-5.0, -1.0) should be -1.0",
                -1.0, new Range(-5.0, -1.0).getUpperBound(), 1e-9);
    }

    // Branch B: lower <= upper → return upper
    // BVA: upper = 0.0 (zero boundary)
    @Test
    public void getUpperBoundWhenUpperIsZero() {
        assertEquals("Upper bound of Range(-10.0, 0.0) should be 0.0",
                0.0, new Range(-10.0, 0.0).getUpperBound(), 1e-9);
    }

    // Branch B: lower <= upper → return upper
    // BVA: lower = upper (zero-length range)
    @Test
    public void getUpperBoundWhenLowerEqualsUpper() {
        assertEquals("Upper bound of Range(5.0, 5.0) should be 5.0",
                5.0, new Range(5.0, 5.0).getUpperBound(), 1e-9);
    }

    // Branch B: lower <= upper → return upper
    // EC: very large positive upper bound (extreme value)
    @Test
    public void getUpperBoundWithLargePositiveValue() {
        assertEquals("Upper bound of Range(0.0, 1e15) should be 1e15",
                1e15, new Range(0.0, 1e15).getUpperBound(), 1.0);
    }

    // Branch B: lower <= upper → return upper
    // Correctness: verify getUpperBound does NOT return lower bound
    @Test
    public void getUpperBoundDoesNotReturnLowerBound() {
        Range r = new Range(-3.0, 7.0);
        assertFalse("getUpperBound() must not return the lower bound -3.0",
                r.getUpperBound() == -3.0);
        assertEquals("getUpperBound() must return 7.0", 7.0, r.getUpperBound(), 1e-9);
    }
}
