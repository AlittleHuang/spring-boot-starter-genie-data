package io.github.genie.data.repository;

import io.github.genie.sql.api.Query;
import io.github.genie.sql.api.Update;

public interface DataAccessor {

    Query reader();

    Update writer();

}
