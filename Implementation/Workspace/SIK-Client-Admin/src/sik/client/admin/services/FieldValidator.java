package sik.client.admin.services;

import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * Class which contains numerous methods for validating String are of a given
 * content
 * 
 * @author Jamie Brindle (06352322) - Manchester Metropolitan University
 */
public class FieldValidator implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Validates the pin number field, in conjunction with the
	 * containsOnlyNumbers method
	 * 
	 * @param aString
	 *            The String needed to be checked
	 * @return boolean - true is valid, false otherwise
	 */
	public static boolean validatePinNumber(String aString) {

		if (NumFunc.containsOnlyNumbers(aString)) {

			if ((NumFunc.stringToInt(aString) >= 1000)
					&& (NumFunc.stringToInt(aString) <= 9999)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * Checks there are no invalid characters in a given text field
	 * 
	 * @param aString
	 *            The string needed to be checked
	 * @return boolean -true if no invalid characters, false otherwise
	 */
	public static boolean checkNoInvalidCharacters(String aString) {

		String invalidList = "!\"" + "\u00A3" + "$%^&*()_+-=`{}[]:@~;#<>?/\\";
		int count = 0;

		String[] temp1 = Pattern.compile("").split(invalidList);
		String[] temp2 = Pattern.compile("").split(aString);

		for (String element : temp2) {
			for (String element2 : temp1) {
				if (element.equalsIgnoreCase(element2)) {
					count++;
				}
			}
		}

		if (count > 1) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Checks there are no invalid characters or numbers in a given text field
	 * Used in conjunction with the checkNoInvalidCharacters method
	 * 
	 * @param aString
	 *            The string needed to be checked
	 * @return boolean -true if no invalid characters, false otherwise
	 */
	public static boolean validateStringsFieldsNumbersNotAllowed(String aString) {
		if (checkNoInvalidCharacters(aString)
				&& !(NumFunc.containsNumbers(aString))) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks there are no invalid characters in a given text field Used in
	 * conjunction with the checkNoInvalidCharacters method
	 * 
	 * @param aString
	 *            The string needed to be checked
	 * @return boolean -true if no invalid characters, false otherwise
	 */
	public static boolean validateStringsFieldsNumbersAllowed(String aString) {

		if (checkNoInvalidCharacters(aString)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks there are only numbers and spaces in telephone text field used in
	 * conjunction with the containsOnlyNumbersAndSpaces method
	 * 
	 * @param aString
	 *            The string needed to be checked
	 * @return boolean -true if no invalid characters, false otherwise
	 */
	public static boolean validateTelephone(String aString) {

		if (NumFunc.containsOnlyNumbersAndSpaces(aString)) {
			return true;
		} else {
			return false;
		}
	}
}
