package sik.client.admin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.Timer;

import sik.client.admin.services.ClientIOServices;
import sik.client.admin.services.FieldValidator;
import sik.client.admin.services.NumFunc;
import sik.client.admin.services.RMIClient;
import sik.client.admin.services.TextFilter;
import sik.common.Store;
import sik.common.StudentRecord;

import com.phidgets.PhidgetException;

/**
 * A class solely for handling the events made in the Records GUI
 * 
 * @author Jamie Brindle (06352322) - Manchester Metropolitan University *
 */
public class RecordsGUIListener implements ActionListener, Serializable {

	/**
	 * Default universal serializable class ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Parent class which created an instance of this class. Set as global in
	 * order to quickly access its methods and send back information
	 */
	protected RecordsGUI recordsGUI;

	/**
	 * GUI to edit and display the messages of the student record object
	 */
	protected EditMessagesGUI editMessagesGUI;

	/**
	 * GUI to edit and display the timetable of the student record object
	 */
	protected EditTimetableGUI editTimestableGUI;

	/**
	 * GUI to edit and display the results of the student record object
	 */
	protected EditResultsGUI editResultsGUI;

	/**
	 * GUI to edit and display the RFID tag serial of the student record object
	 */
	protected AssignRFIDTagGUI rfidGUI;

	/**
	 * Dialog to display messages and prompts to the user
	 */
	protected OptionsDialog dialog;

	/**
	 * Class to provide RMI connectivity and transfer services for the Records
	 * GUI
	 */
	public RMIClient rmiClient;

	/**
	 * Class to provide text file IO services for this GUI - In which the text
	 * file contains attribute values (or setting) that this and other classes
	 * use
	 */
	protected ClientIOServices clientOptions;

	// student records store properties
	private JFileChooser fc, fc2, fc3;
	private File file, saveAsFileChooser, filePic;
	protected Store store, filteredStore;

	// photo area properties
	private String currentFileName;
	protected String defaultPhoto = "res/images/default.jpg";

	// Used to display a message for a given amount of time
	private Timer displayMessageTimer;

	// control properties
	private int validationIsOK;
	private boolean isNewRecord;
	private boolean saveAttempted = false;
	private boolean didSearch = false;
	private boolean isRMIStore = false;

	/**
	 * Specifies if the Records GUI is using an RMI or local student records
	 * store
	 */
	public boolean isConnectedToRMI = false;

