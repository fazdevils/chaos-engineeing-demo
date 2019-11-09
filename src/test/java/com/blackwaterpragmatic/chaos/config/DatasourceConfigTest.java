package com.blackwaterpragmatic.chaos.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;

@RunWith(SpringRunner.class)
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
@ContextConfiguration(
		classes = {DatasourceConfig.class},
		initializers = {ConfigFileApplicationContextInitializer.class})
public class DatasourceConfigTest {
	@Autowired
	private DataSource dataSource;

	@Autowired
	private SqlSessionFactory sqlSessionFactory;

	@Autowired
	private DataSourceTransactionManager transactionManager;

	@Test
	public void testDataSourceConfiguration() throws SQLException {
		final Connection connection = dataSource.getConnection();
		assertEquals("HSQL Database Engine Driver", connection.getMetaData().getDriverName());
		assertNotNull(sqlSessionFactory);
		assertNotNull(transactionManager);
	}

}
