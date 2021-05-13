package dao;

import exceptions.DaoException;
import model.Course;
import model.Student;

public interface CourseDao extends GenericDao<Course, Integer> {

    void addStudentToCourse(Student student, Course course) throws DaoException;

    void removeStudentFromCourse(Student student, Course course) throws DaoException;

    boolean hasStudent(Student student, Course course) throws DaoException;
}