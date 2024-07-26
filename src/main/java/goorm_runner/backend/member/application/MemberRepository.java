package goorm_runner.backend.member.application;

import goorm_runner.backend.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByLoginId(String loginId);
    Optional<Member> findByUsername(String username);
}
