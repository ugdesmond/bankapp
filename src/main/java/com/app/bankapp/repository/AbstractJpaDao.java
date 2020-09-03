package com.app.bankapp.repository;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

@Transactional
public class AbstractJpaDao<T extends Serializable> implements IGenericDao<T> {
    private Class<? extends T> clazz;

    @SuppressWarnings("unchecked")
    public AbstractJpaDao() {
        Type t = getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) t;
        clazz = (Class<? extends T>) pt.getActualTypeArguments()[0];
    }

    //to figure it out is not easy
    @Autowired
    EntityManager entityManager;


    public Session getCurrentSession() {
        Session session = entityManager.unwrap(Session.class);
        return session;
    }


    @Override
    public T findOne(Long id) {
        return entityManager.find(clazz, id);
    }

    public T findOne(Integer id) {
        return entityManager.find(clazz, id);
    }

    @SuppressWarnings("unchecked")
    public List<T> findAll() {

        return entityManager.createQuery("FROM " + clazz.getName() + " entity ORDER BY entity.id ASC")
                .getResultList();
    }

    public void create(T entity) {
        entityManager.persist(entity);
    }

    public void create(List<T> list) {
        for (T entity : list) {
            entityManager.persist(entity);
        }
    }

    public T update(T entity) {
        entityManager.merge(entity);
        return entity;
    }

    public void delete(T entity) {
        entityManager.remove(entity);
    }


    public void deleteById(long entityId) {
        T entity = findOne(entityId);
        delete(entity);
    }

    @Override
    public List<T> findPaginated(int pageNo, int pageSize) {
        return null;
    }

}
