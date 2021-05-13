package dao;

import exceptions.DaoException;
import model.Student;

import java.util.List;

public interface StudentDao extends GenericDao<Student, Integer> {

    List<Student> getByCourseName(String courseName) throws DaoException;
}