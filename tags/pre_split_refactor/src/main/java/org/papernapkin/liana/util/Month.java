package org.papernapkin.liana.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Month extends GregorianCalendar
{
	// CONSTANTS
	
	private static final long serialVersionUID = 1L;

    /** Instinates the Month class with month 01, year.
     * @param month The month to instinate the class with.
     * @param year The year to instinate the class with. */
    public Month(int month, int year) {
	super(month, 1, year);
    }

    /** Adds the number of months to the current value.
     * @param months The number of months to add. */
    public void addMonth(int months) {
	super.add(MONTH, months);
    }
    
    /** Adds the number of years to the current value.
     * @param years The number of years to add. */
    public void addYear(int years) {
	super.add(YEAR, years);
    }

    /** Returns the first day of the month represented by this class.
     * @return The first day of the month. */
    public Calendar getFirstDay() {
	Calendar first = (Calendar)this.clone();
	first.set(DAY_OF_MONTH, this.getMinimum(DAY_OF_MONTH));
	return first;
    }

    /** Returns the last day of the month represented by this class.
     * @return The last day of the month. */
    public Calendar getLastDay() {
	Calendar last = (Calendar)this.clone();
	last.set(DAY_OF_MONTH, this.getMaximum(DAY_OF_MONTH));
	return last;
    }

    /** Sets the month portion of the class to the given month.
     * @param month The value to set the month to. */
    public void setMonth(int month) {
	super.set(MONTH, month);
    }

    /** Sets the year portion of the class to the given year.
     * @param year The value to set the year to. */
    public void setYear(int year) {
	super.set(YEAR, year);
    }

    /** Returns a string representation of the class in mm/yyyy format.
     * @return The string representation. */
    public String toString() {
	SimpleDateFormat fmt = new SimpleDateFormat("MM/yyyy");
	return fmt.format(this.getTime());
    }
}
