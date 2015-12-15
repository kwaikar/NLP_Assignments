package hw1.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import hw1.common.Gram;

public class GoodTurningSmoother {

	public List<Gram> smoothGramsUsingGoodTurning(List<Gram> inputs) {
		List<Gram> outputGrams = new LinkedList<Gram>();
		TreeMap<Double, Integer> ncMap = new TreeMap<Double, Integer>();
		for (Gram gram : inputs) {
			if (ncMap.containsKey(gram.getProbability())) {
				ncMap.put(gram.getProbability(), (ncMap.get(gram.getProbability() + 1)));
			}
			else
			{
				ncMap.put(gram.getProbability(), 1);
			}
		}
	int	totalCount = inputs.size();
//		ncMap.entrySet().iterator()
		return outputGrams;
	}
}