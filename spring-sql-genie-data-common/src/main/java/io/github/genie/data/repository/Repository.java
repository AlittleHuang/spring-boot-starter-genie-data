package io.github.genie.data.repository;


import io.github.genie.sql.api.Query.Select;
import io.github.genie.sql.api.Updater;

import java.io.Serializable;
import java.util.List;

public interface Repository<T> extends Select<T>, Updater<T> {

    T get(Serializable id);

    List<T> getAll(Iterable<? extends Serializable> ids);

}
