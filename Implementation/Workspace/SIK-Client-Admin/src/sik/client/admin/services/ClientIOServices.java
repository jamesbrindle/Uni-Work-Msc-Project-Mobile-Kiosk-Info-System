package sik.client.admin.services;

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
 * Class used for text file IO services. The text file contains attributes
 * values and settings to be used by other classes so that these settings can be
 * changed by simply changing the values in the text file, rather than having to
 * hard code the attribute values or keep prompting the user for an input
 * 
 * @author Jamie Brindle (06352322) - Manchester Metropolitan University
 */
public class ClientIOServices implements Serializable {

	/**
	 * /** Default universal serializable class ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * An array of lines brought from the text file
	 */
	private ArrayList<String> input;

	/**
	 * The text file location
	 */
	private static final String clientOptionsFileLocation = "ClientOptions.txt";

	/**
	 * The status of text file IO execution. It is used to determine if any IO
	 * executions were successful or not, and if not, produce an educated error
	 * message
	 */
	private String status = "";

	/**
	 * Class constructor
	 */
	public ClientIOServices() {
		input = new ArrayList<String>();

		File file = new File(clientOptionsFileLocation);
		BufferedReader in = null;

		// read all the files of the text file
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(
					file)));
			String str;
			while ((str = in.readLine()) != null) {
				input.add(str);
			}

			// problems caught, such as the file doesn't exist
		} catch (FileNotFoundException e) {
			System.err.println("Can't find clientOptions.txt");

			// problems caught, such as input stream read problem
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException IO) {
				System.err.println("Problem closing input stream");
			}
		}

		status = "Successfully loaded client settings file";
		System.out.println("Successfully loaded server settings file");
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

			status = "Export to file successful";
			System.out.println("export file success");

		} catch (IOException e) {
			status = "Export to file unsuccessful";
			System.err.println("export unsuccessful");
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
	 * Returns the status property of this class
	 * 
	 * @return status The status string
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets the RMI location and exports the amended text file
	 * 
	 * @param newUrl
	 *            The new RMI location
	 */
	public void setRMILocation(String newUrl) {
		input.set(3, newUrl);
		exportFile();
	}
}
