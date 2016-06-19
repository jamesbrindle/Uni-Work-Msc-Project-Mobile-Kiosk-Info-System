package sik.server;

import java.io.IOException;
import java.io.Serializable;

/**
 * Class to start the RMI Server running. It defines a new RMI server
 * implementation and server logger instance
 * 
 * @author Jamie Brindle (06352322) - Manchester Metropolitan University
 */
public class StartRMIServer implements Serializable {

	/**
	 * Default universal serializable class ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The RMI server instance
	 */
	protected static RMIServerImpl rmiServer;

	/**
	 * The server logger instance
	 */
	protected static ServerLogger serverLogger;

	public static void main(String[] args) throws IOException {

		serverLogger = new ServerLogger();
		rmiServer = new RMIServerImpl(serverLogger.logsTextArea);
		rmiServer.startRMIServer();
	}
}
