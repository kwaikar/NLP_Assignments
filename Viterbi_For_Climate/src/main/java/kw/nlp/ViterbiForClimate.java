package kw.nlp;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.math3.util.Pair;

import kw.nlp.common.ViterbiState;

/**
  *  HMM Decoding: Viterbi Algorithm 
	Implement the Viterbi algorithm and run it with the HMM in Fig. 6.3 to compute the
	most likely weather sequences for each of the two observation sequences, 331122313
	and 331123312.
  * @param args
  */
public class ViterbiForClimate {

	/**
	 * This method returns the max value.
	 * 
	 * @param first
	 * @param second
	 * @return
	 */
	public static double max(double first, double second) {
		return (first > second ? first : second);
	}

	/**
	 * Accepts state definitions along with transition probabilities and
	 * generates the climate sequence for each of the observation sequences
	 * 
	 * @param sequences
	 * @param hotState
	 * @param coldState
	 */
	private void generateViterbiSequenceForIceCreamObsSequence(int[][] sequences, ViterbiState hotState,
			ViterbiState coldState) {
		int counter = 1;
		for (int[] sequence : sequences) {

			hotState.initCurrentStateProbaility();
			coldState.initCurrentStateProbaility();
			for (int i = 0; i < sequence.length; i++) {
				if (i == 0) {
					hotState.addCurrentStateProbaility(new Pair<String, Double>(null,
							hotState.getEntryProbability() * hotState.getObservationProbability(sequence[i])));
					coldState.addCurrentStateProbaility(new Pair<String, Double>(null,
							coldState.getEntryProbability() * coldState.getObservationProbability(sequence[i])));
				} else {
					calculateObservationProbability(hotState, coldState, sequence, i);
					calculateObservationProbability(coldState, hotState, sequence, i);
				}
			}
			StringBuilder sb = new StringBuilder();
			StringBuilder sequenceStr = new StringBuilder();
			List<String> stck = new LinkedList<String>();
			ViterbiState answerState = hotState.getCurrentStateProbaility()
					.get(hotState.getCurrentStateProbaility().size() - 1).getValue() > coldState
							.getCurrentStateProbaility().get(coldState.getCurrentStateProbaility().size() - 1)
							.getValue() ? hotState : coldState;
			stck.add(answerState.getStateName().toUpperCase());
			for (int i = answerState.getCurrentStateProbaility().size() - 1; i > 0; i--) {
				if (answerState.getCurrentStateProbaility().get(i).getFirst().equals(hotState.getStateName())) {
					answerState = hotState;
				} else {
					answerState = coldState;
				}
				stck.add(answerState.getStateName().toUpperCase());
			}
			for (int value : sequence) {
				sequenceStr.append( value+" ");
			}
			for (int i = stck.size() - 1; i >= 0; i--) {
				sb.append("  " + stck.get(i) + "  ");
			}

			System.out.println("Computing Weather probability for sequence " + counter+++ ") : "+sequenceStr);
			System.out.println(sb.toString().substring(0, sb.toString().length()));
			System.out.println();

		}

	}

	/**
	 *  HMM Decoding: Viterbi Algorithm 
		Implement the Viterbi algorithm and run it with the HMM in Fig. 6.3 to compute the
		most likely weather sequences for each of the two observation sequences, 331122313
		and 331123312.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ViterbiState hotState = new ViterbiState("Hot", .8);
			ViterbiState coldState = new ViterbiState("Cold", .2);
			hotState.addTransitionProbability(hotState, .7);
			hotState.addTransitionProbability(coldState, .3);
			coldState.addTransitionProbability(hotState, .4);
			coldState.addTransitionProbability(coldState, .6);
			hotState.addObservatioProbability(1, .2);
			hotState.addObservatioProbability(2, .4);
			hotState.addObservatioProbability(3, .4);
			coldState.addObservatioProbability(1, .5);
			coldState.addObservatioProbability(2, .4);
			coldState.addObservatioProbability(3, .1);

			ViterbiForClimate vc = new ViterbiForClimate();
			int[][] sequences = { { 3, 3, 1, 1, 2, 2, 3, 1, 3 }, { 3, 3, 1, 1, 2, 3, 3, 1, 2 } ,{3,1,3}};

			vc.generateViterbiSequenceForIceCreamObsSequence(sequences, hotState, coldState);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(
					"Program was unable to write file. Please make sure that account used to execute the program has read and write access to specified directory");
		}

	}

	/**
	 * Calculates observation probability and sets the same in both the states
	 * 
	 * @param firstState
	 * @param secondState
	 * @param sequence
	 * @param i
	 */
	private static void calculateObservationProbability(ViterbiState firstState, ViterbiState secondState,
			int[] sequence, int i) {
		Double firstFirstTransitionProbability = firstState.getTransitionProbability(firstState)
				* firstState.getObservationProbability(sequence[i])
				* firstState.getCurrentStateProbaility().get(i - 1).getValue();

		Double secondFirstTransitionProbability = secondState.getTransitionProbability(firstState)
				* firstState.getObservationProbability(sequence[i])
				* secondState.getCurrentStateProbaility().get(i - 1).getValue();
		firstState.addCurrentStateProbaility(firstFirstTransitionProbability > secondFirstTransitionProbability
				? new Pair<String, Double>(firstState.getStateName(), firstFirstTransitionProbability)
				: new Pair<String, Double>(secondState.getStateName(), secondFirstTransitionProbability));

	}

}
