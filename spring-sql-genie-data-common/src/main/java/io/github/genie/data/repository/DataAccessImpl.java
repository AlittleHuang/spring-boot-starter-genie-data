package io.github.genie.data.repository;

import org.springframework.beans.factory.config.DependencyDescriptor;

public class DataAccessImpl<T> extends AbstractDataAccess<T> implements DataAccess<T> {
    public DataAccessImpl(DataAccessor genieDataBeans, DependencyDescriptor descriptor) {
        super(genieDataBeans, descriptor);
    }
}
