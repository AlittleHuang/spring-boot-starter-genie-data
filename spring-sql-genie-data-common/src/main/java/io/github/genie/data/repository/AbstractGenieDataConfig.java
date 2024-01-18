package io.github.genie.data.repository;

import io.github.genie.sql.api.Query;
import io.github.genie.sql.api.Update;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import java.io.Serializable;

public abstract class AbstractGenieDataConfig {

    @Bean
    protected DataAccessor genieDataBeans(Query query, Update update) {
        return new DataAccessorImpl(query, update);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    protected <T> DataAccess<T> genieDataAccess(DataAccessor accessor,
                                                DependencyDescriptor descriptor) {
        return new DataAccessImpl<>(accessor, descriptor);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    protected <T extends Persistable<ID>, ID extends Serializable> Repository<T, ID>
    genieDataRepository(DataAccessor genieDataBeans,
                        DependencyDescriptor descriptor) {
        return new RepositoryImpl<>(genieDataBeans, descriptor);
    }

}
