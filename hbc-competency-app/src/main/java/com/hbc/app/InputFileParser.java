package com.hbc.app;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InputFileParser {

	private static final Logger logger = LoggerFactory.getLogger(InputFileParser.class);

	private final Path file;

	public InputFileParser(String fileName) throws FileNotFoundException {
		Path inputFile = Paths.get(fileName);
		this.file = inputFile;
	}

	public ColorProcessor parseFile() {
		// read file into stream, try-with-resources

		int numberOfColors = 0;
		List<CustomerPreference> customerPreferences = new ArrayList<CustomerPreference>();
		try {
			numberOfColors = Integer.parseInt(Files.lines(this.file).findFirst().get());
			logger.debug("Number of colors : " + numberOfColors);

			try (Stream<String> stream = Files.lines(this.file)) {
				stream.skip(1).forEach((s) -> {

					String[] stringArray = s.split("\\s");
					CustomerPreference cp = new CustomerPreference(s.hashCode());
					for (int i = 0; i < stringArray.length; i += 2) {
						cp.addPaintPreference(Color.valueOf(stringArray[i+1]), Integer.parseInt(stringArray[i]));
					}
					customerPreferences.add(cp);
				});

			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return new ColorProcessor(numberOfColors, customerPreferences);
	}
}