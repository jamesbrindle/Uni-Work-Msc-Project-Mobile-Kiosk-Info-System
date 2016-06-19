package sik.client.user.services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class used for text file IO services. One text file contains attributes
 * values and settings to be used by other classes so that these settings can be
 * changed by simply changing the values in the text file, rather than having to
 * hard code the attribute values or keep prompting the user for an input.
 * 
 * The second text file provides the same functionality but contains a legend to
 * the campus map used in this student information kiosk system
 * 
 * @author Jamie Brindle (06352322) - Manchester Metropolitan University
 */
public class ClientIOServices implements Serializable {

	/**
	 * Default universal serializable class ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * An array of lines brought from a text file for the client settings
	 */
	private ArrayList<String> input;

	/**
	 * A string of lines brought from a text file for the campus map legend
	 */
	private String input2;

	/**
	 * The text file location of client attribute values and settings
	 */
	private final String clientOptionsFileLocation = "ClientOptions.txt";

	/**
	 * The text file location of the campus map legend
	 */
	private final String campusLegendFileLocation = "data/CampusLegend.txt";

	/**
	 * Class constructor
	 */
	public ClientIOServices() {
		input = new ArrayList<String>();

		File file = new File(clientOptionsFileLocation);
		File file2 = new File(campusLegendFileLocation);
		BufferedReader in = null;
		BufferedReader in2 = null;

		// read all the files of the text files
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(
					file)));

			in2 = new BufferedReader(new InputStreamReader(new FileInputStream(
					file2)));

			String str;
			String str2;
			input2 = "";

			while ((str = in.readLine()) != null) {
				input.add(str);
			}

			while ((str2 = in2.readLine()) != null) {
				input2 = input2 + str2 + "\n";
			}

			// problems caught, such as the file doesn't exist
		} catch (FileNotFoundException e) {
			System.err.println("Can't find clientOptions.txt");

			// problems caught, such as input stream read problem
		} catch (IOException e) {
			System.err.println("Problem reading file");
		} finally {
			try {
				in.close();
				in2.close();
			} catch (IOException IO) {
				System.err.println("Problem closing input stream");
			}
		}
	}

	/**
	 * Used to write the file. It will overwrite any existing file
	 */
	public void exportFile() {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(clientOptionsFileLocation));

			for (int i = 0; i < input.size(); i++) {
				if (input.get(i) != null) {
					out.write(input.get(i));
					out.newLine();
				}
			}

			out.flush();
			out.close();

		} catch (IOException e) {
			System.err.println("Problem exporting to file");
		}
	}

	/**
	 * Returns the RMI address from the text file
	 * 
	 * @return rmiAddress The RMI address String
	 */
	public String getRMIAddress() {
		String rmiAddress = input.get(3).toString();

		return rmiAddress;
	}

	/**
	 * Returns the administration password from the text file
	 * 
	 * @return adminPassword The administration password string
	 */
	public String getAdminPassword() {
		String adminPassword = input.get(6).toString();

		return adminPassword;
	}

	/**
	 * Returns the main GUI's background image location
	 * 
	 * @return imageLocation The background image location in which to return
	 */
	public String getBgImageLocation() {
		String imageLocation = input.get(9).toString();

		return imageLocation;
	}

	/**
	 * Returns a user client identifier, which could be used to determine this
	 * clients location, or to give it a unique identifier
	 * 
	 * @return locationIdentifierString The location identifier string
	 */
	public String getClientLocationIdentifier() {
		String locationIdentifierString = input.get(12).toString();

		return locationIdentifierString;
	}

	/**
	 * Returns a debug option string which determines if the main GUI is to run
	 * in 'debug' or 'live' mode
	 * 
	 * @return debugOption The debug option string (true or false)
	 */
	public String getDebugOption() {
		String debugOption = input.get(15).toString();

		return debugOption;
	}

	/**
	 * Returns the campus map image location to be used in the map panel of this
	 * system
	 * 
	 * @return imageLocation The campus map image to be returned
	 */
	public String getMapImageLocation() {
		String imageLocation = input.get(18).toString();

		return imageLocation;
	}

	/**
	 * Returns the campus map image legend text
	 * 
	 * @return input2 The campus legend in which to be returned
	 */
	public String getCampusLegendText() {
		return input2;
	}

	/**
	 * Sets the RMI location and exports the amended text file
	 * 
	 * @param newUrl
	 *            The new RMI location
	 */
	public void changeRMILocation(String newUrl) {
		input.set(3, newUrl);
		exportFile();
	}
}
