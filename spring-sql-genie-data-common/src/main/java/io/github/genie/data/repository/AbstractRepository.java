package io.github.genie.data.repository;

import io.github.genie.sql.api.Column;
import io.github.genie.sql.api.EntityRoot;
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
import io.github.genie.sql.api.Query;
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
import io.github.genie.sql.builder.meta.Attribute;
import io.github.genie.sql.builder.meta.Metamodel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public abstract class AbstractRepository<T, ID> implements Entities<T, ID> {
    protected Query reader;
    protected Update writer;
    protected Metamodel metamodel;
    protected Class<T> entityType;
    protected Select<T> select;
    protected Column idColumn;

    protected AbstractRepository() {
    }

    public AbstractRepository(Class<T> entityType) {
        this.entityType = entityType;
    }

    @Autowired
    protected void setEntityType(Query query, Update update, Metamodel metamodel) {
        this.reader = query;
        this.writer = update;
        this.metamodel = metamodel;
        this.entityType = resolveEntityType();
        this.select = reader.from(entityType);
        Attribute idAttribute = metamodel.getEntity(entityType).id();
        this.idColumn = Expressions.column(idAttribute.name());
    }

    protected Class<T> resolveEntityType() {
        if (this.entityType != null) {
            return this.entityType;
        }
        ResolvableType type = ResolvableType.forClass(getClass()).as(AbstractRepository.class);
        Class<?> entityType = type.resolveGeneric(0);
        return TypeCastUtil.cast(entityType);
    }

    public List<T> insert(List<T> entities) {
        return writer.insert(entities, entityType);
    }

    public List<T> update(List<T> entities) {
        return writer.update(entities, entityType);
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
        writer.delete(list, entityType);
    }

    public T get(ID id) {
        Expression operate = Expressions.operate(idColumn, Operator.EQ, Expressions.of(id));
        ExpressionHolder<T, Boolean> predicate = ExpressionHolders.of(operate);
        return where(predicate).getSingle();
    }

    public List<T> getAll(Iterable<? extends ID> ids) {
        List<Expression> idsExpression = StreamSupport.stream(ids.spliterator(), false)
                .map(Expressions::of).collect(Collectors.toList());
        if (idsExpression.isEmpty()) {
            return Collections.emptyList();
        }
        Expression operate = Expressions.operate(idColumn, Operator.IN, idsExpression);
        ExpressionHolder<T, Boolean> predicate = ExpressionHolders.of(operate);
        return where(predicate).getList();
    }

    public Map<ID, T> getMap(Iterable<? extends ID> ids) {
        List<T> entities = getAll(ids);
        return entities.stream()
                .collect(Collectors.toMap(
                        it -> TypeCastUtil.unsafeCast(metamodel.getEntity(entityType).id().get(it)),
                        Function.identity()
                ));
    }

    public T updateNonNullColumn(T entity) {
        return writer.updateNonNullColumn(entity, entityType);
    }

    public <R> Where<T, R> select(Class<R> projectionType) {
        return select.select(projectionType);
    }

    public Where0<T, Object[]> select(List<? extends ExpressionHolder<T, ?>> paths) {
        return select.select(paths);
    }

    @Override
    public Where0<T, Object[]> select(Function<EntityRoot<T>, List<? extends ExpressionHolder<T, ?>>> selectBuilder) {
        return select.select(selectBuilder);
    }

    public <R> Where0<T, R> select(ExpressionHolder<T, R> expression) {
        return select.select(expression);
    }

    public <R> Where0<T, R> select(Path<T, ? extends R> path) {
        return select.select(path);
    }

    public Where0<T, Object[]> select(Collection<Path<T, ?>> paths) {
        return select.select(paths);
    }

    public <R> Where<T, R> selectDistinct(Class<R> projectionType) {
        return select.selectDistinct(projectionType);
    }

    public Where0<T, Object[]> selectDistinct(List<? extends ExpressionHolder<T, ?>> paths) {
        return select.selectDistinct(paths);
    }

    @Override
    public Where0<T, Object[]> selectDistinct(Function<EntityRoot<T>, List<? extends ExpressionHolder<T, ?>>> selectBuilder) {
        return select.selectDistinct(selectBuilder);
    }

    public <R> Where0<T, R> selectDistinct(ExpressionHolder<T, R> expression) {
        return select.selectDistinct(expression);
    }

    public <R> Where0<T, R> selectDistinct(Path<T, ? extends R> path) {
        return select.selectDistinct(path);
    }

    public Where0<T, Object[]> selectDistinct(Collection<Path<T, ?>> paths) {
        return select.selectDistinct(paths);
    }

    public Where<T, T> fetch(List<ColumnHolder<T, ?>> expressions) {
        return select.fetch(expressions);
    }

    public Where<T, T> fetch(Collection<Path<T, ?>> paths) {
        return select.fetch(paths);
    }

    public OrderBy<T, T> where(ExpressionHolder<T, Boolean> predicate) {
        return select.where(predicate);
    }

    @Override
    public OrderBy<T, T> where(Function<EntityRoot<T>, ExpressionHolder<T, Boolean>> predicateBuilder) {
        return select.where(predicateBuilder);
    }

    public <N> PathOperator<T, N, ? extends AndBuilder<T, T>> where(Path<T, N> path) {
        return select.where(path);
    }

    public <N extends Number & Comparable<N>> NumberOperator<T, N, ? extends AndBuilder<T, T>> where(NumberPath<T, N> path) {
        return select.where(path);
    }

    public <N extends Comparable<N>> ComparableOperator<T, N, ? extends AndBuilder<T, T>> where(ComparablePath<T, N> path) {
        return select.where(path);
    }

    public StringOperator<T, ? extends AndBuilder<T, T>> where(StringPath<T> path) {
        return select.where(path);
    }

    public AndBuilder<T, T> where(BooleanPath<T> path) {
        return select.where(path);
    }

    public Collector<T> orderBy(List<? extends Order<T>> orders) {
        return select.orderBy(orders);
    }

    @Override
    public Collector<T> orderBy(Function<EntityRoot<T>, List<? extends Order<T>>> ordersBuilder) {
        return select.orderBy(ordersBuilder);
    }

    public OrderOperator<T, T> orderBy(Collection<Path<T, Comparable<?>>> paths) {
        return select.orderBy(paths);
    }

    public long count() {
        return select.count();
    }

    public List<T> getList(int offset, int maxResult, LockModeType lockModeType) {
        return select.getList(offset, maxResult, lockModeType);
    }

    public boolean exist(int offset) {
        return select.exist(offset);
    }

    public <R> R slice(Sliceable<T, R> sliceable) {
        return select.slice(sliceable);
    }

    public Slice<T> slice(int offset, int limit) {
        return select.slice(offset, limit);
    }

    public QueryStructureBuilder buildMetadata() {
        return select.buildMetadata();
    }

}