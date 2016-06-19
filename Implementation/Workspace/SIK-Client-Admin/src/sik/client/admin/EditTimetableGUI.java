package sik.client.admin;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * A GUI for editing and displaying the assignment results from a student record
 * 
 * @author Jamie Brindle (06352322) - Manchester Metropolitan University *
 */
public class EditTimetableGUI implements ActionListener {

	/**
	 * Default universal serializable class ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Parent class which created an instance of this class. Set as global in
	 * order to quickly access its methods and send back information
	 */
	protected RecordsGUIListener recordsGL;

	// frame properties
	private Container container;
	protected JFrame frame;

	// panels
	private JPanel timetableNameArea, controlArea, timetableArea;

	// layouts
	private GridBagConstraints timetableNameAreaLayout, navigationLayout,
			timetableAreaLayout;

	// navigation properties

	private JButton saveCtrlButton, editCtrlButton, clearAllCtrlButton;

	// menu properties
	protected JMenuBar menuBar;
	protected JMenu menu;
	protected JMenuItem menuItem3;

	// timetable area properties
	private JLabel timestableNameLabel;
	private JTextField timetableNameTextField;

	private ArrayList<JTextArea> timetableArrayList;
	private ArrayList<JScrollPane> timetableArrayListScrollPane;
	private ArrayList<String> timetableArrayListString;

	private boolean isTimestableNull = true;
	private boolean isEditing = false;

	/**
	 * Class constructor
	 * 
	 * @param recordsGL
	 *            The parent class which called this class
	 */
	public EditTimetableGUI(RecordsGUIListener recordsGL) {
		this.recordsGL = recordsGL;
		timetableArrayList = new ArrayList<JTextArea>();
		timetableArrayListScrollPane = new ArrayList<JScrollPane>();
		timetableArrayListString = new ArrayList<String>();
	}

