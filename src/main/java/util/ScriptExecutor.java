package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ScriptExecutor {

    public void executeSqlScript(String sqlScript) throws SQLException {
        PropertiesCreator propertiesCreator = new PropertiesCreator();
        ConnectionProvider connectionProvider = new DatabaseConnection(propertiesCreator);
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlScript);) {
            statement.execute();
        }
    }
}