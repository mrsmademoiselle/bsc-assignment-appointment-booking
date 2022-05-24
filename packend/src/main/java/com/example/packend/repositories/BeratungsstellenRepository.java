package com.example.packend.repositories;

import com.example.packend.entities.Beratungsstelle;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BeratungsstellenRepository extends CrudRepository<Beratungsstelle, String> {
    List<Beratungsstelle> findAll();

}
