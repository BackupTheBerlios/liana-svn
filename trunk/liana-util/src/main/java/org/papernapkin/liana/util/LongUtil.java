package org.papernapkin.liana.util;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Utility class for dealing with long native type values and Long Objects.
 *
 * @author Philip A. Chapman
 */
public class LongUtil
{
	/**
	 * This method retrieves a Long value from the resultset at the given
	 * column.  If the column's value is null, null is returned, else a new
	 * long object containing the long value is returned.
	 * @param result The result from which the value is to be retrieved.
	 * @param columnIndex The index of the column from which the value is to
	 *                    be retrieved.
	 * @return If the column's value is null, null; else a new Long object
	 *         containing the long value in the column.
	 * @throws SQLException Indicates an error retrieving the data from the
	 *         ResultSet.
	 */
	public static Long getNullableLong(ResultSet result, int columnIndex)
		throws SQLException
	{
		long l = result.getLong(columnIndex);
		if (result.wasNull()) {
			return null;
		} else {
			return new Long(l);
		}
	}	

	/**
	 * This method retrieves a Long value from the callable statement at
	 * the given parameter.  If the parameter's value is null, null is
	 * returned, else a new Long object containing the long value is
	 * returned.
	 * @param stmt The callable statement from which the value is to be
	 *             retrieved.
	 * @param paramIndex The index of the parameter from which the value is to
	 *                   be retrieved.
	 * @return If the parameter's value is null, null; else a new Long
	 *         object containing the long value in the parameter.
	 * @throws SQLException Indicates an error retrieving the data from the
	 *         CallableStatement.
	 */
	public static Long getNullableLong(CallableStatement stmt, int paramIndex)
		throws SQLException
	{
		long i = stmt.getLong(paramIndex);
		if (stmt.wasNull()) {
			return null;
		} else {
			return new Long(i);
		}
	}
	
	/**
	 * This method uses java.lang.Long.parseLong(String) to parse the
	 * string value into a long.  The difference is that if a
	 * NumberFormatException is caught, 0 is returned. 
	 * @param str The string to return.
	 * @return The long value, or 0 if the string cannot be parsed into a
	 *         long.
	 */
    public static long parseLongSafe(String str) {
		// Initialize
		long i = (long)0;
	
		// Try to parse the string
		try {
		    i = Long.valueOf(str).longValue();
		} catch (NumberFormatException e) {}
	
		// Return the value.  Notice that if a Number Format Exception was 
		// caught, (long)0 is returned.
		return i;
    }

    /**
     * Sets the parameter in the statement with an long value, or null if
     * the long is null.
     * @param stmt The statement in which the parameter value is to be set.
     * @param paramIndex The index of the parameter to set.
     * @param i The value to set (or null).
     * @throws SQLException Indicates an error setting the parameter.
     */
    public static void setNullableLong(PreparedStatement stmt, int paramIndex, Long l)
    	throws SQLException
    {
    	if (l == null) {
    		stmt.setNull(paramIndex, Types.BIGINT);
    	} else {
    		stmt.setLong(paramIndex, l.longValue());
    	}
    }
}
