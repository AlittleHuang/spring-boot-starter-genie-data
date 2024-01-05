package io.github.genie.data.repository;

import io.github.genie.sql.api.Path;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

interface PersistableReader<T extends Persistable<ID>, ID extends Serializable> extends Reader<T> {

    default T get(ID id) {
        return where((Path<T, ID>) Persistable::getId).eq(id).getSingle();
    }

    default List<T> getAll(Collection<? extends ID> ids) {
        return where((Path<T, ID>) Persistable::getId).in(ids).getList();
    }

    default Map<ID, T> toMap(Iterable<T> entities) {
        return StreamSupport.stream(entities.spliterator(), false)
                .collect(Collectors.toMap(Persistable::getId, Function.identity()));
    }

}
