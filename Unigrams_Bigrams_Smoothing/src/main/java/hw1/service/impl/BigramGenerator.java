package hw1.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import hw1.common.Gram;
import hw1.common.SmoothingInputBean;
import hw1.common.TokenList;
import hw1.common.Utils;
import hw1.util.Tokenizer;

/**
 * BigramGenerator class for implementation of bigrams
 * 
 * @author kanchan
 *
 */
public class BigramGenerator extends NGramGenerator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see hw1.service.NGramGenerator#generateProbabilities(java.io.File)
	 */
	public List<Gram> generateProbabilities(File f) throws IOException {
		List<Gram> grams = new LinkedList<Gram>();
		Tokenizer tokenizer = new Tokenizer();
		TokenList tokens = tokenizer.getTokens(f);
		String sentence = Utils.createSingleSentence(tokens);
		Map<String, String> allPossibleTokens = getPossibleTokens(tokens);

		for (Map.Entry<String, String> token : allPossibleTokens.entrySet()) {
			grams.add(new Gram(token.getKey(),
					Utils.round(getBigramProbability(sentence, token.getKey(), token.getValue()), 5)));
		}

		Collections.sort(grams);
		return grams;
	}

	private Map<String, String> getPossibleTokens(TokenList tokens) {
		Map<String, String> allPossibleTokens = new LinkedHashMap<String, String>();
		for (String token : tokens.getTokens()) {
			for (String secondToken : tokens.getTokens()) {
				allPossibleTokens.put(token + " " + secondToken, token);
			}
		}
		return allPossibleTokens;
	}

	private double getBigramProbability(String sentence, String firstAndSecondWord, String firstWord) {
		double value = 0;

		/*
		 * System.out.println("Sentence : " + sentence);
		 * System.out.println(firstAndSecondWord+ " : "+firstWord
		 * +"["+Utils.getCountOfOccurances(sentence, firstAndSecondWord)+"/"+
		 * Utils.getCountOfOccurances(sentence, firstWord)+"]");
		 */
		if(firstAndSecondWord.equals("big ice-creams."))
		{
			System.out.println("hellp");
		}
		value = (double) Utils.getCountOfOccurances(sentence, firstAndSecondWord)
				/ Utils.getCountOfOccurances(sentence, firstWord);

		return value;
	}

	@Override
	public List<Gram> generateSmoothedProbabilities(File f) throws IOException {

		Tokenizer tokenizer = new Tokenizer();

		// Frequency map - this map contains frequency as the key and frequency
		// of frequency as the value.

		TokenList tokens = tokenizer.getTokens(f);
		String sentence = Utils.createSingleSentence(tokens);
		Map<String, String> allPossibleTokens = getPossibleTokens(tokens);
		Map<String, Integer> wordFrequencyMap = new HashMap<String, Integer>();
		int countOfOccurances = 0;
		int totalCountOfValidBigrams = 0;
		for (Map.Entry<String, String> token : allPossibleTokens.entrySet()) {
			countOfOccurances = Utils.getCountOfOccurances(sentence, token.getKey());
			wordFrequencyMap.put(token.getKey(), countOfOccurances);
			if (countOfOccurances != 0) {
				totalCountOfValidBigrams += countOfOccurances;
			}
		}
		System.out.println("totalCountOfValidBigrams" + totalCountOfValidBigrams);
		System.out.println("wordFrequencyMap" + wordFrequencyMap);
		SmoothingInputBean bean = new SmoothingInputBean();
		bean.setTokens(tokenizer.getTokens(f));
		bean.setWordFrequencyMap(wordFrequencyMap);
		bean.setDenominiator(totalCountOfValidBigrams );

		List<Gram> grams = generateSmoothedProbabilities(bean);
		return grams;
	}

}