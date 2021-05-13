package dao;

import exceptions.DaoException;

import java.util.List;
import java.util.Optional;

public interface GenericDao<T, ID> {

    T add(T entity) throws DaoException;

    List<T> getAll() throws DaoException;

    Optional<T> getById(ID id) throws DaoException;

    boolean deleteById(ID id) throws DaoException;

    void addAll(List<T> list) throws DaoException;

    void update(T entity) throws DaoException;
}