package application;

import command.MenuContainer;
import command.FindGroupsByStudentCountCommand;
import command.FindStudentsByCourseNameCommand;
import command.AddNewStudentCommand;
import command.DeleteStudentByIdCommand;
import command.AddStudentToCourseCommand;
import command.RemoveStudentFromCourseCommand;
import command.ExitCommand;
import dao.CourseDao;
import dao.GroupDao;
import dao.StudentDao;
import dao.impl.CourseDaoImpl;
import dao.impl.GroupDaoImpl;
import dao.impl.StudentDaoImpl;
import exceptions.DaoException;
import util.ConnectionProvider;
import util.DatabaseConnection;
import util.ResourceFileReader;
import util.ScriptExecutor;
import util.PropertiesCreator;
import util.FileConverter;
import util.DataGenerator;
import util.DataAssigner;
import util.DataFiller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;


public class Main {
    public static void main(String[] args) throws SQLException, DaoException, IOException, URISyntaxException {
        ResourceFileReader reader = new ResourceFileReader();
        String sqlScript = reader.readFile("create_tables.sql");
        new ScriptExecutor().executeSqlScript(sqlScript);
        PropertiesCreator propertiesCreator = new PropertiesCreator();
        ConnectionProvider connection = new DatabaseConnection(propertiesCreator);
        CourseDao courseDao = new CourseDaoImpl(connection);
        StudentDao studentDao = new StudentDaoImpl(connection, courseDao);
        GroupDao groupDao = new GroupDaoImpl(connection, studentDao);
        FileConverter converter = new FileConverter(reader);
        List<String> coursesList = converter.convertFileToList("courses.txt");
        List<String> firstNames = converter.convertFileToList("first_names.txt");
        List<String> lastNames = converter.convertFileToList("last_names.txt");
        Random random = new Random();
        DataGenerator dataGenerator = DataGenerator.builder().random(random).build();
        DataAssigner dataAssigner = DataAssigner.builder().random(random).courseDao(courseDao).studentDao(studentDao).build();
        DataFiller dataFiller = new DataFiller(dataGenerator, dataAssigner, courseDao, groupDao, studentDao);
        dataFiller.fillDataBase(coursesList, firstNames, lastNames);
        MenuContainer menuContainer = new MenuContainer();
        menuContainer.addCommand(new FindGroupsByStudentCountCommand(groupDao));
        menuContainer.addCommand(new FindStudentsByCourseNameCommand(courseDao, studentDao));
        menuContainer.addCommand(new AddNewStudentCommand(studentDao));
        menuContainer.addCommand(new DeleteStudentByIdCommand(studentDao));
        menuContainer.addCommand(new AddStudentToCourseCommand(studentDao, courseDao));
        menuContainer.addCommand(new RemoveStudentFromCourseCommand(studentDao, courseDao));
        menuContainer.addCommand(new ExitCommand());
        menuContainer.runMenu();
    }
}