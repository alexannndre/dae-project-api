package pt.ipleiria.estg.dei.ei.dae.daeproject.entities;

import pt.ipleiria.estg.dei.ei.dae.academics.entities.Versionable;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class User extends Versionable implements Serializable {
    @Id
    private int id;

    private String name;
    @NotNull
    private String password;
    @Email
    @NotNull
    private String email;

    public User() {
    }

    public User(int id, String name, String email, String password) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

}
