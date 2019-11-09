package com.blackwaterpragmatic.chaos.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
@MapperScan({"com.blackwaterpragmatic.chaos.mybatis.mapper"})
public class DatasourceConfig {

	@Value("${database.driver:HSQL}")
	private String dbDriver;

	@Value("${database.url:localHSQL}")
	private String dbUrl;

	@Value("${database.username:username}")
	private String dbUserName;

	@Value("${database.password:password}")
	private String dbPassword;

	@Value("${poolSize:10}")
	private Integer poolSize;

	@Bean
	public DataSource dataSource() {
		if ("HSQL".equals(dbDriver)) {
			return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.HSQL)
					.addScript("com/blackwaterpragmatic/chaos/hsql/create-db.sql")
					.addScript("com/blackwaterpragmatic/chaos/hsql/insert-data.sql")
					.setSeparator("/")
					.build();
		} else {
			final HikariConfig hikariConfig = new HikariConfig();
			hikariConfig.setJdbcUrl(dbUrl);
			hikariConfig.setUsername(dbUserName);
			hikariConfig.setPassword(dbPassword);
			hikariConfig.setDriverClassName(dbDriver);
			hikariConfig.setMaximumPoolSize(poolSize);

			return new HikariDataSource(hikariConfig);
		}
	}

	@Bean
	public SqlSessionFactory sqlSessionFactory(final DataSource dataSource) throws Exception {
		final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(dataSource);
		sessionFactory.setTypeAliasesPackage("com.blackwaterpragmatic.chaos.bean");
		return sessionFactory.getObject();
	}

	@Bean
	public DataSourceTransactionManager transactionManager(final DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
}
