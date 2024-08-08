package goorm_runner.backend.member.security.application;

import goorm_runner.backend.global.ErrorCode;
import goorm_runner.backend.member.application.AuthorityRepository;
import goorm_runner.backend.member.application.MemberAuthorityRepository;
import goorm_runner.backend.member.application.MemberRepository;
import goorm_runner.backend.member.domain.*;
import goorm_runner.backend.member.security.application.exception.AuthException;
import goorm_runner.backend.member.security.config.jwt.JwtTokenProvider;
import goorm_runner.backend.member.security.dto.LoginRequest;
import goorm_runner.backend.member.security.dto.MemberSignupRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final AuthorityRepository authorityRepository;
    private final MemberAuthorityRepository memberAuthorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public Member signup(MemberSignupRequest request) {
        Member member = Member.builder()
                .loginId(request.loginId())
                .nickname(request.nickname())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.valueOf(request.role().toUpperCase()))
                .sex(Sex.valueOf(request.sex().toUpperCase()))
                .birth(LocalDate.parse(request.birth(), DateTimeFormatter.ISO_DATE))
                .build();

        Authority authority = authorityRepository.findByType(AuthorityType.ROLE_USER)
                .orElseThrow(() -> new AuthException(ErrorCode.REQUIRED_AUTHORITY_NOT_FOUND));

        MemberAuthority memberAuthority = new MemberAuthority(member, authority);
        memberAuthority.authorize(member, authority);

        memberAuthorityRepository.save(memberAuthority);
        return memberRepository.save(member);
    }

    @Transactional
    public String login(LoginRequest request) {
        Optional<Member> optionalMember = memberRepository.findByLoginId(request.loginId());
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            if (passwordEncoder.matches(request.password(), member.getPassword())) {
                return jwtTokenProvider.createToken(member.getLoginId(), member.getRole());
            }
        }
        throw new AuthException(ErrorCode.INVALID_LOGIN_CREDENTIALS);
    }

}
