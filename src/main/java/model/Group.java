package model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class Group {
    private Integer id;
    private String name;
    private Set<Student> students;

    public Group(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}