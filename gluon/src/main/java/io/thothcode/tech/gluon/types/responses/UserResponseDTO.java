package io.thothcode.tech.gluon.types.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.thothcode.tech.gluon.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDTO implements Serializable {

    private String id;
    private String username;
    private String firstname;
    private String lastname;
    private Boolean enabled;
    private String telephone;
    private String role;
    private String email;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    private LocalDateTime createdAt;

    public UserResponseDTO(UserEntity user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.firstname = user.getFirstname();
        this.lastname = user.getLastname();
        this.role = user.getRole().toString();
        this.email = user.getEmail();
        this.telephone = user.getTelephone();
        this.enabled = user.getEnabled();
        this.createdAt = user.getCreatedAt();
    }


}
