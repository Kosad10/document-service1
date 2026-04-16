package ru.kosad10.documentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kosad10.documentservice.entity.Registry;

public interface RegistryRepository extends JpaRepository<Registry, Long> {
}
