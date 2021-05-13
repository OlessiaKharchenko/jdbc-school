package command;

import dao.CourseDao;
import dao.StudentDao;
import exceptions.DaoException;

import java.util.Scanner;

public class FindStudentsByCourseNameCommand implements Command {
    private static final String DESCRIPTION = "Find all students related to course with given name.";
    private CourseDao courseDao;
    private StudentDao studentDao;

    public FindStudentsByCourseNameCommand(CourseDao courseDao, StudentDao studentDao) {
        this.courseDao = courseDao;
        this.studentDao = studentDao;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public void doCommand() throws DaoException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter course's name, select from the next list: ");
        courseDao.getAll().stream()
                .map(course -> String.format("%d. %s", course.getId(), course.getName()))
                .forEach(System.out::println);
        String courseName = scanner.nextLine();
        studentDao.getByCourseName(courseName).stream()
                .map(student -> String.format("%d. %s %s", student.getId(), student.getFirstName(), student.getLastName()))
                .forEach(System.out::println);
    }
}