	/**
	 * Action Event handler for this class
	 * 
	 * @param e
	 *            The action event
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == menuItem3) {
			frame.dispose();
		}
		if (e.getSource() == clearAllCtrlButton) {
			clear();
		}
		if (e.getSource() == saveCtrlButton) {
			save();
		}
		if (e.getSource() == editCtrlButton) {
			setEditing();
		}
	}

	/**
	 * Builds and displays the elements and their attributes of this GUI
	 */
	public void run() {

		if (recordsGL.store.currentRecord().getTimetable() == null) {
			isTimestableNull = true;
		} else {
			isTimestableNull = false;
		}

		frame = new JFrame("Timestable Editor GUI");

		// create new window listener
		frame.addWindowListener(new WindowAdapter() {
			/**
			 * Creates a new window closing event handler for this class
			 * 
			 * @param e
			 *            The key event
			 */
			public void windowClosing(WindowEvent e) {
				frame.dispose();
			}
		});

		// create new instances of using objects
		container = frame.getContentPane();
		container.setLayout(new BorderLayout());

		timetableNameArea = new JPanel(new GridBagLayout());
		timetableNameAreaLayout = new GridBagConstraints();

		timetableArea = new JPanel(new GridBagLayout());
		timetableAreaLayout = new GridBagConstraints();

		controlArea = new JPanel(new GridBagLayout());
		navigationLayout = new GridBagConstraints();

		// load GUI elements
		loadMenu();
		loadControlElements();
		loadTimetableNameArea();
		loadTimetableArea();

		// add elements to the container
		container.add(timetableNameArea, BorderLayout.NORTH);
		container.add(timetableArea, BorderLayout.CENTER);
		container.add(controlArea, BorderLayout.SOUTH);

		frame.setJMenuBar(menuBar);
		frame.setResizable(false);

		// calls method to centre frame on screen
		centreFrame();

		// prepare and display this GUI
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * Positions this class's frame to appear centred on the screen
	 */
	private void centreFrame() {
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		frame.setLocation(screenSize.width / 4, screenSize.height / 4);
	}

	/**
	 * Clears all the time table values from the time table array list and set
	 * GUI text fields appropriately
	 */
	private void clear() {
		for (int i = 0; i < timetableArrayList.size(); i++) {
			timetableArrayList.get(i).setText("");
		}
		timetableNameTextField.setText("");
	}

	/**
	 * Saves the timetable value to the student record object via the student
	 * store object in the records GUI listener class and also sets appropriate
	 * GUI 'mode'
	 */
	public void save() {
		timetableArrayListString.clear();
		for (JTextArea times : timetableArrayList) {
			timetableArrayListString.add(times.getText());
		}
		recordsGL.store.currentRecord().setTimestableName(
				timetableNameTextField.getText());
		recordsGL.store.currentRecord().setTimetable(timetableArrayListString);
		recordsGL.saveFile();
		isEditing = false;

		setTextAreasEditable(false);
		saveCtrlButton.setEnabled(false);
		clearAllCtrlButton.setEnabled(false);
		editCtrlButton.setText("Edit");
	}

	public void setEditing() {
		if (isEditing) {
			isEditing = false;
			setTextAreasEditable(false);
			editCtrlButton.setText("Edit");
			saveCtrlButton.setEnabled(false);
			clearAllCtrlButton.setEnabled(false);

			if (!(isTimestableNull)) {
				for (int i = 0; i < timetableArrayList.size(); i++) {
					timetableArrayList.get(i).setText(
							recordsGL.store.currentRecord().getTimetable()
									.get(i));
				}
				timetableNameTextField.setText(recordsGL.store.currentRecord()
						.getTimestableName());
			} else {
				for (JTextArea times : timetableArrayList) {
					times.setText("");
				}
			}
		} else {
			isEditing = true;
			setTextAreasEditable(true);
			editCtrlButton.setText("Cancel");
			saveCtrlButton.setEnabled(true);
			clearAllCtrlButton.setEnabled(true);
		}
	}

	/**
	 * Prepares the timetable name area panel elements of this GUI
	 */
	private void loadTimetableNameArea() {
		timetableNameAreaLayout.insets = new Insets(5, 5, 5, 5);

		timestableNameLabel = new JLabel("Timetable Name");
		timetableNameAreaLayout.anchor = GridBagConstraints.WEST;
		timetableNameArea.add(timestableNameLabel, timetableNameAreaLayout);

		timetableNameTextField = new JTextField(30);
		timetableNameTextField.setEditable(false);

		timetableNameArea.add(timetableNameTextField,
				timetableNameAreaLayout);
	}

	/**
	 * Prepares the timetable area panel elements of the GUI
	 */
	private void loadTimetableArea() {
		timetableAreaLayout.anchor = GridBagConstraints.CENTER;
		timetableAreaLayout.insets = new Insets(5, 5, 5, 5);

		addTimeFields();

		for (int i = 0; i < 45; i++) {

			timetableArrayList.add(new JTextArea(3, 8));

			if (!(isTimestableNull)) {
				timetableArrayList.get(i).setText(
						recordsGL.store.currentRecord().getTimetable().get(i));
			}

			timetableArrayList.get(i).setEditable(false);
			timetableArrayListScrollPane.add(new JScrollPane(
					timetableArrayList.get(i)));

		}
		if (!(isTimestableNull)) {
			timetableNameTextField.setText(recordsGL.store.currentRecord()
					.getTimestableName());
		}

		timetableAreaLayout.anchor = GridBagConstraints.WEST;

		addMondayField();
		addTuesdayField();
		addWednesdayField();
		addThursdayField();
		addFridayField();
	}

	/**
	 * Adds text fields to an array list in order for perform attribute
	 * modifications to those text fields via a for loop (minimising amount of
	 * code and increasing possible future maintenance)
	 */
	private void addTimeFields() {
		timetableArea.add(new JLabel(" "), timetableAreaLayout);
		timetableArea.add(new JLabel("9-10"), timetableAreaLayout);
		timetableArea.add(new JLabel("10-11"), timetableAreaLayout);
		timetableArea.add(new JLabel("11-12"), timetableAreaLayout);
		timetableArea.add(new JLabel("12-13"), timetableAreaLayout);
		timetableArea.add(new JLabel("13-14"), timetableAreaLayout);
		timetableArea.add(new JLabel("14-15"), timetableAreaLayout);
		timetableArea.add(new JLabel("15-16"), timetableAreaLayout);
		timetableArea.add(new JLabel("16-17"), timetableAreaLayout);
		timetableArea.add(new JLabel("17-18"), timetableAreaLayout);
	}

	/**
	 * Prepares the elements associates with the day 'Monday' of the timetable
	 * elements of this GUI. Times are stored in a one dimensional array so need
	 * to get the appropriate elements from the array depending on the day,
	 * which is done via a for loop.
	 */
	private void addMondayField() {
		timetableAreaLayout.gridy = 1;
		timetableArea.add(new JLabel("Monday"), timetableAreaLayout);

		timetableAreaLayout.insets = new Insets(5, 1, 5, 1);
		for (int i = 0; i < 9; i++) {
			timetableArea.add(timetableArrayListScrollPane.get(i),
					timetableAreaLayout);
		}
		timetableAreaLayout.insets = new Insets(5, 5, 5, 5);
	}

	/**
	 * Prepares the elements associates with the day 'Tuesday' of the timetable
	 * elements of this GUI. Times are stored in a one dimensional array so need
	 * to get the appropriate elements from the array depending on the day,
	 * which is done via a for loop.
	 */
	private void addTuesdayField() {
		timetableAreaLayout.gridy = 2;
		timetableArea.add(new JLabel("Tuesday"), timetableAreaLayout);

		timetableAreaLayout.insets = new Insets(5, 1, 5, 1);
		for (int i = 9; i < 18; i++) {
			timetableArea.add(timetableArrayListScrollPane.get(i),
					timetableAreaLayout);
		}
		timetableAreaLayout.insets = new Insets(5, 5, 5, 5);
	}

	/**
	 * Prepares the elements associates with the day 'Wednesday' of the
	 * timetable elements of this GUI. Times are stored in a one dimensional
	 * array so need to get the appropriate elements from the array depending on
	 * the day, which is done via a for loop.
	 */
	private void addWednesdayField() {
		timetableAreaLayout.gridy = 3;
		timetableArea.add(new JLabel("Wednesday"), timetableAreaLayout);

		timetableAreaLayout.insets = new Insets(5, 1, 5, 1);
		for (int i = 18; i < 27; i++) {
			timetableArea.add(timetableArrayListScrollPane.get(i),
					timetableAreaLayout);
		}
		timetableAreaLayout.insets = new Insets(5, 5, 5, 5);
	}

	/**
	 * Prepares the elements associates with the day 'Thursday' of the timetable
	 * elements of this GUI. Times are stored in a one dimensional array so need
	 * to get the appropriate elements from the array depending on the day,
	 * which is done via a for loop.
	 */
	private void addThursdayField() {
		timetableAreaLayout.gridy = 4;
		timetableArea.add(new JLabel("Thursday"), timetableAreaLayout);

		timetableAreaLayout.insets = new Insets(5, 1, 5, 1);
		for (int i = 27; i < 36; i++) {
			timetableArea.add(timetableArrayListScrollPane.get(i),
					timetableAreaLayout);
		}
		timetableAreaLayout.insets = new Insets(5, 5, 5, 5);
	}

	/**
	 * Prepares the elements associates with the day 'Friday' of the timetable
	 * elements of this GUI. Times are stored in a one dimensional array so need
	 * to get the appropriate elements from the array depending on the day,
	 * which is done via a for loop.
	 */
	private void addFridayField() {
		timetableAreaLayout.gridy = 5;
		timetableArea.add(new JLabel("Friday"), timetableAreaLayout);

		timetableAreaLayout.insets = new Insets(5, 1, 5, 1);
		for (int i = 36; i < 45; i++) {
			timetableArea.add(timetableArrayListScrollPane.get(i),
					timetableAreaLayout);
		}
		timetableAreaLayout.insets = new Insets(5, 5, 5, 5);
	}

	/**
	 * Prepares the control buttons area panel elements of this GUI
	 */
	private void loadControlElements() {
		navigationLayout.insets = new Insets(10, 2, 5, 2);

		saveCtrlButton = new JButton("Save");
		saveCtrlButton.addActionListener(this);
		saveCtrlButton.setEnabled(false);
		controlArea.add(saveCtrlButton, navigationLayout);

		editCtrlButton = new JButton("Edit");
		editCtrlButton.addActionListener(this);
		controlArea.add(editCtrlButton, navigationLayout);

		clearAllCtrlButton = new JButton("Clear All");
		clearAllCtrlButton.addActionListener(this);
		clearAllCtrlButton.setEnabled(false);
		controlArea.add(clearAllCtrlButton, navigationLayout);
	}

	/**
	 * Prepares the menu elements of this GUI
	 */
	private void loadMenu() {
		menuBar = new JMenuBar();
		menu = new JMenu("File");
		menuBar.add(menu);

		menuItem3 = new JMenuItem("Exit GUI");
		menuItem3.addActionListener(this);

		menu.add(menuItem3);
	}

	/**
	 * Sets whether the result area' text fields are editable or not, which is
	 * based on what mode the GUI is in (editing or viewing)
	 * 
	 * @param b
	 *            Which 'mode' to set to
	 */
	private void setTextAreasEditable(boolean b) {
		for (JTextArea times : timetableArrayList) {
			times.setEditable(b);
		}
		timetableNameTextField.setEditable(true);
	}
}
