package com.gokul.librarymanagement.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @NotBlank(message = "Title should not be blank")
    private String title;

    @NotBlank(message = "author should not be blank")
    private String author;

    @NotNull(message = "totalCopies should not be null")
    private Integer totalCopies;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer availableCopies;

}
