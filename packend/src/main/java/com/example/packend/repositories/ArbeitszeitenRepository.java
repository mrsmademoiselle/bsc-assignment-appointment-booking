package com.example.packend.repositories;

import com.example.packend.entities.Arbeitszeiten;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArbeitszeitenRepository extends JpaRepository<Arbeitszeiten, String> {
    Optional<Arbeitszeiten> findByMitarbeiterId(String id);
}
