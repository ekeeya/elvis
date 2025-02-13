package io.thothcode.tech.gluon.types.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.thothcode.tech.gluon.entities.UserEntity;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRequestDTO implements Serializable {

    private String id;// for updates
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String telephone;
    private Boolean enabled = true;
    private String email;
    private UserEntity.ROLES role;
}
