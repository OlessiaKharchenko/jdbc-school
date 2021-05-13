package util;

import model.Course;
import model.Group;
import model.Student;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;


class DataGeneratorTest {

    private Random random = new Random(1);

    @Test
    void generateGroups_shouldCorrectlyGenerateGroups() {
        DataGenerator dataGenerator = DataGenerator.builder().groupNumber(2).random(random).build();
        List<Group> expected = Arrays.asList(new Group(null, "RA-73"), new Group(null, "MY-46"));
        List<Group> actual = dataGenerator.generateGroups();
        assertEquals(expected, actual);
    }

    @Test
    void generateCourses_shouldCorrectlyGenerateCourses() {
        DataGenerator dataGenerator = DataGenerator.builder().random(random).build();
        List<String> coursesList = Arrays.asList("History_The study of the past.",
                "Biology_The study of life and living organisms.");
        List<Course> expected = Arrays.asList(new Course("History", "The study of the past."),
                new Course("Biology", "The study of life and living organisms."));
        assertEquals(expected, dataGenerator.generateCourses(coursesList));
    }

    @Test
    void generateStudents_shouldCorrectlyGenerateStudents() {
        DataGenerator dataGenerator = DataGenerator.builder().groupNumber(2).studentNumber(2).random(random).build();
        List<String> firstNames = Arrays.asList("George", "Sarah", "Daniel", "James", "Megan");
        List<String> lastNames = Arrays.asList("Anderson", "Green", "Harris", "Cook", "Watson");
        List<Student> expected = Arrays.asList(new Student(null, null, "George", "Cook"),
                new Student(null, null, "Daniel", "Cook"));
        assertEquals(expected, dataGenerator.generateStudents(firstNames, lastNames));
    }
}