package io.github.genie.data.repository;

import io.github.genie.sql.api.Query.Select;
import org.springframework.beans.factory.config.DependencyDescriptor;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AbstractDataAccess<T> implements Reader<T>, Writer<T> {

    private final Select<T> select;
    private final Class<T> entityType;
    private final GenieDataBeans genieDataBeans;

    public AbstractDataAccess(GenieDataBeans genieDataBeans, DependencyDescriptor descriptor) {
        Objects.requireNonNull(descriptor);
        this.genieDataBeans = genieDataBeans;
        Class<?> entityType = descriptor.getResolvableType()
                .as(Reader.class)
                .resolveGeneric(0);
        if (entityType == null) {
            entityType = descriptor.getResolvableType()
                    .as(Writer.class)
                    .resolveGeneric(0);
        }
        Objects.requireNonNull(descriptor);
        this.entityType = TypeCastUtil.cast(entityType);
        select = genieDataBeans.query().from(this.entityType);
    }

    @Override
    public Select<T> query() {
        return select;
    }

    @Override
    public List<T> insert(List<T> entities) {
        return genieDataBeans.update().insert(entities, entityType);
    }

    @Override
    public List<T> update(List<T> entities) {
        return genieDataBeans.update().update(entities, entityType);
    }

    @Override
    public void delete(Iterable<T> entities) {
        List<T> delete = entities instanceof List<?>
                ? (List<T>) entities
                : StreamSupport
                .stream(entities.spliterator(), false)
                .collect(Collectors.toList());
        genieDataBeans.update().delete(delete, entityType);
    }
}
