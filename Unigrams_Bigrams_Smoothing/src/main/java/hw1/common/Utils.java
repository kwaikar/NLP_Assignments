package hw1.common;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * Utility class for doing simple operations
 * 
 * @author kanchan
 *
 */
public class Utils {

	/**
	 * Generic Common method for rounding numbers
	 * 
	 * @param number
	 * @param digits
	 * @return
	 */
	public static double round(double number, int digits) {
		int multiplier = 1;
		if (number != 0 && digits > 0) {
			for (int i = 0; i < digits; i++) {
				multiplier = multiplier * 10;
			}
		}
		return (double) Math.round(number * multiplier) / multiplier;
	}

	/**
	 * As method name suggests, this method accepts list of tokens and creates a
	 * single sentence out of it.
	 * 
	 * @param tokens
	 * @return
	 */
	public static String createSingleSentence(TokenList tokens) {
		StringBuilder sentenceBuilder = new StringBuilder();

		for (String entry : tokens.getTokens()) {
			sentenceBuilder.append(" " + entry);
		}
		return sentenceBuilder.toString();
	}

	public static void main(String[] args) {
		String str = "india india india indiaa india";
		str = " " + str + " ";
		str = str.replaceAll(" india ", " _kanchan_ ").replaceAll(" india ", " _kanchan_ ");
		System.out.println(StringUtils.countMatches(str, "_kanchan_"));
	}

	/**
	 * This method accepts sentence and searchString and returns the number of
	 * occurances of the searchTerm.
	 * 
	 * @param sentence
	 * @param searchTerm
	 * @return
	 */
	public static int getCountOfOccurances(String sentence, String searchTerm) {
		String localSentence = " " + sentence + " ";
		String localSearchTerm = " " + searchTerm + " ";
		localSentence = localSentence.replaceAll(Pattern.quote(localSearchTerm), " _kanchan_ ").replaceAll(Pattern.quote(localSearchTerm),
				" _kanchan_ ");
		return StringUtils.countMatches(localSentence, "_kanchan_");
	}
}
