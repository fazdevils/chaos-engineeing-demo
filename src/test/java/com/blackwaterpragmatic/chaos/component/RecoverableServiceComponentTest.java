package com.blackwaterpragmatic.chaos.component;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.internal.stubbing.answers.AnswersWithDelay;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.junit.MockitoJUnitRunner;

import com.blackwaterpragmatic.chaos.component.RecoverableServiceComponent;
import com.blackwaterpragmatic.chaos.test.MockitoTest;

@RunWith(MockitoJUnitRunner.class)
public class RecoverableServiceComponentTest extends MockitoTest {
	@Mock
	private MockService mockService;

	private final RecoverableServiceComponent serviceComponent = new RecoverableServiceComponent();

	@Test
	public void testSuccessfulServiceExecution() {
		final Supplier<String> mockSupplier = () -> mockService.execute();
		final Function<Exception, String> exceptionFunction = exception -> "exception recovery";
		final Function<Exception, String> timeoutFunction = exception -> "timeout recovery";

		final String expectedResult = "success";
		when(mockService.execute()).thenReturn(expectedResult);

		final String result = serviceComponent.execute(mockSupplier, exceptionFunction, timeoutFunction, 1000L);

		verify(mockService).execute();
		verifyNoMoreInteractions(mocks.toArray());

		assertEquals(expectedResult, result);
	}

	@Test
	public void testUnsuccessfulServiceExecution() {
		final Supplier<String> mockSupplier = () -> mockService.execute();
		final Function<Exception, String> exceptionFunction = exception -> "exception recovery";
		final Function<Exception, String> timeoutFunction = exception -> "timeout recovery";

		when(mockService.execute()).thenThrow(RuntimeException.class);

		final String result = serviceComponent.execute(mockSupplier, exceptionFunction, timeoutFunction, 1000L);

		verify(mockService).execute();
		verifyNoMoreInteractions(mocks.toArray());

		assertEquals("exception recovery", result);
	}

	@Test
	public void testServiceExecutionTimeout() {
		final Supplier<String> mockSupplier = () -> mockService.execute();
		final Function<Exception, String> exceptionFunction = exception -> "exception recovery";
		final Function<Exception, String> timeoutFunction = exception -> "timeout recovery";

		when(mockService.execute()).thenAnswer(new AnswersWithDelay(5L, new Returns("Oops... unexpected delay")));

		final String result = serviceComponent.execute(mockSupplier, exceptionFunction, timeoutFunction, 1L);

		verify(mockService).execute();
		verifyNoMoreInteractions(mocks.toArray());

		assertEquals("timeout recovery", result);
	}

	private interface MockService {
		String execute();
	}
}
