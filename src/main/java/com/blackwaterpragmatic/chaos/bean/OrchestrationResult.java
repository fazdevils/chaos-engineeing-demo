package com.blackwaterpragmatic.chaos.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@JsonInclude(Include.NON_NULL)
public class OrchestrationResult {

	private final List<Result> results;
	private final long requestTime;

}
