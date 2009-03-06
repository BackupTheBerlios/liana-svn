package org.papernapkin.liana.util;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Utility class for dealing with short native type values and Short Objects.
 *
 * @author Philip A. Chapman
 */
public class ShortUtil
{
	/**
	 * This method retrieves a short value from the resultset at the given
	 * column.  If the column's value is null, null is returned, else a new
	 * Short object containing the short value is returned.
	 * @param result The result from which the value is to be retrieved.
	 * @param columnIndex The index of the column from which the value is to
	 *                    be retrieved.
	 * @return If the column's value is null, null; else a new Short object
	 *         containing the short value in the column.
	 * @throws SQLException Indicates an error retrieving the data from the
	 *         ResultSet.
	 */
	public static Short getNullableShort(ResultSet result, int columnIndex)
		throws SQLException
	{
		short s = result.getShort(columnIndex);
		if (result.wasNull()) {
			return null;
		} else {
			return new Short(s);
		}
	}
	
	/**
	 * This method uses java.lang.Short.parseShort(String) to parse the string
	 * value into a double.  The difference is that if a NumberFormatException
	 * is caught, 0 is returned. 
	 * @param str The string to parse.
	 * @return The short value, or 0 if the string could not be parsed.
	 */
    public static short parseShortSafe(String str)
    {
		// Initialize
		short s = (short)0;
	
		// Try to parse the string
		try {
		    s = Short.valueOf(str).shortValue();
		} catch (NumberFormatException e) {}
	
		// Return the value.  Notice that if a Number Format Exception was 
		// caught, (short)0 is returned.
		return s;
    }
}
