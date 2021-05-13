package dao.mappers;

import model.Group;
import model.Student;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.HashSet;

public class GroupMapper implements Mapper<Group> {

    @Override
    public Group mapRow(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("group_id");
        String name = resultSet.getString("group_name");
        Set<Student> students = new HashSet<>();
        do {
            Integer studentId = resultSet.getInt("student_id");
            Integer groupId = resultSet.getInt("group_id");
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            students.add(new Student(studentId, groupId, firstName, lastName));
        } while (resultSet.next() && id == resultSet.getInt("group_id"));
        resultSet.previous();
        return new Group(id, name, students);
    }

    @Override
    public void fillRow(PreparedStatement statement, Group group) throws SQLException {
        statement.setString(1, group.getName());
    }
}