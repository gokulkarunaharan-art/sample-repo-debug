package com.gokul.librarymanagement.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {

    private UUID id;

    private String name;

    private String email;

    private String phoneNumber;
}
