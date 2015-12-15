/**
 * 
 */
package kw.nlp.common;

import java.util.HashMap;
import java.util.Map;

/**
 * POJO that contains Metadata for each unique word found.
 * 
 * @author kanchan
 *
 */
public class ParsedWord implements Comparable<ParsedWord> {

	/**
	 * Map of Tag, Tag metadata
	 */
	Map<String, Tag> tagsAndFrequencies = new HashMap<String, Tag>();
	private String word = null;
	/**
	 * frequency of deviations from the "most probable Tag"
	 */
	private Integer errorCount = 0;
	/**
	 * Most probable tag identified.
	 */
	private Tag maximumProbabilisticTag = null;

	/**
	 * @param word
	 */
	public ParsedWord(String word) {
		this.word = word;
	}

	/**
	 * @return the tagsAndFrequencies
	 */
	public Map<String, Tag> getTagsAndFrequencies() {
		return tagsAndFrequencies;
	}

	/**
	 * @param tagsAndFrequencies
	 *            the tagsAndFrequencies to set
	 */
	public void addTagToFrequenciesMap(String tag, String previousTag) {

		Tag tagFromMap = this.tagsAndFrequencies.get(tag);
		if (tagFromMap == null) {
			tagFromMap = new Tag(tag, 1);
		} else {
			tagFromMap.setCount(tagFromMap.getCount() + 1);
		}
		tagFromMap.addTagToFrequenciesMap(previousTag);
		this.tagsAndFrequencies.put(tagFromMap.getTag(), tagFromMap);
	}

	/**
	 * @return the word
	 */
	public String getWord() {
		return word;
	}

	/**
	 * @param word
	 *            the word to set
	 */
	public void setWord(String word) {
		this.word = word;
	}

	/**
	 * @return the errorCount
	 */
	public int getErrorCount() {
		return errorCount;
	}

	/**
	 * @param errorCount
	 *            the errorCount to set
	 */
	public void setErrorCount(int errorCount) {
		this.errorCount = errorCount;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.word + ":" + errorCount + "{" + maximumProbabilisticTag.getTag() + ":"
				+ maximumProbabilisticTag.getCount() + "}" + "[" + this.tagsAndFrequencies.values() + "]";
	}

	@Override
	public int hashCode() {
		return this.word.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null) {
			return this.word.contentEquals(((ParsedWord) obj).word);
		} else {
			return false;
		}
	}

	/**
	 * @return the maximumProbabilisticTag
	 */
	public Tag getMaximumProbabilisticTag() {
		return maximumProbabilisticTag;
	}

	/**
	 * @param maximumProbabilisticTag
	 *            the maximumProbabilisticTag to set
	 */
	public void setMaximumProbabilisticTag(Tag maximumProbabilisticTag) {
		this.maximumProbabilisticTag = maximumProbabilisticTag;
	}

	public int compareTo(ParsedWord o) {
		return o.errorCount.compareTo(this.errorCount);
	}

}
