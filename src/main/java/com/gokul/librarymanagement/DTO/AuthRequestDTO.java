package com.gokul.librarymanagement.DTO;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequestDTO {
    private String userName;
    private String password;
}
