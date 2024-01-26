package io.github.genie.data.repository;

import io.github.genie.sql.api.Column;
import io.github.genie.sql.api.Expression;
import io.github.genie.sql.api.ExpressionHolder;
import io.github.genie.sql.api.ExpressionHolder.ColumnHolder;
import io.github.genie.sql.api.ExpressionOperator.ComparableOperator;
import io.github.genie.sql.api.ExpressionOperator.NumberOperator;
import io.github.genie.sql.api.ExpressionOperator.PathOperator;
import io.github.genie.sql.api.ExpressionOperator.StringOperator;
import io.github.genie.sql.api.LockModeType;
import io.github.genie.sql.api.Operator;
import io.github.genie.sql.api.Order;
import io.github.genie.sql.api.Path;
import io.github.genie.sql.api.Path.BooleanPath;
import io.github.genie.sql.api.Path.ComparablePath;
import io.github.genie.sql.api.Path.NumberPath;
import io.github.genie.sql.api.Path.StringPath;
import io.github.genie.sql.api.Query.AndBuilder;
import io.github.genie.sql.api.Query.Collector;
import io.github.genie.sql.api.Query.OrderBy;
import io.github.genie.sql.api.Query.OrderOperator;
import io.github.genie.sql.api.Query.QueryStructureBuilder;
import io.github.genie.sql.api.Query.Select;
import io.github.genie.sql.api.Query.Where;
import io.github.genie.sql.api.Query.Where0;
import io.github.genie.sql.api.Slice;
import io.github.genie.sql.api.Sliceable;
import io.github.genie.sql.api.Update;
import io.github.genie.sql.builder.ExpressionHolders;
import io.github.genie.sql.builder.Expressions;
import io.github.genie.sql.builder.meta.Metamodel;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

abstract class RepositoryBase<T> implements Select<T> {
    abstract Select<T> select();

    abstract Update update();

    abstract Class<T> entityType();

    abstract Column idColumn();

    abstract Metamodel metamodel();

    public List<T> insert(List<T> entities) {
        return update().insert(entities, entityType());
    }

    public List<T> update(List<T> entities) {
        return update().update(entities, entityType());
    }

    public T update(T entity) {
        return update(Collections.singletonList(entity)).get(0);
    }

    public void delete(Iterable<T> entities) {
        List<T> list = entities instanceof List<T> l
                ? l
                : StreamSupport
                .stream(entities.spliterator(), false)
                .collect(Collectors.toList());
        update().delete(list, entityType());
    }

    T getEntity(Serializable id) {
        Expression operate = Expressions.operate(idColumn(), Operator.EQ, Expressions.of(id));
        ExpressionHolder<T, Boolean> predicate = ExpressionHolders.of(operate);
        return where(predicate).getSingle();
    }

    <ID extends Serializable> List<T> getEntities(Iterable<? extends ID> ids) {
        List<Expression> idsExpression = StreamSupport.stream(ids.spliterator(), false)
                .map(Expressions::of).collect(Collectors.toList());
        if (idsExpression.isEmpty()) {
            return Collections.emptyList();
        }
        Expression operate = Expressions.operate(idColumn(), Operator.IN, idsExpression);
        ExpressionHolder<T, Boolean> predicate = ExpressionHolders.of(operate);
        return where(predicate).getList();
    }

    public <R> Where<T, R> select(Class<R> projectionType) {
        return select().select(projectionType);
    }

    public Where0<T, Object[]> select(List<? extends ExpressionHolder<T, ?>> paths) {
        return select().select(paths);
    }

    public <R> Where0<T, R> select(ExpressionHolder<T, R> expression) {
        return select().select(expression);
    }

    public <R> Where0<T, R> select(Path<T, ? extends R> path) {
        return select().select(path);
    }

    public Where0<T, Object[]> select(Collection<Path<T, ?>> paths) {
        return select().select(paths);
    }

    public <R> Where<T, R> selectDistinct(Class<R> projectionType) {
        return select().selectDistinct(projectionType);
    }

    public Where0<T, Object[]> selectDistinct(List<? extends ExpressionHolder<T, ?>> paths) {
        return select().selectDistinct(paths);
    }

    public <R> Where0<T, R> selectDistinct(ExpressionHolder<T, R> expression) {
        return select().selectDistinct(expression);
    }

    public <R> Where0<T, R> selectDistinct(Path<T, ? extends R> path) {
        return select().selectDistinct(path);
    }

    public Where0<T, Object[]> selectDistinct(Collection<Path<T, ?>> paths) {
        return select().selectDistinct(paths);
    }

    public Where<T, T> fetch(List<ColumnHolder<T, ?>> expressions) {
        return select().fetch(expressions);
    }

    public Where<T, T> fetch(Collection<Path<T, ?>> paths) {
        return select().fetch(paths);
    }

    public OrderBy<T, T> where(ExpressionHolder<T, Boolean> predicate) {
        return select().where(predicate);
    }

    public <N> PathOperator<T, N, ? extends AndBuilder<T, T>> where(Path<T, N> path) {
        return select().where(path);
    }

    public <N extends Number & Comparable<N>> NumberOperator<T, N, ? extends AndBuilder<T, T>> where(NumberPath<T, N> path) {
        return select().where(path);
    }

    public <N extends Comparable<N>> ComparableOperator<T, N, ? extends AndBuilder<T, T>> where(ComparablePath<T, N> path) {
        return select().where(path);
    }

    public StringOperator<T, ? extends AndBuilder<T, T>> where(StringPath<T> path) {
        return select().where(path);
    }

    public AndBuilder<T, T> where(BooleanPath<T> path) {
        return select().where(path);
    }

    public Collector<T> orderBy(List<? extends Order<T>> orders) {
        return select().orderBy(orders);
    }

    public OrderOperator<T, T> orderBy(Collection<Path<T, Comparable<?>>> paths) {
        return select().orderBy(paths);
    }

    public long count() {
        return select().count();
    }

    public List<T> getList(int offset, int maxResult, LockModeType lockModeType) {
        return select().getList(offset, maxResult, lockModeType);
    }

    public boolean exist(int offset) {
        return select().exist(offset);
    }

    public <R> R slice(Sliceable<T, R> sliceable) {
        return select().slice(sliceable);
    }

    public Slice<T> slice(int offset, int limit) {
        return select().slice(offset, limit);
    }

    public QueryStructureBuilder buildMetadata() {
        return select().buildMetadata();
    }
}