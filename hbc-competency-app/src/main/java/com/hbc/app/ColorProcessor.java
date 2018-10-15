package com.hbc.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.paukov.combinatorics3.Generator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hbc.app.domain.Batch;
import com.hbc.app.domain.Color;
import com.hbc.app.domain.CustomerPreference;
import com.hbc.app.exception.CantProcessPreferencesException;

public class ColorProcessor {

	private static final Logger logger = LoggerFactory.getLogger(ColorProcessor.class);

	private final List<CustomerPreference> preferences = new ArrayList<CustomerPreference>();

	public final static String NO_SOLUTION_EXISTS = "No solution exists";

	private final int numberOfColors;

	public ColorProcessor(int numberOfColors, List<CustomerPreference> preferences) {
		this.numberOfColors = numberOfColors;
		this.preferences.addAll(preferences);
		Collections.sort(this.preferences);
	}

	public Optional<String> getBatchCombination() throws CantProcessPreferencesException {

		List<Batch> combinations = getBatchCombinations(numberOfColors);
		Collections.sort(combinations);
		logger.debug("Combinations = " + combinations);

		Batch selectedBatch = null;
		for (Batch trialBatch : combinations) {
			if (preferences.stream().allMatch(x -> x.alignsWithBatch(trialBatch))) {
				selectedBatch = trialBatch;
				break;
			}
		}

		logger.debug("Selected batch = " + selectedBatch);
		return Optional.of(selectedBatch == null ? NO_SOLUTION_EXISTS : selectedBatch.getOuputBatchString());
	}

	private List<Batch> getBatchCombinations(int colors) {

		List<List<Color>> batchesLists = Generator.permutation(Color.M, Color.G).withRepetitions(colors).stream()
				.collect(Collectors.toList());

		return batchesLists.stream().map(x -> new Batch(x)).collect(Collectors.toList());

	}

	@Override
	public String toString() {
		return "ColorProcessor [preferences=" + preferences + ", numberOfColors=" + numberOfColors + "]";
	}

}
