package org.papernapkin.liana.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Utility methods for use with the java.lang.String class.
 * 
 * @author Philip A. Chapman
 */
public class StringUtil
{
	private static final CharSequence[][] HTML_ENTITIES = {
		{
			"&amp;", "&"	// Must be the first item in the array
		},{
			"&gt;", ">"
		},{
			"&lt;", "<"
		},{
			"&quot;", "\""
		}
	};

	private static int IDEAL_WIDTH = 80;
	
	/**
	 * Returns a string of the specified character of the specified length.
	 * 
	 * @param numChars
	 *            The length of the new string.
	 * @return A string of the specified character of the specified length.
	 */
	public static String chars(int numChars, char c) {
		StringBuffer sb = new StringBuffer(numChars);
		for (int count = 0; count < numChars; count++) {
			sb.append(c);
		}
		return sb.toString();
	}
	
	/**
	 * Truncates string to the required length or returns string if it is
	 * shorter than the required length.
	 * 
	 * @param length
	 *            The required length of the string.
	 * @return A string of the required length or less.
	 */
	public static String clip(String string, int length) {
		if (string.length() > length) {
			return string.substring(0, length);
		} else {
			return new String(string);
		}
	}
	
	// Constants for createGrammaticalList type.
	/**
	 * Creates a grammatical list with the conjunction "and", 
	 */
	public static final int GRAMMATICAL_LIST_TYPE_INCLUSIVE = 0;
	/**
	 * Creates a grammatical list with the conjunction "or", 
	 */
	public static final int GRAMMATICAL_LIST_TYPE_EXCLUSIVE = 1;
	
	public static String createGrammaticalList(String[] words)
	{
		return createGrammaticalList(Arrays.asList(words), GRAMMATICAL_LIST_TYPE_INCLUSIVE);
	}
	
	@SuppressWarnings("unchecked")
	public static String createGrammaticalList(Collection words)
	{
		return createGrammaticalList(words, GRAMMATICAL_LIST_TYPE_INCLUSIVE);
	}
	
	public static String createGrammaticalList(String[] words, int type)
	{
		return createGrammaticalList(Arrays.asList(words), type);
	}
	
	@SuppressWarnings("unchecked")
	public static String createGrammaticalList(Collection words, int type)
	{
		if (words == null || words.size() == 0) {
			return "";
		}
		int length = words.size();
        int i = 0;
        StringBuffer list = new StringBuffer();
        for (Iterator iter = words.iterator(); iter.hasNext(); ) {
            if (++i > 1) {
                if (i == length) {
                    if (type == 1) {
                        list.append(" or ");
                    } else {
                        list.append(" and ");
                    }
                } else {
                    list.append(", ");
                }
            }
            list.append(iter.next().toString());
		}
		return list.toString();		
	}
	
	public static String createList(String[] words, String sep)
	{
		return createList(Arrays.asList(words), sep);
	}
	
	@SuppressWarnings("unchecked")
	public static String createList(Collection words, String sep)
	{
		if (words == null || words.size() == 0) {
			return "";
		}
        int i = 0;
        StringBuffer list = new StringBuffer();
        for (Iterator iter = words.iterator(); iter.hasNext(); ) {
            if (++i > 1) {
                list.append(sep);
            }
            list.append(iter.next().toString());
		}
		return list.toString();		
	}	
	
	/**
	 * This method retrieves a string value from the resultset at the given
	 * column.  If the column's value is null, a zero length String is
	 * returned, else a new a String of the field value is returned.
	 * @param result The result from which the value is to be retrieved.
	 * @param columnIndex The index of the column from which the value is to
	 *                    be retrieved.
	 * @return If the column's value is null, a zero length string; else a
	 *         String object.
	 * @throws SQLException Indicates an error retrieving the data from the
	 *         ResultSet.
	 */
	public static String getNonNullableString(ResultSet result, int columnIndex)
		throws SQLException
	{
		String s = result.getString(columnIndex);
		if (result.wasNull()) {
			return "";
		} else {
			return s;
		}
	}

