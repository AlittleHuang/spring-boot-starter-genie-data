package io.github.genie.data.repository;

import java.io.Serializable;
import java.util.Collection;

public class RepositoryImpl<T extends Persistable<ID>, ID extends Serializable> implements Repository<T, ID> {

    private final DataAccess<T> dataAccess;

    public RepositoryImpl(DataAccess<T> dataAccess) {
        this.dataAccess = dataAccess;
    }

    @Override
    public DataAccess<T> dataAccess() {
        return dataAccess;
    }

    @Override
    public T get(ID id) {
        return null;
    }

    @Override
    public T getAll(Collection<? extends ID> ids) {
        return null;
    }
}
