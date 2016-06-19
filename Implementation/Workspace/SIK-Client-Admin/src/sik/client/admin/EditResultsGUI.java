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
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * A GUI for editing and displaying the assignment results from a student record
 * 
 * @author Jamie Brindle (06352322) - Manchester Metropolitan University *
 */
public class EditResultsGUI implements ActionListener {

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
	private JPanel controlArea, resultsArea;

	// layouts
	private GridBagConstraints controlLayout, resultsAreaLayout;

	// navigation properties
	private JButton saveCtrlButton, editCtrlButton, clearAllCtrlButton;

	// menu properties
	protected JMenuBar menuBar;
	protected JMenu menu;
	protected JMenuItem menuItem3;

	// results properties
	private ArrayList<JTextField> resultsArrayList;
	private ArrayList<String> resultsArrayListString;
	private boolean isResultsNull = true;

	// GUI 'mode'
	private boolean isEditing = false;

	/**
	 * Class constructor
	 * 
	 * @param recordsGL
	 *            The parent class which called this class
	 */
	public EditResultsGUI(RecordsGUIListener recordsGL) {
		this.recordsGL = recordsGL;
		resultsArrayList = new ArrayList<JTextField>();
		resultsArrayListString = new ArrayList<String>();
	}

	/**
	 * Action Event handler for this class
	 * 
	 * @param e
	 *            The action event *
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

		if (recordsGL.store.currentRecord().getResults() == null) {
			isResultsNull = true;
		} else {
			isResultsNull = false;
		}

		frame = new JFrame("Assignment Results Editor GUI");

		// add a window listener to this frame
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

		resultsArea = new JPanel(new GridBagLayout());
		resultsAreaLayout = new GridBagConstraints();

		controlArea = new JPanel(new GridBagLayout());
		controlLayout = new GridBagConstraints();

		// load GUI elements
		loadMenu();
		loadControlButtons();
		loadResultsArea();

		// add elements to container
		container.add(resultsArea, BorderLayout.CENTER);
		container.add(controlArea, BorderLayout.SOUTH);

		frame.setJMenuBar(menuBar);
		frame.setResizable(false);

		// calls methods to centre this frame on the screen
		centreFrame();

		// prepare and display the frame (GUI)
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * Sets frame of this GUI to be centred on the screen when displayed
	 */
	private void centreFrame() {
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		frame.setLocation(screenSize.width / 4, screenSize.height / 4);
	}

	/**
	 * Clears all the results in the results array list and set GUI text fields
	 * appropriately
	 */
	private void clear() {
		for (int i = 0; i < resultsArrayList.size(); i++) {
			resultsArrayList.get(i).setText("");
		}
	}

	/**
	 * Saves the results to the student record object via the student store
	 * object in the records GUI listener class and also sets appropriate GUI
	 * 'mode'
	 */
	public void save() {
		resultsArrayListString.clear();
		for (JTextField results : resultsArrayList) {
			resultsArrayListString.add(results.getText());
		}
		recordsGL.store.currentRecord().setResults(resultsArrayListString);
		recordsGL.saveFile();
		isEditing = false;

		setTextAreasEditable(false);
		saveCtrlButton.setEnabled(false);
		clearAllCtrlButton.setEnabled(false);
		editCtrlButton.setText("Edit");
	}

