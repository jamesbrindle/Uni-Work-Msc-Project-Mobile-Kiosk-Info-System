package sik.client.user.panes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import sik.client.user.FontStyles;
import sik.client.user.panels.HomePanel;

/**
 * Panel which is displayed as a content inside the home panel. As the home
 * panel displays a number of different complex panels corresponding to the
 * selected category this has been given the name 'pane' rather than 'panel'.
 * 
 * This particular pane is is displayed when the 'Timetable' button is pressed
 * on the home panel
 * 
 * @author Jamie Brindle (06352322) - Manchester Metropolitan University
 */
public class TimetablePane extends JPanel {

	/**
	 * Default universal serializable class ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Parent class which created an instance of this class. Set as global in
	 * order to quickly access its methods and send back information
	 */
	protected HomePanel homePanel;

	// internal panels
	private JPanel timetableNameArea, timetableArea;

	// layouts
	private GridBagConstraints timetableNameAreaLayout, timetableAreaLayout;

	// timetable elements
	private JLabel timetableNameLabel;
	private JTextField timetableNameTextField;

	private JLabel /* times */t0, t9, t10, t11, t12, t13, t14, t15, t16, t17;
	private JLabel /* days */mon, tue, wed, thu, fri;

	private ArrayList<JTextArea> timetableArrayList;
	private ArrayList<JScrollPane> timetableArrayListScrollPane;
	private ArrayList<JLabel> timetableTimesArrayList;
	private ArrayList<JLabel> timetableDaysArrayList;

	private boolean isTimetableNull = true;

	/**
	 * Class constructor
	 * 
	 * @param homePanel
	 *            The parent class which called this class
	 */
	public TimetablePane(HomePanel homePanel) {
		this.homePanel = homePanel;

		setPreferredSize(new Dimension(650, 400));
		setMaximumSize(new Dimension(650, 400));
		setMinimumSize(new Dimension(650, 400));

		setOpaque(false);

		timetableTimesArrayList = new ArrayList<JLabel>();
		timetableDaysArrayList = new ArrayList<JLabel>();
		timetableArrayList = new ArrayList<JTextArea>();
		timetableArrayListScrollPane = new ArrayList<JScrollPane>();

		if (homePanel.mainGUI.rmiClient.studentRecord.getTimetable() == null) {
			isTimetableNull = true;
		} else {
			isTimetableNull = false;
		}

		timetableNameArea = new JPanel(new GridBagLayout());
		timetableNameAreaLayout = new GridBagConstraints();
		timetableNameArea.setOpaque(false);

		timetableArea = new JPanel(new GridBagLayout());
		timetableAreaLayout = new GridBagConstraints();
		timetableArea.setOpaque(false);

		loadTimetableNameArea();
		loadTimetableArea();

		add(timetableNameArea, BorderLayout.NORTH);
		add(timetableArea, BorderLayout.SOUTH);
	}

	/**
	 * Prepares timetable name area panel elements
	 */
	private void loadTimetableNameArea() {

		timetableNameAreaLayout.insets = new Insets(0, 5, 10, 5);

		timetableNameAreaLayout.gridx = 0;

		timetableNameLabel = new JLabel("Timetable                ");
		timetableNameLabel.setFont(FontStyles.extraLargeFont);
		timetableNameLabel.setForeground(new Color(255, 255, 255));
		timetableNameArea.add(timetableNameLabel, timetableNameAreaLayout);

		timetableNameAreaLayout.gridx = 1;

		timetableNameTextField = new JTextField(23);
		timetableNameTextField.setFont(FontStyles.smallFont);
		timetableNameTextField.setEditable(false);
		timetableNameTextField.setOpaque(false);
		timetableNameTextField.setForeground(new Color(255, 255, 255));
		timetableNameTextField.setBorder(javax.swing.BorderFactory
				.createEmptyBorder());

		timetableNameArea.add(timetableNameTextField,
				timetableNameAreaLayout);
	}

	/**
	 * Prepares timetable area panel elements of this GUI
	 */
	private void loadTimetableArea() {
		timetableAreaLayout.anchor = GridBagConstraints.CENTER;
		timetableAreaLayout.insets = new Insets(5, 5, 5, 5);

		initialiseTimes();
		addTimesToTimesArrayList();
		setTimeLabelsStyle();
		initialiseDays();
		addDayLabelsToArrayList();
		setDayLabelsStyle();
		addTimetableFields();
	}

	/**
	 * Prepares the time period header title labels
	 */
	private void initialiseTimes() {
		t0 = new JLabel(" ");
		t9 = new JLabel("9-10");
		t10 = new JLabel("10-11");
		t11 = new JLabel("11-12");
		t12 = new JLabel("12-13");
		t13 = new JLabel("13-14");
		t14 = new JLabel("14-15");
		t15 = new JLabel("15-16");
		t16 = new JLabel("16-17");
		t17 = new JLabel("17-18");
	}

	/**
	 * Adds the labels to an array to be able to quickly add label properties in
	 * the form of a for loop
	 */
	public void addTimesToTimesArrayList() {
		timetableTimesArrayList.add(t0);
		timetableTimesArrayList.add(t9);
		timetableTimesArrayList.add(t10);
		timetableTimesArrayList.add(t11);
		timetableTimesArrayList.add(t12);
		timetableTimesArrayList.add(t13);
		timetableTimesArrayList.add(t14);
		timetableTimesArrayList.add(t15);
		timetableTimesArrayList.add(t16);
		timetableTimesArrayList.add(t17);
	}

