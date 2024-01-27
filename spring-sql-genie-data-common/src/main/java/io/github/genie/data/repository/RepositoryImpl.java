package io.github.genie.data.repository;

import java.io.Serializable;

class RepositoryImpl<T> extends AbstractRepository<T, Serializable> implements Repository<T> {
    public RepositoryImpl(Class<T> entityType) {
        this.entityType = entityType;
    }
}
