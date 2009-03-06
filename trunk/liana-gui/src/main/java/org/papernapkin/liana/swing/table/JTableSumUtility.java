package org.papernapkin.liana.swing.table;

import java.math.BigDecimal;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.papernapkin.liana.util.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class which will automatically sum the values of any cells that are
 * selected in an adjoining series provided that the cells contain numeric
 * values.
 * 
 * @author pchapman
 */
public class JTableSumUtility
{
	private JTable table;
	/** The table which will be watched for selections. */
	public JTable getJTable()
	{
		return table;
	}
	/** The table which will be watched for selections. */
	public JTableSumUtility setJTable(JTable table)
	{
		if (table != null && table != this.table) {
			if (this.table != null) {
				this.table.getSelectionModel().removeListSelectionListener(listener);
			}
			this.table = table;
			this.table.getSelectionModel().addListSelectionListener(listener);
		}
		return this;
	}
	
	private JLabel label;
	/** The label which will be updated with sums for selected cells. */
	public JLabel getJLabel()
	{
		return label;
	}
	/** The label which will be updated with sums for selected cells. */
	public JTableSumUtility setJLabel(JLabel label)
	{
		this.label = label;
		return this;
	}
	
	public void updateSum(int x1, int x2, int y1, int y2)
	{
		int mx = table.getModel().getRowCount() - 1;
		int my = table.getModel().getColumnCount() -1;
		if (
				x1 < 0 || x2 < 0 || y1 < 0 || y2 < 0 ||
				x1 > mx || x2 > mx || y1 > my || y2 > my
			)
		{
			new SummationThread(x1, x2, y1, y2).start();
		}
	}
	
	private ListSelectionListener listener = new ListSelectionListener()
	{
		public void valueChanged(ListSelectionEvent evt)
		{
			if (
					!evt.getValueIsAdjusting() &&
					(
						evt.getSource() == table ||
						evt.getSource() == table.getSelectionModel()
					)
				)
			{
				Integer x1 = null, x2 = null, y1 = null, y2 = null;
				if (
						table.getSelectedColumnCount() == 1 &&
						table.getSelectedRowCount() > 1
					)
				{
					int[] rows = table.getSelectedRows();
					x1 = table.getSelectedColumn();
					x2 = x1;
					y1 = rows[0];
					y2 = rows[rows.length - 1];
				} else if (
						table.getSelectedRowCount() == 1 &&
						table.getSelectedColumnCount() > 1
					)
				{
					int[] columns = table.getSelectedColumns();
					x1 = columns[0];
					x2 = columns[columns.length - 1];
					y1 = table.getSelectedRow();
					y2 = y1;
				}
				new SummationThread(x1, x2, y1, y2).start();
			}
		}
	};
	
	public JTableSumUtility()
	{
		super();
	}
	
	public JTableSumUtility(JTable table, JLabel label)
	{
		super();
		setJLabel(label);
		setJTable(table);
	}
	
	private class SummationThread extends Thread
	{
		private BigDecimal total;
		
		private Integer x1; Integer x2; Integer y1; Integer y2;
		
		SummationThread(Integer x1, Integer x2, Integer y1, Integer y2)
		{
			super();
			this.x1 = x1;
			this.x2 = x2;
			this.y1 = y1;
			this.y2 = y2;
		}
		
		private boolean add(int x, int y)
		{
			Object o = table.getValueAt(y, x);
			if (o instanceof BigDecimal) {
				total = total.add((BigDecimal)o);
			} else if (o instanceof Money) {
				total = total.add(((Money)o).getValue());
			} else {
				try {
					total = total.add(new BigDecimal(o.toString()));
				} catch (NumberFormatException nfe) {
					total = null;
					return false;
				}
			}
			return true;
		}
		
		public void run()
		{
			Logger logger = LoggerFactory.getLogger(getClass());
			if (logger.isDebugEnabled()) {
				logger.debug("Summing totals for x1: " + x1 + "\tx2: " + x2 + "\ty1: " + y1 + "\ty2: " + y2);
			}
			if (x1 == null || x2 == null || y1 == null || y2 == null) {
				total = null;
			} else {
				total = BigDecimal.ZERO;
				for (int x = x1; total != null && x <= x2; x++) {
					for (int y = y1; total != null && y <= y2; y++) {
						add(x, y);
					}
				}
			}
			UpdateRunnable r = new UpdateRunnable(label, total);
			if (SwingUtilities.isEventDispatchThread()) {
				r.run();
			} else {
				SwingUtilities.invokeLater(r);
			}
		}		
	}
	
	private class UpdateRunnable implements Runnable
	{
		private JLabel label; private BigDecimal value;
		
		UpdateRunnable(JLabel label, BigDecimal value)
		{
			this.label = label;
			this.value = value;
		}
		
		public void run()
		{
			if (value == null) {
				label.setText(" ");
			} else {
				label.setText("Sum: " + value.toString());
			}
			Logger logger = LoggerFactory.getLogger(JTableSumUtility.class);
			logger.debug("Sum for table updated");
		}
	}
}
