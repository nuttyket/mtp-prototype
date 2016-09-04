package msvision;

import java.math.BigDecimal;

public class Emotion {
	private String emotionName;
	private BigDecimal emotionScore;
	
	public Emotion(String emotionName, BigDecimal emotionScore) {
		this.emotionName = emotionName;
		this.emotionScore = emotionScore;
	}
	
	public String getEmotionName() {
		return emotionName;
	}
	public void setEmotionName(String emotionName) {
		this.emotionName = emotionName;
	}
	public BigDecimal getEmotionScore() {
		return emotionScore;
	}
	public void setEmotionScore(BigDecimal emotionScore) {
		this.emotionScore = emotionScore;
	}
	
	@Override
	public String toString() {
		return emotionName + " " + emotionScore.toPlainString();
	}
	/*
	 * returns true is this emotion score is stronger than the incoming score
	 */
	public boolean isStrongerThan(BigDecimal emotionScore) {
		return this.emotionScore.compareTo(emotionScore) >= 0? true : false;
	}
	
}
