package com.example.packend.repositories;

import com.example.packend.entities.Abwesenheit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbwesenheitRepository extends CrudRepository<Abwesenheit, String> {
}
