package com.example.packend.repositories;

import com.example.packend.entities.Beratungsstelle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BeratungsstellenRepository extends JpaRepository<Beratungsstelle, Long> {
    List<Beratungsstelle> findAll();

    List<Beratungsstelle> findAllByIstArchiviertIsFalse();
}
