package com.ethanzeigler.simplestocks;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by Ethan on 1/8/17.
 */
public class StockTableModel implements TableModel, Printable {
    // Symbol Name   Worth Percent Change
    public static final int COLUMNS = 5;
    public static final int PRINT_COLUMNS = 3;
    public static final int PRINT_COLUMN_SPACER = 100;
    public static final int PRINT_MARGIN_TOP = 50;
    public static final int PRINT_MARGIN_LEFT = 30;
    public static final int PRINT_LINE_SPACING = 22;

    private List<Stock> stocks;

    public StockTableModel() {

        stocks = new ArrayList<Stock>();
    }

    /**
     * Returns the number of rows in the model. A
     * <code>JTable</code> uses this method to determine how many rows it
     * should display.  This method should be quick, as it
     * is called frequently during rendering.
     *
     * @return the number of rows in the model
     * @see #getColumnCount
     */
    public int getRowCount() {
        return stocks.size();
    }

    /**
     * Returns the number of columns in the model. A
     * <code>JTable</code> uses this method to determine how many columns it
     * should create and display by default.
     *
     * @return the number of columns in the model
     * @see #getRowCount
     */
    public int getColumnCount() {
        return COLUMNS;
    }

    /**
     * Returns the name of the column at <code>columnIndex</code>.  This is used
     * to initialize the table's column header name.  Note: this name does
     * not need to be unique; two columns in a table can have the same name.
     *
     * @param columnIndex the index of the column
     * @return the name of the column
     */
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0: return "Symbol";
            case 1: return "Name";
            case 2: return "Price";
            case 3: return "Today's Change";
            case 4: return "Today's Change Percent";
            default: return null;
        }
    }

    /**
     * Returns the most specific superclass for all the cell values
     * in the column.  This is used by the <code>JTable</code> to set up a
     * default renderer and editor for the column.
     *
     * @param columnIndex the index of the column
     * @return the common ancestor class of the object values in the model.
     */
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4: return String.class;
            default: return null;
        }
    }

    /**
     * Returns true if the cell at <code>rowIndex</code> and
     * <code>columnIndex</code>
     * is editable.  Otherwise, <code>setValueAt</code> on the cell will not
     * change the value of that cell.
     *
     * @param rowIndex    the row whose value to be queried
     * @param columnIndex the column whose value to be queried
     * @return true if the cell is editable
     * @see #setValueAt
     */
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    /**
     * Returns the value for the cell at <code>columnIndex</code> and
     * <code>rowIndex</code>.
     *
     * @param rowIndex    the row whose value is to be queried
     * @param columnIndex the column whose value is to be queried
     * @return the value Object at the specified cell
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex > stocks.size() - 1) {
            throw new IndexOutOfBoundsException("Row: " + rowIndex);
        }
        if (columnIndex > COLUMNS -1) {
            throw new IndexOutOfBoundsException("Column: " + columnIndex);
        }

        Stock stock = stocks.get(rowIndex);
        try {
            switch (columnIndex) {
                case 0:
                    return stock.getSymbol();                           // symbol
                case 1:
                    return stock.getName();                             // name
                case 2:
                    return stock.getQuote().getPrice();                 // price
                case 3:
                    String pointChange = stock.getQuote().getChange().toString();      // change since open
                    if (!pointChange.startsWith("-")) {
                        return "+" + pointChange.toString();
                    } else {
                        return pointChange.toString();
                    }
                case 4:
                    String percentChange = stock.getQuote().getChangeInPercent().toString(); // change percent
                    if (!percentChange.startsWith("-")) {
                        return "+" + percentChange.toString();
                    } else {
                        return percentChange.toString();
                    }
                default:
                    return null;
            }
        } catch (Exception e) {
            return "Unknown";
        }
    }

    /**
     * Sets the value in the cell at <code>columnIndex</code> and
     * <code>rowIndex</code> to <code>aValue</code>.
     *
     * @param aValue      the new value
     * @param rowIndex    the row whose value is to be changed
     * @param columnIndex the column whose value is to be changed
     * @see #getValueAt
     * @see #isCellEditable
     */
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

    }

    /**
     * Adds a listener to the list that is notified each time a change
     * to the data model occurs.
     *
     * @param l the TableModelListener
     */
    public void addTableModelListener(TableModelListener l) {

    }

    /**
     * Removes a listener from the list that is notified each time a
     * change to the data model occurs.
     *
     * @param l the TableModelListener
     */
    public void removeTableModelListener(TableModelListener l) {

    }

    public void update() {
        for (Stock stock : stocks) {
            try {
                stock.getQuote(true);
            } catch (IOException e) {
                System.out.println("Error: could not update stock");
            }
        }
    }

    public boolean addStock(String symbol) {
        try {
            stocks.add(YahooFinance.get(symbol));
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    /**
     * Prints the page at the specified index into the specified
     * {@link Graphics} context in the specified
     * format.  A <code>PrinterJob</code> calls the
     * <code>Printable</code> interface to request that a page be
     * rendered into the context specified by
     * <code>graphics</code>.  The format of the page to be drawn is
     * specified by <code>pageFormat</code>.  The zero based index
     * of the requested page is specified by <code>pageIndex</code>.
     * If the requested page does not exist then this method returns
     * NO_SUCH_PAGE; otherwise PAGE_EXISTS is returned.
     * The <code>Graphics</code> class or subclass implements the
     * information.  If the <code>Printable</code> object
     * aborts the print job then it throws a {@link PrinterException}.
     *
     * @param graphics   the context into which the page is drawn
     * @param pageFormat the size and orientation of the page being drawn
     * @param pageIndex  the zero based index of the page to be drawn
     * @return PAGE_EXISTS if the page is rendered successfully
     * or NO_SUCH_PAGE if <code>pageIndex</code> specifies a
     * non-existent page.
     * @throws PrinterException thrown when the print job is terminated.
     */
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex == 0) {
            int line = 0; // current line writing to
            int column = 0;

            graphics.setFont(new Font("Times New Roman", Font.BOLD, 18));

            graphics.drawString("Stock prices: "
                    + new SimpleDateFormat("MM/dd/yy h:mm a").format(new Date(System.currentTimeMillis())),
                    PRINT_MARGIN_LEFT, PRINT_MARGIN_TOP);

            line += 2;

            for (Stock stock : stocks) {
                // draw symbol, current price
                graphics.drawString(
                        String.format("%s:\t%s", stock.getSymbol(), stock.getQuote().getPrice().toString()),
                        PRINT_MARGIN_LEFT, (line * PRINT_LINE_SPACING) + PRINT_MARGIN_TOP);
                line++;
            }

            return PAGE_EXISTS;
        } else {
            return NO_SUCH_PAGE;
        }
    }
}
