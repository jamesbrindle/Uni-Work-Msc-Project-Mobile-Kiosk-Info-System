package sik.common;

import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.ImageIcon;

/**
 * Student student record object class. It contains the details of a student
 * 
 * @author Jamie Brindle (06352322) - Manchester Metropolitan University
 */
public class StudentRecord implements Serializable {

	/**
	 * Default universal serializable class ID
	 */
	private static final long serialVersionUID = 1L;

	// object properties
	private String studentID, pinNo, title, firstName, lastName,
			firstLineAddress, secondLineAddress, city, county, postCode,
			telephone, timestableName, rfidTagID, course;
	private ImageIcon image;
	private ArrayList<String> messagesArrayList, timestableArrayList,
			resultsArrayList;

	/**
	 * Class constructor
	 */
	public StudentRecord() {
	}

	/**
	 * Class constructor which creates this student student record object with
	 * the specified student ID
	 */
	public StudentRecord(String studentID) {
		this.studentID = studentID;
	}

	/**
	 * Returns studentID number
	 * 
	 * @return studentID student record number integer
	 */
	public String getStudentID() {
		return studentID;
	}

	/**
	 * Returns pinNo
	 * 
	 * @return pinNo student record pin number integer
	 */
	public String getPinNo() {
		return pinNo;
	}

	/**
	 * Returns course
	 * 
	 * @return course Course Name String
	 */
	public String getCourse() {
		return course;
	}

	/**
	 * Returns title
	 * 
	 * @return title student record name title string
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Returns firstName
	 * 
	 * @return firstName student record first name string
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Returns lastName
	 * 
	 * @return lastName student record last name string
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Returns firstLineAddress
	 * 
	 * @return firstLineAddress student record number string
	 */
	public String getFirstLineAddress() {
		return firstLineAddress;
	}

	/**
	 * Returns secondLineAddress
	 * 
	 * @return secondLineAddress student record second name string
	 */
	public String getSecondLineAddress() {
		return secondLineAddress;
	}

	/**
	 * Returns city
	 * 
	 * @return city student record city string
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Returns county
	 * 
	 * @return county student record county string
	 */
	public String getCounty() {
		return county;
	}

	/**
	 * Returns postCode
	 * 
	 * @return postCode student record post code string
	 */
	public String getPostCode() {
		return postCode;
	}

	/**
	 * Returns telephone
	 * 
	 * @return telephone student record telephone number string
	 */
	public String getTelephone() {
		return telephone;
	}

	/**
	 * Returns student photo
	 * 
	 * @return image student record image of student photo
	 */
	public ImageIcon getImage() {
		return image;
	}

	/**
	 * Returns student messages
	 * 
	 * @return messageArrayList Array List of messages
	 */
	public ArrayList<String> getMessages() {
		return messagesArrayList;
	}

	/**
	 * Returns student timetable
	 * 
	 * @return timestableArrayList Array List of timetable
	 */
	public ArrayList<String> getTimetable() {
		return timestableArrayList;
	}

	/**
	 * Returns student timetable name string
	 * 
	 * @return timestableName String of the name of the timetable
	 */
	public String getTimestableName() {
		return timestableName;
	}

	/**
	 * Returns student assignment results
	 * 
	 * @return resultsArrayList Array List of assignment results
	 */
	public ArrayList<String> getResults() {
		return resultsArrayList;
	}

	/**
	 * Returns RFID Tag ID
	 * 
	 * @return rfidTagID String for RFID Tag ID
	 */
	public String getRFIDTagID() {
		return rfidTagID;
	}

	// ------------------------------------------------------------------------

	/**
	 * Sets student record pin number
	 * 
	 * @param pinNo
	 *            The pin number integer
	 */
	public void setPinNo(String pinNo) {
		this.pinNo = pinNo;
	}

	/**
	 * Sets course name
	 * 
	 * @param course
	 *            The course name string
	 */
	public void setCourse(String course) {
		this.course = course;
	}

	/**
	 * Sets student record title
	 * 
	 * @param title
	 *            the name title string
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Sets student record first name
	 * 
	 * @param firstName
	 *            the first name string
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Sets student record last name
	 * 
	 * @param lastName
	 *            the last name string
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Sets student record first line of address
	 * 
	 * @param firstLineAddress
	 *            the first line of address string
	 */
	public void setFirstLineAddress(String firstLineAddress) {
		this.firstLineAddress = firstLineAddress;
	}

	/**
	 * Sets student record second line of address
	 * 
	 * @param secondLineAddress
	 *            the second line of the address string
	 */
	public void setSecondLineAddress(String secondLineAddress) {
		this.secondLineAddress = secondLineAddress;
	}

	/**
	 * Sets student record city
	 * 
	 * @param city
	 *            the city string
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * Sets student record county
	 * 
	 * @param county
	 *            the county string
	 */
	public void setCounty(String county) {
		this.county = county;
	}

	/**
	 * Sets student record post code
	 * 
	 * @param postCode
	 *            the post code string
	 */
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	/**
	 * Sets student record telephone
	 * 
	 * @param telephone
	 *            the telephone number string
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	/**
	 * Sets student photo image
	 * 
	 * @param image
	 *            the ImageIcon of student photo
	 */
	public void setImage(ImageIcon image) {
		this.image = image;
	}

	/**
	 * Sets student messages
	 * 
	 * @param messages
	 *            ArrayList of messages Strings
	 */
	public void setMessages(ArrayList<String> messages) {
		this.messagesArrayList = messages;
	}

	/**
	 * Sets student timetable
	 * 
	 * @param timestable
	 *            ArrayList of student timetable Strings
	 */
	public void setTimetable(ArrayList<String> timestable) {
		this.timestableArrayList = timestable;
	}

	/**
	 * Sets student timetable name
	 * 
	 * @param ttn
	 *            String of the timetable name
	 */
	public void setTimestableName(String ttn) {
		this.timestableName = ttn;
	}

	/**
	 * Sets student assignment results
	 * 
	 * @param results
	 *            ArrayList of student assignment results
	 */
	public void setResults(ArrayList<String> results) {
		this.resultsArrayList = results;
	}

	/**
	 * Sets RFID Tag ID
	 * 
	 * @param rfidTagID
	 *            String of rfid Tag ID
	 */
	public void setRFIDTagID(String rfidTagID) {
		this.rfidTagID = rfidTagID;
	}
}
