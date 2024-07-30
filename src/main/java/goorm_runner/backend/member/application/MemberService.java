package goorm_runner.backend.member.application;

import goorm_runner.backend.global.ErrorCode;
import goorm_runner.backend.member.application.exception.MemberException;
import goorm_runner.backend.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Long getMemberIdByUsername(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));
        return member.getId();
    }
}
