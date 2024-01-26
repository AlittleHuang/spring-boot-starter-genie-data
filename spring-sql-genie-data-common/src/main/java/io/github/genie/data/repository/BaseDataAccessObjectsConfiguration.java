package io.github.genie.data.repository;

import io.github.genie.sql.api.Query;
import io.github.genie.sql.api.Update;
import io.github.genie.sql.builder.meta.Metamodel;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.io.Serializable;

@Configuration
public class BaseDataAccessObjectsConfiguration {

    @Bean
    protected DataAccessor gineDataAccessor(Query query, Update update, Metamodel metamodel) {
        return new DataAccessorImpl(query, update, metamodel);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    protected <T> DataAccess<T> genieDataAccess(DataAccessor dataAccessor,
                                                DependencyDescriptor descriptor) {
        return new DataAccessImpl<>(dataAccessor, descriptor);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    protected <T extends Persistable<ID>, ID extends Serializable> Repository<T, ID>
    genieDataRepository(DataAccessor dataAccessor,
                        DependencyDescriptor descriptor) {
        return new RepositoryImpl<>(dataAccessor, descriptor);
    }

    @Bean
    protected BeanPostProcessor updateWrapperBeanPostProcessor() {
        return new UpdateWrapperBeanPostProcessor();
    }

    private static class UpdateWrapperBeanPostProcessor implements BeanPostProcessor {
        @Override
        public Object postProcessBeforeInitialization(@NotNull Object bean, @NotNull String beanName) {
            if (bean instanceof Update && !(bean instanceof TransactionalUpdate)) {
                return new TransactionalUpdate((Update) bean);
            }
            return bean;
        }
    }


}
