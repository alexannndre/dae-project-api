package pt.ipleiria.estg.dei.ei.dae.daeproject.entities;


import pt.ipleiria.estg.dei.ei.dae.academics.entities.Course;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@NamedQueries(value = {
        @NamedQuery(
                name = "getAllClients",
                query = "SELECT c FROM Client c ORDER BY c.name" // JPQL
        )
})
public class Client extends User implements Serializable {
    @ManyToOne
    @JoinColumn(name = "course_code")
    @NotNull
    private Course course;

    public Client() {
    }

    public Client(String username, String password, String name, String email, Course course) {
        super(username, password, name, email);
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
