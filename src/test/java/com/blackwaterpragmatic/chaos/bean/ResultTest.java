package com.blackwaterpragmatic.chaos.bean;

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.junit.Test;

public class ResultTest {

	private final ObjectMapper jsonMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

	@Test
	public void testSerialization() throws JsonProcessingException {
		final String expectedResult =
				"{" + System.lineSeparator() +
						"  \"id\" : 2319," + System.lineSeparator() +
						"  \"message\" : \"white sock\"" + System.lineSeparator() +
						"}";

		final Result result = Result.builder()
				.id(2319L)
				.message("white sock")
				.build();

		final String json = jsonMapper.writeValueAsString(result);

		assertEquals(expectedResult, json);
	}
}
