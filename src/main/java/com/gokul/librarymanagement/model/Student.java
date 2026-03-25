package com.gokul.librarymanagement.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;


@Getter
@Setter
@Table(name="students")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Student {

    @Version
    private Integer version;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String email;

    private String phoneNumber;

    @OneToMany(mappedBy = "student")
    private Set<StudentBookEntry> studentBookEntries;
}
