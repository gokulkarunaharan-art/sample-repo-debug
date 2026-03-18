package com.gokul.librarymanagement.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;


@Getter
@Setter
@Table(name="students")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Student {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    private String email;

    private String phoneNumber;
}
