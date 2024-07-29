package goorm_runner.backend.picture.application;

import goorm_runner.backend.member.application.MemberRepository;
import goorm_runner.backend.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UpdateMemberService {
    private final MemberRepository memberRepository;

    public Long getMemberIdByLoginId(String loginId) {
        Optional<Member> member = memberRepository.findByLoginId(loginId);
        if (member.isPresent()) {
            return member.get().getId();
        } else {
            throw new RuntimeException("Member not found");
        }
    }

    public Member getMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new RuntimeException("Member not found"));
    }

    public void updateMemberProfileImage(Long memberId, String fileUrl) {
        Member member = getMemberById(memberId);
        member = Member.builder()
                .loginId(member.getLoginId())
                .username(member.getUsername())
                .password(member.getPassword())
                .role(member.getRole())
                .sex(member.getSex())
                .birth(member.getBirth())
                .teamId(member.getTeamId())
                .profilePictureUrl(fileUrl)
                .memberAuthorities(member.getMemberAuthorities())
                .build();
        memberRepository.save(member);
    }

    public void removeMemberProfileImage(Long memberId) {
        Member member = getMemberById(memberId);
        member = Member.builder()
                .loginId(member.getLoginId())
                .username(member.getUsername())
                .password(member.getPassword())
                .role(member.getRole())
                .sex(member.getSex())
                .birth(member.getBirth())
                .teamId(member.getTeamId())
                .profilePictureUrl(null)
                .memberAuthorities(member.getMemberAuthorities())
                .build();
        memberRepository.save(member);
    }
}
