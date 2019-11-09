package com.blackwaterpragmatic.chaos.component;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.github.resilience4j.core.SupplierUtils;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.vavr.control.Try;

@Component
public class RecoverableServiceComponent {
	private static final Logger logger = LoggerFactory.getLogger(RecoverableServiceComponent.class);

	public <T> T execute(final Supplier<T> serviceSupplier,
			final Function<Exception, T> exceptionFunction,
			final Function<Exception, T> timeoutFunction,
			final long timoutMs) {

		// recover from service exception
		final Supplier<T> recoverableSupplier = SupplierUtils.recover(serviceSupplier, exceptionFunction);

		// configure TimeLimiter to return if the service call exceeds timeout
		final TimeLimiter timeLimiter = TimeLimiter.of(Duration.ofMillis(timoutMs));

		// log time out event (optional)
		timeLimiter.getEventPublisher().onTimeout(event -> logger.error("Service timeout: " + event));

		// set up an executor service to make an asynchronous service call
		final ExecutorService executor = Executors.newFixedThreadPool(1);
		try {
			final ServiceRequest<T> serviceRequest = new ServiceRequest<>(recoverableSupplier);
			final Callable<T> timedRequest = timeLimiter.decorateFutureSupplier(() -> executor.submit(serviceRequest));
			return Try.ofCallable(timedRequest).recover(TimeoutException.class, timeoutFunction).get();
		} finally {
			executor.shutdownNow();
		}
	}

	private class ServiceRequest<T> implements Callable<T> {
		private final Supplier<T> supplier;

		public ServiceRequest(final Supplier<T> supplier) {
			this.supplier = Objects.requireNonNull(supplier);
		}

		@Override
		public T call() throws Exception {
			return supplier.get();
		}
	}
}