	/**
	 * This method retrieves a string value from the resultset at the given
	 * column.  If the column's value is null, null is returned, else a new
	 * a String is returned.
	 * @param result The result from which the value is to be retrieved.
	 * @param columnIndex The index of the column from which the value is to
	 *                    be retrieved.
	 * @return If the column's value is null, null; else a String object.
	 * @throws SQLException Indicates an error retrieving the data from the
	 *         ResultSet.
	 */
	public static String getNullableString(ResultSet result, int columnIndex)
		throws SQLException
	{
		String s = result.getString(columnIndex);
		if (result.wasNull()) {
			return null;
		} else {
			return s;
		}
	}
	
	/**
	 * Returns the string unless s is null.  If s is null, returns empty
	 * string.
	 */
	public static String notNull(String s) {
		return s == null ? "" : s;
	}
	
	/**
	 * Returns the toString() result on the object, unless it is null.  If it
	 * is null, returns empty string.
	 */
	public static String notNull(Object obj) {
		return obj == null ? "" : obj.toString();
	}
	
	/**
	 * Returns the string after trimming it.  Unless s is null.  If s is null,
	 * returns empty string.
	 */
	public static String notNullTrimmed(String s) {
		return s == null ? "" : s.trim();
	}
	
	/**
	 * Returns the toString() result on the object after trimming it.  Unless
	 * it is null.  If it is null, returns empty string.
	 */
	public static String notNullTrimmed(Object obj) {
		return obj == null ? "" : obj.toString().trim();
	}
	
	/**
	 * Returns null if s is null or is zero length.
	 * Else, returns the string.
	 */
	public static String nullableNotEmpty(String s) {
		if (s == null || s.length() == 0) {
			return null;
		}
		return s;
	}
	
	/**
	 * Returns null if s is null or zero length after being trimmed.
	 * Else, returns the trimmed string.
	 */
	public static String nullableNotEmptyTrimmed(String s) {
		if (s == null) {
			return null;
		}
		return nullableNotEmpty(s.trim());
	}
	
	/**
	 * Replaces characters that are special in HTML with their equivelent
	 * entitites (such as replacing an ampersand with "&amp;").
	 * @param s The string to parse.
	 * @return The parsed string with all special characters converted.
	 */
	public static String replaceHTMLEntities(String s)
	{
		if (s == null) {
			return null;
		} else {
			if (s != null) {
				for (int i = 0; i < HTML_ENTITIES.length; i++) {
					s = s.replace(HTML_ENTITIES[i][1], HTML_ENTITIES[i][0]);
				}
			}
			return s;
		}
	}

	/**
	 * Pads the right side of a string with spaces so that it has the specified
	 * length. If the string provided is longer than the specified length, the
	 * string is truncated and returned.
	 * 
	 * @param length
	 *            The total length of the new string.
	 * @param string
	 *            The string to pad.
	 * @return A string with numChars length, padded on the right with spaces if
	 *         necessary.
	 */
	public static String rightPad(String string, int length) {
		StringBuffer sb = new StringBuffer(length);
		for (int count = 0; count < length; count++) {
			if (count < string.length()) {
				sb.append(string.charAt(count));
			} else {
				sb.append(" ");
			}
		}
		return sb.toString();
	}
	
	/**
	 * Surrounds the given string with a character.
	 * 
	 * @param string
	 *            The string to surround.
	 * @param quoteIdentifier
	 *            The character or characterw which are to be used to surround
	 *            the string.
	 * @param escString
	 *            If the quote identifier is found inside the string to quote,
	 *            replace the quote identifier with this string. If null, quote
	 *            identifiers found within the string will not be replaced.
	 * @return The new string.
	 */
	public static String quotedString(String string, char quoteIdentifier,
			String escString) {
		if (string == null) {
			return null;
		} else {
			char c;
			int index;
			StringBuffer sb = new StringBuffer(string.length() + 2);
			sb.append(quoteIdentifier);
			for (index = 0; index < string.length(); index++) {
				c = string.charAt(index);
				if (c == quoteIdentifier && escString != null) {
					sb.append(escString);
				} else {
					sb.append(c);
				}
			}
			sb.append(quoteIdentifier);
			return sb.toString();
		}
	}
	
	public static int occurrenceCount(String string, String match)
	{
		int count = 0;
		for (int index = string.indexOf(match, 0); index > -1; index = string.indexOf(match, index + 1)) {
			count++;
		}
		return count;
	}
	
