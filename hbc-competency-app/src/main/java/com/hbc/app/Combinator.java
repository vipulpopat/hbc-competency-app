package com.hbc.app;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Combinator {
	final int numColours;
	final int numCombinations;

	public Combinator(int numColours) {
		if (numColours > 30)
			throw new IllegalArgumentException("too many colours");

		this.numColours = numColours;
		numCombinations = pow(numColours);
	}

	int pow(int i) {
		return (int) Math.pow(2, i);
	}

	public int getNumCombinations() {
		return numCombinations;
	}

	public List<Boolean> getCombination(int com) {
		List<Boolean> combination = new ArrayList<>(numColours);
		for (int i = numColours - 1; i >= 0; i--) {
			int mask = 1 << i;
			combination.add((com & mask) == mask);
		}

		return combination;
	}

	public List<List<Boolean>> getAllNonZeroCombinations() {

		List<List<Boolean>> retVal = new ArrayList<>();
		for (int i = 0; i < getNumCombinations(); i++) {
			retVal.add(getCombination(i));
		}
		return retVal.stream().filter(x -> x.contains(Boolean.TRUE)).collect(Collectors.toList());

	}

	public static void main(String[] args) {
		Combinator c = new Combinator(3);
		for (int i = 0; i < c.getNumCombinations(); i++) {
			System.out.println(i + " -> " + c.getCombination(i));
		}
	}

};