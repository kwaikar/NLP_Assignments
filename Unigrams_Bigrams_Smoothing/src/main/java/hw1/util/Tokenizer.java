/**
 * 
 */
package hw1.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import hw1.common.TokenList;

/**
 * This class accepts the input and returns the list of tokens. This does not
 * remove the end of line characters and tokenizes based on white spaces and
 * returns tokens in the sequence in which bli
 * 
 * @author kanchan waikar
 *
 */
public class Tokenizer {

	/**
	 * This method chunks file contents into tokens and returns the same. Any
	 * code related to pre-processing of text should be added here.
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public TokenList getTokens(File file) throws IOException {

		TokenList list = new TokenList();
		String[] listOfWords = FileUtils.readFileToString(file).split("[\\r|\\n]");
		for (String string : listOfWords) {
			if (StringUtils.isNotBlank(string)) {
				list.addToken(string.trim());
			}
		}
		return list;
	}


	/**
	 * This method chunks file contents into tokens and returns the same. Any
	 * code related to pre-processing of text should be added here.
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public TokenList getTokensFromTokensFile(File file) throws IOException {

		TokenList list = new TokenList();
		String[] listOfWords = FileUtils.readFileToString(file).split("\\s");
		for (String string : listOfWords) {
			list.addToken(string.trim().replaceAll("\n", "").replaceAll("\r", ""));
		}
		return list;
	}

	/**
	 * This method writes tokens to a file and returns the complete output path
	 * of the file
	 * 
	 * @param tokens
	 * @return
	 * @throws IOException
	 */
	public String writeTokensToAFile(TokenList tokenList) throws IOException {
		StringBuilder data = new StringBuilder();
		File outputFile = new File("Tokens.txt");
		for (int i = 0; i < tokenList.getTokens().size(); i++) {
			String string = tokenList.getTokens().get(i);
			data.append(i != 0 ? "\r\n" : "");
			data.append(string);
		}
		FileUtils.writeStringToFile(outputFile, data.toString());
		return outputFile.getAbsolutePath();
	}

	/**
	 * This method extracts the count of number of times the token was found in
	 * the list
	 * 
	 * @param tokens
	 * @return
	 */
	public Map<String, Integer> extractCorpusFrequency(TokenList tokenList) {
		Map<String, Integer> frequencyMap = new HashMap<String, Integer>();
		for (String token : tokenList.getTokens()) {
			if (token.length() > 0) {
				Integer frequency = frequencyMap.get(token);
				if (frequency != null) {
					frequency++;
					frequencyMap.put(token, frequency);
				} else {
					frequencyMap.put(token, 1);
				}
			}
		}
		System.out.println("TokenList:[" + tokenList + "] : Map:" + frequencyMap);
		return frequencyMap;
	}
}
