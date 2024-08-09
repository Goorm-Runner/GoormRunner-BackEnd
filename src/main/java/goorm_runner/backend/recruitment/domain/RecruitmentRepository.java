package goorm_runner.backend.recruitment.domain;

import goorm_runner.backend.team.domain.Team;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface RecruitmentRepository extends JpaRepository<Recruitment, Long>, JpaSpecificationExecutor<Recruitment> {
    List<Recruitment> findByDeletedAtIsNull();
    Optional<Recruitment> findByIdAndDeletedAtIsNull(Long id);
    List<Recruitment> findByTeamAndDeletedAtIsNull(Team team);
}
