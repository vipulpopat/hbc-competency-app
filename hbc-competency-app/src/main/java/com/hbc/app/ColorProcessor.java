package com.hbc.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hbc.app.CustomerPreference.PaintPreference;

public class ColorProcessor {

	private static final Logger logger = LoggerFactory.getLogger(ColorProcessor.class);

	private final List<CustomerPreference> preferences = new ArrayList<CustomerPreference>();

	private final int numberOfColors;

	public ColorProcessor(int numberOfColors, List<CustomerPreference> preferences) {
		this.numberOfColors = numberOfColors;
		this.preferences.addAll(preferences);
		Collections.sort(this.preferences);
	}

	public Optional<Color[]> getBatchCombination() throws CantProcessPreferencesException {

		Map<Integer, Color> batchMap = new HashMap<Integer, Color>(numberOfColors);

		// populate the default color types
		preferences.stream().forEach(p -> {
			logger.debug(p.toString());
			p.getPaintPreferences().stream().forEach(pp -> {
				if (batchMap.containsKey(pp.getColorNumber())) {
					if (pp.getColor().equals(Color.M))
						batchMap.put(pp.getColorNumber(), Color.M);

				} else
					batchMap.put(pp.getColorNumber(), pp.getColor());
			});
		});

		Batch baseBatch = new Batch(batchMap);
		logger.debug("Batch = " + baseBatch);
		final int mattes = (int) batchMap.values().stream().filter(x -> x.equals(Color.M)).count();

		List<Batch> combinations = getBatchCombinations(mattes, baseBatch);
		Collections.sort(combinations);
		logger.debug("Combinations = " + combinations);

		// get single color preferences customers
		List<Batch> finalCombinations = new ArrayList<>();
		List<CustomerPreference> singlePrefs = preferences.stream().filter(x -> x.getPaintPreferences().size() == 1)
				.collect(Collectors.toList());
		// filter batch combinations which dont meed this criteria
		for (CustomerPreference spref : singlePrefs) {
			for (Batch batch : combinations) {
				if (batch.isEqualPreference(spref))
					finalCombinations.add(batch);
			}
		}
		
		logger.debug("Final Combinations = " + finalCombinations);
		Batch selectedBatch = null;
		for (Batch trialBatch : finalCombinations) {
			for (CustomerPreference cp : preferences) {
				logger.debug(cp + "----" + trialBatch + "---> " + cp.alignsWithBatch(trialBatch));
			}
			if (preferences.stream().allMatch(x -> x.alignsWithBatch(trialBatch))) {
				selectedBatch = trialBatch;
				break;
			}
		}

		logger.debug("Selected batch = " + selectedBatch);

		return Optional.empty();
	}

	private List<Batch> getBatchCombinations(int mattes, Batch baseBatch) {

		Combinator c = new Combinator(mattes);
		final int numberOfCombinations = c.getNumCombinations();
		logger.debug("No of matte combinations = " + numberOfCombinations);

		List<Batch> retVal = new ArrayList<>(numberOfCombinations);
		try {
			for (List<Boolean> bcombination : c.getAllNonZeroCombinations()) {
				logger.debug("Combination : " + bcombination);
				Batch batchCombination = (Batch) baseBatch.clone();
				logger.debug("Cloned Batch=" + batchCombination);
				batchCombination.applyCombination(bcombination);
				logger.debug("Output Batch=" + batchCombination);
				retVal.add(batchCombination);
			}
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retVal;
	}

	public static final class Batch implements Cloneable, Comparable<Batch> {

		private Map<Integer, Color> details = null;

		public Batch(Map<Integer, Color> details) {
			super();
			this.details = details;
		}

		public void applyCombination(List<Boolean> combination) {
			int combIndex = 0;
			for (Map.Entry<Integer, Color> entry : details.entrySet()) {
				if (entry.getValue().equals(Color.M)) {
					Boolean combinationElement = combination.get(combIndex);
					if (!combinationElement)
						entry.setValue(Color.G);
					combIndex++;
				}
			}
		}

		public int numberOfMattes() {
			return (int) details.values().stream().filter(x -> x.equals(Color.M)).count();
		}

		public Color getColor(int colorNumber) {
			return details.get(colorNumber);
		}

		public int getColorCount(Color color) {
			return (int) details.values().stream().filter(x -> x.equals(color)).count();
		}

		@Override
		public String toString() {
			return "Batch [details=" + details + "]";
		}

		@Override
		protected Object clone() throws CloneNotSupportedException {
			return new Batch(MapUtils.clone(details));
		}

		@Override
		public int compareTo(Batch o) {
			return new Integer(this.numberOfMattes()).compareTo(new Integer(o.numberOfMattes()));
		}

		public boolean isEqualPreference(CustomerPreference cp) {
			for (PaintPreference pp : cp.getPaintPreferences()) {
				if (!details.get(pp.getColorNumber()).equals(pp.getColor())) {
					return false;
				}
			}
			return true;
		}

	}

	@Override
	public String toString() {
		return "ColorProcessor [preferences=" + preferences + ", numberOfColors=" + numberOfColors + "]";
	}

}
