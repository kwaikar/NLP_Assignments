package hw1.common;

import java.util.HashMap;
import java.util.Map;

public class SmoothingInputBean {

	Map<String, Integer> wordFrequencyMap =new HashMap<String, Integer>();
	private Integer denominiator;
	private TokenList tokens;
	Integer[] frequencyOfFrequencyCount;
	/**
	 * @return the denominiator
	 */
	public Integer getDenominiator() {
		return denominiator;
	}
	/**
	 * @param denominiator the denominiator to set
	 */
	public void setDenominiator(Integer denominiator) {
		this.denominiator = denominiator;
	}
	 
	/**
	 * @return the frequencyOfFrequencyCount
	 */
	public Integer[] getFrequencyOfFrequencyCount() {
		return frequencyOfFrequencyCount;
	}
	/**
	 * @param frequencyOfFrequencyCount the frequencyOfFrequencyCount to set
	 */
	public void setFrequencyOfFrequencyCount(Integer[] frequencyOfFrequencyCount) {
		this.frequencyOfFrequencyCount = frequencyOfFrequencyCount;
	}
	/**
	 * @return the wordFrequencyMap
	 */
	public Map<String, Integer> getWordFrequencyMap() {
		return wordFrequencyMap;
	}
	/**
	 * @param wordFrequencyMap the wordFrequencyMap to set
	 */
	public void setWordFrequencyMap(Map<String, Integer> wordFrequencyMap) {
		this.wordFrequencyMap = wordFrequencyMap;
	}
	/**
	 * @return the tokens
	 */
	public TokenList getTokens() {
		return tokens;
	}
	/**
	 * @param tokens the tokens to set
	 */
	public void setTokens(TokenList tokens) {
		this.tokens = tokens;
	}
	
	
	
	
	
}
