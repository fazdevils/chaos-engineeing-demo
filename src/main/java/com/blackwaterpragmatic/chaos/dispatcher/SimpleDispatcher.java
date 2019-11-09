package com.blackwaterpragmatic.chaos.dispatcher;

import com.blackwaterpragmatic.chaos.bean.Result;
import com.blackwaterpragmatic.chaos.constant.RepositoryType;
import com.blackwaterpragmatic.chaos.mybatis.mapper.SimpleResultMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class SimpleDispatcher {
	private final SimpleResultMapper resultMapper;

	@Autowired
	public SimpleDispatcher(final SimpleResultMapper resultMapper) {
		this.resultMapper = resultMapper;
	}

	public List<Result> listResults(
			final RepositoryType repositoryType) {
		switch (repositoryType) {
			case DATABASE:
				return resultMapper.list();
			case CACHE:
				return resultMapper.list(); // no cache in this demo - simulated with db.
			default:
				throw new UnsupportedOperationException(String.format("Unsupposted RepositoryType: %s", repositoryType));
		}
	}

}
