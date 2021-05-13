package dao.impl;

import dao.GenericDao;
import dao.mappers.Mapper;
import exceptions.DaoException;
import util.ConnectionProvider;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractDao<T> implements GenericDao<T, Integer> {
    protected Mapper<T> mapper;
    protected ConnectionProvider connectionProvider;

    public AbstractDao(Mapper<T> mapper, ConnectionProvider connectionProvider) {
        this.mapper = mapper;
        this.connectionProvider = connectionProvider;
    }

    protected PreparedStatement getStatement(Connection connection, String query) throws SQLException {
        return connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
    }

    @Override
    public List<T> getAll() throws DaoException {
        List<T> entities = new ArrayList<>();
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = getStatement(connection, getQueryToGetAll()))  {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                entities.add(mapper.mapRow(resultSet));
            }
            return entities;
        } catch (SQLException e) {
            throw new DaoException("Couldn't get all " + entities, e);
        }
    }

    @Override
    public Optional<T> getById(Integer id) throws DaoException {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = getStatement(connection, getQueryToGetById())) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapper.mapRow(resultSet));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DaoException("Couldn't get with id = " + id, e);
        }
    }

    @Override
    public boolean deleteById(Integer id) throws DaoException {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(getQueryToDeleteById())) {
            statement.setInt(1, id);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new DaoException("Couldn't delete by id " + id, e);
        }
    }

    @Override
    public void addAll(List<T> entities) throws DaoException {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(getQueryToAddAll())) {
            for (T entity : entities) {
                mapper.fillRow(statement, entity);
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            throw new DaoException("Couldn't add list ", e);
        }
    }

    protected abstract String getQueryToGetAll();

    protected abstract String getQueryToGetById();

    protected abstract String getQueryToDeleteById();

    protected abstract String getQueryToAddAll();
}