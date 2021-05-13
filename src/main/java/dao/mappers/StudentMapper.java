package dao.mappers;

import model.Course;
import model.Student;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.HashSet;

public class StudentMapper implements Mapper<Student> {

    @Override
    public Student mapRow(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("student_id");
        int groupId = resultSet.getInt("group_id");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        Set<Course> courses = new HashSet<>();
        do {
            Integer courseId = resultSet.getInt("course_id");
            String courseName = resultSet.getString("course_name");
            String courseDescription = resultSet.getString("course_description");
            courses.add(new Course(courseId, courseName, courseDescription));
        } while (resultSet.next() && id == resultSet.getInt("student_id"));
        resultSet.previous();
        return new Student(id, groupId, firstName, lastName, courses);
    }

    @Override
    public void fillRow(PreparedStatement statement, Student student) throws SQLException {
        if (student.getGroupId() != null) {
            statement.setInt(1, student.getGroupId());
        } else {
            statement.setNull(1, java.sql.Types.INTEGER);
        }
        statement.setString(2, student.getFirstName());
        statement.setString(3, student.getLastName());
    }
}
