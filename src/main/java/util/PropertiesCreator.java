package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesCreator {

    public Properties getProperties() {
        Properties properties = null;
        try (InputStream inputStream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream("connection.properties")) {
            properties = new Properties();
            properties.load(inputStream);
            properties.setProperty("url", properties.getProperty("url"));
            properties.setProperty("user", properties.getProperty("user"));
            properties.setProperty("password", properties.getProperty("password"));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return properties;
    }
}
