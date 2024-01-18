package io.github.genie.data.jpa;

import io.github.genie.data.repository.AbstractGenieDataConfig;
import io.github.genie.data.repository.DataAccess;
import io.github.genie.data.repository.DataAccessor;
import io.github.genie.data.repository.Persistable;
import io.github.genie.data.repository.Repository;
import io.github.genie.sql.api.Query;
import io.github.genie.sql.api.Update;
import io.github.genie.sql.builder.AbstractQueryExecutor;
import io.github.genie.sql.builder.QueryStructurePostProcessor;
import io.github.genie.sql.builder.meta.Metamodel;
import io.github.genie.sql.executor.jdbc.JdbcQueryExecutor.QuerySqlBuilder;
import io.github.genie.sql.executor.jdbc.MySqlQuerySqlBuilder;
import io.github.genie.sql.executor.jpa.JpaQueryExecutor;
import io.github.genie.sql.executor.jpa.JpaUpdate;
import io.github.genie.sql.meta.JpaMetamodel;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.io.Serializable;

@Configuration
public class GenieDataJpaConfig extends AbstractGenieDataConfig {

    @Bean
    @ConditionalOnMissingBean
    protected Metamodel genieMetamodel() {
        return new JpaMetamodel();
    }

    @Bean
    @ConditionalOnMissingBean
    protected QuerySqlBuilder querySqlBuilder() {
        return new MySqlQuerySqlBuilder();
    }

    @Bean
    @ConditionalOnMissingBean
    protected JpaQueryExecutor queryExecutor(EntityManager entityManager,
                                             Metamodel metamodel,
                                             QuerySqlBuilder querySqlBuilder) {
        return new JpaQueryExecutor(entityManager, metamodel, querySqlBuilder);
    }

    @Bean
    @ConditionalOnMissingBean
    protected Query genieQuery(AbstractQueryExecutor executor,
                               @Autowired(required = false)
                               QueryStructurePostProcessor structurePostProcessor) {
        return structurePostProcessor != null
                ? executor.createQuery(structurePostProcessor)
                : executor.createQuery();
    }

    @Bean
    @ConditionalOnMissingBean
    protected Update genieUpdate(EntityManager entityManager, JpaQueryExecutor jpaQueryExecutor) {
        return new JpaUpdate(entityManager, jpaQueryExecutor);
    }

    @Override
    @Bean
    protected DataAccessor genieDataBeans(Query query, Update update) {
        return super.genieDataBeans(query, update);
    }

    @Override
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    protected <T> DataAccess<T> genieDataAccess(DataAccessor accessor, DependencyDescriptor descriptor) {
        return super.genieDataAccess(accessor, descriptor);
    }

    @Override
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    protected <T extends Persistable<ID>, ID extends Serializable> Repository<T, ID> genieDataRepository(DataAccessor genieDataBeans, DependencyDescriptor descriptor) {
        return super.genieDataRepository(genieDataBeans, descriptor);
    }
}
