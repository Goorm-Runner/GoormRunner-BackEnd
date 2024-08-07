package goorm_runner.backend.member.security.config.oauth2;

import goorm_runner.backend.member.domain.Member;
import goorm_runner.backend.member.domain.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class CustomOauth2User implements OAuth2User {
    private final Member member;
    private final Map<String,Object> attributes;

    public CustomOauth2User(Member member, Map<String, Object> attributes) {
        this.member = member;
        this.attributes = attributes;
    }
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
       return Collections.singleton(new SimpleGrantedAuthority(Role.USER.name()));
       //Role 반환
    }

    @Override
    public String getName() {
        return member.getLoginId();
    }
}
