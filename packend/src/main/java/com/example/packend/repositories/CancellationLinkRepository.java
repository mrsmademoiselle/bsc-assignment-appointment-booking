package com.example.packend.repositories;

import com.example.packend.entities.CancellationUrl;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CancellationLinkRepository extends CrudRepository<CancellationUrl, String> {
    Optional<CancellationUrl> findByToken(String token);

    void deleteByTerminId(String terminId);
}
