package io.github.genie.data.repository;

import java.util.Collections;
import java.util.List;

interface Writer<T> {

    default T insert(T entity) {
        return insert(Collections.singletonList(entity)).get(0);
    }

    List<T> insert(List<T> entities);

    default T update(T entity) {
        return update(Collections.singletonList(entity)).get(0);
    }

    List<T> update(List<T> entities);

    default void delete(T entity) {
        delete(Collections.singletonList(entity));
    }

    void delete(Iterable<T> entities);

}