	/**
	 * Sets the GUI to be in 'edit mode'. Also, if there are no results
	 * available in the student record object in the records GUI listener class
	 * it sets the result fields to be of text "" in order to not try and save
	 * null values
	 */
	private void setEditing() {
		if (isEditing) {
			isEditing = false;
			setTextAreasEditable(false);
			editCtrlButton.setText("Edit");
			saveCtrlButton.setEnabled(false);
			clearAllCtrlButton.setEnabled(false);

			if (!(isResultsNull)) {
				for (int i = 0; i < resultsArrayList.size(); i++) {
					resultsArrayList.get(i)
							.setText(
									recordsGL.store.currentRecord()
											.getResults().get(i));
				}
			} else {
				for (int i = 0; i < resultsArrayList.size(); i++) {
					resultsArrayList.get(i).setText("");
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
	 * Prepares the results area panel elements of this GUI
	 */
	private void loadResultsArea() {
		resultsAreaLayout.anchor = GridBagConstraints.CENTER;
		resultsAreaLayout.insets = new Insets(5, 5, 5, 5);

		resultsAreaLayout.gridy = 0;

		// Add title labels
		resultsArea.add(new JLabel("Unit ID"), resultsAreaLayout);
		resultsArea.add(new JLabel("Unit Name"), resultsAreaLayout);
		resultsArea.add(new JLabel("Assignment 1"), resultsAreaLayout);
		resultsArea.add(new JLabel("Assignment 2"), resultsAreaLayout);
		resultsArea.add(new JLabel("Exam"), resultsAreaLayout);

		int charLength = 0;

		// set of appropriate text field sizes
		for (int i = 0; i < 30; i++) {

			if ((i == 0) || (i == 5) || (i == 10) || (i == 15) || (i == 20)
					|| (i == 25)) {
				charLength = 8;
			} else if ((i == 1) || (i == 6) || (i == 11) || (i == 16)
					|| (i == 21) || (i == 26)) {
				charLength = 25;
			} else if ((i == 2) || (i == 7) || (i == 12) || (i == 17)
					|| (i == 22) || (i == 27)) {
				charLength = 5;
			} else if ((i == 3) || (i == 8) || (i == 13) || (i == 18)
					|| (i == 23) || (i == 28)) {
				charLength = 5;
			} else if ((i == 4) || (i == 9) || (i == 14) || (i == 19)
					|| (i == 24) || (i == 29)) {
				charLength = 5;
			}

			// add text field to array list in order for perform attribute
			// modifications to those text fields via a for loop (minimising
			// amount of code and increasing possible future maintenance)
			resultsArrayList.add(new JTextField(charLength));
			resultsArrayList.get(i).setHorizontalAlignment(
					SwingConstants.CENTER);

			if (!(isResultsNull)) {
				resultsArrayList.get(i).setText(
						recordsGL.store.currentRecord().getResults().get(i));
			}

			resultsArrayList.get(i).setEditable(false);
		}

		// 1st unit

		resultsAreaLayout.gridy = 1;

		for (int i = 0; i < 5; i++) {
			resultsArea.add(resultsArrayList.get(i), resultsAreaLayout);
		}

		// 2nd unit

		resultsAreaLayout.gridy = 2;

		for (int i = 5; i < 10; i++) {
			resultsArea.add(resultsArrayList.get(i), resultsAreaLayout);
		}

		// 3rd unit

		resultsAreaLayout.gridy = 3;

		for (int i = 10; i < 15; i++) {
			resultsArea.add(resultsArrayList.get(i), resultsAreaLayout);
		}

		// 4th unit

		resultsAreaLayout.gridy = 4;

		for (int i = 15; i < 20; i++) {
			resultsArea.add(resultsArrayList.get(i), resultsAreaLayout);
		}

		// 5th unit

		resultsAreaLayout.gridy = 5;

		for (int i = 20; i < 25; i++) {
			resultsArea.add(resultsArrayList.get(i), resultsAreaLayout);
		}

		// 6th unit

		resultsAreaLayout.gridy = 6;

		for (int i = 25; i < 30; i++) {
			resultsArea.add(resultsArrayList.get(i), resultsAreaLayout);
		}
	}

	/**
	 * Prepared control button panel elements of this GUI
	 */
	private void loadControlButtons() {

		controlLayout.insets = new Insets(10, 2, 5, 2);

		saveCtrlButton = new JButton("Save");
		saveCtrlButton.addActionListener(this);
		saveCtrlButton.setEnabled(false);
		controlArea.add(saveCtrlButton, controlLayout);

		editCtrlButton = new JButton("Edit");
		editCtrlButton.addActionListener(this);
		controlArea.add(editCtrlButton, controlLayout);

		clearAllCtrlButton = new JButton("Clear All");
		clearAllCtrlButton.addActionListener(this);
		clearAllCtrlButton.setEnabled(false);
		controlArea.add(clearAllCtrlButton, controlLayout);
	}

	/**
	 * Prepared menu elements of this GUI
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
		for (int i = 0; i < resultsArrayList.size(); i++) {
			resultsArrayList.get(i).setEditable(b);
		}
	}
}
