package io.github.genie.data.repository;


import lombok.experimental.Delegate;

import java.io.Serializable;
import java.util.Collection;

public interface Repository<T extends Persistable<ID>, ID extends Serializable> {

    @Delegate
    DataAccess<T> dataAccess();

    T get(ID id);

    T getAll(Collection<? extends ID> ids);

}
