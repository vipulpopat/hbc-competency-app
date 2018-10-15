package com.hbc.app;

import static org.junit.Assert.assertEquals;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

import org.junit.Test;

import com.hbc.app.exception.CantProcessPreferencesException;
import com.hbc.app.exception.InputNotExpectedException;

public class ColorApplicationTest {

	@Test
	public void testcase1_success()
			throws InputNotExpectedException, CantProcessPreferencesException, URISyntaxException {
		testcase_success("/real-data/test_data_1.txt", "G G G G M");
	}

	@Test
	public void testcase2_success()
			throws InputNotExpectedException, CantProcessPreferencesException, URISyntaxException {
		testcase_success("/real-data/test_data_2.txt", ColorProcessor.NO_SOLUTION_EXISTS);
	}

	@Test
	public void testcase3_success()
			throws InputNotExpectedException, CantProcessPreferencesException, URISyntaxException {
		testcase_success("/real-data/test_data_3.txt", "G M G M G");
	}

	@Test
	public void testcase4_success()
			throws InputNotExpectedException, CantProcessPreferencesException, URISyntaxException {
		testcase_success("/real-data/test_data_4.txt", "M M");
	}

	private void testcase_success(String fileName, String result)
			throws InputNotExpectedException, CantProcessPreferencesException, URISyntaxException {
		URL url = this.getClass().getResource(fileName);
		InputFileParser parser = new InputFileParser(Paths.get(url.toURI()));
		ColorProcessor processor = parser.parseFile();
		assertEquals(processor.getBatchCombination().get(), result);
	}

}