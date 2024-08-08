package goorm_runner.backend.member.application;

import goorm_runner.backend.member.domain.Authority;
import goorm_runner.backend.member.domain.AuthorityType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Optional<Authority> findByType(AuthorityType type);
}
