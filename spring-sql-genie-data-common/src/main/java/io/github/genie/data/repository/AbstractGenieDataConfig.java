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
    protected GenieDataBeans genieDataBeans(Query query, Update update) {
        return new GenieDataBeans(query, update);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    protected <T> DataAccess<T> genieDataAccess(GenieDataBeans genieDataBeans,
                                                DependencyDescriptor descriptor) {
        return new DataAccessImpl<>(genieDataBeans, descriptor);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    protected <T extends Persistable<ID>, ID extends Serializable> Repository<T, ID>
    genieDataRepository(GenieDataBeans genieDataBeans,
                        DependencyDescriptor descriptor) {
        return new RepositoryImpl<>(genieDataBeans, descriptor);
    }

}
