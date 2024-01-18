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
    private final DataAccessor dataAccessor;

    public AbstractDataAccess(DataAccessor dataAccessor, DependencyDescriptor descriptor) {
        Objects.requireNonNull(descriptor);
        this.dataAccessor = dataAccessor;
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
        select = dataAccessor.reader().from(this.entityType);
    }

    @Override
    public Select<T> query() {
        return select;
    }

    @Override
    public List<T> insert(List<T> entities) {
        return dataAccessor.writer().insert(entities, entityType);
    }

    @Override
    public List<T> update(List<T> entities) {
        return dataAccessor.writer().update(entities, entityType);
    }

    @Override
    public void delete(Iterable<T> entities) {
        List<T> delete = entities instanceof List<?>
                ? (List<T>) entities
                : StreamSupport
                .stream(entities.spliterator(), false)
                .collect(Collectors.toList());
        dataAccessor.writer().delete(delete, entityType);
    }
}
