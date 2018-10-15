package com.hbc.app;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

import org.junit.Test;

import com.hbc.app.exception.InputNotExpectedException;

public class InputFileParserTest {

	@Test(expected = InputNotExpectedException.class)
	public void incorrect_input_data_structure() throws InputNotExpectedException, URISyntaxException {
		URL url = this.getClass().getResource("/test-data/incorrect_file_structure.txt");
		InputFileParser parser = new InputFileParser(Paths.get(url.toURI()));
		parser.parseFile();

	}

	@Test(expected = NullPointerException.class)
	public void input_file_not_present() throws InputNotExpectedException, URISyntaxException {
		URL url = this.getClass().getResource("/test-data/file_absent.txt");
		InputFileParser parser = new InputFileParser(Paths.get(url.toURI()));
		parser.parseFile();

	}

	@Test(expected = InputNotExpectedException.class)
	public void input_color_count_not_correct() throws InputNotExpectedException, URISyntaxException {
		URL url = this.getClass().getResource("/test-data/incorrect_color_numbers.txt");
		InputFileParser parser = new InputFileParser(Paths.get(url.toURI()));
		parser.parseFile();
	}
}