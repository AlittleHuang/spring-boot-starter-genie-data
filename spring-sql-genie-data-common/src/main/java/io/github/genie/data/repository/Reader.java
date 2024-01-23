package io.github.genie.data.repository;

import io.github.genie.sql.api.ExpressionHolder;
import io.github.genie.sql.api.ExpressionHolder.ColumnHolder;
import io.github.genie.sql.api.ExpressionOperator.ComparableOperator;
import io.github.genie.sql.api.ExpressionOperator.NumberOperator;
import io.github.genie.sql.api.ExpressionOperator.PathOperator;
import io.github.genie.sql.api.ExpressionOperator.StringOperator;
import io.github.genie.sql.api.LockModeType;
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
import io.github.genie.sql.api.Query.Select;
import io.github.genie.sql.api.Query.Where;
import io.github.genie.sql.api.Query.Where0;
import io.github.genie.sql.api.Slice;
import io.github.genie.sql.api.Sliceable;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

interface Reader<T> {
    Select<T> query();

    default <R> Where<T, R> select(Class<R> projectionType) {
        return query().select(projectionType);
    }

    default Where0<T, Object[]> select(List<? extends ExpressionHolder<T, ?>> paths) {
        return query().select(paths);
    }

    default <R> Where0<T, R> select(ExpressionHolder<T, R> expression) {
        return query().select(expression);
    }

    default <R> Where0<T, R> select(Path<T, ? extends R> path) {
        return query().select(path);
    }

    default Where0<T, Object[]> select(Collection<Path<T, ?>> paths) {
        return query().select(paths);
    }

    default Where0<T, Object[]> select(Path<T, ?> p0, Path<T, ?> p1) {
        return query().select(p0, p1);
    }

    default Where0<T, Object[]> select(Path<T, ?> p0, Path<T, ?> p1, Path<T, ?> p2) {
        return query().select(p0, p1, p2);
    }

    default Where0<T, Object[]> select(Path<T, ?> p0, Path<T, ?> p1, Path<T, ?> p2, Path<T, ?> p3) {
        return query().select(p0, p1, p2, p3);
    }

    default Where0<T, Object[]> select(Path<T, ?> p0, Path<T, ?> p1, Path<T, ?> p2, Path<T, ?> p3, Path<T, ?> p4) {
        return query().select(p0, p1, p2, p3, p4);
    }

    default Where0<T, Object[]> select(Path<T, ?> p0, Path<T, ?> p1, Path<T, ?> p2, Path<T, ?> p3, Path<T, ?> p4, Path<T, ?> p5) {
        return query().select(p0, p1, p2, p3, p4, p5);
    }

    default Where0<T, Object[]> select(Path<T, ?> p0, Path<T, ?> p1, Path<T, ?> p2, Path<T, ?> p3, Path<T, ?> p4, Path<T, ?> p5, Path<T, ?> p6) {
        return query().select(p0, p1, p2, p3, p4, p5, p6);
    }

    default Where0<T, Object[]> select(Path<T, ?> p0, Path<T, ?> p1, Path<T, ?> p2, Path<T, ?> p3, Path<T, ?> p4, Path<T, ?> p5, Path<T, ?> p6, Path<T, ?> p7) {
        return query().select(p0, p1, p2, p3, p4, p5, p6, p7);
    }

