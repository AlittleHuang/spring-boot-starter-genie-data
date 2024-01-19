package io.github.genie.data.repository;

import io.github.genie.sql.api.Column;
import io.github.genie.sql.api.Expression;
import io.github.genie.sql.api.ExpressionHolder;
import io.github.genie.sql.api.Operator;
import io.github.genie.sql.builder.ExpressionHolders;
import io.github.genie.sql.builder.Expressions;
import io.github.genie.sql.builder.meta.Attribute;
import io.github.genie.sql.builder.meta.EntityType;
import io.github.genie.sql.builder.meta.Metamodel;
import org.springframework.beans.factory.config.DependencyDescriptor;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DataAccessImpl<T> extends AbstractDataAccess<T> implements DataAccess<T> {
    private final Metamodel metamodel;

    public DataAccessImpl(DataAccessor dataAccessor,
                          DependencyDescriptor descriptor,
                          Metamodel metamodel) {
        super(dataAccessor, descriptor);
        this.metamodel = metamodel;
    }

    @Override
    public T get(Serializable id) {
        EntityType entity = metamodel.getEntity(entityType);
        Attribute idAttribute = entity.id();
        Column idColumn = Expressions.column(idAttribute.name());
        Expression operate = Expressions.operate(idColumn, Operator.EQ, Expressions.of(id));
        ExpressionHolder<T, Boolean> predicate = ExpressionHolders.of(operate);
        return query().where(predicate).getSingle();
    }

    @Override
    public <ID extends Serializable> List<T> getAll(Collection<? extends ID> ids) {
        if (ids.isEmpty()) {
            return Collections.emptyList();
        }
        EntityType entity = metamodel.getEntity(entityType);
        Attribute idAttribute = entity.id();
        Column idColumn = Expressions.column(idAttribute.name());
        List<Expression> idsExpression = ids.stream().map(Expressions::of).collect(Collectors.toList());
        Expression operate = Expressions.operate(idColumn, Operator.IN, idsExpression);
        ExpressionHolder<T, Boolean> predicate = ExpressionHolders.of(operate);
        return query().where(predicate).getList();
    }

}
