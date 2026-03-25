package org.jfree.data.datautilitiestestbefore;

import static org.junit.Assert.*;

import org.jfree.data.DataUtilities;
import org.jfree.data.Values2D;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

public class CalculateRowTotalTest {

    private Mockery context;
    private Values2D values;

    @Before
    public void setUp() {
        context = new Mockery();
        values = context.mock(Values2D.class);
    }

    @Test
    public void testTC1_firstRow() {
        context.checking(new Expectations() {{
            allowing(values).getColumnCount(); will(returnValue(2));
            allowing(values).getValue(0, 0); will(returnValue(1.0));
            allowing(values).getValue(0, 1); will(returnValue(2.0));
        }});
        double result = DataUtilities.calculateRowTotal(values, 0);
        assertEquals(3.0, result, 1e-9);
    }

    @Test
    public void testTC2_secondRow_ALB() {
        context.checking(new Expectations() {{
            allowing(values).getColumnCount(); will(returnValue(2));
            allowing(values).getValue(1, 0); will(returnValue(3.0));
            allowing(values).getValue(1, 1); will(returnValue(4.0));
        }});
        double result = DataUtilities.calculateRowTotal(values, 1);
        assertEquals(7.0, result, 1e-9);
    }

    @Test
    public void testTC3_BUB_row() {
        context.checking(new Expectations() {{
            allowing(values).getColumnCount(); will(returnValue(2));
            allowing(values).getValue(2, 0); will(returnValue(5.0));
            allowing(values).getValue(2, 1); will(returnValue(6.0));
        }});
        double result = DataUtilities.calculateRowTotal(values, 2);
        assertEquals(11.0, result, 1e-9);
    }

    @Test
    public void testTC4_lastRow_UB() {
        context.checking(new Expectations() {{
            allowing(values).getColumnCount(); will(returnValue(2));
            allowing(values).getValue(3, 0); will(returnValue(7.0));
            allowing(values).getValue(3, 1); will(returnValue(8.0));
        }});
        double result = DataUtilities.calculateRowTotal(values, 3);
        assertEquals(15.0, result, 1e-9);
    }

    @Test
    public void testTC5_singleElement() {
        context.checking(new Expectations() {{
            allowing(values).getColumnCount(); will(returnValue(1));
            allowing(values).getValue(0, 0); will(returnValue(5.0));
        }});
        double result = DataUtilities.calculateRowTotal(values, 0);
        assertEquals(5.0, result, 1e-9);
    }

    @Test
    public void testTC6_allZeros() {
        context.checking(new Expectations() {{
            allowing(values).getColumnCount(); will(returnValue(2));
            allowing(values).getValue(0, 0); will(returnValue(0.0));
            allowing(values).getValue(0, 1); will(returnValue(0.0));
        }});
        double result = DataUtilities.calculateRowTotal(values, 0);
        assertEquals(0.0, result, 1e-9);
    }

    @Test
    public void testTC7_negativeValues() {
        context.checking(new Expectations() {{
            allowing(values).getColumnCount(); will(returnValue(2));
            allowing(values).getValue(0, 0); will(returnValue(-1.0));
            allowing(values).getValue(0, 1); will(returnValue(-2.0));
        }});
        double result = DataUtilities.calculateRowTotal(values, 0);
        assertEquals(-3.0, result, 1e-9);
    }

    @Test
    public void testTC8_mixedPositiveNegative() {
        context.checking(new Expectations() {{
            allowing(values).getColumnCount(); will(returnValue(2));
            allowing(values).getValue(1, 0); will(returnValue(3.0));
            allowing(values).getValue(1, 1); will(returnValue(-3.0));
        }});
        double result = DataUtilities.calculateRowTotal(values, 1);
        assertEquals(0.0, result, 1e-9);
    }

    // null data → ParamChecks.nullNotPermitted() throws IllegalArgumentException
    @Test(expected = IllegalArgumentException.class)
    public void testTC9_nullData() {
        DataUtilities.calculateRowTotal(null, 0);
    }

