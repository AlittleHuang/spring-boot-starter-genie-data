package io.github.genie.data.repository;

import io.github.genie.sql.api.Query;
import io.github.genie.sql.api.Update;
import io.github.genie.sql.builder.meta.Metamodel;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.io.Serializable;

@Configuration
public class BaseDataAccessObjectsConfiguration {

    @Bean
    protected DataAccessor genieDataBeans(Query query, Update update) {
        return new DataAccessorImpl(query, update);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    protected <T> DataAccess<T> genieDataAccess(DataAccessor dataAccessor,
                                                DependencyDescriptor descriptor,
                                                Metamodel metamodel) {
        return new DataAccessImpl<>(dataAccessor, descriptor, metamodel);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    protected <T extends Persistable<ID>, ID extends Serializable> Repository<T, ID>
    genieDataRepository(DataAccessor dataAccessor,
                        DependencyDescriptor descriptor) {
        return new RepositoryImpl<>(dataAccessor, descriptor);
    }

}
