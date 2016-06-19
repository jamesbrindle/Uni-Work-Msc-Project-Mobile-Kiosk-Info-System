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
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import sik.client.user.FontStyles;
import sik.client.user.panels.HomePanel;

/**
 * Panel which is displayed as a content inside the home panel. As the home
 * panel displays a number of different complex panels corresponding to the
 * selected category this has been given the name 'pane' rather than 'panel'.
 * 
 * This particular pane is is displayed when the 'Assignment Results' button is
 * pressed on the home panel
 * 
 * @author Jamie Brindle (06352322) - Manchester Metropolitan University
 */
public class ResultsPane extends JPanel {

	/**
	 * Default universal serializable class ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Parent class which created an instance of this class. Set as global in
	 * order to quickly access its methods and send back information
	 */
	protected HomePanel homePanel;

	// panel properties
	private JLabel titleLabel, unit, name, ass1, ass2, exam;
	private JPanel resultsArea;
	private GridBagConstraints resultsAreaLayout;
	private ArrayList<JTextField> resultsArrayList;
	private ArrayList<JLabel> resultTitlesArrayList;
	private boolean isResultsNull = true;

	public ResultsPane(HomePanel homePanel) {
		this.homePanel = homePanel;
		resultsArrayList = new ArrayList<JTextField>();
		resultTitlesArrayList = new ArrayList<JLabel>();

		setPreferredSize(new Dimension(650, 400));
		setMaximumSize(new Dimension(650, 400));
		setMinimumSize(new Dimension(650, 400));

		setOpaque(false);

		if (homePanel.mainGUI.rmiClient.studentRecord.getResults() == null) {
			isResultsNull = true;
		} else {
			isResultsNull = false;
		}

		resultsArea = new JPanel(new GridBagLayout());
		resultsAreaLayout = new GridBagConstraints();

		loadResultsArea();

		add(resultsArea, BorderLayout.CENTER);
	}

	/**
	 * Prepares the results area panel elements
	 */
	private void loadResultsArea() {
		resultsArea.setOpaque(false);

		resultsAreaLayout.gridwidth = 5;
		resultsAreaLayout.gridy = 0;

		resultsAreaLayout.anchor = GridBagConstraints.NORTHWEST;

		titleLabel = new JLabel("Assignment Results");
		titleLabel.setFont(FontStyles.extraLargeFont);
		titleLabel.setForeground(new Color(255, 255, 255));

		resultsArea.add(titleLabel, resultsAreaLayout);

		resultsAreaLayout.anchor = GridBagConstraints.CENTER;

		resultsAreaLayout.gridwidth = 1;
		resultsAreaLayout.gridy = 1;

		resultsAreaLayout.insets = new Insets(25, 5, 8, 5);

		initialiseResultTitles();
		addResultTitlesToArrayList();

		for (JLabel label : resultTitlesArrayList) {
			label.setFont(FontStyles.largeFont);
			label.setForeground(new Color(255, 255, 255));
			resultsArea.add(label, resultsAreaLayout);
		}

		resultsAreaLayout.insets = new Insets(8, 5, 12, 5);

		int charLength = 0;

		// set of appropriate text field sizes
		for (int i = 0; i < 30; i++) {

			if ((i == 0) || (i == 5) || (i == 10) || (i == 15) || (i == 20)
					|| (i == 25)) {
				charLength = 8;
			} else if ((i == 1) || (i == 6) || (i == 11) || (i == 16)
					|| (i == 21) || (i == 26)) {
				charLength = 17;
			} else if ((i == 2) || (i == 7) || (i == 12) || (i == 17)
					|| (i == 22) || (i == 27)) {
				charLength = 3;
			} else if ((i == 3) || (i == 8) || (i == 13) || (i == 18)
					|| (i == 23) || (i == 28)) {
				charLength = 3;
			} else if ((i == 4) || (i == 9) || (i == 14) || (i == 19)
					|| (i == 24) || (i == 29)) {
				charLength = 3;
			}

			JTextField textField = new JTextField(charLength);
			textField.setFont(FontStyles.largeFont);

			// add text field to array list in order for perform attribute
			// modifications to those text fields via a for loop (minimising
			// amount of code and increasing possible future maintenance)
			resultsArrayList.add(textField);
			resultsArrayList.get(i).setHorizontalAlignment(
					SwingConstants.CENTER);

			if (!(isResultsNull)) {
				resultsArrayList.get(i).setText(
						homePanel.mainGUI.rmiClient.studentRecord.getResults()
								.get(i));
			}

			resultsArrayList.get(i).setEditable(false);
		}

		// 1st unit

		resultsAreaLayout.gridy = 2;

		for (int i = 0; i < 5; i++) {
			resultsArea.add(resultsArrayList.get(i), resultsAreaLayout);
		}

		// 2nd unit

		resultsAreaLayout.gridy = 3;

		for (int i = 5; i < 10; i++) {
			resultsArea.add(resultsArrayList.get(i), resultsAreaLayout);
		}

		// 3rd unit

		resultsAreaLayout.gridy = 4;

		for (int i = 10; i < 15; i++) {
			resultsArea.add(resultsArrayList.get(i), resultsAreaLayout);
		}

		// 4th unit

		resultsAreaLayout.gridy = 5;

		for (int i = 15; i < 20; i++) {
			resultsArea.add(resultsArrayList.get(i), resultsAreaLayout);
		}

		// 5th unit

		resultsAreaLayout.gridy = 6;

		for (int i = 20; i < 25; i++) {
			resultsArea.add(resultsArrayList.get(i), resultsAreaLayout);
		}

		// 6th unit

		resultsAreaLayout.gridy = 7;

		for (int i = 25; i < 30; i++) {
			resultsArea.add(resultsArrayList.get(i), resultsAreaLayout);
		}
	}

	/**
	 * Prepared the result header titles of the panel
	 */
	private void initialiseResultTitles() {
		unit = new JLabel("Unit ID");
		name = new JLabel("Unit Name");
		ass1 = new JLabel("Ass 1");
		ass2 = new JLabel("Ass 2");
		exam = new JLabel("Exam");
	}

	/**
	 * Adds the header titles to an array to quickly add label properties in the
	 * form of a for loop
	 */
	private void addResultTitlesToArrayList() {
		resultTitlesArrayList.add(unit);
		resultTitlesArrayList.add(name);
		resultTitlesArrayList.add(ass1);
		resultTitlesArrayList.add(ass2);
		resultTitlesArrayList.add(exam);
	}
}
