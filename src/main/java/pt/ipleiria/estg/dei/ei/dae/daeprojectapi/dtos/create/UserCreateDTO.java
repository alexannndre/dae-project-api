package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.create;

import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.UserDTO;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.User;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

public class UserCreateDTO extends UserDTO {
    @NotNull
    private String password;

    public UserCreateDTO() {
    }

    public UserCreateDTO(String vat, String name, String email, String password) {
        super(vat, name, email);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static UserCreateDTO from(User user) {
        return new UserCreateDTO(
                user.getVat(),
                user.getName(),
                user.getEmail(),
                user.getPassword()
        );
    }

    public static List<UserDTO> from(List<User> users) {
        return users.stream().map(UserCreateDTO::from).collect(Collectors.toList());
    }
}
