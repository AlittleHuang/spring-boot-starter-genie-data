package io.github.genie.data.repository;

import io.github.genie.sql.api.Query.Select;
import io.github.genie.sql.api.Updater;

import java.util.List;
import java.util.Map;

public interface Entities<T, ID> extends Select<T>, Updater<T> {

    T get(ID id);

    List<T> getAll(Iterable<? extends ID> ids);

    Map<ID, T> getMap(Iterable<? extends ID> ids);

}
