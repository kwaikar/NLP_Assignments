package hw1.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import hw1.common.Gram;
import hw1.common.SmoothingInputBean;

 public abstract class NGramGenerator {

	/**
	 * This method is used for generating probabilities 
	 * @param inputFile
	 * @return
	 * @throws IOException
	 */
	public abstract List<Gram> generateProbabilities(File inputFile) throws IOException;
	
	public abstract List<Gram> generateSmoothedProbabilities(File f) throws IOException;
	
	/**
	 * This method is used for writing the probabilities to a file
	 * @param probabilities
	 * @throws IOException
	 */
	public String writeProbabilitiesToFile(List<Gram> probabilities,String fileName) throws IOException{
		
		StringBuilder data = new StringBuilder();
		Collections.sort(probabilities);
		for (Gram gram : probabilities) {
			data.append(gram.getEntry()+ " "+gram.getProbability()+"\r\n");
		}
		File outputFile=new File(fileName);
		FileUtils.writeStringToFile(outputFile, data.toString());
		return ("Output has been written to following file \r\n"+outputFile.getAbsolutePath());
	}

	/**
	 * This method extracts and returns Frequency of Frequency. The key is frequency and value is frequencyOfFrequency.
	 * @param wordFrequencyMap
	 * @return
	 */
	public Map<Integer, Integer> extractFrequencyOfFrequency(Map<String, Integer> wordFrequencyMap) {
		Map<Integer, Integer> frequencyOfFrequencyMap = new HashMap<Integer, Integer>();

		for (Map.Entry<String, Integer> entry : wordFrequencyMap.entrySet()) {
			// Fetch the bucket by count. If found, increment the count
			Integer countOfCurrentEntriesInBucket = frequencyOfFrequencyMap.get(entry.getValue());
			if (countOfCurrentEntriesInBucket != null) {
				countOfCurrentEntriesInBucket = countOfCurrentEntriesInBucket + 1;
			} else {
				countOfCurrentEntriesInBucket = 1;
			}
			frequencyOfFrequencyMap.put(entry.getValue(), countOfCurrentEntriesInBucket);
		}
		return frequencyOfFrequencyMap;
	}

	/**
	 * This method accepts the smoothing input bean and generates smoothed probabilities and returns the same.
	 * @param bean
	 * @return
	 */
	public List<Gram> generateSmoothedProbabilities(SmoothingInputBean bean) {
		Map<Integer, Integer> frequencyOfFrequencyMap = extractFrequencyOfFrequency(bean.getWordFrequencyMap());
		List<Gram> grams = new ArrayList<Gram>();
		List<Integer> nSubC = new ArrayList<Integer>(frequencyOfFrequencyMap.keySet());
		System.out.println("before --->" + nSubC);
		Collections.sort(nSubC);
		System.out.println("after --->" + nSubC);
		Integer[] array = new Integer[Collections.max(nSubC) + 2]; // one entry
																	// for max+1
																	// as well!
		for (int i = 0; i < array.length; i++) {
			array[i] = 0; // simple declaration since there can be zero values
		}
		for (Integer nSubCEntry : nSubC) {
			array[nSubCEntry] = frequencyOfFrequencyMap.get(nSubCEntry);
		}
		System.out.println("---->");
		for (Integer integer : array) {
			System.out.println(":" + integer);
		}
		System.out.println("---->");
		bean.setFrequencyOfFrequencyCount(array);
		for (Map.Entry<String, Integer> entry : bean.getWordFrequencyMap().entrySet()) {
			int c = entry.getValue();
			System.out.println("Entrykey : " + entry.getKey());
			System.out.println("calculating : " + c + " : " + array[c] + " : " + array[c + 1]);
			double cStar = 0;
			if(c!=0){
			if (array[c + 1] == 0) {
				cStar = 0;
			} else {
				cStar = ((double) ((c + 1) * array[c + 1]) / array[c]);
			}
			}
			else
			{
				cStar= array[c + 1];
			}

			System.out.println("Cstar : " + cStar+"/"+bean.getDenominiator() );
			double totalProbability = (double) cStar / bean.getDenominiator();
			grams.add(new Gram(entry.getKey(), totalProbability));
		}
		
		Collections.sort(grams);
		System.out.println("grams->" + grams);
		return grams;
	}


}
