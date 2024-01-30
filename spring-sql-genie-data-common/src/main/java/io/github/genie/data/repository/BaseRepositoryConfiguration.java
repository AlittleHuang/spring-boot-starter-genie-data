package io.github.genie.data.repository;

import io.github.genie.sql.api.Query.Select;
import io.github.genie.sql.api.Update;
import io.github.genie.sql.api.Updater;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.io.Serializable;
import java.util.Objects;

@Configuration
public class BaseRepositoryConfiguration {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @ConditionalOnMissingBean
    protected <T> Repository<T> genieDataRepository(@SuppressWarnings("ALL") DependencyDescriptor descriptor) {
        return new RepositoryImpl<>(getEntityType(descriptor));
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @ConditionalOnMissingBean
    protected <T extends Identifiable<ID>, ID extends Serializable>
    Entities<T, ID> genieDataEntities(@SuppressWarnings("ALL") DependencyDescriptor descriptor) {
        Class<T> entityType = getEntityType(descriptor);
        return new AbstractRepository<>(entityType) {
        };
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

    public static <T> Class<T> getEntityType(DependencyDescriptor descriptor) {
        Objects.requireNonNull(descriptor);
        Class<?> entityType = descriptor.getResolvableType()
                .as(Select.class)
                .resolveGeneric(0);
        if (entityType == null) {
            entityType = descriptor.getResolvableType()
                    .as(Updater.class)
                    .resolveGeneric(0);
        }
        Objects.requireNonNull(entityType);
        return TypeCastUtil.cast(entityType);
    }

}
