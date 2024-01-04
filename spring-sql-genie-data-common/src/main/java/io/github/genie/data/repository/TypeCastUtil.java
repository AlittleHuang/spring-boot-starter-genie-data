package io.github.genie.data.repository;

public class TypeCastUtil {
    public static <T> Class<T> cast(Class<?> resolve) {
        return doCast(resolve);
    }

    private static <T> T doCast(Object o) {
        //noinspection unchecked
        return (T) o;
    }

}
