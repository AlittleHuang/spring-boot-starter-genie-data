package io.github.genie.data.repository;

import org.springframework.beans.factory.config.DependencyDescriptor;

import java.io.Serializable;

public class RepositoryImpl<T extends Persistable<ID>, ID extends Serializable>
        extends AbstractDataAccess<T>
        implements Repository<T, ID> {
    public RepositoryImpl(DataAccessor dataAccessor, DependencyDescriptor descriptor) {
        super(dataAccessor, descriptor);
    }
}
