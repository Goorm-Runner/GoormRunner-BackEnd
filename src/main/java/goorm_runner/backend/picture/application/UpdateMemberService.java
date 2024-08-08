package goorm_runner.backend.picture.application;

import goorm_runner.backend.member.application.MemberRepository;
import goorm_runner.backend.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateMemberService {
    private final MemberRepository memberRepository;

    /**
     * 로그인 아이디로 멤버 아이디 조회
     */

    public Long getMemberIdByLoginId(String loginId) {
        Optional<Member> member = memberRepository.findByLoginId(loginId);
        if (member.isPresent()) {
            log.info("Found Member: {}", member.get());
            return member.get().getId();
        } else {
            log.error("Member not found for loginId: {}", loginId);
            throw new RuntimeException("Member not found");
        }
    }

    /**
     * 사용하지 않는 메소드
     */
//    public Long getMemberIdByUsername(String username) {
//        Optional<Member> member = memberRepository.findByUsername(username);
//        if (member.isPresent()) {
//            log.info("Found Member: {}", member.get());
//            return member.get().getId();
//        } else {
//            log.error("Member not found for username: {}", username);
//            throw new RuntimeException("Member not found");
//        }
//    }

    /**
     * 멤버 아이디 조회
     */
    public Member getMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new RuntimeException("Member not found"));
    }

    /**
     * 멤버 프로필 업데이트
     */
    @Transactional
    public void updateMemberProfileImage(Long memberId, String fileUrl) {
        Member member = getMemberById(memberId);
        member.updateProfilePictureUrl(fileUrl);
        memberRepository.save(member);
    }

    /**
     * 멤버 프로필 삭제
     */
    @Transactional
    public void removeMemberProfileImage(Long memberId) {
        Member member = getMemberById(memberId);
        member.removeProfilePictureUrl();
        memberRepository.save(member);
    }
}
