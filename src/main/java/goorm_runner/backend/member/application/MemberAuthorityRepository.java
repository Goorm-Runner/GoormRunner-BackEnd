package goorm_runner.backend.member.application;

import goorm_runner.backend.member.domain.MemberAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberAuthorityRepository extends JpaRepository<MemberAuthority, Long> {
}
