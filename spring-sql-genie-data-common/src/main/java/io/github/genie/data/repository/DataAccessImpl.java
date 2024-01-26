package io.github.genie.data.repository;

import io.github.genie.sql.api.Column;
import io.github.genie.sql.api.Expression;
import io.github.genie.sql.api.ExpressionHolder;
import io.github.genie.sql.api.Operator;
import io.github.genie.sql.builder.ExpressionHolders;
import io.github.genie.sql.builder.Expressions;
import io.github.genie.sql.builder.meta.Attribute;
import io.github.genie.sql.builder.meta.EntityType;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.config.DependencyDescriptor;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DataAccessImpl<T> extends AbstractDataAccess<T> implements DataAccess<T> {
    private Column idColumn;

    public DataAccessImpl(DataAccessor dataAccessor, DependencyDescriptor descriptor) {
        super(dataAccessor, descriptor);
    }

    @Override
    public T get(Serializable id) {
        Expression operate = Expressions.operate(getIdColumn(), Operator.EQ, Expressions.of(id));
        ExpressionHolder<T, Boolean> predicate = ExpressionHolders.of(operate);
        return query().where(predicate).getSingle();
    }

    @NotNull
    private Column getIdColumn() {
        if (idColumn == null) {
            EntityType entity = dataAccessor.metamodel().getEntity(entityType);
            Attribute idAttribute = entity.id();
            idColumn = Expressions.column(idAttribute.name());
        }
        return idColumn;
    }

    @Override
    public <ID extends Serializable> List<T> getAll(Collection<? extends ID> ids) {
        if (ids.isEmpty()) {
            return Collections.emptyList();
        }
        List<Expression> idsExpression = ids.stream().map(Expressions::of).collect(Collectors.toList());
        Expression operate = Expressions.operate(getIdColumn(), Operator.IN, idsExpression);
        ExpressionHolder<T, Boolean> predicate = ExpressionHolders.of(operate);
        return query().where(predicate).getList();
    }

}
