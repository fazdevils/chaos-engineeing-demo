package com.blackwaterpragmatic.chaos.test;

import static org.mockito.Mockito.framework;

import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.mockito.listeners.MockCreationListener;

public abstract class MockitoTest {
	private static MockCreationListener mockListener;
	protected static final List<Object> mocks = new ArrayList<>();

	@BeforeClass
	public static void beforeClass() {
		mockListener = (mock, settings) -> mocks.add(mock);
		framework().addListener(mockListener);
	}

	@AfterClass
	public static void afterClass() {
		mocks.clear();
		framework().removeListener(mockListener);
	}

}
