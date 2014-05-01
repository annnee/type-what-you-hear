package accounts;

public class RecaptchaResponse {
	private String recaptcha_challenge_field;
	private String recaptcha_response_field;
	
	public RecaptchaResponse(String recaptcha_challenge_field,
			String recaptcha_response_field) {
		this.recaptcha_challenge_field = recaptcha_challenge_field;
		this.recaptcha_response_field = recaptcha_response_field;
	}

	/**
	 * @return the recaptcha_challenge_field
	 */
	public String getRecaptcha_challenge_field() {
		return recaptcha_challenge_field;
	}

	/**
	 * @param recaptcha_challenge_field the recaptcha_challenge_field to set
	 */
	public void setRecaptcha_challenge_field(String recaptcha_challenge_field) {
		this.recaptcha_challenge_field = recaptcha_challenge_field;
	}

	/**
	 * @return the recaptcha_response_field
	 */
	public String getRecaptcha_response_field() {
		return recaptcha_response_field;
	}

	/**
	 * @param recaptcha_response_field the recaptcha_response_field to set
	 */
	public void setRecaptcha_response_field(String recaptcha_response_field) {
		this.recaptcha_response_field = recaptcha_response_field;
	}
	
	
}