	/**
	 * Segments the string into specified lengths
	 * 
	 * @param length
	 *            The length of each segment.
	 * @param string
	 *            The string to be segmented.
	 */
	public static String[] segment(String string, int length) {
		String segs[];
		String token;
		StringBuffer newSeg = new StringBuffer(length);
		StringTokenizer st = new StringTokenizer(string);
		List<String> segments = new LinkedList<String>();
		while (st.hasMoreTokens()) {
			token = st.nextToken();
			if ((newSeg.length() + token.length() + 1) > length) {
				if (token.length() > length) {
					// Token's length is greater than the length of each
					// segment. The token will have to be broken up.
					if ((newSeg.length() + 2) < length) {
						newSeg.append(' ');
					} else {
						segments.add(newSeg.toString());
						newSeg = new StringBuffer(length);
					}
					for (int i = 0; i < token.length(); i++) {
						if (newSeg.length() < length) {
							newSeg.append(token.charAt(i));
						} else {
							segments.add(newSeg.toString());
							newSeg = new StringBuffer(length);
							newSeg.append(token.charAt(i));
						}
					}
				} else {
					// Break to next segment
					segments.add(newSeg.toString());
					newSeg = new StringBuffer(length);
					newSeg.append(token);
				}
			} else {
				// Add the token to the string.
				if (newSeg.length() > 0) {
					newSeg.append(' ');
				}
				newSeg.append(token);
			}
		}
		// Build an array of strings from the vector.
		segments.add(newSeg.toString());
		segs = new String[segments.size()];
		for (int i = 0; i < segs.length; i++) {
			segs[i] = (String) segments.get(i);
		}
		// Return the array.
		return segs;
	}
	
    /**
     * Sets the parameter in the statement with an string value or if the
     * string is null, the parameter value is set to null and the parameter
     * type is set to VARCHAR. @see java.sql.Types#VARCHAR
     * @param stmt The statement in which the parameter value is to be set.
     * @param paramIndex The index of the parameter to set.
     * @param s The value to set (or null).
     * @param type The type to set the parameter to if the string is null
     *        @see java.sql.Types
     * @throws SQLException Indicates an error setting the parameter.
     */
    public static void setNullableString
		(PreparedStatement stmt, int paramIndex, String s)
    	throws SQLException
    {
    	setNullableString(stmt, paramIndex, s, Types.VARCHAR);
    }
	
    /**
     * Sets the parameter in the statement with an string value, or null if
     * the string is null.
     * @param stmt The statement in which the parameter value is to be set.
     * @param paramIndex The index of the parameter to set.
     * @param s The value to set (or null).
     * @param type The type to set the parameter to if the string is null
     *        @see java.sql.Types
     * @throws SQLException Indicates an error setting the parameter.
     */
    public static void setNullableString
		(PreparedStatement stmt, int paramIndex, String s, int type)
    	throws SQLException
    {
    	if (s == null) {
    		stmt.setNull(paramIndex, type);
    	} else {
    		stmt.setString(paramIndex, s);
    	}
    }
	
	/**
	 * Method which returns a string of spaces of the indicated length.
	 * 
	 * @param numSpaces
	 *            The length of the new string.
	 * @return A string of spaces of the length specified.
	 */
	public static String spaces(int numSpaces) {
		return chars(numSpaces, ' ');
	}
	
	/**
	 * Breaks a string into elements based on a seperator.
	 * @param string The string to process.
	 * @param sep The seperator to use when parsing out the elements.
	 * @param emptyElementSignificant Whether an empty string is significant.
	 * @return An array of element Strings.
	 */
	public static String[] tokenize
		(String string, String sep, boolean emptyElementSignificant)
	{
		String s = null;
		List<String> v = new LinkedList<String>();
		int j = 0;
		int i = string.indexOf(sep, j);
		int l = 0;
		while (i > -1) {
			l = i - j ;
			if (l > 0) {
				if (j < 0) {
					j = 0;
				}
				s = string.substring(j, i);
				v.add(s);
			} else if (emptyElementSignificant) {
				v.add("");
			}
			j = ++i;
			if (i < string.length()) {
				i = string.indexOf(sep, j);
			} else {
				i = -1;
			}
		}
		
		// The last element
		i = string.length();
		l = i - j ;
		if (l > 0) {
			if (j < 0) {
				j = 0;
			}
			s = string.substring(j, string.length());
			v.add(s);
		} else if (emptyElementSignificant) {
			v.add("");
		}

		String[] returnArray = new String[v.size()];
		v.toArray((Object[])returnArray);
		return returnArray;
	}
	
