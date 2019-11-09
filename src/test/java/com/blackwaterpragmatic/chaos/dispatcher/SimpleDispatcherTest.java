package com.blackwaterpragmatic.chaos.dispatcher;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.blackwaterpragmatic.chaos.constant.RepositoryType;
import com.blackwaterpragmatic.chaos.mybatis.mapper.SimpleResultMapper;
import com.blackwaterpragmatic.chaos.test.MockitoTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SimpleDispatcherTest extends MockitoTest {

	@Mock
	private SimpleResultMapper resultsMapper;

	@InjectMocks
	private SimpleDispatcher resultsDispatcher;

	@Test
	public void testListResultsFromDatabase() {
		resultsDispatcher.listResults(RepositoryType.DATABASE);

		verify(resultsMapper).list();
		verifyNoMoreInteractions(mocks.toArray());
	}

	@Test
	public void testListResultsFromCache() {
		resultsDispatcher.listResults(RepositoryType.CACHE);

		verify(resultsMapper).list();
		verifyNoMoreInteractions(mocks.toArray());
	}
}
