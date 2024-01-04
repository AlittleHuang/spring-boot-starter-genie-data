package io.github.genie.data.jdbc;

import io.github.genie.data.repository.*;
import io.github.genie.sql.api.Query;
import io.github.genie.sql.api.Update;
import io.github.genie.sql.builder.AbstractQueryExecutor;
import io.github.genie.sql.builder.QueryStructurePostProcessor;
import io.github.genie.sql.builder.meta.Metamodel;
import io.github.genie.sql.core.mapping.JpaMetamodel;
import io.github.genie.sql.executor.jdbc.*;
import io.github.genie.sql.executor.jdbc.JdbcQueryExecutor.QuerySqlBuilder;
import io.github.genie.sql.executor.jdbc.JdbcQueryExecutor.ResultCollector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.Serializable;

@Configuration
public class GenieDataJdbcConfig {

    @Bean
    @ConditionalOnMissingBean
    JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    @ConditionalOnMissingBean
    Metamodel genieMetamodel() {
        return new JpaMetamodel();
    }

    @Bean
    @ConditionalOnMissingBean
    AbstractQueryExecutor queryExecutor(Metamodel metamodel,
                                        QuerySqlBuilder querySqlBuilder,
                                        ResultCollector resultCollector,
                                        ConnectionProvider connectionProvider) {
        return new JdbcQueryExecutor(metamodel, querySqlBuilder, connectionProvider, resultCollector);
    }

    @Bean
    @ConditionalOnMissingBean
    ConnectionProvider connectionProvider(JdbcTemplate jdbcTemplate) {
        return new ConnectionProvider() {
            @Override
            public <T> T execute(ConnectionCallback<T> action) {
                return jdbcTemplate.execute(action::doInConnection);
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean
    ResultCollector resultCollector() {
        return new JdbcResultCollector();
    }

    @Bean
    @ConditionalOnMissingBean
    QuerySqlBuilder querySqlBuilder() {
        return new MySqlQuerySqlBuilder();
    }

    @Bean
    @ConditionalOnMissingBean
    Query genieQuery(AbstractQueryExecutor executor,
                     @Autowired(required = false) QueryStructurePostProcessor structurePostProcessor) {
        return structurePostProcessor != null
                ? executor.createQuery(structurePostProcessor)
                : executor.createQuery();
    }

    @Bean
    @ConditionalOnMissingBean
    Update genieUpdate(JdbcUpdateSqlBuilder sqlBuilder,
                       ConnectionProvider connectionProvider,
                       Metamodel metamodel) {
        return new JdbcUpdate(sqlBuilder, connectionProvider, metamodel);
    }

    @Bean
    @ConditionalOnMissingBean
    JdbcUpdateSqlBuilder jdbcUpdateSqlBuilder() {
        return new MysqlUpdateSqlBuilder();
    }

    @Bean
    GenieDataBeans genieDataBeans(Query query, Update update) {
        return new GenieDataBeans(query, update);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    <T> DataAccess<T> dataAccess(GenieDataBeans genieDataBeans,
                                 @Autowired(required = false) DependencyDescriptor descriptor) {
        return new DataAccessImpl<>(genieDataBeans, descriptor);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    <T extends Persistable<ID>, ID extends Serializable> Repository<T, ID> dataRepository(DataAccess<T> dataAccess) {
        return new RepositoryImpl<>(dataAccess);
    }

}
