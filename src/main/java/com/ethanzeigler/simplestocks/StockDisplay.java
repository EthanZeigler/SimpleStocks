package com.ethanzeigler.simplestocks;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

/**
 * Created by Ethan on 1/8/17.
 */
public class StockDisplay {
    private JPanel mainPanel;
    private JButton printButton;
    private JTable table;
    private StockTableModel model;

    public StockDisplay() {
        printButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            public void actionPerformed(ActionEvent e) {
                startPrintJob();
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("StockDisplay");
        frame.setContentPane(new StockDisplay().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        String[] stocks = {"VWIAX", "FKINX", "VBIAX", "VMMXX", "VBLTX", "VWIGX", "VASVX", "VBIIX", "VWETX", "VTMGX",
                            "VEMAX", "VIMAX", "VSMAX"};
        model = new StockTableModel();
        for (String str: stocks) {
            model.addStock(str);
        }

        table = new JTable(model);
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        table.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
    }

    private void startPrintJob() {
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        printerJob.setPrintable(model);
        if (printerJob.printDialog()) {
            try {
                printerJob.print();
            } catch (PrinterException e) {
                System.out.println("Print cancelled");
            }
        }
    }
}
