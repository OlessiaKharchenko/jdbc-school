package command;

import dao.GroupDao;
import exceptions.DaoException;

import java.util.Scanner;

public class FindGroupsByStudentCountCommand implements Command {
    private static final String DESCRIPTION = "Find all groups with less or equals student count.";
    private GroupDao groupDao;

    public FindGroupsByStudentCountCommand(GroupDao groupDao) {
        this.groupDao = groupDao;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public void doCommand() throws DaoException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter student count from 10 to 30: ");
        int studentCount = scanner.nextInt();
        groupDao.getAllGroups(studentCount).stream()
                .map(group -> String.format("%s - %d", group.getName(), group.getStudents().size()))
                .forEach(System.out::println);
    }
}