package com.gokul.librarymanagement.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @NotBlank(message = "name should not be blank")
    private String name;

    @NotBlank(message = "email should not be blank")
    @Email(message = "email should be valid")
    private String email;

    @NotBlank(message = "phone number should not be blank")
    private String phoneNumber;

}
