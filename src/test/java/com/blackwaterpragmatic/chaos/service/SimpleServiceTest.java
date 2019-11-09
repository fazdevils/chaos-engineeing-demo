package com.blackwaterpragmatic.chaos.service;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.blackwaterpragmatic.chaos.component.RecoverableServiceComponent;
import com.blackwaterpragmatic.chaos.constant.RepositoryType;
import com.blackwaterpragmatic.chaos.dispatcher.SimpleDispatcher;
import com.blackwaterpragmatic.chaos.exception.ServerException;
import com.blackwaterpragmatic.chaos.exception.TimeoutException;
import com.blackwaterpragmatic.chaos.test.MockitoTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.internal.stubbing.answers.AnswersWithDelay;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.junit.MockitoJUnitRunner;

// TODO need to figure out a way to unit test circuit breaker configuration
@RunWith(MockitoJUnitRunner.class)
public class SimpleServiceTest extends MockitoTest {

	@Mock
	private SimpleDispatcher resultsDispatcher;

	private SimpleService resultsService;

	@Before
	public void before() {
		// using a real (non-mocked) service component
		resultsService = new SimpleService(resultsDispatcher, new RecoverableServiceComponent());
	}

	@Test
	public void testListResults() {
		resultsService.listResults();

		verify(resultsDispatcher).listResults(RepositoryType.CACHE);
		verifyNoMoreInteractions(mocks.toArray());
	}

	@Test
	public void testListResultsRecoverFromException() {
		when(resultsDispatcher.listResults(RepositoryType.CACHE)).thenThrow(RuntimeException.class);

		try {
			resultsService.listResults();
			fail();
		} catch (final ServerException e) {
			// expected
		}

		verify(resultsDispatcher).listResults(RepositoryType.CACHE);
		verifyNoMoreInteractions(mocks.toArray());
	}

	@Test
	public void testListResultsRecoverFromTimeout() {
		when(resultsDispatcher.listResults(RepositoryType.CACHE))
				.thenAnswer(new AnswersWithDelay(1500L, new ReturnsEmptyValues()));

		try {
			resultsService.listResults();
			fail();
		} catch (final TimeoutException e) {
			// expected
		}

		verify(resultsDispatcher).listResults(RepositoryType.CACHE);
		verifyNoMoreInteractions(mocks.toArray());
	}

}
