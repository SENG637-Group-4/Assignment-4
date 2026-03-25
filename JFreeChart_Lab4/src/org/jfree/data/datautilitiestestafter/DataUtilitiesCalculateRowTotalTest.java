package org.jfree.data.datautilitiestestafter;

import static org.junit.Assert.*;

import org.jfree.data.DataUtilities;
import org.jfree.data.Values2D;
import org.junit.Test;

public class DataUtilitiesCalculateRowTotalTest {

    // Simple stub class for Values2D
    static class MyValues2D implements Values2D {
        private final Number[][] data;

        public MyValues2D(Number[][] data) {
            this.data = data;
        }

        @Override
        public Number getValue(int row, int column) {
            return data[row][column];
        }

        @Override
        public int getColumnCount() {
            return data.length > 0 ? data[0].length : 0;
        }

        @Override
        public int getRowCount() {
            return data.length;
        }
    }

    /**
     * Normal case: all values present
     */
    @Test
    public void testNormalCase() {
        Number[][] data = {
            {1.0, 2.0, 3.0}
        };
        MyValues2D myData = new MyValues2D(data);
        int[] validCols = {0,1,2};

        double total = DataUtilities.calculateRowTotal(myData, 0, validCols);
        assertEquals(6.0, total, 0.0000001);
    }

    /**
     * Some values null → skipped
     */
    @Test
    public void testSomeNullValues() {
        Number[][] data = {
            {null, 2.0, null}
        };
        MyValues2D myData = new MyValues2D(data);
        int[] validCols = {0,1,2};

        double total = DataUtilities.calculateRowTotal(myData, 0, validCols);
        assertEquals(2.0, total, 0.0000001);
    }

    /**
     * Some columns out of range → skipped
     */
    @Test
    public void testColsOutOfRange() {
        Number[][] data = {
            {1.0, 2.0}
        };
        MyValues2D myData = new MyValues2D(data);
        int[] validCols = {0,1,2}; // column 2 out of range

        double total = DataUtilities.calculateRowTotal(myData, 0, validCols);
        assertEquals(3.0, total, 0.0000001);
    }

    /**
     * Empty validCols → total 0
     */
    @Test
    public void testEmptyValidCols() {
        Number[][] data = {
            {1.0, 2.0, 3.0}
        };
        MyValues2D myData = new MyValues2D(data);
        int[] validCols = {};

        double total = DataUtilities.calculateRowTotal(myData, 0, validCols);
        assertEquals(0.0, total, 0.0000001);
    }

    /**
     * Null data → exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullData() {
        DataUtilities.calculateRowTotal(null, 0, new int[]{0,1});
    }

    /**
     * colCount negative → should return 0
     */
    @Test
    public void testNegativeColCount() {
        Number[][] data = {};
        MyValues2D myData = new MyValues2D(data) {
            @Override
            public int getColumnCount() {
                return -1;
            }
        };
        int[] validCols = {0,1};

        double total = DataUtilities.calculateRowTotal(myData, 0, validCols);
        assertEquals(0.0, total, 0.0000001);
    }
}
