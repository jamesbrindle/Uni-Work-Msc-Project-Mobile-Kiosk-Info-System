package sik.client.user;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

//@formatter:off
/**
 * Contains static methods which plays action clip sounds
 * 
 * @author Jamie Brindle (06352322) - Manchester Metropolitan University
 */
public class ActionClips {
	
	private static InputStream in;
	private static AudioStream as;
	
	private static final String cancelButton = "res/sounds/cancelButton.wav";
	private static final String clearButton = "res/sounds/clearButton.wav";
	private static final String connectionFailure = "res/sounds/connectionFailure.wav";
	private static final String error = "res/sounds/error.wav";
	private static final String numButton = "res/sounds/numButton.wav";
	private static final String welcome = "res/sounds/welcome.wav";
	private static final String wrongPassword = "res/sounds/wrongPassword.wav";
	private static final String click = "res/sounds/click.wav";
	private static final String adminButton = "res/sounds/adminButton.wav";
	
    public ActionClips(){}
	
	public static final void playCancelButton() { playSoundFile(cancelButton); }
	public static final void playClearButton() { playSoundFile(clearButton); }
	public static final void playConnectionFailure() { playSoundFile(connectionFailure); }
	public static final void playError() { playSoundFile(error); }
	public static final void playNumButton() { playSoundFile(numButton); }
	public static final void playWelcome() { playSoundFile(welcome); }
	public static final void playWrongPassword() { playSoundFile(wrongPassword); }	
	public static final void playClick() { playSoundFile(click); }
	public static final void playAdminButton() { playSoundFile(adminButton); }
	
	//@formatter:on
	/**
	 * Plays a sound file
	 * 
	 * @param fileName
	 *            The sound file file name in which to play
	 */
	public static void playSoundFile(String fileName) {

		try {
			in = new FileInputStream(fileName);
			as = new AudioStream(in);
			AudioPlayer.player.start(as);

		} catch (FileNotFoundException e) {
			System.out.println("Error finding sound file");
		} catch (IOException e) {
			System.out.println("Error loading in sound file");
		}
	}
}
