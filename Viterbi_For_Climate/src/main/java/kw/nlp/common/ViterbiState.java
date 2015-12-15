package kw.nlp.common;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.util.Pair;

/**
 * POJO for Holding Viterbi State information.
 * @author kanchan
 *
 */
public class ViterbiState {

	private String stateName;
	private Double entryProbability;
	private Map<ViterbiState, Double> transitionProbabilityMap;
	List<Pair<String, Double>> currentStateProbaility = new LinkedList<Pair<String, Double>>();
	private Map<Integer, Double> observationProbability = new HashMap<Integer, Double>();

	/**
	 * @return the currentStateProbaility
	 */
	public List<Pair<String, Double>> getCurrentStateProbaility() {
		return currentStateProbaility;
	}

	/**
	 * @param currentStateProbaility
	 *            the currentStateProbaility to add
	 */
	public void initCurrentStateProbaility() {
		this.currentStateProbaility= new LinkedList<Pair<String, Double>>();
	}

	
	/**
	 * @param currentStateProbaility
	 *            the currentStateProbaility to add
	 */
	public void addCurrentStateProbaility(Pair<String, Double> pair) {
		this.currentStateProbaility.add(pair);
	}

	/**
	 * @param stateName
	 * @param entryProbability
	 */
	public ViterbiState(String stateName, Double entryProbability) {
		super();
		this.stateName = stateName;
		this.entryProbability = entryProbability;
	}

	/**
	 * @return the stateName
	 */
	public String getStateName() {
		return stateName;
	}

	/**
	 * @param stateName
	 *            the stateName to set
	 */
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	/**
	 * @return the entryProbability
	 */
	public Double getEntryProbability() {
		return entryProbability;
	}

	/**
	 * @param entryProbability
	 *            the entryProbability to set
	 */
	public void setEntryProbability(Double entryProbability) {
		this.entryProbability = entryProbability;
	}

	/**
	 * This method returns the transition probability.
	 * 
	 * @param state
	 * @return
	 */
	public Double getTransitionProbability(ViterbiState state) {
		return transitionProbabilityMap.get(state);
	}

	/**
	 * @param transitionProbabilityMap
	 *            the transitionProbabilityMap to set
	 */
	public void addTransitionProbability(ViterbiState destinationState, double probability) {
		if (this.transitionProbabilityMap == null) {
			this.transitionProbabilityMap = new HashMap<ViterbiState, Double>();
		}
		this.transitionProbabilityMap.put(destinationState, probability);
	}

	/**
	 * This method returns the transition probability.
	 * 
	 * @param state
	 * @return
	 */
	public Double getObservationProbability(Integer observation) {
		return observationProbability.get(observation);
	}

	/**
	 * @param transitionProbabilityMap
	 *            the transitionProbabilityMap to set
	 */
	public void addObservatioProbability(Integer observation, double probability) {
		if (this.observationProbability == null) {
			this.observationProbability = new HashMap<Integer, Double>();
		}
		this.observationProbability.put(observation, probability);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		for (Pair<String, Double> pair : currentStateProbaility) {
			sb.append(pair.getFirst()+" : "+pair.getSecond()+","); 
		}
		return "ViterbiState [stateName=" + stateName + ", entryProbability=" + entryProbability
				+ ", currentStateProbaility="
				+ sb.toString().substring(0, sb.length()-1)				+ "]";
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return this.getStateName().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return this.getStateName().equals(((ViterbiState) obj).getStateName());
	}

}
