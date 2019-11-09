package com.blackwaterpragmatic.chaos.controller;

import com.blackwaterpragmatic.chaos.bean.OrchestrationResult;
import com.blackwaterpragmatic.chaos.bean.Result;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RestController
public class OrchestrationController {

	@RequestMapping(value = "/orchestration_results", method = RequestMethod.GET)
	public OrchestrationResult orchestratedResponse() throws InterruptedException, ExecutionException {
		final List<Result> simpleResponses = new ArrayList<>();
		final ExecutorService executor = Executors.newFixedThreadPool(3);
		try {
			final Future<List<Result>> restRequest1 = executor.submit(new RestRequest());
			final Future<List<Result>> restRequest2 = executor.submit(new RestRequest());
			final Future<List<Result>> restRequest3 = executor.submit(new RestRequest());
			final long start = System.currentTimeMillis();
			simpleResponses.addAll(restRequest1.get());
			simpleResponses.addAll(restRequest2.get());
			simpleResponses.addAll(restRequest3.get());
			return OrchestrationResult.builder()
					.results(simpleResponses)
					.requestTime(System.currentTimeMillis() - start)
					.build();
		} finally {
			executor.shutdownNow();
		}
	}

	private static class RestRequest implements Callable<List<Result>> {
		@Override
		public List<Result> call() throws Exception {
			final RestTemplate restTemplate = new RestTemplate(); // TODO try WebClient
			// https://docs.spring.io/spring/docs/5.1.10.RELEASE/spring-framework-reference/web-reactive.html#webflux-client
			return restTemplate.getForObject("http://localhost:8080/simple_results", List.class);
		}
	}
}
