package util;

import lombok.Builder;
import model.Course;
import model.Group;
import model.Student;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Builder
public class DataGenerator {
    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMBERS = "0123456789";
    @Builder.Default
    private final int groupNumber = 10;
    @Builder.Default
    private int studentNumber = 200;
    @Builder.Default
    private int stringLength = 2;
    @Builder.Default
    private Random random = new Random();

    public List<Group> generateGroups() {
        return IntStream.range(0, groupNumber)
                .mapToObj(index -> new Group(null, String.format("%s-%s", getRandomString(LETTERS), getRandomString(NUMBERS))))
                .collect(Collectors.toList());
    }

    public List<Course> generateCourses(List<String> coursesList) {
        return coursesList.stream()
                .map(line -> line.split("_"))
                .map(array -> new Course(array[0], array[1]))
                .collect(Collectors.toList());
    }

    public List<Student> generateStudents(List<String> firstNames, List<String> lastNames) {
        return Stream.generate(() -> new Student(null, null, firstNames.get(random.nextInt(firstNames.size())),
                lastNames.get(random.nextInt(lastNames.size()))))
                .limit(studentNumber)
                .collect(Collectors.toList());
    }

    private String getRandomString(String string) {
        StringBuilder builder = new StringBuilder(stringLength);
        for (int i = 0; i < stringLength; i++) {
            int randomCharIndex = random.nextInt(string.length());
            builder.append(string.charAt(randomCharIndex));
        }
        return builder.toString();
    }
}