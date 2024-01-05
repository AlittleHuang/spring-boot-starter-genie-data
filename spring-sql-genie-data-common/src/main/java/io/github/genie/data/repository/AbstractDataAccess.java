package io.github.genie.data.repository;

import io.github.genie.sql.api.Query.Select0;
import org.springframework.beans.factory.config.DependencyDescriptor;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AbstractDataAccess<T> implements Reader<T>, Writer<T> {

    private final Select0<T, T> select0;
    private final Class<T> entityType;
    private final GenieDataBeans genieDataBeans;

    public AbstractDataAccess(GenieDataBeans genieDataBeans, DependencyDescriptor descriptor) {
        Objects.requireNonNull(descriptor);
        this.genieDataBeans = genieDataBeans;
        Class<?> entityType = descriptor.getResolvableType().as(DataAccess.class)
                .resolveGeneric(0);
        this.entityType = TypeCastUtil.cast(entityType);
        select0 = genieDataBeans.query()
                .from(this.entityType);
    }

    @Override
    public Select0<T, T> query() {
        return select0;
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
        List<T> list = entities instanceof List<T> l
                ? l
                : StreamSupport
                .stream(entities.spliterator(), false)
                .collect(Collectors.toList());
        genieDataBeans.update().delete(list, entityType);
    }
}