	/**
	 * Class constructor
	 * 
	 * @param GUI
	 *            The parent class which called this class
	 */
	public RecordsGUIListener(RecordsGUI GUI) {
		this.recordsGUI = GUI;

		// create a new store object to store work with and store student
		// records
		store = new Store();

		// create a new RMI client instance to be able to connect to RMI server
		// and fetch a store object remotely
		rmiClient = new RMIClient();

		// create new client IO services instance to retrieve attribute values
		// and
		// setting from a text file
		clientOptions = new ClientIOServices();

		// set up a new timer in to display messages to the user for a given
		// time
		displayMessageTimer = new Timer(5000, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {

				recordsGUI.setWarningMessage("use the navigation bar below to "
						+ "move through records, create, modify or remove");
				displayMessageTimer.stop();
			}
		});
	}

	/**
	 * Action event handler
	 * 
	 * @param e
	 *            The action event
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == recordsGUI.editImageButton) {
			updatePhoto();
		}
		if (e.getSource() == recordsGUI.editMessages) {
			loadEditMessagesGUI();
		}
		if (e.getSource() == recordsGUI.editTimetable) {
			loadEditTimetableGUI();
		}
		if (e.getSource() == recordsGUI.editAssignmentResults) {
			loadEditResultsGUI();
		}
		if (e.getSource() == recordsGUI.menuItem_newStore) {
			createNewStore();
		}
		if (e.getSource() == recordsGUI.menuItem_open) {
			openFile();
		}
		if (e.getSource() == recordsGUI.menuItem_saveAs) {
			saveFileAs();
		}
		if (e.getSource() == recordsGUI.menuItem_exit) {
			loadExitDialog();
		}
		if (e.getSource() == recordsGUI.nextNavButton) {
			nextPointer();
		}
		if (e.getSource() == recordsGUI.previousNavButton) {
			previousPointer();
		}
		if (e.getSource() == recordsGUI.firstNavButton) {
			firstPointer();
		}
		if (e.getSource() == recordsGUI.lastNavButton) {
			lastPointer();
		}
		if (e.getSource() == recordsGUI.removeNavButton) {
			removeRecord();
		}
		if (e.getSource() == recordsGUI.editNavButton) {
			setEditMode();
		}
		if (e.getSource() == recordsGUI.newRecordNavButton) {
			setNewRecordMode();
		}
		if (e.getSource() == recordsGUI.saveNavButton) {
			saveStore();
		}
		if (e.getSource() == recordsGUI.searchBarSearchButton) {
			search();
		}
		if (e.getSource() == recordsGUI.menuItem_RMISave) {
			saveRMIFile();
		}
		if (e.getSource() == recordsGUI.menuItem_RMILoad) {
			openRMIFile();
		}
		if (e.getSource() == recordsGUI.menuItem_changeRMILoc) {
			loadChangeRMILocationDialog();
		}
		if (e.getSource() == recordsGUI.RFIDTagAssignButton) {
			try {
				loadAssignRFIDTagGUI();
			} catch (PhidgetException e1) {
				recordsGUI
						.setWarningMessage("Problem loading RFID assigment GUI");
			}
		}
	}

	/**
	 * Calls two method which set the 'mode' of the navigation control pointers
	 * based on pointer index of the student records store i.e. If pointing to
	 * the first student record in the student records store there's no need for
	 * the 'last' and 'previous' navigation buttons to be enabled, as there is
	 * no 'last' and 'previous' pointer from this point, so disable them. This
	 * method will also re-enable navigation buttons appropriately
	 */
	private void refreshRecordPointer() {
		recordsGUI.setIsFirst(store.isFirst());
		recordsGUI.setIsLast(store.isLast());
	}

	/**
	 * Run the GUI to assign an RFID serial number to a student record
	 * 
	 * @throws PhidgetException
	 *             If there are any problems with the RFID scanner (connection
	 *             or hardware related)
	 */
	public void loadAssignRFIDTagGUI() throws PhidgetException {
		rfidGUI = new AssignRFIDTagGUI(this);
		rfidGUI.run();
	}

	/**
	 * Creates and runs a dialog which allows the user to change the set RMI
	 * location to connect to
	 */
	private void loadChangeRMILocationDialog() {
		this.dialog = new OptionsDialog(new JFrame(), "Change RMI Location",
				"Please specify the URL of the RMI Server to connect to",
				"changeRMILocation");

		if (dialog.getChoice() == 1) {
			clientOptions.setRMILocation(dialog.getNewRMILocation());
			recordsGUI.setWarningMessage(clientOptions.getStatus());
			displayMessageTimer.start();
		}
	}

	/**
	 * When the user tries to load or save a store object from / to a file, we
	 * need to work out if this is an RMI file or a local file, and execute the
	 * correct file save and load methods. This is particularly useful as it
	 * allows us to use some of the same methods and aids in the fact that a lot
	 * of store saves are done automatically without the user's knowledge
	 */
	private void executeCorrectFileInMethod() {
		if (isRMIStore) {
			try {
				store = rmiClient.loadStoreFromRMI(true);
			} catch (ConnectException e) {
				recordsGUI
						.setWarningMessage("RMI Conneciton failure, please try again");
				System.err.println("Connection failure");
				e.printStackTrace();
			} catch (RemoteException e) {
				recordsGUI
						.setWarningMessage("RMI Conneciton failure, please try again");
				System.err.println("Connection failure");
				e.printStackTrace();
			}
		} else {
			store = (Store) store.fileIn(currentFileName);
		}
	}

	/**
	 * Creates a new editTimestableGUI object and displays it
	 */
	private void loadEditTimetableGUI() {
		editTimestableGUI = new EditTimetableGUI(this);
		editTimestableGUI.run();
	}

	/**
	 * Creates a new editResultsGUI object and displays it
	 */
	private void loadEditResultsGUI() {
		editResultsGUI = new EditResultsGUI(this);
		editResultsGUI.run();
	}

	/**
	 * Creates a new editMessagesGUI object and displays it
	 */
	private void loadEditMessagesGUI() {
		editMessagesGUI = new EditMessagesGUI(this);
		editMessagesGUI.run();
	}

	/**
	 * Creates a new store object to work with and set the appropriate GUI
	 * 'mode' (i.e. cleared text fields and reset record pointer)
	 */
	private void createNewStore() {
		store = new Store();
		recordsGUI.setIsEmpty(true);
		recordsGUI
				.setWarningMessage("Be sure to save the store before exiting");
		currentFileName = "Unsaved.dat";
		setRecord();
		recordsGUI.menuItem_saveAs.setEnabled(true);
		recordsGUI.menuItem_RMISave.setEnabled(true);
	}

	/**
	 * Creates a dialog to be displayed when the user tries to exit and displays
	 * it, prompting the user if they wish to exit in case they have made a
	 * mistake
	 */
	private void loadExitDialog() {
		dialog = new OptionsDialog(new JFrame(), "Exit",
				"Are you sure you wish to exit?", "option");

		if (dialog.getChoice() == 1) {
			recordsGUI.frame.dispose();
			System.exit(0);
		}
	}

	/**
	 * Displays the next student record object details from the student records
	 * store
	 */
	private void nextPointer() {
		store.nextRecordPointer();
		refreshRecordPointer();
		recordsGUI.setIsEmpty(false);
		setRecord();
	}

	/**
	 * Displays the previous student record object details from the student
	 * records store
	 */
	private void previousPointer() {
		store.previousRecordPointer();
		refreshRecordPointer();
		recordsGUI.setIsEmpty(false);
		setRecord();
	}

	/**
	 * Displays the first student record object details from the student records
	 * store
	 */
	private void firstPointer() {
		store.firstRecordPointer();
		refreshRecordPointer();
		recordsGUI.setIsEmpty(false);
		setRecord();
	}

	/**
	 * Displays the last student record object details from the student records
	 * store
	 */
	private void lastPointer() {
		store.lastRecordPointer();
		refreshRecordPointer();
		recordsGUI.setIsEmpty(false);
		setRecord();
	}

	/**
	 * Removes a student record from the student record store, in which one of
	 * two methods will be run depending on the mode of the GUI. I.e. if the
	 * user made a 'search' then the results of that search (or filter) will
	 * have created a new student records store to navigate through, which
	 * consists of only those 'filtered' student records. This has an impact
	 * when trying to remove a record, as ideally if the user has performed a
	 * search, then we want to keep the results of that search. So performing
	 * this search will either remove the record from the main store only or
	 * both the main store AND filter store
	 */
	private void removeRecord() {
		dialog = new OptionsDialog(new JFrame(), "Remove",
				"Are you sure you want to delete the current record?", "option");

		if (dialog.getChoice() == 1) {

			if (didSearch) {
				remove_fromSearched();

			} else {
				remove_fromNotSerached();
			}
		}
	}

	/**
	 * Removes a student records from the student records store and is called if
	 * the user performs a remove record after a student record search has been
	 * made
	 * 
	 * @see #removeRecord()
	 */
	private void remove_fromSearched() {
		Store tempStore2 = new Store();
		executeCorrectFileInMethod();

		for (int i = 0; i < store.recordArrayList.size(); i++) {
			if (!(store.recordArrayList.get(i).getStudentID()
					.equalsIgnoreCase(recordsGUI.studentIDTextField.getText()))) {
				tempStore2.recordArrayList.add(store.recordArrayList.get(i));
			}
		}

		store = tempStore2;
		saveFile();
		store.firstRecordPointer();

		refreshRecordPointer();
		recordsGUI.searchBarTextField.setText("");
		setRecord();
		didSearch = false;
	}

	/**
	 * Removes a student records from the student records store and is called if
	 * the user performs a remove record after a student record search hasn't
	 * been made
	 * 
	 * @see #removeRecord()
	 */
	private void remove_fromNotSerached() {
		store.removeRecord(store.currentRecord());
		saveFile();

		if (store.isEmpty()) {
			recordsGUI.setIsEmpty(true);
			recordsGUI.setWarningMessage("There are no account "
					+ "records in this store");
			setRecord();

		} else {

			if (store.isFirst()) {
				store.nextRecordPointer();
				refreshRecordPointer();
				recordsGUI.setIsEmpty(false);
				setRecord();
			} else {
				store.lastRecordPointer();
				refreshRecordPointer();
				recordsGUI.setIsEmpty(false);
				setRecord();
			}
		}
	}

	/**
	 * Sets the GUI to be in 'edit' mode - i.e. specific text fields become
	 * editable and navigation buttons are set to disabled
	 */
	private void setEditMode() {
		recordsGUI.isEditing = true;
		recordsGUI.setTextFieldsEditable(true);
		recordsGUI.setWarningMessage("Be sure to click the save button when "
				+ "alterations are complete");
		recordsGUI.setEditNavButtonMode(true);
	}

	/**
	 * Sets the GUI to be in 'new record' mode - i.e. A new element is added to
	 * the end of the student record object array list in the store object and
	 * the text fields are set to empty
	 */
	private void setNewRecordMode() {
		if (recordsGUI.isEditing) {
			isNewRecord = false;
			recordsGUI.setTextFieldsEditable(false);
			recordsGUI.setWarningMessage("use the navigation bar below to "
					+ "move through records, create, modify or remove");
			recordsGUI.setEditNavButtonMode(false);

			// can't create a new record if no store is loaded
			if (store.isEmpty()) {
				recordsGUI.setIsEmpty(true);
				recordsGUI.setWarningMessage("There is no store currently "
						+ "loaded");
			} else {
				recordsGUI.setIsFirst(store.isFirst());
				recordsGUI.setIsLast(store.isLast());
			}
			// save the addition student record
			if (saveAttempted) {
				store.lastRecordPointer();
				store.removeRecord(store.currentRecord());
				store.lastRecordPointer();
				refreshRecordPointer();
				setRecord();
				// if the user tries to save nothing
				if (store.isEmpty()) {
					recordsGUI.setWarningMessage("There is no store currently "
							+ "loaded");
					recordsGUI.setIsEmpty(true);
				}
			} else {
				setRecord();
			}

		} else {
			didSearch = false;
			isNewRecord = true;
			recordsGUI.setTextFieldsEditable(true);
			recordsGUI
					.setWarningMessage("Be sure to click the save button when "
							+ "accounts details are complete");
			recordsGUI.setEditNavButtonMode(true);
			recordsGUI.setIsEmpty(true);
			setRecord();
		}
	}

	/**
	 * Saves the current store to a file that has already been loaded The method
	 * in which the save is performed will be different depending on what 'mode'
	 * the GUI is currently in i.e. if a search has been made or if a new record
	 * has been created. This type of save is executed by the program
	 * automatically rather than via a user command
	 * 
	 * @see #removeRecord()
	 */
	private void saveStore() {
		if ((recordsGUI.isEditing == true) && (isNewRecord == false)) {
			validationIsOK = 0;
			validateAndStore();
			if (validationIsOK == 11) {
				recordsGUI.setTextFieldsEditable(false);
				recordsGUI.setEditNavButtonMode(false);
				refreshRecordPointer();
				recordsGUI.setWarningMessage("use the navigation bar below to"
						+ " move through "
						+ "records, create, modify or remove");

				if (didSearch) {
					save_searched();
				}
				saveFile();
			}

		} else if (isNewRecord) {
			save_newRecord();

		}
	}

	/**
	 * Performs the save operation when the GUI is in 'new record created' mode
	 * 
	 * @see #saveStore()
	 */
	private void save_newRecord() {
		store.addRecord(new StudentRecord(recordsGUI.studentIDTextField
				.getText()));
		store.lastRecordPointer();
		validationIsOK = 0;

		validateAndStore();

		if (validationIsOK > 10) {
			recordsGUI.setTextFieldsEditable(false);
			recordsGUI.setEditNavButtonMode(false);
			refreshRecordPointer();
			recordsGUI.setWarningMessage("use the navigation bar below to "
					+ "move through records, " + "create, modify or remove");
			saveFile();
			executeCorrectFileInMethod();
			isNewRecord = false;
			store.lastRecordPointer();
		}
	}

	/**
	 * Performs the save operation when the GUI is in 'hasSearched' mode
	 * 
	 * @see #saveStore()
	 */
	private void save_searched() {
		Store tempStore2 = new Store();
		Store tempStore3 = new Store();

		tempStore3.recordArrayList.add(store.currentRecord());

		executeCorrectFileInMethod();

		for (int i = 0; i < store.recordArrayList.size(); i++) {
			if (!(store.recordArrayList.get(i).getStudentID()
					.equalsIgnoreCase(recordsGUI.studentIDTextField.getText()))) {
				tempStore2.recordArrayList.add(store.recordArrayList.get(i));
			}
		}
		tempStore2.recordArrayList.add(tempStore3.recordArrayList.get(0));

		store = tempStore2;
	}

	/**
	 * Saves the current store to a file specified by the user. The file name
	 * and file location is set via the Swing's file chooser dialog which
	 * prompts the user. This method also allows a user to decide if they wish
	 * to override an existing file or not, which is in the form of another
	 * dialog, created also by this method
	 */
	private void saveFileAs() {

		// create a 'text filter object' which is used to filter the files
		// shown in Swings file chooser to only show files with a specified file
		// extension
		int returnVal3 = 0;

		fc2 = new JFileChooser();
		TextFilter tf3 = new TextFilter(".dat");

		fc2.setCurrentDirectory(new File("./data/"));
		fc2.setFileFilter(tf3);
		returnVal3 = fc2.showSaveDialog(recordsGUI.menuItem_saveAs);

		// if the user the user has clicked 'save' or 'OK' and a file
		// and location has been set
		if (returnVal3 == JFileChooser.APPROVE_OPTION) {
			try {
				saveAsFileChooser = fc2.getSelectedFile();

				// if the file trying to save is already exists
				if (saveAsFileChooser.exists()) {
					dialog = new OptionsDialog(new JFrame(),
							"File Name Already Exists",
							"Would you like to overwrite the existing file?",
							"option");

					// either overwrite or not depending on users choice from
					// the created dialog
					if (dialog.getChoice() == 1) {
						store.fileOut(saveAsFileChooser.getName());
						currentFileName = saveAsFileChooser.getName();
						isRMIStore = false;
						recordsGUI
								.setWarningMessage("Store successfully saved");
						displayMessageTimer.start();
						recordsGUI.setWarningMessage2("Using local store");

					} else if (dialog.getChoice() == 0) {
						dialog.setChoice(1);

					}

					// if file doesn't exist, just save
				} else {
					store.fileOut(saveAsFileChooser.getName());
					currentFileName = saveAsFileChooser.getName();
					setRecord();
					recordsGUI.setWarningMessage("Store successfully saved");
					recordsGUI.setWarningMessage2("Using local store");
					displayMessageTimer.start();
					isRMIStore = false;
				}

				// any problems with IO
			} catch (Exception k) {
				recordsGUI.setWarningMessage("Could Not Save File");
				System.err.println("Could not save file");
			}
		}
	}

	/**
	 * Runs the actual methods to save the student records store file - This
	 * method is called as an end result determined by a complicated series of
	 * validations by prior 'pre-save' methods. It will run one of two save
	 * operation based on the 'mode' of the GUI. I.e. a RMI save will be
	 * performed if the store is a remote store or a local save is the store is
	 * local
	 * 
	 * @see #saveStore()
	 * @see #saveFileAs()
	 * @see #save_searched()
	 * @see #save_newRecord()
	 */
	public void saveFile() {
		if (isRMIStore) {
			saveRMIFile();
		} else {
			store.fileOut(currentFileName);
		}
	}

	/**
	 * Saves the current store to a pre-defined RMI location
	 * 
	 * @see #saveFile()
	 */
	protected void saveRMIFile() {
		try {
			rmiClient.connectToServer();
			System.out.println("Successfully Connected");
			rmiClient.saveStoreToRMI(store);
			System.out.println("Store upload successful");
			recordsGUI.setWarningMessage("Successfully saved to RMI location");
			displayMessageTimer.start();

		} catch (MalformedURLException e) {
			recordsGUI.setWarningMessage("RMI Conneciton failure "
					+ "(incorrect URL syntax) please try again");
			System.err.println("Connection failure: Incorrect RMI url syntax");
			e.printStackTrace();
		} catch (NotBoundException e) {
			recordsGUI.setWarningMessage("RMI Conneciton failure "
					+ "(RMI server not bound) please try again");
			System.err.println("Connection failure: RMI server not bound");
			e.printStackTrace();
		} catch (ConnectException e) {
			recordsGUI.setWarningMessage("RMI Conneciton failure "
					+ "(transport problems) please try again");
			System.err
					.println("Connection failure: transport or connection failure");
			e.printStackTrace();
		} catch (RemoteException e) {
			recordsGUI
					.setWarningMessage("RMI Conneciton failure, please try again");
			System.err.println("Connection failure: Some remote exception");
			e.printStackTrace();
		}
	}

	/**
	 * Performs a search through the student records store and created a new
	 * filtered store which is then displayed. The search is performed on the
	 * student ID number or the student surname, from the value in the 'search'
	 * text field. The search performed is not a 'whole word match' but can be a
	 * substring of the searched results
	 */
	private void search() {
		// i.e. if it's a RMI store file or local store file
		executeCorrectFileInMethod();
		filteredStore = new Store(); // to store the results in

		// if the search field is empty, then reset to show all student records
		// in the store
		// ... if there's been no search beforehand however, then don't bother
		// wasting processing
		// time, just do nothing
		if (recordsGUI.searchBarTextField.getText().equalsIgnoreCase("")
				|| (recordsGUI.searchBarTextField.getText() == null)) {

			if (didSearch) {
				didSearch = false;
				executeCorrectFileInMethod();
				refreshRecordPointer();
				setRecord();
			}
			// perform the search
		} else {
			// if search field only contains number, we know we're searching
			// with
			// the student ID number, so use this as search parameter
			if (NumFunc.containsOnlyNumbers(recordsGUI.searchBarTextField
					.getText())) {

				// iterate through student record array list in student record
				// store object
				// and do a 'substring' match
				for (int i = 0; i < store.recordArrayList.size(); i++) {
					if (store.recordArrayList.get(i).getStudentID()
							.contains(recordsGUI.searchBarTextField.getText())) {
						filteredStore.recordArrayList.add(store.recordArrayList
								.get(i));
					}
				}

				// we have no made a search, make a note of this
				// because it will affect future operations
				didSearch = true;
				store = filteredStore;
				refreshRecordPointer();
				setRecord();

				if (store.isEmpty()) {
					executeCorrectFileInMethod();
					recordsGUI
							.setWarningMessage("There are no records matching "
									+ "your search");
				}
				// if the search field contains letters, then use surname as
				// search parameter
			} else {
				for (int i = 0; i < store.recordArrayList.size(); i++) {

					if (store.recordArrayList
							.get(i)
							.getLastName()
							.toLowerCase()
							.indexOf(
									recordsGUI.searchBarTextField.getText()
											.toLowerCase()) > -1) {
						filteredStore.recordArrayList.add(store.recordArrayList
								.get(i));
					}
				}
				didSearch = true;
				store = filteredStore;
				refreshRecordPointer();
				setRecord();
				if (store.isEmpty()) {
					recordsGUI.setIsEmpty(true);
					recordsGUI
							.setWarningMessage("There are no records matching "
									+ "your search");
				}
			}
		}
	}

	/**
	 * Is called when there exists no student records in a store, and it sets
	 * the appropriate GUI 'mode' - i.e. buttons disabled and text fields
	 * un-editable
	 */
	private void setNoRecordsAvailableMode() {
		if (store.isEmpty()) {

			recordsGUI.setWarningMessage("There are no account records in "
					+ "this store");
			recordsGUI.setIsEmpty(true);
			recordsGUI.newRecordNavButton.setEnabled(true);

		} else {

			recordsGUI.setWarningMessage("use the navigation bar below to "
					+ "move through records, create, modify or remove");
			recordsGUI.setNavButtonsEnabled(true);
			refreshRecordPointer();
		}
	}

	/**
	 * Loads a student records store from a pre-defined RMI location
	 */
	protected void openRMIFile() {

		try {
			rmiClient.connectToServer();
			System.out.println("Successfully connected");
			store = rmiClient.loadStoreFromRMI(false);

			store.firstRecordPointer();
			setRecord();
			recordsGUI.setWarningMessage("RMI Store successfully loaded");
			recordsGUI.setWarningMessage2("Using Remote Store");

			isRMIStore = true;

			recordsGUI.menuItem_saveAs.setEnabled(true);
			recordsGUI.menuItem_RMISave.setEnabled(true);
			isConnectedToRMI = true;

		} catch (MalformedURLException e) {
			isConnectedToRMI = false;
		} catch (NotBoundException e) {
			System.err.println("Connection failure");
			e.printStackTrace();
			isConnectedToRMI = false;
		} catch (ConnectException e) {
			isConnectedToRMI = false;
		} catch (RemoteException e) {
			System.err.println("Connection failure");
			e.printStackTrace();
			isConnectedToRMI = false;
		}

		if (isConnectedToRMI == false) {
			recordsGUI
					.setWarningMessage("RMI Conneciton failure, please try again");
		} else {
			setNoRecordsAvailableMode();
		}
	}

	/**
	 * Loads a student records store from a local file, which is determined via
	 * the user's choice when the select a file from the Swing file chooser
	 * created in this method.
	 */
	private void openFile() {
		// create a 'text filter object' which is used to filter the files
		// shown in Swings file chooser to only show files with a specified file
		// extension
		int returnVal = 0;
		fc = new JFileChooser();
		TextFilter tf = new TextFilter(".dat");

		// location is 'current directory + data folder'
		fc.setCurrentDirectory(new File("./data/"));
		fc.setFileFilter(tf);
		returnVal = fc.showOpenDialog(recordsGUI.menuItem_open);

		if (returnVal == JFileChooser.APPROVE_OPTION) {

			try {
				file = fc.getSelectedFile();
				store = store.fileIn(file.getPath());
				currentFileName = file.getName();
				store.firstRecordPointer();

				setRecord();

				recordsGUI.menuItem_saveAs.setEnabled(true);
				recordsGUI.menuItem_RMISave.setEnabled(true);

				isRMIStore = false;

				setNoRecordsAvailableMode();

			} catch (Exception k) {
				System.err.println("could not load file");
				recordsGUI.setWarningMessage("Error loading file");
				recordsGUI.setIsEmpty(true);
			}
		}
	}

	/**
	 * Method to update the student records photo image. It uses Swing's file
	 * chooser to specify the image and save it to the student record object
	 */
	private void updatePhoto() {
		// create a 'text filter object' which is used to filter the files
		// shown in Swings file chooser to only show files with a specified file
		// extension
		int returnVal2 = 0;
		fc3 = new JFileChooser();
		TextFilter tf3 = new TextFilter(".jpg");

		// location is 'current directory + data folder'
		fc3.setCurrentDirectory(new File("./res/images"));
		fc3.setFileFilter(tf3);
		returnVal2 = fc3.showOpenDialog(recordsGUI.editImageButton);

		if (returnVal2 == JFileChooser.APPROVE_OPTION) {
			try {
				filePic = fc3.getSelectedFile();
				recordsGUI.photo = new ImageIcon(filePic.getPath());
				store.currentRecord().setImage(recordsGUI.photo);
				recordsGUI.image.setIcon(recordsGUI.photo);
				validateAndStore();

				saveFile();
			} catch (Exception k) {
				recordsGUI
						.setWarningMessage("Wrong file size or type or file corrupt. Please retry");
				System.err.println("wrong file size or type or file currupt");
			}
		}
	}

	/**
	 * Sets the appropriate text fields to show the details of the student
	 * record object
	 */
	public void setRecord() {
		if ((store.isEmpty() == false) && (isNewRecord == false)) {
			recordsGUI.studentIDTextField.setText(store.currentRecord()
					.getStudentID());
			recordsGUI.pinNumberTextField.setText(store.currentRecord()
					.getPinNo());
			recordsGUI.titleTextField.setText(store.currentRecord().getTitle());
			recordsGUI.firstNameTextField.setText(store.currentRecord()
					.getFirstName());
			recordsGUI.lastNameTextField.setText(store.currentRecord()
					.getLastName());
			recordsGUI.firstLineAddrTextField.setText(store.currentRecord()
					.getFirstLineAddress());
			recordsGUI.secondLineAddrTextField.setText(store.currentRecord()
					.getSecondLineAddress());
			recordsGUI.cityTextField.setText(store.currentRecord().getCity());
			recordsGUI.countyTextField.setText(store.currentRecord()
					.getCounty());
			recordsGUI.postCodeTextField.setText(store.currentRecord()
					.getPostCode());
			recordsGUI.telephoneTextField.setText(store.currentRecord()
					.getTelephone());
			recordsGUI.courseTextField.setText(store.currentRecord()
					.getCourse());

			recordsGUI.rfidTagIDTextField.setText(store.currentRecord()
					.getRFIDTagID());

			if (recordsGUI.rfidTagIDTextField.getText().isEmpty()
					|| recordsGUI.rfidTagIDTextField.getText()
							.equalsIgnoreCase("")) {
				recordsGUI.rfidTagIDTextField.setText("Unassigned");
			}
			if (store.currentRecord().getImage() != null) {
				recordsGUI.image.setIcon(store.currentRecord().getImage());
			} else {
				recordsGUI.photo = new ImageIcon(defaultPhoto);
				recordsGUI.image.setIcon(recordsGUI.photo);
			}

		} else if ((store.isEmpty() == true) && (isNewRecord == false)) {
			recordsGUI.studentIDTextField.setText("");
			recordsGUI.pinNumberTextField.setText("");

			clearTextFields();

		} else if (isNewRecord) {
			createNewRecord();
		}
	}

	/**
	 * Clears the text fields on the Record GUI
	 */
	private void clearTextFields() {
		recordsGUI.titleTextField.setText("");
		recordsGUI.firstNameTextField.setText("");
		recordsGUI.lastNameTextField.setText("");
		recordsGUI.firstLineAddrTextField.setText("");
		recordsGUI.secondLineAddrTextField.setText("");
		recordsGUI.cityTextField.setText("");
		recordsGUI.countyTextField.setText("");
		recordsGUI.postCodeTextField.setText("");
		recordsGUI.telephoneTextField.setText("");
		recordsGUI.photo = new ImageIcon(defaultPhoto);
		recordsGUI.image.setIcon(recordsGUI.photo);
		recordsGUI.courseTextField.setText("");
		recordsGUI.rfidTagIDTextField.setText("");
	}

	/**
	 * Creates a new student record for the student records store. It also
	 * determines the student ID number based on what is currently stored in the
	 * store object and randomly generates a student pin number (this pin number
	 * can be changed at a later date, the student ID number however has to be
	 * unique, and it cannot be changed).
	 */
	private void createNewRecord() {
		if (store.isEmpty()) {
			recordsGUI.studentIDTextField.setText("4000001");
		} else {
			int lastAccountNumber = NumFunc.stringToInt(store.recordArrayList
					.get(store.recordArrayList.size() - 1).getStudentID());

			recordsGUI.studentIDTextField.setText(NumFunc
					.numberToString(lastAccountNumber + 1));

		}
		recordsGUI.pinNumberTextField.setText(NumFunc
				.numberToString(randomPinGenerator()));

		clearTextFields();
	}

	/**
	 * Generates a random 4 digit pin number
	 * 
	 * @return actualPinNumber The randomly generated pin number
	 */
	private int randomPinGenerator() {
		Random generator = new Random();
		int randomNumber = generator.nextInt(9999) + 1000;
		int actualPinNumber = 0;

		if (randomNumber > 9999) {
			actualPinNumber = randomNumber - 1000;

		} else {
			actualPinNumber = randomNumber;
		}

		return actualPinNumber;
	}

	/**
	 * Method to validate the values in the text fields of the Records GUI upon
	 * a 'save edit' or 'save new' record execution before performing the actual
	 * save to file operation. It validates the fields to discover a possible
	 * human error entry in one or more of the fields and informs. An example is
	 * making sure that the telephone text field has no letters in it. Some of
	 * the validation is done by an instance of the 'Field Validator' class
	 */
	private void validateAndStore() {
		if (FieldValidator.validatePinNumber(recordsGUI.pinNumberTextField
				.getText())) {
			store.currentRecord().setPinNo(
					recordsGUI.pinNumberTextField.getText());
			validationIsOK++;
			saveAttempted = false;
		} else {
			validationIsOK--;
			saveAttempted = true;
			recordsGUI
					.setWarningMessage("Make sure the pin number field is 4 digits "
							+ "long between 1000 and 9999");
		}
		if (FieldValidator
				.validateStringsFieldsNumbersNotAllowed(recordsGUI.titleTextField
						.getText())) {
			validationIsOK++;
			saveAttempted = false;
			store.currentRecord().setTitle(recordsGUI.titleTextField.getText());
		} else {
			validationIsOK--;
			saveAttempted = true;
			recordsGUI
					.setWarningMessage("Make sure there are no invalid characters "
							+ "in the title field (i.e. $%^&) or numebrs");
		}
		if (FieldValidator
				.validateStringsFieldsNumbersNotAllowed(recordsGUI.firstNameTextField
						.getText())) {
			validationIsOK++;
			saveAttempted = false;
			store.currentRecord().setFirstName(
					recordsGUI.firstNameTextField.getText());
		} else {
			validationIsOK--;
			saveAttempted = true;
			recordsGUI
					.setWarningMessage("Make sure there are no invalid characters "
							+ "in the first name field (i.e. $%^&)");
		}
		if (FieldValidator
				.validateStringsFieldsNumbersNotAllowed(recordsGUI.lastNameTextField
						.getText())) {
			validationIsOK++;
			saveAttempted = false;
			saveAttempted = false;
			store.currentRecord().setLastName(
					recordsGUI.lastNameTextField.getText());
		} else {
			validationIsOK--;
			saveAttempted = true;
			recordsGUI
					.setWarningMessage("Make sure there are no invalid characters "
							+ "in the last name field (i.e. $%^&)");
		}
		if (FieldValidator
				.validateStringsFieldsNumbersAllowed(recordsGUI.firstLineAddrTextField
						.getText())) {
			validationIsOK++;
			saveAttempted = false;
			store.currentRecord().setFirstLineAddress(
					recordsGUI.firstLineAddrTextField.getText());
		} else {
			validationIsOK--;
			saveAttempted = true;
			recordsGUI
					.setWarningMessage("Make sure there are no invalid characters "
							+ "in the first line address field (i.e. $%^&)");
		}
		if (FieldValidator
				.validateStringsFieldsNumbersAllowed(recordsGUI.secondLineAddrTextField
						.getText())) {
			validationIsOK++;
			saveAttempted = false;
			store.currentRecord().setSecondLineAddress(
					recordsGUI.secondLineAddrTextField.getText());
		} else {
			validationIsOK--;
			saveAttempted = true;
			recordsGUI
					.setWarningMessage("Make sure there are no invalid characters "
							+ "in the second line address field (i.e. $%^&)");
		}
		if (FieldValidator
				.validateStringsFieldsNumbersNotAllowed(recordsGUI.cityTextField
						.getText())) {
			validationIsOK++;
			saveAttempted = false;
			store.currentRecord().setCity(recordsGUI.cityTextField.getText());
		} else {
			validationIsOK--;
			saveAttempted = true;
			recordsGUI
					.setWarningMessage("Make sure there are no invalid characters "
							+ "in the city field (i.e. $%^&) or numbers");
		}
		if (FieldValidator
				.validateStringsFieldsNumbersNotAllowed(recordsGUI.countyTextField
						.getText())) {
			validationIsOK++;
			saveAttempted = false;
			store.currentRecord().setCounty(
					recordsGUI.countyTextField.getText());
		} else {
			validationIsOK--;
			saveAttempted = true;
			recordsGUI
					.setWarningMessage("Make sure there are no invalid characters "
							+ "in the county field (i.e. $%^&) or numbers");
		}
		if (FieldValidator
				.validateStringsFieldsNumbersAllowed(recordsGUI.postCodeTextField
						.getText())) {
			validationIsOK++;
			saveAttempted = false;
			store.currentRecord().setPostCode(
					recordsGUI.postCodeTextField.getText());
		} else {
			validationIsOK--;
			saveAttempted = true;
			recordsGUI
					.setWarningMessage("Make sure there are no invalid characters "
							+ "in the post code field (i.e. $%^&)");
		}
		if (FieldValidator.validateTelephone(recordsGUI.telephoneTextField
				.getText())) {
			validationIsOK++;
			saveAttempted = false;
			store.currentRecord().setTelephone(
					recordsGUI.telephoneTextField.getText());
		} else {
			validationIsOK--;
			saveAttempted = true;
			recordsGUI
					.setWarningMessage("Make sure there are no invalid characters "
							+ "in the telephone field (i.e. $%^&)");
		}

		store.currentRecord().setCourse(recordsGUI.courseTextField.getText());

		if (relaventFieldsAreEmpty()) {
			validationIsOK--;
			recordsGUI
					.setWarningMessage("You must at least fill in the title, name, course, "
							+ "first line address, city, county and post code fields");
			saveAttempted = true;
		} else {
			validationIsOK++;
			saveAttempted = false;
		}
	}

	/**
	 * Method to check if any required fields are empty before performing a
	 * store save
	 * 
	 * @return someFieldsAreEmpty If they are empty or not
	 */
	private boolean relaventFieldsAreEmpty() {
		boolean someFieldsAreEmpty = false;
		if (recordsGUI.firstNameTextField.getText().equalsIgnoreCase("")
				|| recordsGUI.titleTextField.getText().equalsIgnoreCase("")
				|| recordsGUI.lastNameTextField.getText().equalsIgnoreCase("")
				|| recordsGUI.firstLineAddrTextField.getText()
						.equalsIgnoreCase("")
				|| recordsGUI.postCodeTextField.getText().equalsIgnoreCase("")
				|| recordsGUI.cityTextField.getText().equalsIgnoreCase("")
				|| recordsGUI.countyTextField.getText().equalsIgnoreCase("")
				|| recordsGUI.courseTextField.getText().equalsIgnoreCase("")) {

			someFieldsAreEmpty = true;
		} else {
			someFieldsAreEmpty = false;
		}

		return someFieldsAreEmpty;
	}
}
