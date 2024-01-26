package io.github.genie.data.repository;

import io.github.genie.sql.api.Column;
import io.github.genie.sql.api.Query.Select;
import io.github.genie.sql.api.Update;
import io.github.genie.sql.builder.meta.Metamodel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;

import java.io.Serializable;
import java.util.List;

public class AbstractRepository<T, ID extends Serializable> extends RepositoryBase<T> {

    @Autowired
    private DataAccessor dataAccessor;

    private volatile RepositoryBase<T> repository;

    RepositoryBase<T> repository() {
        if (repository == null) {
            synchronized (this) {
                if (repository == null) {
                    repository = newRepository();
                }
            }
        }
        return repository;
    }

    private RepositoryBase<T> newRepository() {
        Class<?> entityType = ResolvableType.forClass(getClass())
                .as(Select.class)
                .resolveGeneric(0);
        return new RepositoryImpl<>(dataAccessor, TypeCastUtil.cast(entityType));
    }


    @Override
    public Select<T> select() {
        return repository().select();
    }

    @Override
    public Update update() {
        return repository().update();
    }

    @Override
    public Class<T> entityType() {
        return repository().entityType();
    }

    @Override
    public Column idColumn() {
        return repository().idColumn();
    }

    @Override
    public Metamodel metamodel() {
        return repository().metamodel();
    }

    public List<T> getAll(Iterable<? extends ID> ids) {
        return super.getEntities(ids);
    }

    public T get(ID id) {
        return super.getEntity(id);
    }
}
