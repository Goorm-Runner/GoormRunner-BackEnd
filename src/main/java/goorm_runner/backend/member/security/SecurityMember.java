package goorm_runner.backend.member.security;

import goorm_runner.backend.member.domain.Member;
import goorm_runner.backend.member.domain.MemberAuthority;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@RequiredArgsConstructor
public class SecurityMember implements UserDetails {

    private final Member member;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return member.getMemberAuthorities()
                .stream()
                .map(MemberAuthority::getAuthority)
                .map(SecurityAuthority::new)
                .toList();
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getUsername();
    }
}
