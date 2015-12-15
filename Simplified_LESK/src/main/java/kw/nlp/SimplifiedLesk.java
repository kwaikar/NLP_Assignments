package kw.nlp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;

/**
 * Using WordNet as the dictionary, implement the SIMPLIFIED LESK algorithm to
 * disambiguate the word bank in the sentence. Your output should show the word
 * overlap for each sense of the word bank in WordNet and the final chosen
 * sense.
 * 
 * @author Kanchan Waikar
 *
 */
public class SimplifiedLesk {

	/**
	 * Handle to Wordnet dictionary
	 */
	private IDictionary dictionary;
	String stopWords = "";

	/**
	 * Accept path of wordnet Dictionary 3.0 and stopwords file.
	 * 
	 * @param path
	 * @throws Exception
	 */
	public SimplifiedLesk(String path) throws Exception {

		try {
			dictionary = new Dictionary(new URL("file", null, path));
		} catch (Exception m) {
			m.printStackTrace();
		}

		try {
			InputStream ioStream = getClass().getClassLoader().getResourceAsStream("stopWords.file");
			stopWords = IOUtils.toString(ioStream);
			ioStream.close();
		} catch (Exception e) {
			System.out.println("Exception occured while reading stopWords file");
			e.printStackTrace();
		}

	}

	/**
	 * This function returns senses corresponding to given word.
	 * 
	 * @param word
	 * @return
	 */
	public List<IWord> getSenses(String word) {
		List<IWord> words = new LinkedList<IWord>();
		try {
			List<POS> parOfSpeechTags = new LinkedList<POS>();
			parOfSpeechTags.add(POS.NOUN);
			parOfSpeechTags.add(POS.VERB);
			parOfSpeechTags.add(POS.ADJECTIVE);
			parOfSpeechTags.add(POS.ADVERB);
			dictionary.open();
			for (POS pos : parOfSpeechTags) {
				words.addAll(getAllWordSenses(dictionary.getIndexWord(word, pos)));
			}
			dictionary.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return words;
	}

	/**
	 * Return collection of all word senses
	 * 
	 * @param words
	 * @param idxWord
	 */
	private Collection<IWord> getAllWordSenses(IIndexWord idxWord) {
		List<IWord> words = new LinkedList<IWord>();
		if (idxWord != null && CollectionUtils.isNotEmpty(idxWord.getWordIDs())) {
			for (IWordID wordID : idxWord.getWordIDs()) {
				IWord wordMeaning = dictionary.getWord(wordID);
				words.add(wordMeaning);
			}
		}
		return words;
	}

	/**
	 * This Method implements Simplified Lesk Algorithm in order to disambiguate
	 * the word's sense in the sentence.
	 * 
	 * @param sentence
	 * @param wordToBeDisambiguated
	 * @return
	 */
	public IWord implementLesk(String sentence, String wordToBeDisambiguated) {
		IWord bestSense = null;
		int maxOverlap = 0;
		System.out.println("Word to be disambiguated : " + wordToBeDisambiguated);
		System.out.println();
		System.out.println("Input Sentence : " + sentence);
		System.out.println();
		System.out.println(
				"stop words that would be ignored during overlap identification: " + stopWords.replaceAll("\\|", ","));
		System.out.println();
		List<IWord> sensesOfWordToBeDisambiguated = getSenses(wordToBeDisambiguated);
		System.out.println("Numbser of Senses found : " + sensesOfWordToBeDisambiguated.size());
		System.out.println();
		System.out.println("Matched Synsets: ");
		int counter = 0;
		int matchedCounter = 0;
		POS previousPOS = null;
		for (IWord iWord : sensesOfWordToBeDisambiguated) {
			counter++;
			String gloss = iWord.getSynset().getGloss();
			int wordSentenceOverlap = returnOverlapBetweenTwoSentences(sentence, gloss, wordToBeDisambiguated);
			if (previousPOS == null) {
				previousPOS = iWord.getSenseKey().getPOS();
			} else if (iWord.getSenseKey().getPOS() != previousPOS) {
				counter = 1;
				previousPOS = iWord.getSenseKey().getPOS();
			}
			System.out.println(iWord.getSenseKey().getPOS() + " Sense " + counter + " ");
			System.out.println("Gloss/Example[" + iWord.getSynset().getGloss() + "]");
			if (wordSentenceOverlap > maxOverlap) {
				maxOverlap = wordSentenceOverlap;
				bestSense = iWord;
				matchedCounter = counter;
			}
		}
		System.out.println("================================================================================");
		System.out.println(
				"Best Sense found : Sense #" + matchedCounter + " : Gloss: " + bestSense.getSynset().getGloss());
		return bestSense;
	}

	/**
	 * This method returns the number of words that overlap between the two
	 * sentences.
	 * 
	 * @param firstSentence
	 * @param secondSentence
	 * @return
	 */
	private int returnOverlapBetweenTwoSentences(String firstSentence, String secondSentence, String excludeWord) {
		String secondSentenceTmp = removeStopWordsFromInput(secondSentence).toLowerCase().trim();
		int overlapCount = 0;
		Set<String> matchedWords = new HashSet<String>();
		String[] chunkedSentence = removeStopWordsFromInput(firstSentence).toLowerCase().trim().split(" ");
		for (String firstSentSubString : chunkedSentence) {
			if (!excludeWord.equalsIgnoreCase(firstSentSubString)) {
				if (secondSentenceTmp.contains(firstSentSubString)) {
					overlapCount++;
					matchedWords.add(firstSentSubString);
				}
			}
		}
		System.out.println();
		if (overlapCount != 0) {
			System.out.print("Significant Overlap : " + matchedWords + " : ");
		} else {
			System.out.print("Significant Overlap : [] : ");
		}
		return overlapCount;
	}

	/**
	 * This method removes stop words from the input sentence.
	 * 
	 * @param input
	 * @return
	 */
	public String removeStopWordsFromInput(String input) {

		return input.toLowerCase().replaceAll("(^| )(" + stopWords + ")( |$)", " ").replaceAll("[?!.]", " ")
				.replaceAll("\"", "").replaceAll(";", "").replaceAll("  ", " ").trim();
	}

	/**
	 * Main method for following requirement 
	 * Using WordNet as the dictionary, implement the SIMPLIFIED LESK algorithm to disambiguate the word bank in
	 * the sentence. Your output should show the word overlap for each sense of
	 * the word bank in WordNet and the final chosen sense.
	 * " The bank can guarantee deposits will eventually cover future tuition costs because it invests in adjustable-rate mortgage securities."
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		String filePath = System.console().readLine("Please provide complete dictionary Directory Path : ");
		File dictionaryFolder = new File(filePath);
		if (!dictionaryFolder.exists() || !dictionaryFolder.isDirectory()) {
			System.out.println("Please check filepath - Please make sure your provide complete path of dict_3.0: ");
		} else {
			SimplifiedLesk sl = new SimplifiedLesk(dictionaryFolder.getAbsolutePath());
			sl.implementLesk(
					"The bank can guarantee deposits will eventually cover future tuition costs because it invests in adjustable-rate mortgage securities",
					"bank");
		}

	}
}