    // Out-of-bounds row: no bounds check in the method.
    // getValue is still called per column; returning null skips addition → 0.0
    @Test
    public void testTC10_rowBelowLowerBound() {
        context.checking(new Expectations() {{
            allowing(values).getColumnCount(); will(returnValue(2));
            allowing(values).getValue(-1, 0); will(returnValue(null));
            allowing(values).getValue(-1, 1); will(returnValue(null));
        }});
        double result = DataUtilities.calculateRowTotal(values, -1);
        assertEquals(0.0, result, 1e-9);
    }

    @Test
    public void testTC11_rowAboveUpperBound() {
        context.checking(new Expectations() {{
            allowing(values).getColumnCount(); will(returnValue(2));
            allowing(values).getValue(5, 0); will(returnValue(null));
            allowing(values).getValue(5, 1); will(returnValue(null));
        }});
        double result = DataUtilities.calculateRowTotal(values, 5);
        assertEquals(0.0, result, 1e-9);
    }

    @Test
    public void testTC12_NaNValue() {
        context.checking(new Expectations() {{
            allowing(values).getColumnCount(); will(returnValue(2));
            allowing(values).getValue(0, 0); will(returnValue(Double.NaN));
            allowing(values).getValue(0, 1); will(returnValue(1.0));
        }});
        double result = DataUtilities.calculateRowTotal(values, 0);
        assertTrue(Double.isNaN(result));
    }

    @Test
    public void testTC13_infinityValue() {
        context.checking(new Expectations() {{
            allowing(values).getColumnCount(); will(returnValue(2));
            allowing(values).getValue(0, 0); will(returnValue(Double.POSITIVE_INFINITY));
            allowing(values).getValue(0, 1); will(returnValue(1.0));
        }});
        double result = DataUtilities.calculateRowTotal(values, 0);
        assertEquals(Double.POSITIVE_INFINITY, result, 0.0);
    }

    @Test
    public void testTC14_overflow_MAX_VALUE() {
        context.checking(new Expectations() {{
            allowing(values).getColumnCount(); will(returnValue(2));
            allowing(values).getValue(0, 0); will(returnValue(Double.MAX_VALUE));
            allowing(values).getValue(0, 1); will(returnValue(Double.MAX_VALUE));
        }});
        double result = DataUtilities.calculateRowTotal(values, 0);
        assertEquals(Double.POSITIVE_INFINITY, result, 0.0);
    }
    
	 @Test
	 public void testTC15_nullCellValue() {
	     context.checking(new Expectations() {{
	         allowing(values).getRowCount(); will(returnValue(1));
	         allowing(values).getColumnCount(); will(returnValue(2));
	         allowing(values).getValue(0, 0); will(returnValue(null));   // null branch
	         allowing(values).getValue(0, 1); will(returnValue(3.0));
	     }});
	
	     double result = DataUtilities.calculateRowTotal(values, 0);
	     assertEquals(3.0, result, 1e-9); // null cell is skipped
	 }
	
	 @Test
	 public void testTC16_allNullCellValues() {
	     context.checking(new Expectations() {{
	         allowing(values).getRowCount(); will(returnValue(1));
	         allowing(values).getColumnCount(); will(returnValue(2));
	         allowing(values).getValue(0, 0); will(returnValue(null));   // null branch
	         allowing(values).getValue(0, 1); will(returnValue(null));   // null branch
	     }});
	
	     double result = DataUtilities.calculateRowTotal(values, 0);
	     assertEquals(0.0, result, 1e-9); // all nulls skipped, total stays 0
	 }
	
	 @Test
	 public void testTC18_negativeColumnCount_nullValue() {
	     context.checking(new Expectations() {{
	         allowing(values).getRowCount();    will(returnValue(1));
	         allowing(values).getColumnCount(); will(returnValue(-1)); // makes c2(0) > columnCount(-1) TRUE
	         allowing(values).getValue(0, 0);   will(returnValue(null)); // null: covers inner if == false
	     }});
	
	     double result = DataUtilities.calculateRowTotal(values, 0);
	     assertEquals(0.0, result, 1e-9); // null skipped, total stays 0
	 }
}