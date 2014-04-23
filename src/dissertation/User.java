package dissertation;

import java.sql.Date;

public class User {
	private String fName;
	private String lName;
	private Date birthday;
	private String gender;
	private String username;
	private String password;
	private String hearingImpaired;
	
	public User(String fName, String lName, Date birthday, String gender, String username, String password, String hearingImpaired) {
		this.fName = fName;
		this.lName = lName;
		this.birthday = birthday;
		this.gender = gender;
		this.username = username;
		this.password = password;
		this.hearingImpaired = hearingImpaired;
	}

	/**
	 * @return the fName
	 */
	public String getfName() {
		return fName;
	}

	/**
	 * @param fName the fName to set
	 */
	public void setfName(String fName) {
		this.fName = fName;
	}

	/**
	 * @return the lName
	 */
	public String getlName() {
		return lName;
	}

	/**
	 * @param lName the lName to set
	 */
	public void setlName(String lName) {
		this.lName = lName;
	}

	/**
	 * @return the dob
	 */
	public Date getBirthday() {
		return birthday;
	}

	/**
	 * @param dob the dob to set
	 */
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the hearingImpaired
	 */
	public String getHearingImpaired() {
		return hearingImpaired;
	}

	/**
	 * @param hearingImpaired the hearingImpaired to set
	 */
	public void setHearingImpaired(String hearingImpaired) {
		this.hearingImpaired = hearingImpaired;
	}

}
