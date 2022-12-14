package pt.ipleiria.estg.dei.ei.dae.academics.entities;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Entity
@NamedQueries(value = {
        @NamedQuery(
                name = "getAllStudents",
                query = "SELECT s FROM Student s ORDER BY s.name" // JPQL
        )
})
public class Student extends User implements Serializable {
    @ManyToOne
    @JoinColumn(name = "course_code")
    @NotNull
    private Course course;
    @ManyToMany(mappedBy = "students")
    private List<Subject> subjects;

    @OneToMany(mappedBy = "student")
    private List<Document> documents;

    public Student() {
        this.subjects = new LinkedList<>();
    }

    public Student(String username, String password, String name, String email, Course course) {
        super(username, password, name, email);
        this.course = course;
        this.subjects = new LinkedList<>();
        this.documents = new LinkedList<>();
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<Subject> getSubjects() {
        return new LinkedList<>(subjects);
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

    public List<Document> getDocuments() {
        return new LinkedList<>(documents);
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public void addDocument(Document document) {
        documents.add(document);
    }

    public void removeDocument(Document document) {
        documents.remove(document);
    }
}
