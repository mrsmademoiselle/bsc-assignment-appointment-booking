package com.example.packend.repositories;


import com.example.packend.entities.Mitarbeiter;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<Mitarbeiter, Long> {
    List<Mitarbeiter> findAll();

    Optional<Mitarbeiter> findByUsername(String username);

    boolean existsByUsername(String username);
}
