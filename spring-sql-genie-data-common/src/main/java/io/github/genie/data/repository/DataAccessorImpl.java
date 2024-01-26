package io.github.genie.data.repository;

import io.github.genie.sql.api.Query;
import io.github.genie.sql.api.Update;
import io.github.genie.sql.builder.meta.Metamodel;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class DataAccessorImpl implements DataAccessor {
    final Query reader;
    final Update writer;
    final Metamodel metamodel;
}
