package com.hbc.app;

import java.util.HashSet;
import java.util.Set;

import com.hbc.app.ColorProcessor.Batch;

public class CustomerPreference implements Comparable<CustomerPreference> {

	private final int customerId;

	private final Set<PaintPreference> paintPreferences = new HashSet<PaintPreference>();

	public CustomerPreference(int customerId) {
		this.customerId = customerId;

	}

	public void addPaintPreference(Color color, int colorNumber) {
		paintPreferences.add(new PaintPreference(color, colorNumber));
	}

	public int getCustomerId() {
		return customerId;
	}

	public Integer numOfPaintPreferences() {
		return paintPreferences.size();
	}

	public Set<PaintPreference> getPaintPreferences() {
		return paintPreferences;
	}

	@Override
	public String toString() {
		return "CustomerPreference [customerId=" + customerId + ", paintPreferences=" + paintPreferences + "]";
	}

	public boolean alignsWithBatch(Batch batch) {

		// case for only 1 preference
		if (paintPreferences.size() == 1) {
			PaintPreference pp = paintPreferences.iterator().next();
			if (batch.getColor(pp.getColorNumber()) == pp.getColor())
				return true;
		} else if (paintPreferences.size() == 2) {
			for (PaintPreference pp : paintPreferences) {
				if (pp.getColor() != batch.getColor(pp.getColorNumber()))
					return false;
			}
		}
		else {
			if (paintPreferences.stream().filter(x -> x.getColor().equals(Color.M)).count() <= batch
					.getColorCount(Color.M)) {
				return true;
			}
		}

		return false;
	}

	public static final class PaintPreference {
		private final Color color;

		private final int colorNumber;

		public PaintPreference(Color color, int colorNumber) {
			super();
			this.color = color;
			this.colorNumber = colorNumber;
		}

		public Color getColor() {
			return color;
		}

		public int getColorNumber() {
			return colorNumber;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((color == null) ? 0 : color.hashCode());
			result = prime * result + colorNumber;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			PaintPreference other = (PaintPreference) obj;
			if (color != other.color)
				return false;
			if (colorNumber != other.colorNumber)
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "PaintPreference [color=" + color + ", colorNumber=" + colorNumber + "]";
		}

	}

	@Override
	public int compareTo(CustomerPreference o) {
		return new Integer(this.paintPreferences.size()).compareTo(o.getPaintPreferences().size());
	}

}
