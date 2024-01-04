package io.github.genie.data.repository;

import java.io.Serializable;

public interface Persistable<ID extends Serializable> {

    ID getId();

}
