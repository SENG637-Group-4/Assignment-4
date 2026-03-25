package org.jfree.data.rangetestafter;

import static org.junit.Assert.*;
import java.lang.reflect.Field;
import org.jfree.data.Range;
import org.junit.*;

// Tests for Range.getLength() — EC1 (cross-zero), EC2 (positive),
// EC3 (negative), EC4 (zero-length), BVA around zero for each bound.
public class RangeGetLengthTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    // EC2 NOM: both bounds positive
    @Test
    public void getLengthOfPositiveRange() {
        Range range = new Range(2.0, 6.0);
        assertEquals("Range(2.0, 6.0) length should be 4.0",
                4.0, range.getLength(), .000000001d);
    }

    // EC1 NOM: cross-zero range
    @Test
    public void getLengthOfCrossZeroRange() {
        Range range = new Range(-10.0, 10.0);
        assertEquals("Range(-10.0, 10.0) length should be 20.0",
                20.0, range.getLength(), .000000001d);
    }

    // EC3: both bounds negative
    @Test
    public void getLengthOfNegativeOnlyRange() {
        Range range = new Range(-10.0, -2.0);
        assertEquals("Range(-10.0, -2.0) length should be 8.0",
                8.0, range.getLength(), .000000001d);
    }

    // EC4: lower = upper, zero-length (LB = UB)
    @Test
    public void getLengthOfZeroLengthRange() {
        Range range = new Range(5.0, 5.0);
        assertEquals("Range(5.0, 5.0) length should be 0.0",
                0.0, range.getLength(), .000000001d);
    }

    // EC2: lower = 0.0 (LB boundary for lower)
    @Test
    public void getLengthWhenLowerBoundIsZero() {
        Range range = new Range(0.0, 5.0);
        assertEquals("Range(0.0, 5.0) length should be 5.0",
                5.0, range.getLength(), .000000001d);
    }

    // EC3: upper = 0.0 (UB boundary for upper)
    @Test
    public void getLengthWhenUpperBoundIsZero() {
        Range range = new Range(-5.0, 0.0);
        assertEquals("Range(-5.0, 0.0) length should be 5.0",
                5.0, range.getLength(), .000000001d);
    }

    // EC4: both bounds at zero
    @Test
    public void getLengthWhenBothBoundsAreZero() {
        Range range = new Range(0.0, 0.0);
        assertEquals("Range(0.0, 0.0) length should be 0.0",
                0.0, range.getLength(), .000000001d);
    }

    // EC1: tiny range straddling zero (BLB lower, AUB upper)
    @Test
    public void getLengthOfTinyRangeStraddlingZero() {
        Range range = new Range(-0.0001, 0.0001);
        assertEquals("Range(-0.0001, 0.0001) length should be 0.0002",
                0.0002, range.getLength(), .000000001d);
    }

    // EC3: upper just below zero (BUB boundary)
    @Test
    public void getLengthWhenUpperBoundIsJustBelowZero() {
        Range range = new Range(-1.0, -0.0001);
        assertEquals("Range(-1.0, -0.0001) length should be 0.9999",
                0.9999, range.getLength(), .000000001d);
    }

    // EC2: lower just above zero (ALB boundary)
    @Test
    public void getLengthWhenLowerBoundIsJustAboveZero() {
        Range range = new Range(0.0001, 1.0);
        assertEquals("Range(0.0001, 1.0) length should be 0.9999",
                0.9999, range.getLength(), .000000001d);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetLengthThrowsExceptionWhenLowerGreaterThanUpper() throws Exception {

        // create a valid Range first
        Range r = new Range(1.0, 5.0);

        // use reflection to break the invariant
        Field lowerField = Range.class.getDeclaredField("lower");
        Field upperField = Range.class.getDeclaredField("upper");

        lowerField.setAccessible(true);
        upperField.setAccessible(true);

        // force lower > upper
        lowerField.set(r, 10.0);
        upperField.set(r, 5.0);

        // now call method
        r.getLength();
    }

    @After
    public void tearDown() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }
}
