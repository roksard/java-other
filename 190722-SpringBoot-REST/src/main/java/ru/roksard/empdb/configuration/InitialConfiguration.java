package ru.roksard.empdb.configuration;

import javax.sql.DataSource;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

@Configuration
public class InitialConfiguration {
	@Autowired
	private DataSource dataSource;
	 
	@Bean
	public DataSourceConnectionProvider connectionProvider() {
	    return new DataSourceConnectionProvider
	      (new TransactionAwareDataSourceProxy(dataSource));
	}
	
	@Bean
	public DSLContext dsl() {
	    return new DefaultDSLContext(configuration());
	}
	
	@Bean
	public ExceptionTranslator exceptionTransformer() {
	    return new ExceptionTranslator();
	}
	
	public DefaultConfiguration configuration() {
	    DefaultConfiguration jooqConfiguration = new DefaultConfiguration();
	    jooqConfiguration.set(SQLDialect.POSTGRES_10);
	    /*jooqConfiguration
	    	.setSettings(jooqConfiguration.settings()
	    			.withStatementType(StatementType.STATIC_STATEMENT)); */
	    jooqConfiguration.set(connectionProvider());
	    jooqConfiguration
	      .set(new DefaultExecuteListenerProvider(exceptionTransformer()));
	 
	    return jooqConfiguration;
	}
}
