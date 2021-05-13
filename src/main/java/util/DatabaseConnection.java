package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection implements ConnectionProvider {
    private PropertiesCreator propertiesCreator;

    public DatabaseConnection(PropertiesCreator propertiesCreator) {
        this.propertiesCreator = propertiesCreator;
    }

    @Override
    public Connection getConnection() throws SQLException {
        Properties properties = propertiesCreator.getProperties();
        try {
            return DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("user"),
                    properties.getProperty("password"));
        } catch (SQLException e) {
            throw new SQLException("Can't connect to DB" + e);
        }
    }
}