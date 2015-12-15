package kw.nlp;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import junit.framework.Assert;
import junit.framework.TestCase;
import kw.nlp.common.ParsedWord;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {

	public void testRuleBasedPOSTagger() throws Exception {
		File manuallyTaggedFile = new File(
				this.getClass().getResource("/HW2_F15_NLP6320_POSTaggedTrainingSet.txt").getFile());

		RuleBasedPOSTagger rbpt = new RuleBasedPOSTagger();
		Map<String, ParsedWord> map = rbpt.parseFileAndReturnPosTagPossibility(manuallyTaggedFile);
		List<ParsedWord> words = new LinkedList(map.values());
		String test = FileUtils.readFileToString(manuallyTaggedFile);
		String[] stringsInFile = test.split(" ");
		for (String string : stringsInFile) {
			{
				string=string.replaceAll("\\r", " ").replaceAll("\\n", " ").replaceAll(" ", "");
				if (string.indexOf('_') != -1) {
					Assert.assertTrue(words.contains(new ParsedWord(string.substring(0, string.indexOf('_')))));
				}
			}
		}
		int counts = 0;
		for (ParsedWord parsedWord : words) {
			if (parsedWord.getWord().equals(",")) {
				counts++;
				Assert.assertEquals(parsedWord.getTagsAndFrequencies().size(), 1);
				Assert.assertEquals(parsedWord.getTagsAndFrequencies().get(",").getCount(), new Integer(3842));
			}
		}
		Assert.assertEquals(counts, 1);
		rbpt.printTop5ErroneousWords(map);
		File retaggedFile = new File(rbpt.regenerateFileUsingmaximumFrequency(manuallyTaggedFile, map, 0));
		Assert.assertTrue(retaggedFile.exists());
		double error = rbpt.calculateTotalErrorRate(manuallyTaggedFile, retaggedFile);
		System.out.println(error);

		retaggedFile = new File(rbpt.regenerateFileUsingmaximumFrequency(manuallyTaggedFile, map, 1));
		Assert.assertTrue(retaggedFile.exists());
		double error1 = rbpt.calculateTotalErrorRate(manuallyTaggedFile, retaggedFile);
		System.out.println(error1);
		Assert.assertTrue(error1 < error);

		
		retaggedFile = new File(rbpt.regenerateFileUsingmaximumFrequency(manuallyTaggedFile, map, 2));
		Assert.assertTrue(retaggedFile.exists());
		double error2 = rbpt.calculateTotalErrorRate(manuallyTaggedFile, retaggedFile);
		System.out.println(error2);
		Assert.assertTrue(error2 < error1);

		
		retaggedFile = new File(rbpt.regenerateFileUsingmaximumFrequency(manuallyTaggedFile, map, 3));
		Assert.assertTrue(retaggedFile.exists());
		double error3 = rbpt.calculateTotalErrorRate(manuallyTaggedFile, retaggedFile);
		System.out.println(error3);
		Assert.assertTrue(error3 < error2);

		
		
		retaggedFile = new File(rbpt.regenerateFileUsingmaximumFrequency(manuallyTaggedFile, map, 4));
		Assert.assertTrue(retaggedFile.exists());
		double error4 = rbpt.calculateTotalErrorRate(manuallyTaggedFile, retaggedFile);
		System.out.println(error4);
		Assert.assertTrue(error4 < error3);

		
		retaggedFile = new File(rbpt.regenerateFileUsingmaximumFrequency(manuallyTaggedFile, map, 5));
		Assert.assertTrue(retaggedFile.exists());
		double error5 = rbpt.calculateTotalErrorRate(manuallyTaggedFile, retaggedFile);
		System.out.println(error5);
		Assert.assertTrue(error5 < error4);


	}
}
