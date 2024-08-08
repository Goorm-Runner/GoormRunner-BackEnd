package goorm_runner.backend.recruitment.domain;

import goorm_runner.backend.team.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecruitmentRepository extends JpaRepository<Recruitment, Long> {
    List<Recruitment> findByDeletedAtIsNull();
    Optional<Recruitment> findByIdAndDeletedAtIsNull(Long id);
    List<Recruitment> findByTeamAndDeletedAtIsNull(Team team);
}
