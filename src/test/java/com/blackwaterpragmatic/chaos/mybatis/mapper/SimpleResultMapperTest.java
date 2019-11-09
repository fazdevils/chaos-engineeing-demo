package com.blackwaterpragmatic.chaos.mybatis.mapper;

import static org.junit.Assert.assertEquals;

import com.blackwaterpragmatic.chaos.bean.Result;
import com.blackwaterpragmatic.chaos.config.DatasourceConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
@ContextConfiguration(
		classes = {DatasourceConfig.class})
public class SimpleResultMapperTest {
	@Autowired
	private SimpleResultMapper simpleResultMapper;

	@Test
	public void testList() {
		final List<Result> results = simpleResultMapper.list();
		assertEquals(3, results.size());
		assertEquals("my first message", results.get(0).getMessage());
		assertEquals("another message", results.get(1).getMessage());
		assertEquals("final message", results.get(2).getMessage());
	}
}
