package sik.client.admin.services;

import java.io.File;

/**
 * Used in conjunction with the file browser in order to only look for or to
 * default to a particular file type i.e *.dat
 * 
 * @author Jamie Brindle (06352322)
 */
public class TextFilter extends javax.swing.filechooser.FileFilter {

	private String fileType;

	public TextFilter(String s) {
		fileType = s;
	}

	/**
	 * Display only directories and the fileType given by another class or
	 * method
	 * 
	 * @param file
	 *            the file
	 * @return true of file ends with fileType (string given by another class)
	 *         and is a directory
	 */
	public boolean accept(File file) {
		return file.getName().toLowerCase().endsWith(fileType)
				|| file.isDirectory();
	}

	/**
	 * Returns file type description
	 * 
	 * @return description of fileType the TextFilter class is currently set to
	 */
	public String getDescription() {
		return "Files of file type:  " + fileType;
	}
}