	/**
	 * Adds time period label properties to the labels
	 */
	public void setTimeLabelsStyle() {
		for (JLabel label : timetableTimesArrayList) {
			label.setFont(FontStyles.smallFont);
			label.setForeground(new Color(255, 255, 255));
			timetableArea.add(label, timetableAreaLayout);
		}
	}

	/**
	 * Prepares the day header labels
	 */
	private void initialiseDays() {
		mon = new JLabel("Mon       ");
		tue = new JLabel("Tue       ");
		wed = new JLabel("Wed       ");
		thu = new JLabel("Thu       ");
		fri = new JLabel("Fri       ");
	}

	/**
	 * Adds the day header labels to an array list to be able to quickly add
	 * label properties in the form of a for loop
	 */
	public void addDayLabelsToArrayList() {
		timetableDaysArrayList.add(mon);
		timetableDaysArrayList.add(tue);
		timetableDaysArrayList.add(wed);
		timetableDaysArrayList.add(thu);
		timetableDaysArrayList.add(fri);
	}

	/**
	 * Adds day label properties to the day header labels
	 */
	public void setDayLabelsStyle() {
		for (JLabel label : timetableDaysArrayList) {
			label.setFont(FontStyles.mediumFont);
			label.setForeground(new Color(255, 255, 255));
		}
	}

	/**
	 * Adds the time period to the panel and set the contents of that period to
	 * that of the time in the student records object
	 */
	public void addTimetableFields() {
		for (int i = 0; i < 45; i++) {

			timetableArrayList.add(new JTextArea(3, 5));

			if (!(isTimetableNull)) {
				timetableArrayList.get(i).setText(
						homePanel.mainGUI.rmiClient.studentRecord
								.getTimetable().get(i));
			}

			timetableArrayList.get(i).setEditable(false);
			timetableArrayListScrollPane.add(new JScrollPane(
					timetableArrayList.get(i)));
		}
		if (!(isTimetableNull)) {
			timetableNameTextField
					.setText(homePanel.mainGUI.rmiClient.studentRecord
							.getTimestableName());
		}

		timetableAreaLayout.anchor = GridBagConstraints.WEST;

		// adds the times corresponding the particular day
		// done in different methods to keep this method a lot smaller
		addMondayField();
		addTuesdayField();
		addWednesdayField();
		addThursdayField();
		addFridayField();
	}

	/**
	 * Adds the times corresponding the particular day to the panel. In this
	 * case the 'Monday Field'
	 * 
	 * @see #addTimetableFields()
	 */
	public void addMondayField() {
		timetableAreaLayout.gridy = 1;
		timetableArea
				.add(timetableDaysArrayList.get(0), timetableAreaLayout);

		timetableAreaLayout.insets = new Insets(5, 2, 5, 1);
		for (int i = 0; i < 9; i++) {
			timetableArea.add(timetableArrayListScrollPane.get(i),
					timetableAreaLayout);
		}
		timetableAreaLayout.insets = new Insets(5, 5, 5, 5);
	}

	/**
	 * Adds the times corresponding the particular day to the panel. In this
	 * case the 'Tuesday Field'
	 * 
	 * @see #addTimetableFields()
	 */
	public void addTuesdayField() {
		timetableAreaLayout.gridy = 2;
		timetableArea
				.add(timetableDaysArrayList.get(1), timetableAreaLayout);

		timetableAreaLayout.insets = new Insets(5, 2, 5, 1);
		for (int i = 9; i < 18; i++) {
			timetableArea.add(timetableArrayListScrollPane.get(i),
					timetableAreaLayout);
		}
		timetableAreaLayout.insets = new Insets(5, 5, 5, 5);
	}

	/**
	 * Adds the times corresponding the particular day to the panel. In this
	 * case the 'Wednesday Field'
	 * 
	 * @see #addTimetableFields()
	 */
	public void addWednesdayField() {
		timetableAreaLayout.gridy = 3;
		timetableArea
				.add(timetableDaysArrayList.get(2), timetableAreaLayout);

		timetableAreaLayout.insets = new Insets(5, 2, 5, 1);
		for (int i = 18; i < 27; i++) {
			timetableArea.add(timetableArrayListScrollPane.get(i),
					timetableAreaLayout);
		}
		timetableAreaLayout.insets = new Insets(5, 5, 5, 5);
	}

	/**
	 * Adds the times corresponding the particular day to the panel. In this
	 * case the 'Thursday Field'
	 * 
	 * @see #addTimetableFields()
	 */
	public void addThursdayField() {
		timetableAreaLayout.gridy = 4;
		timetableArea
				.add(timetableDaysArrayList.get(3), timetableAreaLayout);

		timetableAreaLayout.insets = new Insets(5, 2, 5, 1);
		for (int i = 27; i < 36; i++) {
			timetableArea.add(timetableArrayListScrollPane.get(i),
					timetableAreaLayout);
		}
		timetableAreaLayout.insets = new Insets(5, 5, 5, 5);
	}

	/**
	 * Adds the times corresponding the particular day to the panel. In this
	 * case the 'Friday Field'
	 * 
	 * @see #addTimetableFields()
	 */
	public void addFridayField() {
		timetableAreaLayout.gridy = 5;
		timetableArea
				.add(timetableDaysArrayList.get(4), timetableAreaLayout);

		timetableAreaLayout.insets = new Insets(5, 2, 5, 1);
		for (int i = 36; i < 45; i++) {
			timetableArea.add(timetableArrayListScrollPane.get(i),
					timetableAreaLayout);
		}
		timetableAreaLayout.insets = new Insets(5, 5, 5, 5);
	}
}
