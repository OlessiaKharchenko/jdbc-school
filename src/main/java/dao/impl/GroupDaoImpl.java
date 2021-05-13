package dao.impl;

import dao.GroupDao;
import dao.StudentDao;
import dao.mappers.GroupMapper;
import exceptions.DaoException;
import model.Group;
import model.Student;
import util.ConnectionProvider;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GroupDaoImpl extends AbstractDao<Group> implements GroupDao {

    private StudentDao studentDao;

    public GroupDaoImpl(ConnectionProvider connectionProvider, StudentDao studentDao) {
        super(new GroupMapper(), connectionProvider);
        this.studentDao = studentDao;
    }

    @Override
    public Group add(Group group) throws DaoException {
        String query = "INSERT INTO groups (group_name) VALUES (?);";
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            fillRow(statement, group);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                group.setId(resultSet.getInt(1));
            }
            return group;
        } catch (SQLException e) {
            throw new DaoException("Couldn't add group " + group, e);
        }
    }

    @Override
    public void addAll(List<Group> groups) throws DaoException {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(getQueryToAddAll())) {
            for (Group group : groups) {
                fillRow(statement, group);
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            throw new DaoException("Couldn't add list ", e);
        }
    }

    @Override
    public List<Group> getAllGroups(int studentCount) throws DaoException {
        String query = "SELECT g.group_id, g.group_name, s.student_id, s.first_name, s.last_name FROM groups g JOIN students s ON " +
                "g.group_id = s.group_id WHERE g.group_id IN (SELECT g.group_id FROM groups g JOIN students s ON g.group_id = s.group_id " +
                "GROUP BY g.group_id, g.group_name HAVING COUNT(*) <= ?) ORDER BY group_id;";
        List<Group> groups = new ArrayList<>();
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = getStatement(connection, query)) {
            statement.setInt(1, studentCount);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                groups.add(mapper.mapRow(resultSet));
            }
            return groups;
        } catch (SQLException e) {
            throw new DaoException("Couldn't get all groups with student count less or equals " + studentCount, e);
        }
    }

    @Override
    public void update(Group group) throws DaoException {
        String query = "UPDATE groups SET group_name= ? WHERE group_id = ?;";
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, group.getName());
            statement.setInt(2, group.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Couldn't update group " + group, e);
        }
    }

    private void fillRow(PreparedStatement statement, Group group) throws SQLException, DaoException {
        mapper.fillRow(statement, group);
        if (group.getStudents() != null) {
            for (Student student : group.getStudents()) {
                if (student.getId() != null) {
                    if (student.getGroupId() == 0) {
                        student.setGroupId(group.getId());
                        studentDao.update(student);
                    }
                } else {
                    student.setGroupId(group.getId());
                    studentDao.add(student);
                }
            }
        }
    }

    @Override
    protected String getQueryToGetAll() {
        return "SELECT g.group_id, group_name, s.student_id, s.first_name, s.last_name FROM groups g " +
                "LEFT JOIN students s ON g.group_id = s.group_id ORDER BY g.group_id, s.student_id;";
    }

    @Override
    protected String getQueryToGetById() {
        return "SELECT g.group_id, group_name, s.student_id, s.first_name, s.last_name FROM groups g " +
                "LEFT JOIN students s ON g.group_id = s.group_id WHERE g.group_id = ? ORDER BY g.group_id, s.student_id;";
    }

    @Override
    protected String getQueryToDeleteById() {
        return "DELETE FROM groups WHERE group_id = ?;";
    }

    @Override
    protected String getQueryToAddAll() {
        return "INSERT INTO groups (group_name) VALUES (?);";
    }
}

