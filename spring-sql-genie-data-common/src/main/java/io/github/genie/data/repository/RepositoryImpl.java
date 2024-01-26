package io.github.genie.data.repository;

import io.github.genie.sql.api.Column;
import io.github.genie.sql.api.Query.Select;
import io.github.genie.sql.api.Update;
import io.github.genie.sql.builder.Expressions;
import io.github.genie.sql.builder.meta.Attribute;
import io.github.genie.sql.builder.meta.EntityType;
import io.github.genie.sql.builder.meta.Metamodel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.config.DependencyDescriptor;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

class RepositoryImpl<T> extends RepositoryBase<T> implements Repository<T> {

    protected final Select<T> select;
    protected final Class<T> entityType;
    protected final DataAccessor dataAccessor;
    private final Column idColumn;

    public RepositoryImpl(DataAccessor dataAccessor, DependencyDescriptor descriptor) {
        this(dataAccessor, getEntityType(descriptor));
    }

    public RepositoryImpl(DataAccessor dataAccessor, Class<T> entityType) {
        this.dataAccessor = dataAccessor;
        this.entityType = entityType;
        select = dataAccessor.reader().from(this.entityType);
        idColumn = getIdColumn(dataAccessor, entityType);
    }

    @Nullable
    private static <T> Class<T> getEntityType(DependencyDescriptor descriptor) {
        Objects.requireNonNull(descriptor);
        Class<?> entityType = descriptor.getResolvableType()
                .as(Select.class)
                .resolveGeneric(0);
        return TypeCastUtil.cast(entityType);
    }

    @Override
    public Column idColumn() {
        return idColumn;
    }

    @NotNull
    static Column getIdColumn(DataAccessor dataAccessor, Class<?> entityType) {
        EntityType entity = dataAccessor.metamodel().getEntity(entityType);
        Attribute idAttribute = entity.id();
        return Expressions.column(idAttribute.name());
    }

    @Override
    public Select<T> select() {
        return select;
    }

    @Override
    public Update update() {
        return dataAccessor.writer();
    }

    @Override
    public Class<T> entityType() {
        return entityType;
    }

    @Override
    public Metamodel metamodel() {
        return dataAccessor.metamodel();
    }

    @Override
    public T get(Serializable id) {
        return getEntity(id);
    }

    @Override
    public <ID extends Serializable> List<T> getAll(Iterable<? extends ID> ids) {
        return getEntities(ids);
    }
}
