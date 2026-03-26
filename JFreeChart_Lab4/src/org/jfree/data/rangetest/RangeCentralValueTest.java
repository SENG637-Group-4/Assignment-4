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
}
