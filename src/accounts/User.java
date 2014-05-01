package accounts;

import java.sql.Date;

public class User {
	private String username;
	private String password;
	private Date birthday;
	private String gender;
	private String hearingImpaired;
	
	public User(String username, String password, Date birthday, String gender,
			String hearingImpaired) {
		this.username = username;
		this.password = password;
		this.birthday = birthday;
		this.gender = gender;
		this.hearingImpaired = hearingImpaired;
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
	 * @return the birthday
	 */
	public Date getBirthday() {
		return birthday;
	}

	/**
	 * @param birthday the birthday to set
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
