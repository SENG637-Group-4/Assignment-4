package org.jfree.data.datautilitiestestbefore;

import static org.junit.Assert.*;
import java.security.InvalidParameterException;

import org.jfree.data.Values2D;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;
import org.jfree.data.DataUtilities;


public class DataUtilitiesCalculateColumnTotalTest {

    private Mockery context;
    private Values2D values;

    @Before
    public void setUp() {
        context = new Mockery();
        values = context.mock(Values2D.class);
    }

      
    // TC1 – LB column (0)
     
    @Test
    public void testTC1_firstColumn() {
        context.checking(new Expectations() {{
            allowing(values).getRowCount(); will(returnValue(2));
            allowing(values).getValue(0, 0); will(returnValue(1.0));
            allowing(values).getValue(1, 0); will(returnValue(3.0));
        }});

        double result = DataUtilities.calculateColumnTotal(values, 0);
        assertEquals(4.0, result, 1e-9);
    }

      
    // TC2 – UB column
      
    @Test
    public void testTC2_lastColumn() {
        context.checking(new Expectations() {{
            allowing(values).getRowCount(); will(returnValue(2));
            allowing(values).getValue(0, 1); will(returnValue(2.0));
            allowing(values).getValue(1, 1); will(returnValue(4.0));
        }});

        double result = DataUtilities.calculateColumnTotal(values, 1);
        assertEquals(6.0, result, 1e-9);
    }

      
    // TC3 – NOM column (3x3 table)
      
    @Test
    public void testTC3_middleColumn() {
        context.checking(new Expectations() {{
            allowing(values).getRowCount(); will(returnValue(3));
            allowing(values).getValue(0, 1); will(returnValue(2.0));
            allowing(values).getValue(1, 1); will(returnValue(5.0));
            allowing(values).getValue(2, 1); will(returnValue(8.0));
        }});

        double result = DataUtilities.calculateColumnTotal(values, 1);
        assertEquals(15.0, result, 1e-9);
    }

      
    // TC5 – Single row
      
    @Test
    public void testTC5_singleRow() {
        context.checking(new Expectations() {{
            allowing(values).getRowCount(); will(returnValue(1));
            allowing(values).getValue(0, 0); will(returnValue(5.0));
        }});

        double result = DataUtilities.calculateColumnTotal(values, 0);
        assertEquals(5.0, result, 1e-9);
    }

      
    // TC6 – Single column
      
    @Test
    public void testTC6_singleColumn() {
        context.checking(new Expectations() {{
            allowing(values).getRowCount(); will(returnValue(3));
            allowing(values).getValue(0, 0); will(returnValue(2.0));
            allowing(values).getValue(1, 0); will(returnValue(3.0));
            allowing(values).getValue(2, 0); will(returnValue(4.0));
        }});

        double result = DataUtilities.calculateColumnTotal(values, 0);
        assertEquals(9.0, result, 1e-9);
    }

      
    // TC7 – Empty table (0 rows)
      
    @Test
    public void testTC7_emptyTable() {
        context.checking(new Expectations() {{
            allowing(values).getRowCount(); will(returnValue(0));
        }});

        double result = DataUtilities.calculateColumnTotal(values, 0);
        assertEquals(0.0, result, 1e-9);
    }

      
    // TC8 – All null values
      
    @Test
    public void testTC8_allNullColumn() {
        context.checking(new Expectations() {{
            allowing(values).getRowCount(); will(returnValue(2));
            allowing(values).getValue(0, 0); will(returnValue(null));
            allowing(values).getValue(1, 0); will(returnValue(null));
        }});

        double result = DataUtilities.calculateColumnTotal(values, 0);
        assertEquals(0.0, result, 1e-9);
    }

      
    // TC9 – Some null values
      
    @Test
    public void testTC9_someNullValues() {
        context.checking(new Expectations() {{
            allowing(values).getRowCount(); will(returnValue(2));
            allowing(values).getValue(0, 0); will(returnValue(null));
            allowing(values).getValue(1, 0); will(returnValue(4.0));
        }});

        double result = DataUtilities.calculateColumnTotal(values, 0);
        assertEquals(4.0, result, 1e-9);
    }

      
    // TC10 – All zeros
      
    @Test
    public void testTC10_allZeroColumn() {
        context.checking(new Expectations() {{
            allowing(values).getRowCount(); will(returnValue(2));
            allowing(values).getValue(0, 0); will(returnValue(0.0));
            allowing(values).getValue(1, 0); will(returnValue(0.0));
        }});

        double result = DataUtilities.calculateColumnTotal(values, 0);
        assertEquals(0.0, result, 1e-9);
    }

      
    // TC12 – All negative values
      
    @Test
    public void testTC12_allNegative() {
        context.checking(new Expectations() {{
            allowing(values).getRowCount(); will(returnValue(2));
            allowing(values).getValue(0, 0); will(returnValue(-2.0));
            allowing(values).getValue(1, 0); will(returnValue(-4.0));
        }});

        double result = DataUtilities.calculateColumnTotal(values, 0);
        assertEquals(-6.0, result, 1e-9);
    }

      
    // TC14 – Null data (real behavior = NullPointerException)
      
    @Test(expected = IllegalArgumentException.class)
    public void testTC14_nullData() {
        DataUtilities.calculateColumnTotal(null, 0);
    }
	
	 // TC_NEG2 – Negative row count: enters second loop, getValue returns null
	 // Covers: second loop condition (true), n != null (false branch)
	 // NOTE: This test will hang due to infinite loop bug in implementation
	 @Test(timeout = 1000)
	 public void testNegativeRowCount_nullValue_returnsZero() {
	     context.checking(new Expectations() {{
	         allowing(values).getRowCount(); will(returnValue(-1));
	         allowing(values).getValue(with(any(Integer.class)), with(equal(0)));
	             will(returnValue(null));
	     }});
	
	     double result = DataUtilities.calculateColumnTotal(values, 0);
	     assertEquals(0.0, result, 1e-9);
	 }
}