package command;

import dao.StudentDao;
import exceptions.DaoException;
import model.Student;

import java.util.Scanner;

public class DeleteStudentByIdCommand implements Command {
    private static final String DESCRIPTION = "Delete student by STUDENT_ID.";
    private StudentDao studentDao;

    public DeleteStudentByIdCommand(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public void doCommand() throws DaoException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter student's id: ");
        int studentId = scanner.nextInt();
        Student student = studentDao.getById(studentId).get();
        studentDao.deleteById(studentId);
        System.out.println(student.toString() + " was successfully deleted.");
    }
}
