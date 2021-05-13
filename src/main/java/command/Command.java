package command;

import exceptions.DaoException;

import java.sql.SQLException;

public interface Command {
    void doCommand() throws SQLException, DaoException;

    String getDescription();
}