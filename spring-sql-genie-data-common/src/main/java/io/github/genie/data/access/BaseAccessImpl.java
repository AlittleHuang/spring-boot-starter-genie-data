package io.github.genie.data.access;

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
import io.github.genie.sql.api.Path.ComparablePath;
import io.github.genie.sql.api.Path.NumberPath;
import io.github.genie.sql.api.Path.StringPath;
import io.github.genie.sql.api.Query;
import io.github.genie.sql.api.Query.Collector;
import io.github.genie.sql.api.Query.OrderOperator;
import io.github.genie.sql.api.Query.QueryStructureBuilder;
import io.github.genie.sql.api.Query.Select;
import io.github.genie.sql.api.Query.Where;
import io.github.genie.sql.api.Query.Where0;
import io.github.genie.sql.api.Root;
import io.github.genie.sql.api.Slice;
import io.github.genie.sql.api.Sliceable;
import io.github.genie.sql.api.Update;
import io.github.genie.sql.api.tuple.Tuple;
import io.github.genie.sql.api.tuple.Tuple10;
import io.github.genie.sql.api.tuple.Tuple2;
import io.github.genie.sql.api.tuple.Tuple3;
import io.github.genie.sql.api.tuple.Tuple4;
import io.github.genie.sql.api.tuple.Tuple5;
import io.github.genie.sql.api.tuple.Tuple6;
import io.github.genie.sql.api.tuple.Tuple7;
import io.github.genie.sql.api.tuple.Tuple8;
import io.github.genie.sql.api.tuple.Tuple9;
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

class BaseAccessImpl<T, ID> implements BaseAccess<T> {
    protected Query query;
    protected Update update;
    protected Metamodel metamodel;
    protected Class<T> entityType;
    protected Select<T> select;
    protected Column idColumn;

    protected BaseAccessImpl() {
    }

    public BaseAccessImpl(Class<T> entityType) {
        this.entityType = entityType;
    }

    static <T, ID> Access<T, ID> access(Class<T> entityType) {
        return new AccessImpl<>(entityType);
    }

    @Autowired
    protected void setEntityType(Query query, Update update, Metamodel metamodel) {
        this.query = query;
        this.update = update;
        this.metamodel = metamodel;
        this.entityType = resolveEntityType();
        this.select = query.from(entityType);
        Attribute idAttribute = metamodel.getEntity(entityType).id();
        this.idColumn = Expressions.column(idAttribute.name());
    }

    protected Class<T> resolveEntityType() {
        if (this.entityType != null) {
            return this.entityType;
        }
        ResolvableType type = ResolvableType.forClass(getClass()).as(BaseAccessImpl.class);
        Class<?> entityType = type.resolveGeneric(0);
        return TypeCastUtil.cast(entityType);
    }

    @Override
    public List<T> insert(List<T> entities) {
        return update.insert(entities, entityType);
    }

    @Override
    public List<T> update(List<T> entities) {
        return update.update(entities, entityType);
    }

