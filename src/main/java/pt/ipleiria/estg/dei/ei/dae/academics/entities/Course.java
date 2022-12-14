package pt.ipleiria.estg.dei.ei.dae.academics.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(
        name = "courses",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name"})
)
@NamedQueries(value = {
        @NamedQuery(
                name = "getAllCourses",
                query = "SELECT c FROM Course c ORDER BY c.name"
        )
})
public class Course extends Versionable implements Serializable {
    @Id
    long code;
    String name;
    @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE)
    List<Student> students;
    @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE)
    List<Subject> subjects;

    public Course() {
        this.students = new LinkedList<>();
        this.subjects = new LinkedList<>();
    }

    public Course(long code, String name) {
        this.code = code;
        this.name = name;
        students = new LinkedList<>();
        subjects = new LinkedList<>();
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
        return new ArrayList<>(students);
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void removeStudent(Student student) {
        students.remove(student);
    }

    public List<Subject> getSubjects() {
        return new ArrayList<>(subjects);
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    public void addSubject(Subject subject) {
        subjects.add(subject);
    }

    public void removeSubject(Subject subject) {
        subjects.remove(subject);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return code == course.code && Objects.equals(name, course.name) && Objects.equals(students, course.students) && Objects.equals(subjects, course.subjects);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name, students, subjects);
    }
}
