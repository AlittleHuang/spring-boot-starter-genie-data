package io.github.genie.data.repository;


import java.io.Serializable;

public interface Repository<T extends Persistable<ID>, ID extends Serializable>
        extends PersistableReader<T, ID>, Writer<T> {

}
