package dao.mappers;

import model.Course;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CourseMapper implements Mapper<Course> {

    @Override
    public Course mapRow(ResultSet resultSet) throws SQLException {
        Integer id = resultSet.getInt("course_id");
        String name = resultSet.getString("course_name");
        String description = resultSet.getString("course_description");
        return new Course(id, name, description);
    }

    @Override
    public void fillRow(PreparedStatement statement, Course course) throws SQLException {
        statement.setString(1, course.getName());
        statement.setString(2, course.getDescription());
    }
}