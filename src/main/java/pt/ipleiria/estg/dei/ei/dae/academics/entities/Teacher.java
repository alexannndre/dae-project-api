package pt.ipleiria.estg.dei.ei.dae.academics.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Entity
@NamedQueries(value = {
        @NamedQuery(
                name = "getAllTeachers",
                query = "SELECT t FROM Teacher t ORDER BY t.name" // JPQL
        )
})
public class Teacher extends User implements Serializable {
    private String office;
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "teachers")
    private List<Subject> subjects;

    public Teacher() {
        this.subjects = new LinkedList<>();
    }

    public Teacher(String username, String password, String name, String email, String office) {
        super(username, password, name, email);
        this.office = office;
        this.subjects = new LinkedList<>();
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
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
}
