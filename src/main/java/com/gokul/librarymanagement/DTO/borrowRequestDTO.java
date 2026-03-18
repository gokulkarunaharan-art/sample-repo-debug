package com.gokul.librarymanagement.DTO;


import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
public class borrowRequestDTO {
    private UUID bookId;
    private UUID studentID;
}