	/**
	 * Wraps text so that the lines are no longer than IDEAL_WIDTH characters,
	 * or the length of the longest single word (think file paths or other long
	 * strings without whitespace).
	 * @param text The text to wrap.
	 * @return The wrapped text.
	 */
	public static String wrapText(String text)
	{
		return wrapText(text, IDEAL_WIDTH);
	}
	
	public static String wrapText(String text, int maxWidth)
	{
		// Since the default font is probably not a fixed with font, we would
		// idealy go by display width rather than character width.  However,
		// we don't do that because it is more CPU intensive to caclulate
		// display width and reasonably long line usually average out.  We're
		// not being scientific, just attempting to keep from having a dialog
		// that stretches across the screen. 
		char[] chars = text.toCharArray();
		int i;
		int lineWidth = 0;
		StringBuffer sb = new StringBuffer();
		String s;
		List<String> words = new LinkedList<String>();
		
		// Break the string into words and record any single "word" longer
		// than the maximum width.
		for (i = 0; i < chars.length; i++) {
			if (Character.isWhitespace(chars[i])) {
				if (sb.length() > 0) {
					s = sb.toString();
					sb = new StringBuffer();
					words.add(s);
					if (s.length() > maxWidth) {
						maxWidth = s.length();
					}
				}
			} else {
				sb.append(chars[i]);
			}
		}
		// Add the last word to the vector;
		s = sb.toString();
		sb = new StringBuffer();
		words.add(s);
		if (s.length() > maxWidth) {
			maxWidth = s.length();
		}
		
		// Build the wrapped string.  Perhaps code could be added here to
		// allow only a maximum number of lines...
		sb = new StringBuffer();
		for (i = 0; i < words.size(); i++) {
			s = (String)words.get(i);
			if (lineWidth + s.length() > maxWidth) {
				sb.append('\n');
				lineWidth = s.length();
			} else {
				sb.append(' ');
				lineWidth = lineWidth + 1 + s.length(); 
			}
			sb.append(s);
		}
		return sb.toString();
	}
	
	/**
	 * Takes a String input and removes all punctuation and spaces between
	 * the alphnumeric words in the string and returns the words as a List
	 * of non-empty strings.
	 * @param str a string to split into words
	 * @return a List of the non-empty string words
	 */
	public static List<String> getSearchPhrases(String str) {
		List<String> searchList = new ArrayList<String>();
		String phrase = ".*\".+\".*";
		String word = "[^a-zA-Z0-9]+\\s*";
		
		if (str.matches(phrase)) {
			do {
				int i = str.indexOf('\"');
				int j = str.indexOf('\"', i+1);
				searchList.add(str.substring(i+1, j));
				str = str.replace(str.subSequence(i, j+1), "");
			} while (str.contains("\""));
		}
		if (! str.equalsIgnoreCase("")) {
			String[] words = str.split(word);
			for (String w : words) {
				if (!w.equalsIgnoreCase("")) {
					searchList.add(w);
				}
			}
		}
		return searchList;
	}
	
	/**
	 * Replaces placeholders with values.
	 * @param s The string to operate on.
	 * @param messageargs values that will be used to replace placeholders in
	 *                    the string.  The placeholders should take the form
	 *                    of {n} where n is the position of the arg.  The first
	 *                    arg will replace {1}, the second arg will replace
	 *                    {2}, etc.
	 */
	public static String replaceArgs(String s, Object ... args) {
		StringBuilder sb;
		String val;
		for (int i = 0; i < args.length; i++) {
			sb = new StringBuilder();
			sb.append('{');
			sb.append(i + 1);
			sb.append('}');
			val = args[i] == null ? "null" : args[i].toString();
			s = s.replace(sb.toString(), val);
		}
		return s;
	}
}