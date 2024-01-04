package io.github.genie.data.jpa;

import io.github.genie.data.repository.*;
import io.github.genie.sql.api.Query;
import io.github.genie.sql.api.Update;
import io.github.genie.sql.builder.AbstractQueryExecutor;
import io.github.genie.sql.builder.QueryStructurePostProcessor;
import io.github.genie.sql.builder.meta.Metamodel;
import io.github.genie.sql.core.mapping.JpaMetamodel;
import io.github.genie.sql.executor.jdbc.JdbcQueryExecutor.QuerySqlBuilder;
import io.github.genie.sql.executor.jdbc.MySqlQuerySqlBuilder;
import io.github.genie.sql.executor.jpa.JpaQueryExecutor;
import io.github.genie.sql.executor.jpa.JpaUpdate;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.io.Serializable;

@Configuration
public class GenieDataJpaConfig {

    @Bean
    @ConditionalOnMissingBean
    Metamodel genieMetamodel() {
        return new JpaMetamodel();
    }

    @Bean
    @ConditionalOnMissingBean
    QuerySqlBuilder querySqlBuilder() {
        return new MySqlQuerySqlBuilder();
    }

    @Bean
    @ConditionalOnMissingBean
    JpaQueryExecutor queryExecutor(EntityManager entityManager, Metamodel metamodel, QuerySqlBuilder querySqlBuilder) {
        return new JpaQueryExecutor(entityManager, metamodel, querySqlBuilder);
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

    @Bean
    @ConditionalOnMissingBean
    Update genieUpdate(EntityManager entityManager, JpaQueryExecutor jpaQueryExecutor) {
        return new JpaUpdate(entityManager, jpaQueryExecutor);
    }
}
