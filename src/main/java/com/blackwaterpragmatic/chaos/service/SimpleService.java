package com.blackwaterpragmatic.chaos.service;

import com.blackwaterpragmatic.chaos.bean.Result;
import com.blackwaterpragmatic.chaos.component.RecoverableServiceComponent;
import com.blackwaterpragmatic.chaos.constant.RepositoryType;
import com.blackwaterpragmatic.chaos.dispatcher.SimpleDispatcher;
import com.blackwaterpragmatic.chaos.exception.ServerException;
import com.blackwaterpragmatic.chaos.exception.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class SimpleService {
	private static final Logger logger = LoggerFactory.getLogger(SimpleService.class);

	private final SimpleDispatcher simpleDispatcher;
	private final RecoverableServiceComponent recoverableServiceComponent;

	@Autowired
	public SimpleService(
			final SimpleDispatcher simpleDispatcher,
			final RecoverableServiceComponent recoverableServiceComponent) {
		this.simpleDispatcher = simpleDispatcher;
		this.recoverableServiceComponent = recoverableServiceComponent;
	}

	@CircuitBreaker(name = "database", fallbackMethod = "listResultDatabaseFallback")
	public List<Result> listResults() {
		final Supplier<List<Result>> resultSupplier = () -> simpleDispatcher.listResults(
				RepositoryType.CACHE);
		final Function<Exception, List<Result>> resultExceptionFunction = exception -> {
			throw new ServerException();
		};
		final Function<Exception, List<Result>> resultTimeoutFunction = exception -> {
			throw new TimeoutException();
		};
		return recoverableServiceComponent.execute(resultSupplier, resultExceptionFunction, resultTimeoutFunction, 1000L);
	}

	@SuppressWarnings("unused")
	private List<Result> listResultDatabaseFallback(
			final Exception e) {
		logger.info("In listResultDatabaseFallback...");
		final Supplier<List<Result>> resultSupplier = () -> simpleDispatcher.listResults(
				RepositoryType.DATABASE);
		final Function<Exception, List<Result>> resultExceptionFunction = exception -> {
			throw new ServerException();
		};
		final Function<Exception, List<Result>> resultTimeoutFunction = exception -> {
			throw new TimeoutException();
		};
		return recoverableServiceComponent.execute(resultSupplier, resultExceptionFunction, resultTimeoutFunction, 1000L);
	}
}
