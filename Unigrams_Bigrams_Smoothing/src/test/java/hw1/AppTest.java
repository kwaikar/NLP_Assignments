package hw1;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.unitils.reflectionassert.ReflectionAssert;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import hw1.common.Gram;
import hw1.common.TokenList;
import hw1.service.impl.BigramGenerator;
import hw1.service.impl.UnigramGenerator;
import hw1.util.Tokenizer;
import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	public void testTokenizer() throws Exception {
		File inputFile = new File(this.getClass().getResource("/input.txt").getFile());
		UnigramGenerator ug = new UnigramGenerator();
		List<Gram> atualUnigrams = ug.generateProbabilities(inputFile);
		ObjectMapper mapper = new ObjectMapper();
		// mapper.writeValue(new
		// File("S:\\NLP\\Source_Code\\hw1\\src\\test\\resources\\unigrams.txt"),
		// atualUnigrams);
		File unigramsFile = new File(this.getClass().getResource("/unigrams.txt").getFile());
		List<Gram> expectedGrams = mapper.readValue(unigramsFile, new TypeReference<List<Gram>>() {
		});
		ReflectionAssert.assertReflectionEquals(atualUnigrams, expectedGrams);
		ug.writeProbabilitiesToFile(atualUnigrams, "output-unigrams.txt");

		BigramGenerator bg = new BigramGenerator();
		List<Gram> bigrams = bg.generateProbabilities(inputFile);
		// mapper.writeValue(new
		// File("S:\\NLP\\Source_Code\\hw1\\src\\test\\resources\\bigrams.txt"),
		// bigrams);
		File bigramsFile = new File(this.getClass().getResource("/bigrams.txt").getFile());
		List<Gram> expectedBiGrams = mapper.readValue(bigramsFile, new TypeReference<List<Gram>>() {
		});
		ReflectionAssert.assertReflectionEquals(bigrams, expectedBiGrams);
		ug.writeProbabilitiesToFile(bigrams, "output-bigrams.txt");
	}

	@Test
	public void testUnigramSmoothing() throws IOException {
		UnigramGenerator ug = new UnigramGenerator();
		List<Gram> grams = ug
				.generateSmoothedProbabilities(new File(this.getClass().getResource("/unigram_input.txt").getFile()));
		int count = 0;
		for (Gram gram : grams) {
			if (gram.getEntry().equals("perch") || gram.getEntry().equals("carp")) {
				Assert.assertEquals(gram.getProbability(), 0.0);
				count++;
			} else if (gram.getEntry().equals("trout")) {
				Assert.assertEquals(gram.getProbability(), new Double((double) 1 / 27));
				count++;
			} else if (gram.getEntry().equals("whitefish")) {
				Assert.assertEquals(gram.getProbability(), new Double((double) 3 / 18));
				count++;
			}
		}
		Assert.assertEquals(count, 4);

		ug.writeProbabilitiesToFile(grams, "smoothed-unigrams.txt");
	}

	@Test
	public void testBigramSmoothing() throws IOException {
		BigramGenerator ug = new BigramGenerator();
		List<Gram> grams = ug
				.generateSmoothedProbabilities(new File(this.getClass().getResource("/bigram_input.txt").getFile()));
		ug.writeProbabilitiesToFile(grams, "smoothed-bigrams.txt");

		int count = 0;
		for (Gram gram : grams) {
			if (gram.getEntry().equals("I am")) {
				Assert.assertEquals(gram.getProbability(), 0.0);
				count++;
			} else if (gram.getEntry().equals("Hi I") || gram.getEntry().equals("am good.")
					|| gram.getEntry().equals("am good")) {
				Assert.assertEquals(gram.getProbability(), new Double((double) 2 / 7 / 8));
				count++;
			} else if (gram.getEntry().equals("I I")) {
				Assert.assertEquals(gram.getProbability(), new Double((double) 7 / 8));
				count++;
			}
		}
		Assert.assertEquals(count, 5);

	}
	@Test
	public void testBigrams() throws Exception{
		BigramGenerator ug = new BigramGenerator();
		List<Gram> 	 grams = ug
					.generateSmoothedProbabilities(new File(this.getClass().getResource("/input.txt").getFile()));
			ug.writeProbabilitiesToFile(grams, "smoothed-input-bigrams.txt");

	}

	@Test
	public void testTokenization() throws IOException {
		Tokenizer t = new Tokenizer();
		File inputFile = new File(this.getClass().getResource("/input.txt").getFile());

		TokenList tokens = t.getTokens(inputFile);
		Assert.assertEquals(new Integer(tokens.getTokens().size()), new Integer(14));
		String outputPath = t.writeTokensToAFile(tokens);
		System.out.println("outputPath :" + outputPath);
		String tokenOutputFile = FileUtils.readFileToString(new File(outputPath));

		Assert.assertEquals(tokenOutputFile,
				FileUtils.readFileToString(new File(this.getClass().getResource("/tokens.txt").getFile())));

	}
}
