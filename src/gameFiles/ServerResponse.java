package gameFiles;

public class ServerResponse {
	private String correctAnswer;
	private double score;
	private double totalScore;
	private String opponentResponse;

	public ServerResponse(String correctAnswer, double score, double totalScore, String opponentResponse) {
		this.correctAnswer = correctAnswer;
		this.score = score;
		this.totalScore = totalScore;
		this.opponentResponse = opponentResponse;
	}
	
	/**
	 * @return the correctAnswer
	 */
	public String getCorrectAnswer() {
		return correctAnswer;
	}

	/**
	 * @param correctAnswer the correctAnswer to set
	 */
	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}

	/**
	 * @return the score
	 */
	public double getScore() {
		return score;
	}

	/**
	 * @param score the score to set
	 */
	public void setScore(double score) {
		this.score = score;
	}

	/**
	 * @return the totalScore
	 */
	public double getTotalScore() {
		return totalScore;
	}

	/**
	 * @param totalScore the totalScore to set
	 */
	public void setTotalScore(double totalScore) {
		this.totalScore = totalScore;
	}
	
	/**
	 * @return the opponentResponse
	 */
	public String getOpponentResponse() {
		return opponentResponse;
	}

	/**
	 * @param opponentResponse the opponentResponse to set
	 */
	public void setOpponentResponse(String opponentResponse) {
		this.opponentResponse = opponentResponse;
	}
}
