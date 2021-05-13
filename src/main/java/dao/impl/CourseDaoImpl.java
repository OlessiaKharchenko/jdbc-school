package dao.impl;

import dao.CourseDao;
import dao.mappers.CourseMapper;
import exceptions.DaoException;
import model.Course;
import model.Student;
import util.ConnectionProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class CourseDaoImpl extends AbstractDao<Course> implements CourseDao {

    public CourseDaoImpl(ConnectionProvider connectionProvider) {
        super(new CourseMapper(), connectionProvider);
    }

    @Override
    public Course add(Course course) throws DaoException {
        String query = "INSERT INTO courses (course_name, course_description) VALUES (?, ?);";
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            mapper.fillRow(statement, course);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                course.setId(resultSet.getInt(1));
            }
            return course;
        } catch (SQLException e) {
            throw new DaoException("Couldn't add course " + course, e);
        }
    }

    @Override
    public void addStudentToCourse(Student student, Course course) throws DaoException {
        String query = "INSERT INTO students_courses (student_id, course_id) VALUES (?, ?);";
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, student.getId());
            statement.setInt(2, course.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Couldn't add student " + student + " to course " + course, e);
        }
    }

    @Override
    public boolean hasStudent(Student student, Course course) throws DaoException {
        String query = "SELECT COUNT(*) <> 0 FROM students_courses WHERE student_id = ? AND course_id = ?;";
        boolean relation = false;
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, student.getId());
            statement.setInt(2, course.getId());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                relation = resultSet.getBoolean(1);
            }
            return relation;
        } catch (SQLException e) {
            throw new DaoException("Couldn't check relation between student " + student + " and course  " + course, e);
        }
    }

    @Override
    public void removeStudentFromCourse(Student student, Course course) throws DaoException {
        String query = "DELETE FROM students_courses WHERE student_id = ? AND course_id = ?;";
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, student.getId());
            statement.setInt(2, course.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Couldn't remove student " + student + " from course " + course, e);
        }
    }

    @Override
    public void update(Course course) throws DaoException {
        String query = "UPDATE courses SET course_name = ?, course_description = ? WHERE course_id = ?;";
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            mapper.fillRow(statement, course);
            statement.setInt(3, course.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Couldn't update course " + course, e);
        }
    }

    @Override
    protected String getQueryToGetAll() {
        return "SELECT * FROM courses;";
    }

    @Override
    protected String getQueryToGetById() {
        return "SELECT * FROM courses WHERE course_id = ?;";
    }

    @Override
    protected String getQueryToDeleteById() {
        return "DELETE FROM courses WHERE course_id = ?;";
    }

    @Override
    protected String getQueryToAddAll() {
        return "INSERT INTO courses (course_name, course_description) VALUES (?, ?);";
    }
}
