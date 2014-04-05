package dissertation;

import java.sql.Date;

public class Users {
	private String fName;
	private String lName;
	private Date dob;
	private String gender;
	private String email;
	private String password;
	private String hearingImpaired;
	
	public Users(String fName, String lName, Date dob, String gender, String email, String password, String hearingImpaired) {
		this.fName = fName;
		this.lName = lName;
		this.dob = dob;
		this.gender = gender;
		this.email = email;
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
	public Date getDob() {
		return dob;
	}

	/**
	 * @param dob the dob to set
	 */
	public void setDob(Date dob) {
		this.dob = dob;
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
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
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
	public String isHearingImpaired() {
		return hearingImpaired;
	}

	/**
	 * @param hearingImpaired the hearingImpaired to set
	 */
	public void setHearingImpaired(String hearingImpaired) {
		this.hearingImpaired = hearingImpaired;
	}

}
