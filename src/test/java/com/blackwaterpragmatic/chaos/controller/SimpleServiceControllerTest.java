package com.blackwaterpragmatic.chaos.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blackwaterpragmatic.chaos.bean.Result;
import com.blackwaterpragmatic.chaos.exception.ServerException;
import com.blackwaterpragmatic.chaos.exception.TimeoutException;
import com.blackwaterpragmatic.chaos.service.SimpleService;
import com.blackwaterpragmatic.chaos.test.MockitoTest;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class SimpleServiceControllerTest extends MockitoTest {
	@Mock
	private SimpleService simpleService;

	@InjectMocks
	private SimpleServiceController simpleServiceController;

	private MockMvc mockMvc;

	private final ObjectMapper jsonMapper = new ObjectMapper();

	@Before
	public void init() {
		mockMvc = MockMvcBuilders
				.standaloneSetup(simpleServiceController)
				.build();
	}

	@Test
	public void testSearchResultsWithoutParams() throws Exception {
		final List<Result> expectedResult = new ArrayList<>();
		final String expectedJson = jsonMapper.writeValueAsString(expectedResult);

		when(simpleService.listResults()).thenReturn(expectedResult);

		mockMvc.perform(MockMvcRequestBuilders
				.get("/simple_results")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().json(expectedJson));

		verify(simpleService).listResults();
		verifyNoMoreInteractions(mocks.toArray());
	}

	@Test
	public void testSearchResultsWithServiceTimeout() throws Exception {
		when(simpleService.listResults()).thenThrow(new TimeoutException());

		mockMvc.perform(MockMvcRequestBuilders
				.get("/simple_results")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isServiceUnavailable());

		verify(simpleService).listResults();
		verifyNoMoreInteractions(mocks.toArray());
	}

	@Test
	public void testSearchResultsWithServiceException() throws Exception {
		when(simpleService.listResults()).thenThrow(new ServerException());

		mockMvc.perform(MockMvcRequestBuilders
				.get("/simple_results")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError());

		verify(simpleService).listResults();
		verifyNoMoreInteractions(mocks.toArray());
	}

}
