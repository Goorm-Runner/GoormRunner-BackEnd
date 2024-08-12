package goorm_runner.backend.recruitment.domain;

import goorm_runner.backend.team.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecruitmentRepository extends JpaRepository<Recruitment, Long>, JpaSpecificationExecutor<Recruitment> {
    List<Recruitment> findByDeletedAtIsNull();
    Optional<Recruitment> findByIdAndDeletedAtIsNull(Long id);
    List<Recruitment> findByTeamAndDeletedAtIsNull(Team team);
    List<Recruitment> findByGathering_Host_IdAndDeletedAtIsNull(Long hostId);

    @Query("SELECT r FROM Recruitment r JOIN r.gathering g JOIN MemberGathering mg ON g.id = mg.gathering.id WHERE mg.guest.id = :guestId AND r.deletedAt IS NULL")
    List<Recruitment> findByGuestIdAndDeletedAtIsNull(@Param("guestId") Long guestId);
}
