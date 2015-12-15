package hw1.common;

import java.util.LinkedList;
import java.util.List;

/**
 * Bean for holding tokens found.
 * 
 * @author kanchan
 *
 */
public class TokenList {

	List<String> tokens = new LinkedList<String>();

	/**
	 * @return the tokens
	 */
	public List<String> getTokens() {
		return tokens;
	}

	/**
	 * @param tokens
	 *            the tokens to set
	 */
	public void addToken(String token) {

		tokens.add(token);
	}

}
