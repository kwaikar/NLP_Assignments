/**
 * 
 */
package hw1.common;

import java.io.Serializable;

/**
 * This bean will hold Ngrams based on implementation
 * 
 * @author kanchan
 *
 */
public class Gram implements Comparable<Gram>, Serializable {

	private static final long serialVersionUID = 12323423421L;
	private String entry;
	private double probability;

	public Gram() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * constructor
	 * 
	 * @param entry
	 * @param probability
	 */
	public Gram(String entry, double probability) {
		super();
		this.entry = entry;
		this.probability = probability;
	}

	/**
	 * @return the entry
	 */
	public String getEntry() {
		return entry;
	}

	/**
	 * @param entry
	 *            the entry to set
	 */
	public void setEntry(String entry) {
		this.entry = entry;
	}

	/**
	 * @return the probability
	 */
	public double getProbability() {
		return probability;
	}

	/**
	 * @param probability
	 *            the probability to set
	 */
	public void setProbability(double probability) {
		this.probability = probability;
	}

	public int compareTo(Gram o) {
		return this.entry.toLowerCase().compareTo(o.entry.toLowerCase());
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return entry + ":" + probability;
	}
}
