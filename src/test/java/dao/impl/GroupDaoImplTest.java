package dao.impl;

import dao.CourseDao;
import dao.GroupDao;
import exceptions.DaoException;
import model.Group;
import model.Student;
import org.junit.jupiter.api.Test;
import util.ConnectionProvider;
import util.DatabaseConnection;
import util.PropertiesCreator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class GroupDaoImplTest extends AbstractDaoTest {
    private ConnectionProvider connectionProvider = new DatabaseConnection(new PropertiesCreator());
    private final CourseDao courseDao = new CourseDaoImpl(connectionProvider);
    private final GroupDao groupDao = new GroupDaoImpl(connectionProvider, new StudentDaoImpl(connectionProvider, courseDao));


    @Test
    void add_shouldReturnNewGroup_whenAddNewGroup() throws DaoException {
        Group expected = new Group(3, "CF-33");
        Group group = new Group(null, "CF-33");
        Group actual = groupDao.add(group);
        assertEquals(expected, actual);
    }

    @Test
    void add_shouldThrowException_whenGroupsNameIsNull() {
        assertThrows(DaoException.class, () -> groupDao.add(new Group(null, null)));
    }

    @Test
    void getAll_shouldReturnAllGroups() throws DaoException {
        Set<Student> firstGroupStudents = new HashSet<>();
        firstGroupStudents.add(new Student(1, 1, "Adam", "Jonson"));
        firstGroupStudents.add(new Student(2, 1, "Tina", "Hamilton"));
        Set<Student> secondGroupStudents = new HashSet<>();
        secondGroupStudents.add(new Student(3, 2, "Melinda", "Doy"));
        List<Group> expected = new ArrayList<>();
        expected.add(new Group(1, "AB-15", firstGroupStudents));
        expected.add(new Group(2, "BD-22", secondGroupStudents));
        List<Group> actual = groupDao.getAll();
        assertEquals(expected, actual);
    }

    @Test
    void getById_shouldReturnCorrectGroupByGivenId() throws DaoException {
        Set<Student> students = new HashSet<>();
        students.add(new Student(3, 2, "Melinda", "Doy"));
        Group expected = new Group(2, "BD-22", students);
        Group actual = groupDao.getById(2).get();
        assertEquals(expected, actual);
    }

    @Test
    void getById_shouldReturnEmpty_whenGroupNotExist() throws DaoException {
        Optional<Group> actual = groupDao.getById(3);
        assertEquals(Optional.empty(), actual);
    }

    @Test
    void getAllGroups_shouldReturnGroups_whenGivenStudentCount() throws DaoException {
        Set<Student> students = new HashSet<>();
        students.add(new Student(3, 2, "Melinda", "Doy"));
        List<Group> expected = new ArrayList<>();
        expected.add(new Group(2, "BD-22", students));
        List<Group> actual = groupDao.getAllGroups(1);
        assertEquals(expected, actual);
    }

    @Test
    void getAllGroups_shouldReturnAllGroups_whenMaxStudentCount() throws DaoException {
        Set<Student> firstStudents = new HashSet<>();
        firstStudents.add(new Student(3, 2, "Melinda", "Doy"));
        Set<Student> secondStudents = new HashSet<>();
        secondStudents.add(new Student(1, 1, "Adam", "Jonson"));
        secondStudents.add(new Student(2, 1, "Tina", "Hamilton"));
        List<Group> expected = new ArrayList<>();
        expected.add(new Group(1, "AB-15", secondStudents));
        expected.add(new Group(2, "BD-22", firstStudents));
        List<Group> actual = groupDao.getAllGroups(3);
        assertEquals(expected, actual);
    }

    @Test
    void deleteById_shouldCorrectlyDeleteGroup() throws DaoException {
        groupDao.deleteById(1);
        Optional<Group> actual = groupDao.getById(1);
        Optional<Group> expected = Optional.empty();
        assertEquals(expected, actual);
    }

    @Test
    void addAll_shouldReturnGroups_whenAddedGroups() throws DaoException {
        List<Group> groups = new ArrayList<>();
        groups.add(new Group(null, "CC-33"));
        groups.add(new Group(null, "FF-44"));
        groupDao.addAll(groups);

        Set<Student> firstGroupStudents = new HashSet<>();
        firstGroupStudents.add(new Student(1, 1, "Adam", "Jonson"));
        firstGroupStudents.add(new Student(2, 1, "Tina", "Hamilton"));
        Set<Student> secondGroupStudents = new HashSet<>();
        secondGroupStudents.add(new Student(3, 2, "Melinda", "Doy"));
        Set<Student> thirdGroupStudents = new HashSet<>();
        thirdGroupStudents.add(new Student(0, 3, null, null, null));
        Set<Student> fourthGroupStudents = new HashSet<>();
        fourthGroupStudents.add(new Student(0, 4, null, null, null));

        List<Group> expected = new ArrayList<>();
        expected.add(new Group(1, "AB-15", firstGroupStudents));
        expected.add(new Group(2, "BD-22", secondGroupStudents));
        expected.add(new Group(3, "CC-33", thirdGroupStudents));
        expected.add(new Group(4, "FF-44", fourthGroupStudents));
        List<Group> actual = groupDao.getAll();
        assertEquals(expected, actual);
    }

    @Test
    void update_shouldCorrectlyUpdateGroup() throws DaoException {
        Set<Student> firstGroupStudents = new HashSet<>();
        firstGroupStudents.add(new Student(1, 1, "Adam", "Jonson"));
        firstGroupStudents.add(new Student(2, 1, "Tina", "Hamilton"));
        Group expected = new Group(1, "CC-33", firstGroupStudents);
        groupDao.update(expected);
        Group actual = groupDao.getById(1).get();
        assertEquals(expected, actual);
    }

    @Test
    void update_shouldThrowException_whenGroupsNameIsNull() {
        assertThrows(DaoException.class, () -> groupDao.update(new Group(1, null)));
    }
}