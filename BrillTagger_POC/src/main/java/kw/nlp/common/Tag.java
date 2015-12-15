/**
 * 
 */
package kw.nlp.common;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

/**
 * POJO for holding Tag and Tag Metadata found.
 * 
 * @author kanchan
 */
public class Tag {

	/**
	 * Map of Previous Tags occured for the given word-tag combination
	 */
	Map<String, Tag> previousTagAndFrequency = new HashMap<String, Tag>();
	private String tag = null;
	private Integer count = null;
	private Tag mostProbablePreviousTag = null;

	public Tag(String tag, Integer count) {
		super();
		this.tag = tag;
		this.count = count;
	}

	/**
	 * @return the mostProbablePreviousTag
	 */
	public Tag getMostProbablePreviousTag() {
		return mostProbablePreviousTag;
	}

	/**
	 * @param mostProbablePreviousTag
	 *            the mostProbablePreviousTag to set
	 */
	public void setMostProbablePreviousTag(Tag mostProbablePreviousTag) {
		this.mostProbablePreviousTag = mostProbablePreviousTag;
	}

	/**
	 * @return the count
	 */
	public Integer getCount() {
		return count;
	}

	/**
	 * @param count
	 *            the count to set
	 */
	public void setCount(Integer count) {
		this.count = count;
	}

	/**
	 * @return the previousTagAndFrequency
	 */
	public Map<String, Tag> getPreviousTagAndFrequency() {
		return previousTagAndFrequency;
	}

	/**
	 * @param tag
	 */
	public void addTagToFrequenciesMap(String tag) {
		Tag tagFromMap = this.previousTagAndFrequency.get(tag);
		if (tagFromMap == null) {
			this.previousTagAndFrequency.put(tag, new Tag(tag, 1));
		} else {
			tagFromMap.setCount(tagFromMap.getCount() + 1);
			this.previousTagAndFrequency.put(tag, (tagFromMap));
		}
	}

	/**
	 * @return the tag
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * @param tag
	 *            the tag to set
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[" + tag + "=" + count
				+ (MapUtils.isNotEmpty(previousTagAndFrequency) ? "(" + previousTagAndFrequency.values() + ")" : "")
				+ "]";
	}

	public Tag(String tag) {
		super();
		this.tag = tag;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return this.getTag().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return this.getTag().equals(((Tag) obj).getTag());
	}

}
