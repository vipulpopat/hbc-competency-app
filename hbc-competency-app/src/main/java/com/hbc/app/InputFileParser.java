package com.hbc.app;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hbc.app.domain.Color;
import com.hbc.app.domain.CustomerPreference;
import com.hbc.app.domain.CustomerPreference.PaintPreference;
import com.hbc.app.exception.InputNotExpectedException;

public class InputFileParser {

	private static final Logger logger = LoggerFactory.getLogger(InputFileParser.class);

	private final Path file;

	public InputFileParser(String fileName) {
		Path inputFile = Paths.get(fileName);
		this.file = inputFile;
	}
	
	public InputFileParser(Path inputFile) {
		this.file = inputFile;
	}

	public ColorProcessor parseFile() throws InputNotExpectedException {
		int numberOfColors = 0;
		List<CustomerPreference> customerPreferences = new ArrayList<CustomerPreference>();
		try {
			numberOfColors = Integer.parseInt(Files.lines(this.file).findFirst().get());
			logger.debug("Number of colors : " + numberOfColors);

			Files.lines(this.file).skip(1).forEach((s) -> {

				String[] stringArray = s.split("\\s");
				CustomerPreference cp = new CustomerPreference(s.hashCode());
				for (int i = 0; i < stringArray.length; i += 2) {
					cp.addPaintPreference(Color.valueOf(stringArray[i + 1]), Integer.parseInt(stringArray[i]));
				}
				customerPreferences.add(cp);
			});

		} catch (Exception e1) {
			throw new InputNotExpectedException("Caught IOException", e1);
		}
		validateInputs(numberOfColors, customerPreferences);
		return new ColorProcessor(numberOfColors, customerPreferences);
	}

	private void validateInputs(int numberOfColors, List<CustomerPreference> customerPreferences)
			throws InputNotExpectedException {
		List<Set<PaintPreference>> preferences = customerPreferences.stream()
				.map(CustomerPreference::getPaintPreferences).collect(Collectors.toList());
		List<PaintPreference> prefs = preferences.stream().flatMap(x -> x.stream()).collect(Collectors.toList());
		Set<Integer> totalColorsFromPrefs = prefs.stream().map(x -> x.getColorNumber()).collect(Collectors.toSet());
		if (numberOfColors != totalColorsFromPrefs.size())
			throw new InputNotExpectedException("No of colors dont match the distinct colors from preferences");
	}
}