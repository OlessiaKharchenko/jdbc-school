package model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class Student {
    private Integer id;
    private Integer groupId;
    private String firstName;
    private String lastName;
    private Set<Course> courses;

    public Student(Integer id, Integer groupId, String firstName, String lastName) {
        this.id = id;
        this.groupId = groupId;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}