    @Override
    public void delete(Iterable<T> entities) {
        update.delete(entities, entityType);
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

    @Override
    public T updateNonNullColumn(T entity) {
        return update.updateNonNullColumn(entity, entityType);
    }

    @Override
    public <R> Where<T, R> select(Class<R> projectionType) {
        return select.select(projectionType);
    }

    @Override
    public Where0<T, Tuple> select(List<? extends ExpressionHolder<T, ?>> paths) {
        return select.select(paths);
    }

    @Override
    public Where0<T, Tuple> select(Function<Root<T>, List<? extends ExpressionHolder<T, ?>>> selectBuilder) {
        return select.select(selectBuilder);
    }

    @Override
    public <R> Where0<T, R> select(ExpressionHolder<T, R> expression) {
        return select.select(expression);
    }

    @Override
    public <R> Where0<T, R> select(Path<T, ? extends R> path) {
        return select.select(path);
    }

    @Override
    public Where0<T, Tuple> select(Collection<Path<T, ?>> paths) {
        return select.select(paths);
    }

    @Override
    public <R> Where<T, R> selectDistinct(Class<R> projectionType) {
        return select.selectDistinct(projectionType);
    }

    @Override
    public Where0<T, Tuple> selectDistinct(List<? extends ExpressionHolder<T, ?>> paths) {
        return select.selectDistinct(paths);
    }

    @Override
    public Where0<T, Tuple> selectDistinct(Function<Root<T>, List<? extends ExpressionHolder<T, ?>>> selectBuilder) {
        return select.selectDistinct(selectBuilder);
    }

    @Override
    public <R> Where0<T, R> selectDistinct(ExpressionHolder<T, R> expression) {
        return select.selectDistinct(expression);
    }

    @Override
    public <R> Where0<T, R> selectDistinct(Path<T, ? extends R> path) {
        return select.selectDistinct(path);
    }

    @Override
    public Where0<T, Tuple> selectDistinct(Collection<Path<T, ?>> paths) {
        return select.selectDistinct(paths);
    }

    @Override
    public Where<T, T> fetch(List<ColumnHolder<T, ?>> expressions) {
        return select.fetch(expressions);
    }

    @Override
    public Where<T, T> fetch(Collection<Path<T, ?>> paths) {
        return select.fetch(paths);
    }

    @Override
    public Where<T, T> where(ExpressionHolder<T, Boolean> predicate) {
        return select.where(predicate);
    }

    @Override
    public Where<T, T> where(Function<Root<T>, ExpressionHolder<T, Boolean>> predicateBuilder) {
        return select.where(predicateBuilder);
    }

    @Override
    public <N> PathOperator<T, N, ? extends Where<T, T>> where(Path<T, N> path) {
        return select.where(path);
    }

    @Override
    public <N extends Number & Comparable<N>> NumberOperator<T, N, ? extends Where<T, T>> where(NumberPath<T, N> path) {
        return select.where(path);
    }

    @Override
    public <N extends Comparable<N>> ComparableOperator<T, N, ? extends Where<T, T>> where(ComparablePath<T, N> path) {
        return select.where(path);
    }

    @Override
    public StringOperator<T, ? extends Where<T, T>> where(StringPath<T> path) {
        return select.where(path);
    }

    @Override
    public Collector<T> orderBy(List<? extends Order<T>> orders) {
        return select.orderBy(orders);
    }

    @Override
    public Collector<T> orderBy(Function<Root<T>, List<? extends Order<T>>> ordersBuilder) {
        return select.orderBy(ordersBuilder);
    }

    @Override
    public OrderOperator<T, T> orderBy(Collection<Path<T, Comparable<?>>> paths) {
        return select.orderBy(paths);
    }

    @Override
    public long count() {
        return select.count();
    }

    @Override
    public List<T> getList(int offset, int maxResult, LockModeType lockModeType) {
        return select.getList(offset, maxResult, lockModeType);
    }

    @Override
    public boolean exist(int offset) {
        return select.exist(offset);
    }

    @Override
    public <R> R slice(Sliceable<T, R> sliceable) {
        return select.slice(sliceable);
    }

    @Override
    public Slice<T> slice(int offset, int limit) {
        return select.slice(offset, limit);
    }

    @Override
    public QueryStructureBuilder buildMetadata() {
        return select.buildMetadata();
    }

    @Override
    public Root<T> root() {
        return select.root();
    }

    @Override
    public <A, B> Where0<T, Tuple2<A, B>> select(Path<T, A> a, Path<T, B> b) {
        return select.select(a, b);
    }

    @Override
    public <A, B, C> Where0<T, Tuple3<A, B, C>> select(Path<T, A> a, Path<T, B> b, Path<T, C> c) {
        return select.select(a, b, c);
    }

    @Override
    public <A, B, C, D> Where0<T, Tuple4<A, B, C, D>> select(Path<T, A> a, Path<T, B> b, Path<T, C> c, Path<T, D> d) {
        return select.select(a, b, c, d);
    }

    @Override
    public <A, B, C, D, E> Where0<T, Tuple5<A, B, C, D, E>>
    select(Path<T, A> a, Path<T, B> b, Path<T, C> c, Path<T, D> d, Path<T, E> e) {
        return select.select(a, b, c, d, e);
    }

    @Override
    public <A, B, C, D, E, F> Where0<T, Tuple6<A, B, C, D, E, F>>
    select(Path<T, A> a, Path<T, B> b, Path<T, C> c, Path<T, D> d, Path<T, E> e, Path<T, F> f) {
        return select.select(a, b, c, d, e, f);
    }

    @Override
    public <A, B, C, D, E, F, G> Where0<T, Tuple7<A, B, C, D, E, F, G>>
    select(Path<T, A> a, Path<T, B> b, Path<T, C> c, Path<T, D> d, Path<T, E> e, Path<T, F> f, Path<T, G> g) {
        return select.select(a, b, c, d, e, f, g);
    }

    @Override
    public <A, B, C, D, E, F, G, H> Where0<T, Tuple8<A, B, C, D, E, F, G, H>>
    select(Path<T, A> a, Path<T, B> b, Path<T, C> c, Path<T, D> d, Path<T, E> e, Path<T, F> f, Path<T, G> g, Path<T, H> h) {
        return select.select(a, b, c, d, e, f, g, h);
    }

    @Override
    public <A, B, C, D, E, F, G, H, I> Where0<T, Tuple9<A, B, C, D, E, F, G, H, I>>
    select(Path<T, A> a, Path<T, B> b, Path<T, C> c, Path<T, D> d, Path<T, E> e, Path<T, F> f, Path<T, G> g, Path<T, H> h, Path<T, I> i) {
        return select.select(a, b, c, d, e, f, g, h, i);
    }

    @Override
    public <A, B, C, D, E, F, G, H, I, J> Where0<T, Tuple10<A, B, C, D, E, F, G, H, I, J>>
    select(Path<T, A> a, Path<T, B> b, Path<T, C> c, Path<T, D> d, Path<T, E> e, Path<T, F> f, Path<T, G> g, Path<T, H> h, Path<T, I> i, Path<T, J> j) {
        return select.select(a, b, c, d, e, f, g, h, i, j);
    }

    @Override
    public <A, B> Where0<T, Tuple2<A, B>> selectDistinct(Path<T, A> a, Path<T, B> b) {
        return select.selectDistinct(a, b);
    }

    @Override
    public <A, B, C> Where0<T, Tuple3<A, B, C>>
    selectDistinct(Path<T, A> a, Path<T, B> b, Path<T, C> c) {
        return select.selectDistinct(a, b, c);
    }

    @Override
    public <A, B, C, D> Where0<T, Tuple4<A, B, C, D>>
    selectDistinct(Path<T, A> a, Path<T, B> b, Path<T, C> c, Path<T, D> d) {
        return select.selectDistinct(a, b, c, d);
    }

    @Override
    public <A, B, C, D, E> Where0<T, Tuple5<A, B, C, D, E>>
    selectDistinct(Path<T, A> a, Path<T, B> b, Path<T, C> c, Path<T, D> d, Path<T, E> e) {
        return select.selectDistinct(a, b, c, d, e);
    }

    @Override
    public <A, B, C, D, E, F> Where0<T, Tuple6<A, B, C, D, E, F>>
    selectDistinct(Path<T, A> a, Path<T, B> b, Path<T, C> c, Path<T, D> d, Path<T, E> e, Path<T, F> f) {
        return select.selectDistinct(a, b, c, d, e, f);
    }

    @Override
    public <A, B, C, D, E, F, G> Where0<T, Tuple7<A, B, C, D, E, F, G>>
    selectDistinct(Path<T, A> a, Path<T, B> b, Path<T, C> c, Path<T, D> d, Path<T, E> e, Path<T, F> f, Path<T, G> g) {
        return select.selectDistinct(a, b, c, d, e, f, g);
    }

    @Override
    public <A, B, C, D, E, F, G, H> Where0<T, Tuple8<A, B, C, D, E, F, G, H>>
    selectDistinct(Path<T, A> a, Path<T, B> b, Path<T, C> c, Path<T, D> d, Path<T, E> e, Path<T, F> f, Path<T, G> g, Path<T, H> h) {
        return select.selectDistinct(a, b, c, d, e, f, g, h);
    }

    @Override
    public <A, B, C, D, E, F, G, H, I> Where0<T, Tuple9<A, B, C, D, E, F, G, H, I>>
    selectDistinct(Path<T, A> a, Path<T, B> b, Path<T, C> c, Path<T, D> d, Path<T, E> e, Path<T, F> f, Path<T, G> g, Path<T, H> h, Path<T, I> i) {
        return select.selectDistinct(a, b, c, d, e, f, g, h, i);
    }

    @Override
    public <A, B, C, D, E, F, G, H, I, J> Where0<T, Tuple10<A, B, C, D, E, F, G, H, I, J>>
    selectDistinct(Path<T, A> a, Path<T, B> b, Path<T, C> c, Path<T, D> d, Path<T, E> e, Path<T, F> f, Path<T, G> g, Path<T, H> h, Path<T, I> i, Path<T, J> j) {
        return select.selectDistinct(a, b, c, d, e, f, g, h, i, j);
    }

    static class AccessImpl<T, ID> extends BaseAccessImpl<T, ID> implements Access<T, ID> {
        public AccessImpl(Class<T> entityType) {
            super(entityType);
        }
    }
}