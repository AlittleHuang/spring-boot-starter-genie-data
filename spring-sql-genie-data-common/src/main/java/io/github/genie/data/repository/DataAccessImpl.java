package io.github.genie.data.repository;

import io.github.genie.sql.api.Query.Select0;
import org.springframework.beans.factory.config.DependencyDescriptor;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

public class DataAccessImpl<T> implements DataAccess<T> {

    private final GenieDataBeans genieDataBeans;
    private final Select0<T, T> select0;
    private final Class<T> entityType;

    public DataAccessImpl(GenieDataBeans genieDataBeans, DependencyDescriptor descriptor) {
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
        List<T> list;
        if (entities instanceof List<T> l) {
            list = l;
        } else if (entities instanceof Collection<T> collection) {
            list = collection.stream().toList();
        } else {
            list = StreamSupport.stream(entities.spliterator(), false).toList();
        }
        genieDataBeans.update().delete(list, entityType);
    }
}
