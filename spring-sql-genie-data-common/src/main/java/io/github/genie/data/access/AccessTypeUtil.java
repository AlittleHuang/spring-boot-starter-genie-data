package io.github.genie.data.access;

import io.github.genie.sql.api.Query.Select;
import io.github.genie.sql.api.Updater;
import org.springframework.beans.factory.config.DependencyDescriptor;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author huang
 * @since 2024-03-16
 */
public class AccessTypeUtil {
    public static <T> Class<T> getEntityType(DependencyDescriptor descriptor) {
        Class<?> entityType;
        if (Select.class.isAssignableFrom(descriptor.getDependencyType())) {
            entityType = descriptor.getResolvableType()
                    .as(Select.class)
                    .resolveGeneric(0);
        } else {
            entityType = descriptor.getResolvableType()
                    .as(Updater.class)
                    .resolveGeneric(0);
        }
        Objects.requireNonNull(entityType);
        return TypeCastUtil.cast(entityType);
    }


    public static <ID extends Serializable> Class<ID> getIdType(DependencyDescriptor descriptor) {
        Class<?> type = descriptor.getResolvableType().as(Access.class).resolveGeneric(1);
        return TypeCastUtil.cast(type);
    }


    private AccessTypeUtil() {
    }
}
