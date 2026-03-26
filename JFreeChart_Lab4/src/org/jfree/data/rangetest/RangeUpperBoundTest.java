package org.jfree.data.rangetest;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import org.jfree.data.Range;
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
    
    private Range makeInvalidRange(double lower, double upper) throws Exception {
        Range r = new Range(0.0, 1.0);
        Field lowerField = Range.class.getDeclaredField("lower");
        Field upperField = Range.class.getDeclaredField("upper");
        lowerField.setAccessible(true);
        upperField.setAccessible(true);
        lowerField.set(r, lower);
        upperField.set(r, upper);
        return r;
    }
    
    @Test
    public void getUpperBound_ThrowsWhenLowerExceedsUpper() throws Exception {
        Range invalid = makeInvalidRange(10.0, 5.0);
        try {
            invalid.getUpperBound();
            fail("Expected IllegalArgumentException when lower > upper");
        } catch (IllegalArgumentException e) {
            // expected — dead branch successfully reached
        }
    }

    @Test
    public void getUpperBound_ExceptionMessageContainsLowerValue() throws Exception {
        Range invalid = makeInvalidRange(10.0, 5.0);
        try {
            invalid.getUpperBound();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            String msg = e.getMessage();
            assertNotNull("Exception message must not be null", msg);
            assertTrue("Message must contain lower value '10.0', was: " + msg,
                    msg.contains("10.0"));
        }
    }

    @Test
    public void getUpperBound_ExceptionMessageContainsUpperValue() throws Exception {
        Range invalid = makeInvalidRange(10.0, 5.0);
        try {
            invalid.getUpperBound();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            String msg = e.getMessage();
            assertNotNull("Exception message must not be null", msg);
            assertTrue("Message must contain upper value '5.0', was: " + msg,
                    msg.contains("5.0"));
        }
    }

    @Test
    public void getUpperBound_ExceptionMessageHasCorrectFormat() throws Exception {
        Range invalid = makeInvalidRange(3.0, 1.0);
        try {
            invalid.getUpperBound();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            String msg = e.getMessage();
            assertNotNull(msg);
            assertTrue("Message must contain 'lower (' literal",  msg.contains("lower ("));
            assertTrue("Message must contain lower value '3.0'",  msg.contains("3.0"));
            assertTrue("Message must contain ') <= upper ('",     msg.contains(") <= upper ("));
            assertTrue("Message must contain upper value '1.0'",  msg.contains("1.0"));
        }
    }

    @Test
    public void getUpperBound_LowerFieldNotCorruptedAfterException() throws Exception {
        Range invalid = makeInvalidRange(10.0, 5.0);
        Field lowerField = Range.class.getDeclaredField("lower");
        lowerField.setAccessible(true);
        try {
            invalid.getUpperBound();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            double lowerAfter = (double) lowerField.get(invalid);
            assertEquals("lower field must remain 10.0 after exception (a++ would give 11.0)",
                    10.0, lowerAfter, 0.0);
        }
    }

    @Test
    public void getUpperBound_UpperFieldNotCorruptedAfterException() throws Exception {
        Range invalid = makeInvalidRange(10.0, 5.0);
        Field upperField = Range.class.getDeclaredField("upper");
        upperField.setAccessible(true);
        try {
            invalid.getUpperBound();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            double upperAfter = (double) upperField.get(invalid);
            assertEquals("upper field must remain 5.0 after exception (a++ would give 6.0)",
                    5.0, upperAfter, 0.0);
        }
    }

    @Test
    public void getUpperBound_ExceptionMessageWithFractionalValues() throws Exception {
        Range invalid = makeInvalidRange(7.5, 2.5);
        try {
            invalid.getUpperBound();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            String msg = e.getMessage();
            assertTrue("Message must contain lower value '7.5', was: " + msg,
                    msg.contains("7.5"));
            assertTrue("Message must contain upper value '2.5', was: " + msg,
                    msg.contains("2.5"));
        }
    }

    @Test
    public void getUpperBound_ExceptionMessageWithNegativeValues() throws Exception {
        Range invalid = makeInvalidRange(-3.0, -8.0);
        try {
            invalid.getUpperBound();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            String msg = e.getMessage();
            assertTrue("Message must contain '-3.0' (not '3.0' from negation mutant), was: " + msg,
                    msg.contains("-3.0"));
            assertTrue("Message must contain '-8.0' (not '8.0' from negation mutant), was: " + msg,
                    msg.contains("-8.0"));
        }
    }
}
