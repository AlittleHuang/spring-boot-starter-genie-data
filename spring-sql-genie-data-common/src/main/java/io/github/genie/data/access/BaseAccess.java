package io.github.genie.data.access;

import io.github.genie.sql.api.Query.Select;
import io.github.genie.sql.api.Updater;

public interface BaseAccess<T> extends Select<T>, Updater<T> {
}
