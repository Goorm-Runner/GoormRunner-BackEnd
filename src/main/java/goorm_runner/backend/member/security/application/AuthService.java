package goorm_runner.backend.member.security.application;

import goorm_runner.backend.member.application.AuthorityRepository;
import goorm_runner.backend.member.application.MemberAuthorityRepository;
import goorm_runner.backend.member.application.MemberRepository;
import goorm_runner.backend.member.domain.*;
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
    public void signup(MemberSignupRequest request) {
        Member member = Member.builder()
                .loginId(request.getLoginId())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.valueOf(request.getRole().toUpperCase()))
                .sex(Sex.valueOf(request.getSex().toUpperCase()))
                .birth(LocalDate.parse(request.getBirth(), DateTimeFormatter.ISO_DATE))
                .build();

        Authority authority = authorityRepository.findByName("read")
                .orElseThrow(() -> new IllegalArgumentException("Authority not found"));

        MemberAuthority memberAuthority = new MemberAuthority(member, authority);
        memberAuthority.authorize(member, authority);

        memberAuthorityRepository.save(memberAuthority);
        memberRepository.save(member);
    }

    @Transactional
    public String login(LoginRequest request) {
        Optional<Member> optionalMember = memberRepository.findByLoginId(request.getLoginId());
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            if (passwordEncoder.matches(request.getPassword(), member.getPassword())) {
                return jwtTokenProvider.createToken(member.getLoginId(), member.getRole());
            }
        }
        throw new RuntimeException("Invalid login credentials");
    }

}