    default Where0<T, Object[]> select(Path<T, ?> p0, Path<T, ?> p1, Path<T, ?> p2, Path<T, ?> p3, Path<T, ?> p4, Path<T, ?> p5, Path<T, ?> p6, Path<T, ?> p7, Path<T, ?> p8) {
        return query().select(p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }

    default Where0<T, Object[]> select(Path<T, ?> p0, Path<T, ?> p1, Path<T, ?> p2, Path<T, ?> p3, Path<T, ?> p4, Path<T, ?> p5, Path<T, ?> p6, Path<T, ?> p7, Path<T, ?> p8, Path<T, ?> p9) {
        return query().select(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }

    default <R> Where<T, R> selectDistinct(Class<R> projectionType) {
        return query().selectDistinct(projectionType);
    }

    default Where0<T, Object[]> selectDistinct(List<? extends ExpressionHolder<T, ?>> paths) {
        return query().selectDistinct(paths);
    }

    default <R> Where0<T, R> selectDistinct(ExpressionHolder<T, R> expression) {
        return query().selectDistinct(expression);
    }

    default <R> Where0<T, R> selectDistinct(Path<T, ? extends R> path) {
        return query().selectDistinct(path);
    }

    default Where0<T, Object[]> selectDistinct(Collection<Path<T, ?>> paths) {
        return query().selectDistinct(paths);
    }

    default Where0<T, Object[]> selectDistinct(Path<T, ?> p0, Path<T, ?> p1) {
        return query().selectDistinct(p0, p1);
    }

    default Where0<T, Object[]> selectDistinct(Path<T, ?> p0, Path<T, ?> p1, Path<T, ?> p2) {
        return query().selectDistinct(p0, p1, p2);
    }

    default Where0<T, Object[]> selectDistinct(Path<T, ?> p0, Path<T, ?> p1, Path<T, ?> p2, Path<T, ?> p3) {
        return query().selectDistinct(p0, p1, p2, p3);
    }

    default Where0<T, Object[]> selectDistinct(Path<T, ?> p0, Path<T, ?> p1, Path<T, ?> p2, Path<T, ?> p3, Path<T, ?> p4) {
        return query().selectDistinct(p0, p1, p2, p3, p4);
    }

    default Where0<T, Object[]> selectDistinct(Path<T, ?> p0, Path<T, ?> p1, Path<T, ?> p2, Path<T, ?> p3,
                                               Path<T, ?> p4, Path<T, ?> p5) {
        return query().selectDistinct(p0, p1, p2, p3, p4, p5);
    }

    default Where0<T, Object[]> selectDistinct(Path<T, ?> p0, Path<T, ?> p1, Path<T, ?> p2, Path<T, ?> p3,
                                               Path<T, ?> p4, Path<T, ?> p5, Path<T, ?> p6) {
        return query().selectDistinct(p0, p1, p2, p3, p4, p5, p6);
    }

    default Where0<T, Object[]> selectDistinct(Path<T, ?> p0, Path<T, ?> p1, Path<T, ?> p2, Path<T, ?> p3,
                                               Path<T, ?> p4, Path<T, ?> p5, Path<T, ?> p6, Path<T, ?> p7) {
        return query().selectDistinct(p0, p1, p2, p3, p4, p5, p6, p7);
    }

    default Where0<T, Object[]> selectDistinct(Path<T, ?> p0, Path<T, ?> p1, Path<T, ?> p2, Path<T, ?> p3,
                                               Path<T, ?> p4, Path<T, ?> p5, Path<T, ?> p6, Path<T, ?> p7, Path<T, ?> p8) {
        return query().selectDistinct(p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }

    default Where0<T, Object[]> selectDistinct(Path<T, ?> p0, Path<T, ?> p1, Path<T, ?> p2, Path<T, ?> p3,
                                               Path<T, ?> p4, Path<T, ?> p5, Path<T, ?> p6, Path<T, ?> p7,
                                               Path<T, ?> p8, Path<T, ?> p9) {
        return query().selectDistinct(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }

    default Where<T, T> fetch(List<ColumnHolder<T, ?>> expressions) {
        return query().fetch(expressions);
    }

    default Where<T, T> fetch(ColumnHolder<T, ?> path) {
        return query().fetch(path);
    }

    default Where<T, T> fetch(ColumnHolder<T, ?> p0, ColumnHolder<T, ?> p1) {
        return query().fetch(p0, p1);
    }

    default Where<T, T> fetch(ColumnHolder<T, ?> p0, ColumnHolder<T, ?> p1, ColumnHolder<T, ?> p3) {
        return query().fetch(p0, p1, p3);
    }

    default Where<T, T> fetch(Collection<Path<T, ?>> paths) {
        return query().fetch(paths);
    }

    default Where<T, T> fetch(Path<T, ?> path) {
        return query().fetch(path);
    }

    default Where<T, T> fetch(Path<T, ?> p0, Path<T, ?> p1) {
        return query().fetch(p0, p1);
    }

    default Where<T, T> fetch(Path<T, ?> p0, Path<T, ?> p1, Path<T, ?> p3) {
        return query().fetch(p0, p1, p3);
    }

    default OrderBy<T, T> where(ExpressionHolder<T, Boolean> predicate) {
        return query().where(predicate);
    }

    default <N> PathOperator<T, N, ? extends AndBuilder<T, T>> where(Path<T, N> path) {
        return query().where(path);
    }

    default <N extends Number & Comparable<N>> NumberOperator<T, N, ? extends AndBuilder<T, T>> where(NumberPath<T, N> path) {
        return query().where(path);
    }

    default <N extends Comparable<N>> ComparableOperator<T, N, ? extends AndBuilder<T, T>> where(ComparablePath<T, N> path) {
        return query().where(path);
    }

    default StringOperator<T, ? extends AndBuilder<T, T>> where(StringPath<T> path) {
        return query().where(path);
    }

    default AndBuilder<T, T> where(BooleanPath<T> path) {
        return query().where(path);
    }

    default Collector<T> orderBy(List<? extends Order<T>> orders) {
        return query().orderBy(orders);
    }

    default Collector<T> orderBy(Order<T> order) {
        return query().orderBy(order);
    }

    default Collector<T> orderBy(Order<T> p0, Order<T> p1) {
        return query().orderBy(p0, p1);
    }

    default Collector<T> orderBy(Order<T> order1, Order<T> order2, Order<T> order3) {
        return query().orderBy(order1, order2, order3);
    }

    default OrderOperator<T, T> orderBy(Collection<Path<T, Comparable<?>>> paths) {
        return query().orderBy(paths);
    }

    default OrderOperator<T, T> orderBy(Path<T, Comparable<?>> path) {
        return query().orderBy(path);
    }

    default OrderOperator<T, T> orderBy(Path<T, Comparable<?>> p1, Path<T, Comparable<?>> p2) {
        return query().orderBy(p1, p2);
    }

    default OrderOperator<T, T> orderBy(Path<T, Comparable<?>> p1, Path<T, Comparable<?>> p2, Path<T, Comparable<?>> p3) {
        return query().orderBy(p1, p2, p3);
    }

    default long count() {
        return query().count();
    }

    default List<T> getList(int offset, int maxResult, LockModeType lockModeType) {
        return query().getList(offset, maxResult, lockModeType);
    }

    default List<T> getList(int offset, int maxResult) {
        return query().getList(offset, maxResult);
    }

    default boolean exist(int offset) {
        return query().exist(offset);
    }

    default Optional<T> first() {
        return query().first();
    }

    default Optional<T> first(int offset) {
        return query().first(offset);
    }

    default T getFirst() {
        return query().getFirst();
    }

    default T getFirst(int offset) {
        return query().getFirst(offset);
    }

    default T requireSingle() {
        return query().requireSingle();
    }

    default Optional<T> single() {
        return query().single();
    }

    default Optional<T> single(int offset) {
        return query().single(offset);
    }

    default T getSingle() {
        return query().getSingle();
    }

    default T getSingle(int offset) {
        return query().getSingle(offset);
    }

    default List<T> getList() {
        return query().getList();
    }

    default boolean exist() {
        return query().exist();
    }

    default Optional<T> first(LockModeType lockModeType) {
        return query().first(lockModeType);
    }

    default Optional<T> first(int offset, LockModeType lockModeType) {
        return query().first(offset, lockModeType);
    }

    default T getFirst(LockModeType lockModeType) {
        return query().getFirst(lockModeType);
    }

    default T getFirst(int offset, LockModeType lockModeType) {
        return query().getFirst(offset, lockModeType);
    }

    default T requireSingle(LockModeType lockModeType) {
        return query().requireSingle(lockModeType);
    }

    default Optional<T> single(LockModeType lockModeType) {
        return query().single(lockModeType);
    }

    default Optional<T> single(int offset, LockModeType lockModeType) {
        return query().single(offset, lockModeType);
    }

    default T getSingle(LockModeType lockModeType) {
        return query().getSingle(lockModeType);
    }

    default T getSingle(int offset, LockModeType lockModeType) {
        return query().getSingle(offset, lockModeType);
    }

    default List<T> getList(int offset, LockModeType lockModeType) {
        return query().getList(offset, lockModeType);
    }

    default List<T> getList(LockModeType lockModeType) {
        return query().getList(lockModeType);
    }

    default <R> R getResult(@NotNull Function<? super Collector<T>, R> function) {
        return query().getResult(function);
    }

    default <R> R slice(Sliceable<T, R> sliceable) {
        return query().slice(sliceable);
    }

    default Slice<T> slice(int offset, int limit) {
        return query().slice(offset, limit);
    }
}
