package org.jfree.data.rangetest;

import static org.junit.Assert.*;

import org.jfree.data.Range;
import org.junit.Test;

public class RangeCentralValueTest {
	@Test
	public void testCentralValueBasic() {
	    Range r = new Range(2.0, 6.0);

	    double result = r.getCentralValue();

	    assertEquals(4.0, result, 0.0000001);
	}
	
	@Test
	public void testCentralValueZero() {
	    Range r = new Range(-2.0, 2.0);

	    double result = r.getCentralValue();

	    assertEquals(0.0, result, 0.0000001);
	}
	
	@Test
	public void testCentralValueNegative() {
	    Range r = new Range(-10.0, -4.0);

	    double result = r.getCentralValue();

	    assertEquals(-7.0, result, 0.0000001);
	}
	
	@Test
	public void testCentralValueMixed() {
	    Range r = new Range(-3.0, 7.0);

	    double result = r.getCentralValue();

	    assertEquals(2.0, result, 0.0000001);
	}
	
	@Test
	public void testCentralValueSameBounds() {
	    Range r = new Range(5.0, 5.0);

	    double result = r.getCentralValue();

	    assertEquals(5.0, result, 0.0000001);
	}
	
	@Test
	public void testCentralValuePrecision() {
	    Range r = new Range(1.5, 2.5);

	    double result = r.getCentralValue();

	    assertEquals(2.0, result, 0.0000001);
	}
	
	@Test
	public void testCentralValueLargeValues() {
	    Range r = new Range(1000000.0, 2000000.0);

	    double result = r.getCentralValue();

	    assertEquals(1500000.0, result, 0.0000001);
	}
	
	// Kills M1, M2, M4: central of (-1,1) = 0.0, NOT -1+1=0 (same, won't kill M1) 
    // Use asymmetric range to distinguish
    @Test
    public void centralValueOfSymmetricRangeIsZero() {
        // (-1,1): central = (-1/2 + 1/2) = 0.0
        // M1 would give: (-1/1 + 1/1) = 0 — same! Use asymmetric range instead
        assertEquals("Central value of Range(-1,1) should be 0.0",
                0.0, new Range(-1.0, 1.0).getCentralValue(), 1e-9);
    }

    // Kills M1 clearly: (1,5) central = (1/2 + 5/2) = 3.0; M1 gives 1+5 = 6.0
    @Test
    public void centralValueOfPositiveRangeIsCorrect() {
        assertEquals("Central value of Range(1.0, 5.0) should be 3.0",
                3.0, new Range(1.0, 5.0).getCentralValue(), 1e-9);
    }

    // Kills M1 and M4: (-4,2) central = -4/2+2/2 = -1.0; M1 gives (-4+2)=-2; M4 gives 0
    @Test
    public void centralValueOfCrossZeroRange() {
        assertEquals("Central value of Range(-4.0, 2.0) should be -1.0",
                -1.0, new Range(-4.0, 2.0).getCentralValue(), 1e-9);
    }

    // Kills M4 (return 0.0) and substitution: Range(2,8) central = 5.0
    @Test
    public void centralValueOfAllPositiveRange() {
        assertEquals("Central value of Range(2.0, 8.0) should be 5.0",
                5.0, new Range(2.0, 8.0).getCentralValue(), 1e-9);
    }

    // Kills substitution: Range(-10,-4) central = -7.0 (not -14, not 0)
    @Test
    public void centralValueOfNegativeRange() {
        assertEquals("Central value of Range(-10.0, -4.0) should be -7.0",
                -7.0, new Range(-10.0, -4.0).getCentralValue(), 1e-9);
    }

    // Edge: zero-length range — central value equals both bounds
    @Test
    public void centralValueOfZeroLengthRange() {
        assertEquals("Central value of Range(3.0, 3.0) should be 3.0",
                3.0, new Range(3.0, 3.0).getCentralValue(), 1e-9);
    }
}
