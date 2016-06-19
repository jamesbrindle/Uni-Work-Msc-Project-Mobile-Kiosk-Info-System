package sik.common;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class which contains student record objects and methods to work with and
 * handle those objects. This class also provides methods to export and import
 * student record store object to and from a file
 * 
 * @author Jamie Brindle (06352322) - Manchester Metropolitan University *
 */
public class Store implements Serializable {

	/**
	 * Default universal serializable class ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The array list of student record objects
	 */
	public ArrayList<StudentRecord> recordArrayList;

	/**
	 * To be used as an array pointer
	 */
	private int currentRecordIndex;

	/**
	 * Class constructor
	 */
	public Store() {
		recordArrayList = new ArrayList<StudentRecord>();
	}

	/**
	 * Returns a unique student record from the store based on the student
	 * record's student ID number
	 * 
	 * @param studentID
	 *            A unique identifier of the student record within the store in
	 *            which to return
	 */
	public StudentRecord getRecordFromStudentID(String studentID) {
		int recordPointer = -1;

		// iterates through array list looking for the student record with the
		// matching
		// student ID
		for (int i = 0; i < recordArrayList.size(); i++) {
			if (recordArrayList.get(i).getStudentID()
					.equalsIgnoreCase(studentID)) {
				recordPointer = i;
				break;
			}
		}
		return recordArrayList.get(recordPointer);
	}

	/**
	 * Returns a unique student record from the store based on the student
	 * record's RFID tag number
	 * 
	 * @param tagID
	 *            A unique identifier of the student record within the store in
	 *            which to return
	 */
	public StudentRecord getRecordFromTagID(String tagID) {

		int recordPointer = -1;

		for (int i = 0; i < recordArrayList.size(); i++) {
			if (recordArrayList.get(i).getRFIDTagID().equalsIgnoreCase(tagID)) {
				recordPointer = i;
				break;
			}
		}

		return recordArrayList.get(recordPointer);
	}

	/**
	 * Add an account to the array list
	 * 
	 * @param record
	 *            The account to add
	 */
	public void addRecord(StudentRecord record) {
		recordArrayList.add(record);
	}

	/**
	 * Removes an account from the array list
	 * 
	 * @param record
	 *            The account to be removed
	 */
	public void removeRecord(StudentRecord record) {
		recordArrayList.remove(record);
	}

	/**
	 * Returns the record corresponding the the currentAccountIndex which are
	 * navigated through by the first record pointer, last record pointer,
	 * previous record pointer and next record pointer methods
	 * 
	 * @return returns the current record
	 */
	public StudentRecord currentRecord() {
		if (recordArrayList.size() == 0) {
			return null;
		} else {
			return recordArrayList.get(currentRecordIndex);
		}
	}

	/**
	 * Points to first record which is that of the first object in the array
	 */
	public void firstRecordPointer() {
		currentRecordIndex = 0;
	}

	/**
	 * points to the last record which is that of the size of the array -1
	 */
	public void lastRecordPointer() {
		currentRecordIndex = recordArrayList.size() - 1;
	}

	/**
	 * Points to the next record pointer, which increments the
	 * currentAccountIndex
	 */
	public void nextRecordPointer() {
		if (currentRecordIndex < recordArrayList.size() - 1) {
			currentRecordIndex++;
		}
	}

	/**
	 * Points to the previous record record, which decrements the
	 * currentAccountIndex
	 */
	public void previousRecordPointer() {
		if (currentRecordIndex > 0) {
			currentRecordIndex--;
		}
	}

	/**
	 * Checks if the current pointer is set at the last record
	 * 
	 * @return boolean true if is pointed at last record, false otherwise
	 */
	public boolean isLast() {
		if (currentRecordIndex == recordArrayList.size() - 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks if the current pointer is set at the first record
	 * 
	 * @return boolean true if is pointed at first record, false otherwise
	 */
	public boolean isFirst() {
		if (currentRecordIndex == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks if the store is empty
	 * 
	 * @return boolean true if store is empty, false otherwise
	 */
	public boolean isEmpty() {
		if (recordArrayList.size() == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Saves the store as an object and saves it to a file
	 * 
	 * @param fileName
	 *            the file name the file is to be saved as
	 */
	public void fileOut(String fileName) {
		ObjectOutputStream oo = null;

		try {
			oo = new ObjectOutputStream(new FileOutputStream(fileName));
			oo.writeObject(Store.this);
		} catch (Exception e) {
			String errMessage = e.getMessage();

			System.out.println("Error: " + errMessage);
		} finally {
			if (oo != null) {
				try {
					oo.flush();
				} catch (IOException ioe) {
				}

				try {
					oo.close();
				} catch (IOException ioe) {
				}
			}
		}
	}

	/**
	 * Opens a file and casts the object found within that file as a store
	 * 
	 * @param fileName
	 *            the file name the file is to be opened
	 * @return returns the store found within the file
	 */
	public Store fileIn(String fileName) {
		ObjectInputStream oi = null;
		Store tempStore = null;

		try {
			FileInputStream fi = new FileInputStream(fileName);

			oi = new ObjectInputStream(fi);
			tempStore = (Store) oi.readObject();
		} catch (Exception e) {
			String error = e.getMessage();

			System.out.println("Error: " + error);
		} finally {
			if (oi != null) {
				try {
					oi.close();
				} catch (IOException ioe) {
				}
			}
		}
		return tempStore;
	}
}
