package io.thothcode.tech.gluon.types.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponseDTO implements Serializable {

    boolean success;
    String message;
    Integer errorCode;

    public BaseResponseDTO() {
        setSuccess(true);
    }

    public BaseResponseDTO(boolean success, String message) {
        setSuccess(success);
        setMessage(message);
    }

}
