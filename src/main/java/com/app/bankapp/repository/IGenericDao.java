package com.app.bankapp.repository;

import java.io.Serializable;
import java.util.List;

public interface IGenericDao<T extends Serializable> {


    T findOne(final Long id);

    T findOne(final Integer id);


    List<T> findAll();

    void create(final T entity);

    void create(final List<T> entity);

    T update(final T entity);

    void delete(final T entity);

    void deleteById(final long entityId);

    List<T> findPaginated(int pageNo, int pageSize);

}
