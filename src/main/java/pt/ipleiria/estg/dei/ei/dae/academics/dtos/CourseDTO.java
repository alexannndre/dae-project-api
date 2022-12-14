package pt.ipleiria.estg.dei.ei.dae.academics.dtos;

import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class CourseDTO implements Serializable {
    private long code;
    private String name;
    private List<Student> students;

    public CourseDTO() {
    }

    public CourseDTO(long code, String name) {
        this.code = code;
        this.name = name;
        this.students = new LinkedList<>();
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
