package com.hbc.app.domain;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class Batch implements Comparable<Batch> {

	private Map<Integer, Color> details = null;

	public Batch(Map<Integer, Color> details) {
		super();
		this.details = details;
	}

	public Batch(List<Color> details) {
		super();
		this.details = IntStream.range(0, details.size()).boxed()
				.collect(Collectors.toMap(i -> i+1, i -> details.get(i)));
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
	public int compareTo(Batch o) {
		return new Integer(this.numberOfMattes()).compareTo(new Integer(o.numberOfMattes()));
	}

	public boolean isEqualPreference(CustomerPreference cp) {
		return cp.getPaintPreferences().stream().anyMatch(x -> details.get(x.getColorNumber()).equals(x.getColor()));
	}

	public String getOuputBatchString() {
		return details.values().stream().map(Object::toString).collect(Collectors.joining(" "));
	}

}