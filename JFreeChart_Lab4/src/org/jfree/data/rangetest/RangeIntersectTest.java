package org.jfree.data.rangetest;

import static org.junit.Assert.*;

import org.jfree.data.Range;
import org.junit.Test;

public class RangeIntersectTest {

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

//    /**
//     * Test when ranges touch at the boundary.
//     */
//    @Test
//    public void testIntersectsAtBoundary() {
//        Range r1 = new Range(1.0, 5.0);
//        Range r2 = new Range(5.0, 10.0);
//
//        boolean result = r1.intersects(r2);
//
//        assertTrue(result);
//    }
}
