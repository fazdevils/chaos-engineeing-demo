package com.blackwaterpragmatic.chaos.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@JsonInclude(Include.NON_NULL)
public class Result {

	private final Long id;
	private final String message;

}
