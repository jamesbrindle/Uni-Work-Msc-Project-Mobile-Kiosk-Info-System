package sik.server;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * A GUI to display any messages dispatched from the RMI server. These message
 * may originate from the RMI server itself or from a remote RMI client, which
 * is relayed through the RMI server. These message generally contain connection
 * and data transfer information and any remote problems that may occur and the
 * possible reasons as to why these problems occurred. It's basically a server
 * logger. The logger also includes control options such as the ability to
 * export the log to a text file, restart the RMI server and change the RMI
 * binding address
 * 
 * @author Jamie Brindle (06352322) - Manchester Metropolitan University
 */
public class ServerLogger implements ActionListener, Serializable {

	/**
	 * Default universal serializable class ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Class that provides text file IO services, in which the text file
	 * contains attribute values and setting to be used by this and other
	 * classes
	 */
	private ServerIOServices serverOptions;

	/**
	 * A dialog to display messages or prompt to the user
	 */
	private OptionsDialog optionsDialog;

	// GUI properties
	private Container container;
	private JFrame frame;
	protected JTextArea logsTextArea;
	private JScrollPane logsScrollPane;
	private JButton closeButton, exportLogButton, resetButton,
			changeRMILocButton;
	private JPanel buttonPanel;

	// A time format to be used when exporting a server log to a text file
	private static final String DATE_FORMAT_NOW = "dd-MM-yyyy HH-mm-ss";

	/**
	 * Class constructor
	 */
	public ServerLogger() {
		frame = new JFrame("RMI Server");
		frame.setResizable(false);

		// create a new window listener
		frame.addWindowListener(new WindowAdapter() {
			/**
			 * Creates a new window closing event handler for this class
			 * 
			 * @param e
			 *            The key event
			 */
			public void windowClosing(WindowEvent e) {
				frame.dispose();
				System.exit(0);
			}
		});

		container = new Container();
		container = frame.getContentPane();
		container.setLayout(new BorderLayout());

		loadServerLoggerElements();

		// method called to centre the GUI on the screen
		centreFrame();

		// prepare and display the GUI
		frame.pack();
		frame.setVisible(true);

	}

	/**
	 * The action event hander
	 * 
	 * @param e
	 *            The action event
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == closeButton) {
			closeOperation();
		}
		if (e.getSource() == exportLogButton) {
			exportFile();
		}
		if (e.getSource() == resetButton) {
			try {
				resetOperation();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == changeRMILocButton) {
			try {
				changeRMILocOperation();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * Prepares the GUI elements
	 */
	private void loadServerLoggerElements() {
		logsTextArea = new JTextArea(20, 55);
		logsTextArea.setEditable(false);
		logsScrollPane = new JScrollPane(logsTextArea);

		resetButton = new JButton("Restart Server");
		resetButton.addActionListener(this);

		closeButton = new JButton("Stop and Close Server");
		closeButton.addActionListener(this);

		changeRMILocButton = new JButton("Change RMI URL");
		changeRMILocButton.addActionListener(this);

		exportLogButton = new JButton("Export Log");
		exportLogButton.addActionListener(this);

		container.add(logsScrollPane, BorderLayout.NORTH);

		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());

		buttonPanel.add(resetButton);
		buttonPanel.add(closeButton);
		buttonPanel.add(changeRMILocButton);
		buttonPanel.add(exportLogButton);

		container.add(buttonPanel, BorderLayout.SOUTH);
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
	 * Used to export the contents of the server logger text area to a file
	 */
	private void exportFile() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		String date = sdf.format(cal.getTime());

		BufferedWriter fo;
		try {
			// The current date is part of the file name so we know when the log
			// was recorded
			fo = new BufferedWriter(new FileWriter("logs/ServerLog-" + date
					+ ".txt"));
			fo.write("\n-" + date + "-\n\n" + logsTextArea.getText());
			fo.newLine();
			fo.flush();
			fo.close();

			logsTextArea.append("\n-Date: " + date
					+ "-\n\nLog stored as ServerLog<current-date>.txt "
					+ "in 'logs' folder in current working directory\n");
		} catch (IOException e) {
			logsTextArea.append("\nLog save unsuccessful\n");
			e.printStackTrace();
		}
	}

	/**
	 * Called when the user wishes to stop and exit the RMI server. It will
	 * prompt a user if they're sure they want to exit in case a mistake has
	 * been made
	 */
	public void closeOperation() {
		this.optionsDialog = new OptionsDialog(new JFrame(), "Stop RMI Server",
				"Are You Sure You Want to Stop the RMI Sever?", "option");

		if (optionsDialog.getChoice() == 1) {

			frame.dispose();
			System.exit(0);
		}
	}

	/**
	 * Restarts the RMI server
	 * 
	 * @throws IOException
	 *             An exception that will be thrown in case any IO problems
	 *             occur, as the logger needs to read attribute values and
	 *             settings from a text file in order to start correctly
	 */
	public void resetOperation() throws IOException {
		try {
			RMIServerImpl rmiServer = new RMIServerImpl(logsTextArea);
			rmiServer.dispatchMessage("Server restarted Successfully");
		} catch (RemoteException e1) {
			logsTextArea.append("Problem Starting Server - Exception Thrown\n");
		}
	}

	/**
	 * Method to change the RMI server binding location, in which a dialog will
	 * be displayed so a user can enter the RMI address URL to bind to. This URL
	 * is stored in a text file
	 * 
	 * @throws IOException
	 *             in case any problems occur with the IO from / to the text
	 *             file
	 */
	public void changeRMILocOperation() throws IOException {
		serverOptions = new ServerIOServices();

		try {
			this.optionsDialog = new OptionsDialog(new JFrame(),
					"Change RMI Location",
					"Please specify the URL of the RMI Server to connect to",
					"changeRMILocation");

			if (optionsDialog.getChoice() == 1) {
				serverOptions.changeRMILocation(optionsDialog
						.getNewRMILocation());

				RMIServerImpl rmiServer = new RMIServerImpl(logsTextArea);
				rmiServer
						.dispatchMessage("RMI Host location changed and server restarted");
			}
		} catch (RemoteException e1) {
			logsTextArea
					.append("Problem Starting Server and changing RMI url - Please check\n");
		}
	}
}
