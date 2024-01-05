package io.github.genie.data.repository;

import io.github.genie.sql.api.ExpressionHolder;
import io.github.genie.sql.api.ExpressionOperator.ComparableOperator;
import io.github.genie.sql.api.ExpressionOperator.NumberOperator;
import io.github.genie.sql.api.ExpressionOperator.PathOperator;
import io.github.genie.sql.api.ExpressionOperator.Predicate;
import io.github.genie.sql.api.ExpressionOperator.StringOperator;
import io.github.genie.sql.api.LockModeType;
import io.github.genie.sql.api.Order;
import io.github.genie.sql.api.Path;
import io.github.genie.sql.api.Path.BooleanPath;
import io.github.genie.sql.api.Path.ComparablePath;
import io.github.genie.sql.api.Path.NumberPath;
import io.github.genie.sql.api.Path.StringPath;
import io.github.genie.sql.api.Query.AggWhere0;
import io.github.genie.sql.api.Query.AndBuilder;
import io.github.genie.sql.api.Query.Collector;
import io.github.genie.sql.api.Query.GroupBy0;
import io.github.genie.sql.api.Query.Having0;
import io.github.genie.sql.api.Query.OrderBy0;
import io.github.genie.sql.api.Query.Select0;
import io.github.genie.sql.api.Query.Where0;
import io.github.genie.sql.api.Slice;
import io.github.genie.sql.api.Sliceable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

interface Reader<T> {
    Select0<T, T> query();

    default GroupBy0<T, T> fetch(List<PathOperator<T, ?, Predicate<T>>> expressions) {
        return query().fetch(expressions);
    }

    default GroupBy0<T, T> fetch(Collection<Path<T, ?>> paths) {
        return query().fetch(paths);
    }

    default GroupBy0<T, T> fetch(Path<T, ?> path) {
        return query().fetch(path);
    }

    default GroupBy0<T, T> fetch(Path<T, ?> p0, Path<T, ?> p1) {
        return query().fetch(p0, p1);
    }

    default GroupBy0<T, T> fetch(Path<T, ?> p0, Path<T, ?> p1, Path<T, ?> p3) {
        return query().fetch(p0, p1, p3);
    }

    default <R> Where0<T, R> select(Class<R> projectionType) {
        return query().select(projectionType);
    }

    default AggWhere0<T, Object[]> select(List<? extends ExpressionHolder<T, ?>> paths) {
        return query().select(paths);
    }

    default <R> AggWhere0<T, R> select(ExpressionHolder<T, R> expression) {
        return query().select(expression);
    }

    default <R> AggWhere0<T, R> select(Path<T, ? extends R> expression) {
        return query().select(expression);
    }

    default AggWhere0<T, Object[]> select(Collection<Path<T, ?>> paths) {
        return query().select(paths);
    }

    default AggWhere0<T, Object[]> select(Path<T, ?> p0, Path<T, ?> p1) {
        return query().select(p0, p1);
    }

    default AggWhere0<T, Object[]> select(Path<T, ?> p0, Path<T, ?> p1, Path<T, ?> p2) {
        return query().select(p0, p1, p2);
    }

    default AggWhere0<T, Object[]> select(Path<T, ?> p0, Path<T, ?> p1, Path<T, ?> p2, Path<T, ?> p3) {
        return query().select(p0, p1, p2, p3);
    }

    default AggWhere0<T, Object[]> select(Path<T, ?> p0, Path<T, ?> p1, Path<T, ?> p2, Path<T, ?> p3, Path<T, ?> p4) {
        return query().select(p0, p1, p2, p3, p4);
    }

    default AggWhere0<T, Object[]> select(Path<T, ?> p0, Path<T, ?> p1, Path<T, ?> p2, Path<T, ?> p3, Path<T, ?> p4, Path<T, ?> p5) {
        return query().select(p0, p1, p2, p3, p4, p5);
    }

    default AggWhere0<T, Object[]> select(Path<T, ?> p0, Path<T, ?> p1, Path<T, ?> p2, Path<T, ?> p3, Path<T, ?> p4, Path<T, ?> p5, Path<T, ?> p6) {
        return query().select(p0, p1, p2, p3, p4, p5, p6);
    }

    default AggWhere0<T, Object[]> select(Path<T, ?> p0, Path<T, ?> p1, Path<T, ?> p2, Path<T, ?> p3, Path<T, ?> p4, Path<T, ?> p5, Path<T, ?> p6, Path<T, ?> p7) {
        return query().select(p0, p1, p2, p3, p4, p5, p6, p7);
    }

    default AggWhere0<T, Object[]> select(Path<T, ?> p0, Path<T, ?> p1, Path<T, ?> p2, Path<T, ?> p3, Path<T, ?> p4, Path<T, ?> p5, Path<T, ?> p6, Path<T, ?> p7, Path<T, ?> p8) {
        return query().select(p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }

    default AggWhere0<T, Object[]> select(Path<T, ?> p0, Path<T, ?> p1, Path<T, ?> p2, Path<T, ?> p3, Path<T, ?> p4, Path<T, ?> p5, Path<T, ?> p6, Path<T, ?> p7, Path<T, ?> p8, Path<T, ?> p9) {
        return query().select(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }

    default Having0<T, T> groupBy(List<? extends ExpressionHolder<T, ?>> expressions) {
        return query().groupBy(expressions);
    }

    default Having0<T, T> groupBy(Path<T, ?> path) {
        return query().groupBy(path);
    }

    default Having0<T, T> groupBy(Collection<Path<T, ?>> paths) {
        return query().groupBy(paths);
    }

    default Having0<T, T> groupBy(Path<T, ?> p0, Path<T, ?> p1) {
        return query().groupBy(p0, p1);
    }

    default Having0<T, T> groupBy(Path<T, ?> p0, Path<T, ?> p1, Path<T, ?> p2) {
        return query().groupBy(p0, p1, p2);
    }

    default Having0<T, T> groupBy(Path<T, ?> p0, Path<T, ?> p1, Path<T, ?> p2, Path<T, ?> p3) {
        return query().groupBy(p0, p1, p2, p3);
    }

    default Having0<T, T> groupBy(Path<T, ?> p0, Path<T, ?> p1, Path<T, ?> p2, Path<T, ?> p3, Path<T, ?> p4) {
        return query().groupBy(p0, p1, p2, p3, p4);
    }

    default Having0<T, T> groupBy(Path<T, ?> p0, Path<T, ?> p1, Path<T, ?> p2, Path<T, ?> p3, Path<T, ?> p4, Path<T, ?> p5) {
        return query().groupBy(p0, p1, p2, p3, p4, p5);
    }

    default OrderBy0<T, T> where(ExpressionHolder<T, Boolean> predicate) {
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

    default Collector<T> orderBy(List<? extends Order<T>> path) {
        return query().orderBy(path);
    }

    default Collector<T> orderBy(Order<T> path) {
        return query().orderBy(path);
    }

    default Collector<T> orderBy(Order<T> p0, Order<T> p1) {
        return query().orderBy(p0, p1);
    }

    default Collector<T> orderBy(Order<T> p0, Order<T> p1, Order<T> p2) {
        return query().orderBy(p0, p1, p2);
    }

    default int count() {
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

    default List<T> getList(int offset) {
        return query().getList(offset);
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

    default <R> R slice(Sliceable<T, R> sliceable) {
        return query().slice(sliceable);
    }

    default Slice<T> slice(int offset, int limit) {
        return query().slice(offset, limit);
    }
}
