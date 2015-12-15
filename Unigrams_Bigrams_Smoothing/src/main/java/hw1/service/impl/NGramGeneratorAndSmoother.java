/**
 * 
 */
package hw1.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import hw1.common.Gram;

/**
 * @author kanchan
 *
 */
public class NGramGeneratorAndSmoother {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			String selectedOption = "";
			File inputFile = null;
			while (!(inputFile != null && inputFile.exists()) && !selectedOption.trim().equalsIgnoreCase("6")) {
				System.out.print("Please enter following options in order to execute the following program : ");
				System.out.println("1: To Generate Unsmoothed Unigrams file");
				System.out.println("2: To Generate Unsmoothed Bigrams file");
				System.out.println("3: To Generate Good turing smoothed Unigrams file");
				System.out.println("4: To Generate Good turning smoothed file");
				System.out.println("5: To Generate all 4 output files ");
				System.out.println("6: To Exit");

				selectedOption = System.console().readLine("Please provide Option:");

				String filePath = System.console().readLine(
						"Please provide complete filePath of file containing tokens separated by lines - [Please make sure that the user account has access to the file]:");
				inputFile = new File(filePath);
				if(!inputFile.exists())
				{
					System.out.println("Please check filepath");
				}
			}
			if (inputFile != null && selectedOption != null) {
				UnigramGenerator ug = new UnigramGenerator();
				List<String> outputPaths = new ArrayList<String>();
				if (selectedOption.equalsIgnoreCase("1") || selectedOption.equalsIgnoreCase("5")) {
					ug = new UnigramGenerator();
					List<Gram> atualUnigrams = ug.generateProbabilities(inputFile);
					outputPaths.add(ug.writeProbabilitiesToFile(atualUnigrams, "unsmoothed-unigrams.txt"));

				}
				if (selectedOption.equalsIgnoreCase("2") || selectedOption.equalsIgnoreCase("5")) {
					ug = new UnigramGenerator();
					List<Gram> atualUnigrams = ug.generateSmoothedProbabilities(inputFile);
					outputPaths.add(ug.writeProbabilitiesToFile(atualUnigrams, "smoothed-unigrams.txt"));

				}
				if (selectedOption.equalsIgnoreCase("3") || selectedOption.equalsIgnoreCase("5")) {
					BigramGenerator bg = new BigramGenerator();
					List<Gram> bigrams = bg.generateProbabilities(inputFile);
					outputPaths.add(bg.writeProbabilitiesToFile(bigrams, "unsmoothed-bigrams.txt"));

				}
				if (selectedOption.equalsIgnoreCase("4") || selectedOption.equalsIgnoreCase("5")) {

					BigramGenerator bg = new BigramGenerator();
					List<Gram> bigrams = bg.generateSmoothedProbabilities(inputFile);
					outputPaths.add(ug.writeProbabilitiesToFile(bigrams, "smoothed-bigrams.txt"));

				}
				System.out.println("==========================================================================================");
				System.out.println("File Creation completed : Please find file(s) below.");
				for (String string : outputPaths) {
					System.out.println(string);
				}
				System.out.println("==========================================================================================");

			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(
					"Program was unable to write file. Please make sure that account used to execute the program has read and write access to specified directory");
		}

	}

}
