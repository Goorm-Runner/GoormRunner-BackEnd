package goorm_runner.backend.picture.application;

import goorm_runner.backend.member.application.MemberRepository;
import goorm_runner.backend.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateMemberService {
    private final MemberRepository memberRepository;

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

    public Long getMemberIdByUsername(String username) {
        Optional<Member> member = memberRepository.findByUsername(username);
        if (member.isPresent()) {
            log.info("Found Member: {}", member.get());
            return member.get().getId();
        } else {
            log.error("Member not found for username: {}", username);
            throw new RuntimeException("Member not found");
        }
    }

    public Member getMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new RuntimeException("Member not found"));
    }

    @Transactional
    public void updateMemberProfileImage(Long memberId, String fileUrl) {
        Member member = getMemberById(memberId);
        member.updateProfilePictureUrl(fileUrl);
        memberRepository.save(member);
    }

    @Transactional
    public void removeMemberProfileImage(Long memberId) {
        Member member = getMemberById(memberId);
        member.removeProfilePictureUrl();
        memberRepository.save(member);
    }
}
