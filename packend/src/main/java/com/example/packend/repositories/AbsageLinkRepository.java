package com.example.packend.repositories;

import com.example.packend.entities.AbsageLink;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AbsageLinkRepository extends CrudRepository<AbsageLink, Long> {
    Optional<AbsageLink> findByToken(String token);

    void deleteByTerminId(Long terminId);
}
