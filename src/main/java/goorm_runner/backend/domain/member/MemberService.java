package goorm_runner.backend.domain.member;

import goorm_runner.backend.domain.config.jwt.JwtTokenProvider;
import goorm_runner.backend.domain.dto.LoginRequest;
import goorm_runner.backend.domain.dto.MemberSignupRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public Member signup(MemberSignupRequest request) {
        Member member = Member.builder()
                .loginId(request.getLoginId())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.valueOf(request.getRole().toUpperCase()))
                .sex(request.getSex())
                .birth(LocalDate.parse(request.getBirth(), DateTimeFormatter.ISO_DATE))
                .build();

        return memberRepository.save(member);
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
