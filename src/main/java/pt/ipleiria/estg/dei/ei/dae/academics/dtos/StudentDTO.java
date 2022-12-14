package pt.ipleiria.estg.dei.ei.dae.academics.dtos;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class StudentDTO implements Serializable {
    private String username, password, name, email, courseName;
    private long courseCode;
    private List<SubjectDTO> subjects;
    private List<DocumentDTO> documents;

    public StudentDTO() {
        this.subjects = new LinkedList<>();
        this.documents = new LinkedList<>();
    }

    public StudentDTO(String username, String password, String name, String email, String courseName, long courseCode) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.subjects = new LinkedList<>();
        this.documents = new LinkedList<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public long getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(long courseCode) {
        this.courseCode = courseCode;
    }

    public List<SubjectDTO> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<SubjectDTO> subjects) {
        this.subjects = subjects;
    }
}
