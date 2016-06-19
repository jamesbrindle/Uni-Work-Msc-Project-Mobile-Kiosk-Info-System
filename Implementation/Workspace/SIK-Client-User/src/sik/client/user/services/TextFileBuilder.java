package sik.client.user.services;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class which constructs a simple String based on a given input to be written
 * to a file in order for it to be used by the bluetooth panel in this system to
 * send to a bluetooth device
 * 
 * @author Jamie Brindle (06352322) - Manchester Metropolitan University
 */
public class TextFileBuilder {

	/**
	 * Returns a 'messages' text file containing a list of all the messages in a
	 * student record object's message array list
	 * 
	 * @param messagesArrayList
	 *            The supplied array list containing a list of message
	 * @return file The text file constructed from the messages array list
	 */
	public static File getMessageFile(ArrayList<String> messagesArrayList) {
		File file = new File("tmp/Uni Messages.txt");

		String messageString = constMessagesString(messagesArrayList);

		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			out.write(messageString);

			out.flush();
			out.close();

		} catch (IOException e) {
			System.err.println("Problem exporting to file");
		}

		return file;
	}

	/**
	 * Returns a 'timetable' text file containing a list of all the time periods
	 * in a student record object's timetable array list
	 * 
	 * @param timetableArrayList
	 *            The supplied array list containing a list of time periods
	 * @param timetableName
	 *            The name of the timetable
	 * @return file The text file constructed from the timetable array list
	 */
	public static File getTimetableFile(ArrayList<String> timetableArrayList,
			String timetableName) {
		File file = new File("tmp/Uni Timetable.txt");

		String messageString = constTimetableString(timetableArrayList,
				timetableName);

		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			out.write(messageString);

			out.flush();
			out.close();

		} catch (IOException e) {
			System.err.println("Problem exporting to file");
		}

		return file;
	}

	/**
	 * Returns a 'results' text file containing a list of all the results in a
	 * student record object's results array list
	 * 
	 * @param resultsArrayList
	 *            The supplied array list containing a list of results
	 * @return file The text file constructed from the results array list
	 */
	public static File getResultsFile(ArrayList<String> resultsArrayList) {
		File file = new File("tmp/Uni Results.txt");

		String messageString = constResultsString(resultsArrayList);

		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			out.write(messageString);

			out.flush();
			out.close();

		} catch (IOException e) {
			System.err.println("Problem exporting to file");
		}

		return file;
	}

	/**
	 * Returns a formatted 'messages' String containing a list of all the
	 * messages in a student record object's messages array list
	 * 
	 * @param messagesArrayList
	 *            The supplied array list containing a list of messages
	 * @return messagesString The string constructed from the messages array
	 *         list
	 */
	protected static String constMessagesString(
			ArrayList<String> messagesArrayList) {
		String messagesString = "";

		messagesString = "-- Messages --\n\n";

		for (int i = 0; i < messagesArrayList.size(); i++) {
			messagesString += "------------------\n";
			messagesString += "Message Number: " + (i + 1) + "\n";
			messagesString += messagesArrayList.get(i) + "\n";

			if (i == messagesArrayList.size() - 1) {
				messagesString += "------------------\n";
			}
		}

		return messagesString;
	}

	/**
	 * Returns a formatted 'timetable' String containing a list of all the time
	 * periods in a student record object's timetable array list
	 * 
	 * @param timetableArrayList
	 *            The supplied array list containing a list of time periods
	 * @param timetableName
	 *            The name of the timetable
	 * @return timetableString The string constructed from the time periods
	 *         array list
	 */
	protected static String constTimetableString(
			ArrayList<String> timetableArrayList, String timetableName) {
		String timetableString = "";

		timetableString = "-- Timetable --\n\n";
		timetableString += "Name: " + timetableName + "\n\n";

		timetableString += "------------------\n";

		int lessonStart = 8;
		int lessonEnd = 9;

		timetableString += "Monday:\n----\n";

		for (int i = 0; i < 9; i++) {
			lessonStart++;
			lessonEnd++;

			timetableString += "Time " + lessonStart + ":00 - " + lessonEnd
					+ ":00\n";

			timetableString += timetableArrayList.get(i);

			if (i == 8) {
				timetableString += "\n------------------\n";
				lessonStart = 8;
				lessonEnd = 9;
			} else {
				timetableString += "\n----\n";
			}
		}

		timetableString += "Tuesday:\n----\n";

		for (int i = 9; i < 18; i++) {
			lessonStart++;
			lessonEnd++;

			timetableString += "Time " + lessonStart + ":00 - " + lessonEnd
					+ ":00\n";

			timetableString += timetableArrayList.get(i);

			if (i == 17) {
				timetableString += "\n------------------\n";
				lessonStart = 8;
				lessonEnd = 9;
			} else {
				timetableString += "\n----\n";
			}
		}

		timetableString += "Wednesday:\n----\n";

		for (int i = 18; i < 27; i++) {
			lessonStart++;
			lessonEnd++;

			timetableString += "Time " + lessonStart + ":00 - " + lessonEnd
					+ ":00\n";

			timetableString += timetableArrayList.get(i);

			if (i == 26) {
				timetableString += "\n------------------\n";
				lessonStart = 8;
				lessonEnd = 9;
			} else {
				timetableString += "\n----\n";
			}
		}

		timetableString += "Thurday:\n----\n";

		for (int i = 27; i < 36; i++) {
			lessonStart++;
			lessonEnd++;

			timetableString += "Time " + lessonStart + ":00 - " + lessonEnd
					+ ":00\n";

			timetableString += timetableArrayList.get(i);

			if (i == 35) {
				timetableString += "\n------------------\n";
				lessonStart = 8;
				lessonEnd = 9;
			} else {
				timetableString += "\n----\n";
			}
		}

		timetableString += "Friday:\n----\n";

		for (int i = 36; i < 45; i++) {
			lessonStart++;
			lessonEnd++;

			timetableString += "Time " + lessonStart + ":00 - " + lessonEnd
					+ ":00\n";

			timetableString += timetableArrayList.get(i);

			if (i == 44) {
				timetableString += "\n------------------\n";
				lessonStart = 8;
				lessonEnd = 9;
			} else {
				timetableString += "\n----\n";
			}
		}

		return timetableString;
	}

	/**
	 * Returns a formatted 'results' String containing a list of all the results
	 * in a student record object's results array list
	 * 
	 * @param resultsArrayList
	 *            The supplied array list containing a list of results
	 * @return resultsString The string constructed from the results array list
	 */
	protected static String constResultsString(
			ArrayList<String> resultsArrayList) {
		String resultsString = "";

		resultsString = "-- Current Results --\n\n";

		resultsString += "------------------\n";
		int i = 0;

		resultsString += "Unit ID: " + resultsArrayList.get(i) + "\n";
		resultsString += "Unit Name: " + resultsArrayList.get(i + 1) + "\n";
		resultsString += "Assignment 1: " + resultsArrayList.get(i + 2) + "%\n";
		resultsString += "Assignment 2: " + resultsArrayList.get(i + 3) + "%\n";
		resultsString += "Exam: " + resultsArrayList.get(i + 4) + "%\n";

		resultsString += "------------------\n";
		i = 5;

		resultsString += "Unit ID: " + resultsArrayList.get(i) + "\n";
		resultsString += "Unit Name: " + resultsArrayList.get(i + 1) + "\n";
		resultsString += "Assignment 1: " + resultsArrayList.get(i + 2) + "%\n";
		resultsString += "Assignment 2: " + resultsArrayList.get(i + 3) + "%\n";
		resultsString += "Exam: " + resultsArrayList.get(i + 4) + "%\n";

		resultsString += "------------------\n";
		i = 10;

		resultsString += "Unit ID: " + resultsArrayList.get(i) + "\n";
		resultsString += "Unit Name: " + resultsArrayList.get(i + 1) + "\n";
		resultsString += "Assignment 1: " + resultsArrayList.get(i + 2) + "%\n";
		resultsString += "Assignment 2: " + resultsArrayList.get(i + 3) + "%\n";
		resultsString += "Exam: " + resultsArrayList.get(i + 4) + "%\n";

		resultsString += "------------------\n";
		i = 15;

		resultsString += "Unit ID: " + resultsArrayList.get(i) + "\n";
		resultsString += "Unit Name: " + resultsArrayList.get(i + 1) + "\n";
		resultsString += "Assignment 1: " + resultsArrayList.get(i + 2) + "%\n";
		resultsString += "Assignment 2: " + resultsArrayList.get(i + 3) + "%\n";
		resultsString += "Exam: " + resultsArrayList.get(i + 4) + "%\n";

		resultsString += "------------------\n";
		i = 20;

		resultsString += "Unit ID: " + resultsArrayList.get(i) + "\n";
		resultsString += "Unit Name: " + resultsArrayList.get(i + 1) + "\n";
		resultsString += "Assignment 1: " + resultsArrayList.get(i + 2) + "%\n";
		resultsString += "Assignment 2: " + resultsArrayList.get(i + 3) + "%\n";
		resultsString += "Exam: " + resultsArrayList.get(i + 4) + "%\n";

		resultsString += "------------------\n";
		i = 25;

		resultsString += "Unit ID: " + resultsArrayList.get(i) + "\n";
		resultsString += "Unit Name: " + resultsArrayList.get(i + 1) + "\n";
		resultsString += "Assignment 1: " + resultsArrayList.get(i + 2) + "%\n";
		resultsString += "Assignment 2: " + resultsArrayList.get(i + 3) + "%\n";
		resultsString += "Exam: " + resultsArrayList.get(i + 4) + "%\n";

		resultsString += "------------------\n";

		return resultsString;
	}
}
