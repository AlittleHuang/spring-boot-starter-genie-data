package io.github.genie.data.access;

import io.github.genie.sql.api.Update;
import io.github.genie.sql.builder.meta.Metamodel;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

import java.io.Serializable;

@Slf4j
@Configuration
public class BaseDbAccessConfiguration {

    @Bean
    @Primary
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    protected <T, ID extends Serializable> Access<T, ID> dbAccess(DependencyDescriptor descriptor,
                                                                  Metamodel metamodel) {
        Class<T> entityType = AccessTypeUtil.getEntityType(descriptor);
        Class<?> dependencyType = descriptor.getDependencyType();
        if (Access.class.isAssignableFrom(dependencyType)) {
            checkIdType(descriptor, metamodel, entityType);
        }
        return BaseAccessImpl.access(entityType);
    }

    @Bean
    protected static BeanPostProcessor updateWrapperBeanPostProcessor() {
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

    private <T, ID extends Serializable> void checkIdType(DependencyDescriptor descriptor,
                                                          Metamodel metamodel,
                                                          Class<T> entityType) {
        Class<ID> idType = AccessTypeUtil.getIdType(descriptor);
        Class<?> expected = metamodel.getEntity(entityType).id().javaType();
        if (expected != idType) {
            String msg = descriptor.getResolvableType() + " " + descriptor
                         + " id type mismatch, expected: " + expected + ", actual: " + idType;
            throw new EntityIdTypeMismatchException(msg);
        }
    }

}
