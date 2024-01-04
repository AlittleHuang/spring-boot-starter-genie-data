package io.github.genie.data.repository;

import io.github.genie.sql.api.Query.Select0;
import lombok.experimental.Delegate;

public interface DataReader<T> {

    @Delegate
    Select0<T, T> query();

}
