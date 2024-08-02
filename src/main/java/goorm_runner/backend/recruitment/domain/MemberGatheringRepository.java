package goorm_runner.backend.recruitment.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberGatheringRepository extends JpaRepository<MemberGathering, Long> {
    long countByGatheringId(Long gatheringId);
    Optional<MemberGathering> findByGatheringAndGuestId(Gathering gathering, Long guestId);
}
