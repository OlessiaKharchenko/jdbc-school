package dao;

import exceptions.DaoException;
import model.Group;

import java.util.List;

public interface GroupDao extends GenericDao<Group, Integer> {

    List<Group> getAllGroups(int studentCount) throws DaoException;
}