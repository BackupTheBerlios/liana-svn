package org.papernapkin.liana.util;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Utility class for dealing with double native type values and Double Objects.
 *
 * @author Philip A. Chapman
 */
public class DoubleUtil
{
	/**
	 * This method retrieves a Double value from the resultset at the given
	 * column.  If the column's value is null, null is returned, else a new
	 * Double object containing the double value is returned.
	 * @param result The result from which the value is to be retrieved.
	 * @param columnIndex The index of the column from which the value is to
	 *                    be retrieved.
	 * @return If the column's value is null, null; else a new Double object
	 *         containing the double value in the column.
	 * @throws SQLException Indicates an error retrieving the data from the
	 *         ResultSet.
	 */
	public static Double getNullableDouble(ResultSet result, int columnIndex)
		throws SQLException
	{
		double d = result.getDouble(columnIndex);
		if (result.wasNull()) {
			return null;
		} else {
			return new Double(d);
		}
	}	

	/**
	 * This method uses java.lang.Double.parseDouble(String) to parse the
	 * string value into a double.  The difference is that if a
	 * NumberFormatException is caught, 0 is returned. 
	 * @param str The string to return.
	 * @return The double value, or 0 if the string cannot be parsed into a
	 *         double.
	 */
    public static double parseDoubleSafe(String str) {
	// Initialize
	double d = (double)0;

	// Try to parse the string
	try {
	    d = Double.valueOf(str).doubleValue();
	} catch (NumberFormatException e) {}

	// Return the value.  Notice that if a Number Format Exception was 
	// caught, (double)0 is returned.
	return d;
    }
}
