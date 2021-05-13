package command;

import dao.StudentDao;
import exceptions.DaoException;
import model.Student;

import java.util.Scanner;

public class AddNewStudentCommand implements Command {
    private static final String DESCRIPTION = "Add new student.";
    private StudentDao studentDao;

    public AddNewStudentCommand(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public void doCommand() throws DaoException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter student's first name: ");
        String firstName = scanner.nextLine();
        if (firstName.isEmpty()) {
            throw new IllegalArgumentException("The student's first name can't be empty");
        }
        System.out.println("Enter student's last name: ");
        String lastName = scanner.nextLine();
        if (lastName.isEmpty()) {
            throw new IllegalArgumentException("The student's last name can't be empty");
        }
        System.out.println("Enter student's group's id: ");
        int groupId = scanner.nextInt();
        studentDao.add(new Student(null, groupId, firstName, lastName));
        System.out.println("The student was successfully added.");
    }
}
