package gameFiles;

public class UserResponse {
	private String formType;
	private String player;
	private String message;
	
	public UserResponse(String formType, String player, String message) {
		this.formType = formType;
		this.player = player;
		this.message = message;
	}
	/**
	 * @return the formType
	 */
	public String getFormType() {
		return formType;
	}
	/**
	 * @param formType the formType to set
	 */
	public void setFormType(String formType) {
		this.formType = formType;
	}
	/**
	 * @return the player's username
	 */
	public String getPlayer() {
		return player;
	}
	/**
	 * @param player the player's username to set
	 */
	public void setPlayer(String player) {
		this.player = player;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
}
