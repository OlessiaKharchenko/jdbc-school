package dao.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import util.ResourceFileReader;
import util.ScriptExecutor;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;

public class AbstractDaoTest {
    static ResourceFileReader reader = new ResourceFileReader();
    static ScriptExecutor scriptExecutor = new ScriptExecutor();

    @BeforeAll
    public static void createTables() throws SQLException, IOException, URISyntaxException {
        String sqlScript = reader.readFile("create_tables.sql");
        scriptExecutor.executeSqlScript(sqlScript);
    }

    @BeforeEach
    void fillTables() throws SQLException, IOException, URISyntaxException {
        String sqlScript = reader.readFile("fill_tables.sql");
        scriptExecutor.executeSqlScript(sqlScript);
    }

    @AfterEach
    void clearTables() throws SQLException, IOException, URISyntaxException {
        String sqlScript = reader.readFile("clear_tables.sql");
        scriptExecutor.executeSqlScript(sqlScript);
    }
}