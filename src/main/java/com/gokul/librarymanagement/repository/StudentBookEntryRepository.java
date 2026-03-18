package com.gokul.librarymanagement.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StudentBookEntryRepository extends JpaRepository<Record, UUID> {
}