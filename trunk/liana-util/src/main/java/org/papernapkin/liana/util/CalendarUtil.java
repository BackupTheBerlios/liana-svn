package org.papernapkin.liana.util;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Utility class for dealing with dates and times.
 *
 * @author Philip A. Chapman
 */
public class CalendarUtil
{
	/**
	 * Uses todays date to create a new calendar that holds only the date
	 * information, not time.  
	 * @return Today's date with no time component.
	 */
	public static Calendar createNewDate()
	{
		Calendar cal = Calendar.getInstance();
		if (cal instanceof GregorianCalendar) {
			return new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
		} else {
			return cal;
		}
	}
	
	public static Calendar createNewCalendar(java.util.Date date)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}
	
	/**
	 * Attempts to retrive a date value form the resultset at the given
	 * column.  If the value is null, an SQLException is thrown.
	 * @param result The resultset from which to get the value.
	 * @param columnIndex The index of the column in which the data resides.
	 * @return The Calendar object with the date data.
	 * @throws SQLException Indicates an error retrieving the data.
	 */
	public static Calendar getNonNullableDate(CallableStatement stmt, int columnIndex)
		throws SQLException
	{
		Calendar cal = getNullableDate(stmt, columnIndex);
		if (cal == null) {
			throw new SQLException("The column " + columnIndex + " does not contain a date value.");
		}
		return cal;
	}
	
	/**
	 * Attempts to retrive a date value form the resultset at the given
	 * column.  If the value is null, an SQLException is thrown.
	 * @param result The resultset from which to get the value.
	 * @param columnIndex The index of the column in which the data resides.
	 * @return The Calendar object with the date data.
	 * @throws SQLException Indicates an error retrieving the data.
	 */
	public static Calendar getNonNullableDate(ResultSet result, int columnIndex)
		throws SQLException
	{
		Calendar cal = getNullableDate(result, columnIndex);
		if (cal == null) {
			throw new SQLException("The column " + columnIndex + " does not contain a date value.");
		}
		return cal;
	}
	
	/**
	 * Attempts to retrive a time value form the resultset at the given
	 * column.  If the value is null, an SQLException is thrown.
	 * @param result The resultset from which to get the value.
	 * @param columnIndex The index of the column in which the data resides.
	 * @return The Calendar object with the date data.
	 * @throws SQLException Indicates an error retrieving the data.
	 */
	public static Calendar getNonNullableTime(ResultSet result, int columnIndex)
		throws SQLException
	{
		Calendar cal = getNullableTime(result, columnIndex);
		if (cal == null) {
			throw new SQLException("The column " + columnIndex + " does not contain a date value.");
		}
		return cal;
	}
	
	/**
	 * Attempts to retrive a date time value form the resultset at the given
	 * column.  If the value is null, an SQLException is thrown.
	 * @param result The resultset from which to get the value.
	 * @param columnIndex The index of the column in which the data resides.
	 * @return The Calendar object with the date data.
	 * @throws SQLException Indicates an error retrieving the data.
	 */
	public static Calendar getNonNullableTimestamp(ResultSet result, int columnIndex)
		throws SQLException
	{
		Calendar cal = getNullableTimestamp(result, columnIndex);
		if (cal == null) {
			throw new SQLException("The column " + columnIndex + " does not contain a date value.");
		}
		return cal;
	}
	
	/**
	 * This method retrieves a date value from the resultset at the given
	 * column.  If the column's value is null, null is returned, else a new
	 * Calendar object containing the date value is returned.
	 * @param result The result from which the value is to be retrieved.
	 * @param columnIndex The index of the column from which the value is to
	 *                    be retrieved.
	 * @return If the column's value is null, null; else a new Calendar object
	 *         containing the date value in the column.
	 * @throws SQLException Indicates an error retrieving the data from the
	 *         ResultSet.
	 */
	public static Calendar getNullableDate(ResultSet result, int columnIndex)
		throws SQLException
	{
		java.util.Date data = result.getDate((columnIndex));
		if (result.wasNull()) {
			return null;
		} else {
			Calendar cal = Calendar.getInstance();
			cal.setTime(data);
			return cal;
		}
	}	

	/**
	 * This method retrieves a date value from the callable statement at the
	 * given parameter.  If the parameter's value is null, null is returned,
	 * else a new Calendar object containing the date value is returned.
	 * @param stmt The callable statement from which the value is to be
	 *             retrieved.
	 * @param paramIndex The index of the parameter from which the value is to
	 *                   be retrieved.
	 * @return If the parameter's value is null, null; else a new Calendar
	 *         object containing the date value in the parameter.
	 * @throws SQLException Indicates an error retrieving the data from the
	 *         CallableStatement.
	 */
	public static Calendar getNullableDate(CallableStatement stmt, int paramIndex)
		throws SQLException
	{
		java.util.Date date = stmt.getDate(paramIndex);
		if (stmt.wasNull()) {
			return null;
		} else {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			return cal;
		}
	}

	/**
	 * This method retrieves a time value from the resultset at the given
	 * column.  If the column's value is null, null is returned, else a new
	 * Calendar object containing the date value is returned.
	 * @param result The result from which the value is to be retrieved.
	 * @param columnIndex The index of the column from which the value is to
	 *                    be retrieved.
	 * @return If the column's value is null, null; else a new Calendar object
	 *         containing the date value in the column.
	 * @throws SQLException Indicates an error retrieving the data from the
	 *         ResultSet.
	 */
	public static Calendar getNullableTime(ResultSet result, int columnIndex)
		throws SQLException
	{
		java.util.Date data = result.getTime((columnIndex));
		if (result.wasNull()) {
			return null;
		} else {
			Calendar cal = Calendar.getInstance();
			cal.setTime(data);
			return cal;
		}
	}	

	/**
	 * This method retrieves a time value from the callable statement at the
	 * given parameter.  If the parameter's value is null, null is returned,
	 * else a new Calendar object containing the date value is returned.
	 * @param stmt The callable statement from which the value is to be
	 *             retrieved.
	 * @param paramIndex The index of the parameter from which the value is to
	 *                   be retrieved.
	 * @return If the parameter's value is null, null; else a new Calendar
	 *         object containing the date value in the parameter.
	 * @throws SQLException Indicates an error retrieving the data from the
	 *         CallableStatement.
	 */
	public static Calendar getNullableTime(CallableStatement stmt, int paramIndex)
		throws SQLException
	{
		java.util.Date date = stmt.getTime(paramIndex);
		if (stmt.wasNull()) {
			return null;
		} else {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			return cal;
		}
	}
	
	/**
	 * This method retrieves a date and time value from the resultset at the
	 * given column.  If the column's value is null, null is returned, else a
	 * new Calendar object containing the date and time value is returned.
	 * @param result The result from which the value is to be retrieved.
	 * @param columnIndex The index of the column from which the value is to
	 *                    be retrieved.
	 * @return If the column's value is null, null; else a new Calendar object
	 *         containing the date value in the column.
	 * @throws SQLException Indicates an error retrieving the data from the
	 *         ResultSet.
	 */
	public static Calendar getNullableTimestamp(ResultSet result, int columnIndex)
		throws SQLException
	{
		java.util.Date data = result.getTimestamp((columnIndex));
		if (result.wasNull()) {
			return null;
		} else {
			Calendar cal = Calendar.getInstance();
			cal.setTime(data);
			return cal;
		}
	}	

	/**
	 * This method retrieves a date and time value from the callable statement
	 * at the given parameter.  If the parameter's value is null, null is
	 * returned, else a new Calendar object containing the date and time value
	 * is returned.
	 * @param stmt The callable statement from which the value is to be
	 *             retrieved.
	 * @param paramIndex The index of the parameter from which the value is to
	 *                   be retrieved.
	 * @return If the parameter's value is null, null; else a new Calendar
	 *         object containing the date value in the parameter.
	 * @throws SQLException Indicates an error retrieving the data from the
	 *         CallableStatement.
	 */
	public static Calendar getNullableTimestamp(CallableStatement stmt, int paramIndex)
		throws SQLException
	{
		java.util.Date date = stmt.getTimestamp(paramIndex);
		if (stmt.wasNull()) {
			return null;
		} else {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			return cal;
		}
	}

	/**
     * Sets the parameter in the statement with a date value, or null if
     * the Calendar is null.
     * @param stmt The statement in which the parameter value is to be set.
     * @param paramIndex The index of the parameter to set.
     * @param cal The value to set (or null).
     * @throws SQLException Indicates an error setting the parameter.
     */
    public static void setNullableDate(PreparedStatement stmt, int paramIndex, Calendar cal)
    	throws SQLException
    {
    	if (cal == null) {
    		stmt.setNull(paramIndex, Types.DATE);
    	} else {
    		stmt.setDate(paramIndex, new java.sql.Date(cal.getTime().getTime()));
    	}
    }

    /**
     * Sets the parameter in the statement with a time value, or null if
     * the Calendar is null.
     * @param stmt The statement in which the parameter value is to be set.
     * @param paramIndex The index of the parameter to set.
     * @param cal The value to set (or null).
     * @throws SQLException Indicates an error setting the parameter.
     */
    public static void setNullableTime(PreparedStatement stmt, int paramIndex, Calendar cal)
    	throws SQLException
    {
    	if (cal == null) {
    		stmt.setNull(paramIndex, Types.TIME);
    	} else {
    		stmt.setTime(paramIndex, new java.sql.Time(cal.getTime().getTime()));
    	}
    }

    /**
     * Sets the parameter in the statement with a timestamp (date and time)
     * value, or null if the Calendar is null.
     * @param stmt The statement in which the parameter value is to be set.
     * @param paramIndex The index of the parameter to set.
     * @param cal The value to set (or null).
     * @throws SQLException Indicates an error setting the parameter.
     */
    public static void setNullableTimeStamp(PreparedStatement stmt, int paramIndex, Calendar cal)
    	throws SQLException
    {
    	if (cal == null) {
    		stmt.setNull(paramIndex, Types.TIMESTAMP);
    	} else {
    		stmt.setTimestamp(paramIndex, new java.sql.Timestamp(cal.getTime().getTime()));
    	}
    }
    
    public static Calendar getWeekEnd(Calendar cal)
    {
    	Calendar c = getWeekStart(cal);
    	c.add(Calendar.DAY_OF_WEEK, 6);
    	return c;
    }
    
    public static Calendar getWeekEnd(Date date)
    {
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(date);
    	return getWeekEnd(cal);
    }
    
    public static Calendar getWeekEnd(Integer weekOfYear)
    {
    	return getWeekEnd(weekOfYear.intValue());
    }
    
    public static Calendar getWeekEnd(Integer weekOfYear, Integer year)
    {
    	return getWeekEnd(weekOfYear.intValue(), year.intValue());
    }
    
    public static Calendar getWeekEnd(int weekOfYear)
    {
    	return getWeekEnd(weekOfYear, Calendar.getInstance().get(Calendar.YEAR));
    }
    
    public static Calendar getWeekEnd(int weekOfYear, int year)
    {
    	Calendar cal = Calendar.getInstance();
     	cal.set(Calendar.MONTH, Calendar.JANUARY);
    	cal.set(Calendar.DAY_OF_MONTH, cal.getMinimalDaysInFirstWeek());
     	cal.set(Calendar.YEAR, year);
     	cal = getWeekEnd(cal);
     	cal.add(Calendar.WEEK_OF_YEAR, weekOfYear - 1);
    	return cal;
    }
    
    public static Calendar getWeekStart(Calendar cal)
    {
    	Calendar c = (Calendar)cal.clone();
    	c.add(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() - c.get(Calendar.DAY_OF_WEEK));
     	c.set(Calendar.HOUR, 0);
     	c.set(Calendar.MINUTE, 0);
     	c.set(Calendar.MILLISECOND, 0);
    	return c;
    }
    
    public static Calendar getWeekStart(Date date)
    {
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(date);
    	return getWeekStart(cal);
    }
    
    public static Calendar getWeekStart(Integer weekOfYear)
    {
    	return getWeekStart(weekOfYear.intValue());
    }
    
    public static Calendar getWeekStart(Integer weekOfYear, Integer year)
    {
    	return getWeekStart(weekOfYear.intValue(), year.intValue());
    }
    
    public static Calendar getWeekStart(int weekOfYear)
    {
    	return getWeekStart(weekOfYear, Calendar.getInstance().get(Calendar.YEAR));
    }
    
    public static Calendar getWeekStart(int weekOfYear, int year)
    {
    	Calendar cal = Calendar.getInstance();
     	cal.set(Calendar.MONTH, Calendar.JANUARY);
    	cal.set(Calendar.DAY_OF_MONTH, cal.getMinimalDaysInFirstWeek());
     	cal.set(Calendar.YEAR, year);
     	cal = getWeekStart(cal);
     	cal.add(Calendar.WEEK_OF_YEAR, weekOfYear - 1);
     	return cal;
    }
    
    public static final void main(String[] args) {
    	DateFormat df = DateFormat.getDateTimeInstance();
    	Calendar cal = Calendar.getInstance();
    	System.out.println(
    		"Current date and time: " + df.format(cal.getTime())
    	);
    	System.out.println(
    		"Week Start date and time: " +
    		df.format(getWeekStart(cal).getTime())
    	);
    	System.out.println(
    		"Week End date and time: " +
    		df.format(getWeekEnd(cal).getTime())
    	);
    	for (int i = 1; i < 10; i++) {
        	System.out.println(
        		"Week: " + i +
        		"\tStart date and time: " +
        		df.format(getWeekStart(i).getTime()) +
        		"\tEnd date and time: " +
        		df.format(getWeekEnd(i).getTime())
        	);
    	}
    	System.exit(0);
    }
}
