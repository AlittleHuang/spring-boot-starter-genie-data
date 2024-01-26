package io.github.genie.data.repository;

import lombok.experimental.Delegate;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public abstract class AbstractRepository<T, ID extends Serializable> {

    @Autowired
    private DataAccess<T> dataAccess;

    @Delegate
    private Reader<T> reader() {
        return dataAccess;
    }

    @Delegate
    private Writer<T> writer() {
        return dataAccess;
    }

    public T get(ID id) {
        return dataAccess.get(id);
    }

    public List<T> getAll(Collection<? extends ID> ids) {
        return dataAccess.getAll(ids);
    }

}
