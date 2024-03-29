package com.blackwaterpragmatic.chaos.controller;

import com.blackwaterpragmatic.chaos.bean.Result;
import com.blackwaterpragmatic.chaos.service.SimpleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SimpleServiceController {
	private final SimpleService simpleService;

	@Autowired
	public SimpleServiceController(final SimpleService simpleService) {
		this.simpleService = simpleService;
	}

	@RequestMapping(value = "/simple_results", method = RequestMethod.GET)
	public List<Result> simpleResponse() throws Exception {
		return simpleService.listResults();
	}
}
