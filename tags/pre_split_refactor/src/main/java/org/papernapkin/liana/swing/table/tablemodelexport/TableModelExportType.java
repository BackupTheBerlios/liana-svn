package org.papernapkin.liana.swing.table.tablemodelexport;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import org.papernapkin.liana.swing.BasicFileFilter;

/**
 * An emumeration that defines the types of exports available.
 *
 * @author Philip A. Chapman
 */
public enum TableModelExportType
{
	CSV("csv", "Comma segmented values"),
	HTML("html", "Web page"),
	SQL("sql", "SQL script"),
	XLS("xls", "Microsoft Excel Spreadsheet");
	
	// CONSTRUCTORS
	
	/**
	 * @param value
	 */
	private TableModelExportType(String ext, String desc)
	{
		description = desc;
		extension = ext;
	}
	
	private String description;
	private String extension;
	
	// MEMBERS
	
	public String getFileDescription()
	{
		return description;
	}

	public String getFileExtension()
	{
		return extension;
	}
	
	public FileFilter getFileFilter()
	{
		return new BasicFileFilter(getFileExtension(), getFileDescription());
	}
	
	// METHODS
	
	public static TableModelExportType fromFile(File file)
	{
		for (TableModelExportType type : values()) {
			if (file.getName().indexOf(type.getFileExtension()) > 1) {
				return type;
			}
		}
		return null;
	}
	
	public String toString()
	{
		return getFileDescription();
	}
}
