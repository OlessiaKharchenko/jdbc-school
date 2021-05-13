package dao.impl;

import dao.CourseDao;
import dao.StudentDao;
import dao.mappers.StudentMapper;
import exceptions.DaoException;
import model.Course;
import model.Student;
import util.ConnectionProvider;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDaoImpl extends AbstractDao<Student> implements StudentDao {

    private CourseDao courseDao;

    public StudentDaoImpl(ConnectionProvider connectionProvider, CourseDao courseDao) {
        super(new StudentMapper(), connectionProvider);
        this.courseDao = courseDao;
    }

    @Override
    public Student add(Student student) throws DaoException {
        String query = "INSERT INTO students (group_id, first_name, last_name) VALUES (?, ?, ?);";
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            fillRow(statement, student);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                student.setId(resultSet.getInt(1));
            }
            return student;
        } catch (SQLException e) {
            throw new DaoException("Couldn't add student " + student, e);
        }
    }

    @Override
    public void addAll(List<Student> students) throws DaoException {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(getQueryToAddAll())) {
            for (Student student : students) {
                fillRow(statement, student);
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            throw new DaoException("Couldn't add list ", e);
        }
    }

    @Override
    public List<Student> getByCourseName(String courseName) throws DaoException {
        List<Student> studentsInCurrentCourse = new ArrayList<>();
        String query = "SELECT s.student_id, group_id, first_name, last_name, c.course_id, course_name, course_description FROM students " +
                "s JOIN students_courses sc ON s.student_id = sc.student_id JOIN courses c ON c.course_id = sc.course_id " +
                "WHERE s.student_id IN (SELECT s.student_id FROM students s JOIN students_courses sc ON s.student_id = sc.student_id " +
                "JOIN courses c ON sc.course_id = c.course_id  WHERE c.course_name = ?) " +
                "ORDER BY s.student_id, c.course_id;";
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = getStatement(connection, query)) {
            statement.setString(1, courseName);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                studentsInCurrentCourse.add(mapper.mapRow(resultSet));
            }
            return studentsInCurrentCourse;
        } catch (SQLException e) {
            throw new DaoException("Couldn't get students by course name " + courseName, e);
        }
    }

    @Override
    public void update(Student student) throws DaoException {
        String query = "UPDATE students SET group_id = ?, first_name = ?, last_name = ? WHERE student_id = ?;";
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            mapper.fillRow(statement, student);
            statement.setInt(4, student.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Couldn't update student " + student, e);
        }
    }

    private void fillRow(PreparedStatement statement, Student student) throws SQLException, DaoException {
        mapper.fillRow(statement, student);
        if (student.getCourses() != null) {
            for (Course course : student.getCourses()) {
                if (course.getId() != null) {
                    if (!courseDao.hasStudent(student, course)) {
                        courseDao.addStudentToCourse(student, course);
                    }
                } else {
                    courseDao.add(course);
                    courseDao.addStudentToCourse(student, course);
                }
            }
        }
    }

    @Override
    protected String getQueryToGetAll() {
        return "SELECT s.student_id, group_id, first_name, last_name, c.course_id, course_name, course_description " +
                "FROM students s LEFT JOIN students_courses sc ON s.student_id = sc.student_id LEFT JOIN courses c ON c.course_id " +
                "= sc.course_id ORDER BY s.student_id, c.course_id;";
    }

    @Override
    protected String getQueryToGetById() {
        return "SELECT s.student_id, group_id, first_name, last_name, c.course_id, course_name, course_description " +
                "FROM students s LEFT JOIN students_courses sc ON s.student_id = sc.student_id LEFT JOIN courses c ON c.course_id " +
                "= sc.course_id  WHERE  s.student_id = ? ORDER BY s.student_id, c.course_id;";
    }

    @Override
    protected String getQueryToDeleteById() {
        return "DELETE FROM students WHERE student_id = ?;";
    }

    @Override
    protected String getQueryToAddAll() {
        return "INSERT INTO students (group_id, first_name, last_name) VALUES (?, ?, ?);";
    }
}
