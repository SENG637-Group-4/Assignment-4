package org.jfree.data.rangetest;

import static org.junit.Assert.*;
import java.lang.reflect.Field;

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
    
    @Test
    public void getLowerBound_ThrowsWhenLowerExceedsUpper() throws Exception {
        Range invalid = makeInvalidRange(10.0, 5.0);
        try {
            invalid.getLowerBound();
            fail("Expected IllegalArgumentException when lower > upper");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void getLowerBound_ExceptionMessageContainsLowerValue() throws Exception {
        Range invalid = makeInvalidRange(10.0, 5.0);
        try {
            invalid.getLowerBound();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            String msg = e.getMessage();
            assertNotNull("Exception message must not be null", msg);
            // Kills L106 #3 (toString removed), #4 (append replaced),
            // #5 (negated: -10.0 ≠ 10.0), #8/9 (pre-inc: 11.0/9.0 ≠ 10.0)
            assertTrue("Message must contain the lower value '10.0', was: " + msg,
                    msg.contains("10.0"));
        }
    }

    /**
     * L107 #7: "Negated double field upper → SURVIVED"
     * Mutation: uses -upper instead of upper in the message.
     * lower=10, upper=5: original contains "5.0", mutant contains "-5.0".
     *
     * L107 #10/11: "++a/--a on upper → SURVIVED"
     * Pre-increment: message contains "6.0" or "4.0" instead of "5.0".
     *
     * L107 #4-6: "replaced append with receiver (×3) → SURVIVED"
     * Three consecutive appends on line 107; replacing any with receiver
     * breaks the chain — upper value or surrounding text is missing.
     */
    @Test
    public void getLowerBound_ExceptionMessageContainsUpperValue() throws Exception {
        Range invalid = makeInvalidRange(10.0, 5.0);
        try {
            invalid.getLowerBound();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            String msg = e.getMessage();
            assertNotNull("Exception message must not be null", msg);
            // Kills L107 #7 (negated: -5.0 ≠ 5.0), #10/11 (pre-inc: 6.0/4.0 ≠ 5.0),
            // #4-6 (append replaced: upper text missing from chain)
            assertTrue("Message must contain the upper value '5.0', was: " + msg,
                    msg.contains("5.0"));
        }
    }

    /**
     * Verifies the full message structure matches the documented format:
     * "Range(double, double): require lower (X) <= upper (Y)."
     * This kills ALL message-content survivors in one assertion by checking
     * both values and the surrounding literal text are present together.
     *
     * Kills: L106 #3,4,5,8,9 and L107 #4,5,6,7,10,11 simultaneously.
     */
    @Test
    public void getLowerBound_ExceptionMessageHasCorrectFormat() throws Exception {
        Range invalid = makeInvalidRange(3.0, 1.0);
        try {
            invalid.getLowerBound();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            String msg = e.getMessage();
            assertNotNull(msg);
            // Full structure check — any mutation to lower/upper values or
            // the StringBuilder chain produces a message that fails this.
            assertTrue("Message must contain 'lower (' literal", msg.contains("lower ("));
            assertTrue("Message must contain lower value 3.0",   msg.contains("3.0"));
            assertTrue("Message must contain ') <= upper ('",    msg.contains(") <= upper ("));
            assertTrue("Message must contain upper value 1.0",   msg.contains("1.0"));
        }
    }

    @Test
    public void getLowerBound_FieldNotCorruptedAfterException() throws Exception {
        // If a++ on lower field: lower becomes 10+1=11 after the throw.
        // Re-reading the field via reflection after the exception detects corruption.
        Range invalid = makeInvalidRange(10.0, 5.0);
        Field lowerField = Range.class.getDeclaredField("lower");
        lowerField.setAccessible(true);

        try {
            invalid.getLowerBound();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // After the throw, verify the lower field was not permanently modified
            // (kills a++ / a-- post-op survivors if the field was written back)
            double lowerAfter = (double) lowerField.get(invalid);
            assertEquals("lower field must remain 10.0 after exception (not corrupted by a++/a--)",
                    10.0, lowerAfter, 0.0);
        }
    }

    @Test
    public void getLowerBound_UpperFieldNotCorruptedAfterException() throws Exception {
        Range invalid = makeInvalidRange(10.0, 5.0);
        Field upperField = Range.class.getDeclaredField("upper");
        upperField.setAccessible(true);

        try {
            invalid.getLowerBound();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            double upperAfter = (double) upperField.get(invalid);
            assertEquals("upper field must remain 5.0 after exception (not corrupted by a++/a--)",
                    5.0, upperAfter, 0.0);
        }
    }

    /**
     * Verify with distinct non-integer values so that +1/-1 mutations on
     * lower and upper produce clearly wrong strings.
     * lower=7.5, upper=2.5: mutant "++(7.5)" would give "8.5" not "7.5".
     */
    @Test
    public void getLowerBound_ExceptionMessageWithFractionalValues() throws Exception {
        Range invalid = makeInvalidRange(7.5, 2.5);
        try {
            invalid.getLowerBound();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            String msg = e.getMessage();
            assertTrue("Message must contain lower value 7.5, was: " + msg,
                    msg.contains("7.5"));
            assertTrue("Message must contain upper value 2.5, was: " + msg,
                    msg.contains("2.5"));
        }
    }

    /**
     * Verify with negative lower value to distinguish negation mutation:
     * lower=-3.0 → negated = +3.0; the message must contain "-3.0" not "3.0".
     */
    @Test
    public void getLowerBound_ExceptionMessageWithNegativeLower() throws Exception {
        // lower=-3, upper=-8 → lower(-3) > upper(-8) → invalid
        Range invalid = makeInvalidRange(-3.0, -8.0);
        try {
            invalid.getLowerBound();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            String msg = e.getMessage();
            // Negation mutant on lower: message contains "3.0" not "-3.0" → fails
            assertTrue("Message must contain '-3.0' (negative lower), was: " + msg,
                    msg.contains("-3.0"));
            // Negation mutant on upper: message contains "8.0" not "-8.0" → fails
            assertTrue("Message must contain '-8.0' (negative upper), was: " + msg,
                    msg.contains("-8.0"));
        }
    }
    
    private Range makeInvalidRange(double lower, double upper) throws Exception {
        // Use the valid constructor path (lower <= upper), then overwrite fields
        Range r = new Range(0.0, 1.0);
        Field lowerField = Range.class.getDeclaredField("lower");
        Field upperField = Range.class.getDeclaredField("upper");
        lowerField.setAccessible(true);
        upperField.setAccessible(true);
        lowerField.set(r, lower);
        upperField.set(r, upper);
        return r;
    }
}
