package gameFiles;

public class UserResponse {
	private String formType;
	private String player;
	private String message;
	private String timeTaken;

	public UserResponse(String formType, String player, String message,
			String timeTaken) {
		super();
		this.formType = formType;
		this.player = player;
		this.message = message;
		this.timeTaken = timeTaken;
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
	/**
	 * @return the timeTaken
	 */
	public String getTimeTaken() {
		return timeTaken;
	}
	/**
	 * @param timeTaken the timeTaken to set
	 */
	public void setTimeTaken(String timeTaken) {
		this.timeTaken = timeTaken;
	}
	
	
	
}
