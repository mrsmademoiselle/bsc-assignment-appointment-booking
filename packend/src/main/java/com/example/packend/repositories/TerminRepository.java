package com.example.packend.repositories;

import com.example.packend.entities.Termin;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TerminRepository extends CrudRepository<Termin, String> {
    List<Termin> findAll();
}
