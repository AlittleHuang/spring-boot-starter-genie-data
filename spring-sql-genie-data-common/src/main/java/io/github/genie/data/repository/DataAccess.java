package io.github.genie.data.repository;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface DataAccess<T> extends DataReader<T>, DataWriter<T> {

    T get(Serializable id);

    <ID extends Serializable> List<T> getAll(Collection<? extends ID> ids);

}
