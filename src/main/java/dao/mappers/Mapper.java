package dao.mappers;

import exceptions.DaoException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface Mapper<T> {
    T mapRow(ResultSet resultSet) throws SQLException;

    void fillRow(PreparedStatement statement, T entity) throws SQLException, DaoException;
}