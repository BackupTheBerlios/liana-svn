package org.papernapkin.liana.swing.table.tablemodelexport;

/**
 * Exports the data out of a table model into a CSV file.
 *
 * @author Philip A. Chapman
 */
final class CSVExportHelper extends ExportHelper
{
	/*
	 * File types and related constants.  The second dimension elements are:
	 *		 0	-	File header text, may be null
	 *		 1	-	File footer text, may be null
	 * 		 2	-	Line beginning for column headers, may be null
	 * 		 3	-	Delimeter for column headers
	 * 		 4	-	Line ending for column headers, may be null
	 * 		 5	-	Line beginning for data row, may be null
	 * 		 6	-	Delimeter for data columns
	 * 		 7	-	Line ending for data row, may be null
	 */
	private static final String[] CONSTANTS =
		{
			null, null,
			null, ",", "\n",
			null, ",", "\n"
		};

	CSVExportHelper(String title)
	{
		super(title);
	}

	/**
	 * @see org.papernapkin.util.tablemodelexport.ExportHelper#getConstantArray()
	 */
	protected String[] getConstantArray()
	{
		return CONSTANTS;
	}

	/**
	 * @see org.papernapkin.util.tablemodelexport.ExportHelper#buildDataColumn(java.lang.Object)
	 */
	public String buildDataColumn(Object data)
	{
		if (data instanceof Integer || data instanceof Long) {
			return INTEGER_FORMATTER.format(((Number)data).longValue());
		} else if (data instanceof Double || data instanceof Float) {
			return NUMBER_FORMATTER.format(((Number)data).doubleValue());
		} else if (data == null) {
			return "";
		} else {
			StringBuffer sb = new StringBuffer("\"");
			sb.append(super.buildDataColumn(data));
			sb.append('"');
			return sb.toString();
		}
	}
}
