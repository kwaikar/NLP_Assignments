package kw.nlp;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import kw.nlp.common.ParsedWord;
import kw.nlp.common.Tag;

/**
 * For this question, you have been given a POS-tagged training file,
 * HW2_F15_NLP6320_POSTaggedTrainingSet.txt, that has been tagged with POS tags
 * from the Penn Treebank POS tagset (Figure 5.6). Use this POS tagged file to
 * compute for each word w the tag t that maximizes P(t|w). Retag the training
 * file with POS tags that are most probable for a given word. Compute the error
 * rate by comparing the retagged file against the original tagged file. Now
 * perform error analysis to find the top-5 erroneously tagged words. Write at
 * least five rules to do a better job of tagging these top-5 erroneously tagged
 * words, and show the difference in error rates.
 * 
 * @author kanchan
 *
 */
public class RuleBasedPOSTagger {

	/**
	 * This method loops through complete file and returns the Map containing
	 * word,ParsedWord pairs of entire text found.
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public Map<String, ParsedWord> parseFileAndReturnPosTagPossibility(File file) throws Exception {
		Map<String, ParsedWord> map = new HashMap<String, ParsedWord>();

		String fileContent = FileUtils.readFileToString(file).replaceAll("\\r", " ").replaceAll("\\n", " ")
				.replaceAll("  ", " ");
		String[] splitContent = fileContent.split(" ");
		String previousTag = null;
		for (String taggedWordPair : splitContent) {
			if (StringUtils.isNotBlank(taggedWordPair) && taggedWordPair.contains("_")) {
				String word = taggedWordPair.substring(0, taggedWordPair.lastIndexOf('_'));
				ParsedWord entryFromMap = map.get(word);
				if (entryFromMap == null) {
					entryFromMap = new ParsedWord(word);
				}
				String tag = taggedWordPair.substring(taggedWordPair.lastIndexOf('_') + 1, taggedWordPair.length());
				entryFromMap.addTagToFrequenciesMap(tag, previousTag);
				map.put(word, entryFromMap);
				previousTag = tag;
			}
		}

		/**
		 * Set "Most Probable tag"
		 */
		for (ParsedWord entry : map.values()) {
			int count = 0;
			Tag maximumProbabilityTag = null;
			for (Tag tagEntry : entry.getTagsAndFrequencies().values()) {
				if (tagEntry.getCount() > count) {
					count = tagEntry.getCount();
					maximumProbabilityTag = tagEntry;
				}
			}
			entry.setMaximumProbabilisticTag(maximumProbabilityTag);
			/**
			 * Set Deviation count
			 */
			for (Tag tagEntry : entry.getTagsAndFrequencies().values()) {
				if (tagEntry.getCount() < count) {
					entry.setErrorCount(entry.getErrorCount() + tagEntry.getCount());
				}
			}
		}
		return map;
	}

	/**
	 * Regenerate Input file Based on "Most Probable Tag" identified for each
	 * word.
	 * 
	 * @param file
	 * @param parsedWordMap
	 * @param numberOfRulesToApply
	 * @return
	 * @throws Exception
	 */
	public String regenerateFileUsingmaximumFrequency(File file, Map<String, ParsedWord> parsedWordMap,
			int numberOfRulesToApply) throws Exception {
		File outputTagFile = new File("Maximized_Tag_" + numberOfRulesToApply + ".txt");
		StringBuilder outputFile = new StringBuilder();
		String fileContent = FileUtils.readFileToString(file);
		String[] splitContent = fileContent.split(" ");
		String previousWord = "";
		String word = "";
		for (String taggedWordPair : splitContent) {

			if (StringUtils.isNotBlank(taggedWordPair) && taggedWordPair.contains("_")) {
				word = taggedWordPair.substring(0, taggedWordPair.lastIndexOf('_')).replaceAll("\\r", " ")
						.replaceAll("\\n", " ").replaceAll(" ", "");
				String mostProbableTag = parsedWordMap.get(word).getMaximumProbabilisticTag().getTag();
				String previousWordsTag = parsedWordMap.get(previousWord) != null
						? parsedWordMap.get(previousWord).getMaximumProbabilisticTag().getTag() : "";

				/**
				 * Apply Additional Business rules in order reduce the POS
				 * Tagging error rate
				 */
				if (numberOfRulesToApply >= 1) {

					if (word.equalsIgnoreCase("have") && previousWordsTag.equalsIgnoreCase("MD")) {
						mostProbableTag = "VB";
					}
					if (numberOfRulesToApply >= 2) {
						if (word.equalsIgnoreCase("'s") && previousWordsTag.equalsIgnoreCase("PRP")) {
							mostProbableTag = "VBZ";
						}
						if (numberOfRulesToApply >= 3) {
							if (word.equalsIgnoreCase("plans") && previousWordsTag.equalsIgnoreCase("NN")) {
								mostProbableTag = "NNS";
							}
							if (numberOfRulesToApply >= 4) {
								if (word.equalsIgnoreCase("that") && previousWordsTag.equalsIgnoreCase("NNS")) {
									mostProbableTag = "WDT";
								}
								if (numberOfRulesToApply >= 5) {
									if (word.equalsIgnoreCase("building") && previousWordsTag.equalsIgnoreCase("DT")) {
										mostProbableTag = "NN";
									}
								}
							}
						}
					}
				}
				outputFile.append(word + "_" + mostProbableTag + " ");
			}
			previousWord = word;
		}
		FileUtils.writeStringToFile(outputTagFile, outputFile.toString());

		System.out.println("File tagged using corpus written to following file ");
		System.out.println(outputTagFile.getAbsolutePath());
		return outputTagFile.getAbsolutePath();
	}
	
	/**
	 * This method prints top 5 Tag deviations
	 * @param map
	 */
	public void printTop5ErroneousWords(Map<String, ParsedWord> map) {
		List<ParsedWord> words = new ArrayList<ParsedWord>(map.values());
		Collections.sort(words);
		System.out.println(
				"---------------------- Top 5 Mistaken tags and their corresponding frequency------------------------");
		for (int i = 0; i < 5; i++) {
			System.out.println(map.get((words.get(i)).getWord()).getWord() + ": "
					+ map.get((words.get(i)).getWord()).getErrorCount());
		}
	}

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
	 * This method writes the Maximum Probability Tags to a file
	 * @param parsedWordMap
	 * @throws Exception
	 */
	public void writeMaximumProbabilityTagToAFile(Map<String, ParsedWord> parsedWordMap) throws Exception {
		File probabilityMaximizerFile = new File("maximumProbabilityTag.txt");
		StringBuilder sb = new StringBuilder();
		for (ParsedWord word : parsedWordMap.values()) {
			sb.append(word.getWord() + " : " + word.getMaximumProbabilisticTag().getTag().replaceAll("\\s", "") + "\t\t"
					+ word.getMaximumProbabilisticTag().getCount() + "\n");
		}
		FileUtils.writeStringToFile(probabilityMaximizerFile, sb.toString());
		System.out.println("Tags with Maximum Probability written to following file: ");
		System.out.println(probabilityMaximizerFile.getAbsolutePath());

	}

	/**
	 * This method accepts the generated and base file, calculates error rate and prints the same
	 * @param manuallyTaggedFile
	 * @param generatedFile
	 * @return
	 * @throws Exception
	 */
	public double calculateTotalErrorRate(File manuallyTaggedFile, File generatedFile) throws Exception {

		String[] idealFile = FileUtils.readFileToString(manuallyTaggedFile).split(" ");
		String[] generatedFileStrings = FileUtils.readFileToString(generatedFile).split(" ");
		int mismatchesFound = 0;
		int totalWords = 0;
		for (int i = 0; i < idealFile.length; i++) {
			idealFile[i] = idealFile[i].replaceAll("\\r", " ").replaceAll("\\n", " ").replaceAll(" ", "");
			if (StringUtils.isNotBlank(idealFile[i]) && idealFile[i].contains("_")
					&& StringUtils.isNotBlank(generatedFileStrings[i]) && generatedFileStrings[i].contains("_")) {
				totalWords++;
				if (idealFile[i].substring(0, idealFile[i].lastIndexOf('_'))
						.equals(generatedFileStrings[i].substring(0, generatedFileStrings[i].lastIndexOf('_')))) {

					String manualTag = idealFile[i].substring(idealFile[i].lastIndexOf('_') + 1, idealFile[i].length());
					String calculatedTag = generatedFileStrings[i]
							.substring(generatedFileStrings[i].lastIndexOf('_') + 1, generatedFileStrings[i].length());
					if (!manualTag.equalsIgnoreCase(calculatedTag)) {
						mismatchesFound++;
					}

				} else {
					throw new Exception(
							"Strings do not match. Please verify that both the files have exactly same number of entries: |"
									+ generatedFileStrings[i] + "| : |" + idealFile[i] + "|");
				}
			}
		}
		System.out.println("Number of Mismatched Tags found in the file : " + mismatchesFound + "/" + totalWords
				+ ". Error Rate=" + round(((double) mismatchesFound / totalWords), 5));
		return round((double) mismatchesFound / totalWords, 5);
	}

	/**
	 * Main program that first tags file by most probable tag based on frequency and later applies business rules in order to bring error rate down.
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			RuleBasedPOSTagger rbpt = new RuleBasedPOSTagger();
			String filePath = System.console()
					.readLine("Please provide complete filePath of HW2_F15_NLP6320_POSTaggedTrainingSet.txt: ");
			File manuallyTaggedFile = new File(filePath);
			if (!manuallyTaggedFile.exists()) {
				System.out.println("Please check filepath");
			}

			System.out.println("Starting Part of speech tagging of HW2_F15_NLP6320_POSTaggedTrainingSet.txt");
			System.out.println("---------------------------------------------------------------------------");

			Map<String, ParsedWord> map = rbpt.parseFileAndReturnPosTagPossibility(manuallyTaggedFile);
			rbpt.writeMaximumProbabilityTagToAFile(map);
			rbpt.printTop5ErroneousWords(map);
			rbpt.calculateTotalErrorRate(manuallyTaggedFile,
					new File(rbpt.regenerateFileUsingmaximumFrequency(manuallyTaggedFile, map, 0)));

			System.out.println(
					"-------------------------------------- Applying Rule 1 -------------------------------------");
			rbpt.calculateTotalErrorRate(manuallyTaggedFile,
					new File(rbpt.regenerateFileUsingmaximumFrequency(manuallyTaggedFile, map, 1)));
			System.out.println(
					"-----------------------------------------------------------------------------------------");
			System.out.println(
					"-------------------------------------- Applying Rule 1,2 -------------------------------------");
			rbpt.calculateTotalErrorRate(manuallyTaggedFile,
					new File(rbpt.regenerateFileUsingmaximumFrequency(manuallyTaggedFile, map, 2)));
			System.out.println(
					"-----------------------------------------------------------------------------------------");
			System.out.println(
					"-------------------------------------- Applying Rule 1,2,3 -------------------------------------");
			rbpt.calculateTotalErrorRate(manuallyTaggedFile,
					new File(rbpt.regenerateFileUsingmaximumFrequency(manuallyTaggedFile, map, 3)));
			System.out.println(
					"-----------------------------------------------------------------------------------------");
			System.out.println(
					"-------------------------------------- Applying Rule 1,2,3,4 -------------------------------------");
			rbpt.calculateTotalErrorRate(manuallyTaggedFile,
					new File(rbpt.regenerateFileUsingmaximumFrequency(manuallyTaggedFile, map, 4)));
			System.out.println(
					"-----------------------------------------------------------------------------------------");
			System.out.println(
					"-------------------------------------- Applying Rule 1,2,3,4,5 -------------------------------------");
			rbpt.calculateTotalErrorRate(manuallyTaggedFile,
					new File(rbpt.regenerateFileUsingmaximumFrequency(manuallyTaggedFile, map, 5)));
			System.out.println(
					"-----------------------------------------------------------------------------------------");

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(
					"Program was unable to write file. Please make sure that account used to execute the program has read and write access to specified directory");
		}

	}

}
