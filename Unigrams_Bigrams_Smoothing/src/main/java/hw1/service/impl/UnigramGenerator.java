package hw1.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import hw1.common.Gram;
import hw1.common.SmoothingInputBean;
import hw1.common.TokenList;
import hw1.common.Utils;
import hw1.util.Tokenizer;

/**
 * UnigramGenerator class for implementation
 * 
 * @author kanchan
 *
 */
public class UnigramGenerator extends NGramGenerator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see hw1.service.NGramGenerator#generateProbabilities(java.io.File)
	 */
	public List<Gram> generateProbabilities(File f) throws IOException {
		List<Gram> grams = new LinkedList<Gram>();
		Tokenizer tokenizer = new Tokenizer();
		TokenList tokens = tokenizer.getTokens(f);

		Map<String, Integer> map = tokenizer.extractCorpusFrequency(tokens);
		int count = 0;
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			count += entry.getValue();
		}
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			grams.add(new Gram(entry.getKey(), (Utils.round(((double) entry.getValue() / count), 5))));
		}
		Collections.sort(grams);
		return grams;
	}

	/**
	 * This function generaes smoothed unigramprobabilities for given data
	 * 
	 * @param f
	 * @return
	 * @throws IOException
	 */
	public List<Gram> generateSmoothedProbabilities(File f) throws IOException {

		Tokenizer tokenizer = new Tokenizer();

		// Frequency map - this map contains frequency as the key and frequency
		// of frequency as the value.

		SmoothingInputBean bean = new SmoothingInputBean();
		bean.setTokens(tokenizer.getTokens(f));
		bean.setWordFrequencyMap(tokenizer.extractCorpusFrequency(bean.getTokens()));
		bean.setDenominiator(bean.getTokens().getTokens().size());

		List<Gram> grams = generateSmoothedProbabilities(bean);
		return grams;
	}

	
	}