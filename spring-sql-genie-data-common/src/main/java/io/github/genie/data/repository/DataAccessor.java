package io.github.genie.data.repository;

import io.github.genie.sql.api.Query;
import io.github.genie.sql.api.Update;
import io.github.genie.sql.builder.meta.Metamodel;

public interface DataAccessor {

    Query reader();

    Update writer();

    Metamodel metamodel();

}
