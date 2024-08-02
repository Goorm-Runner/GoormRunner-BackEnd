package goorm_runner.backend.member.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AuthorityType type;

    @OneToMany(mappedBy = "authority")
    private Set<MemberAuthority> memberAuthorities = new HashSet<>();

    public Authority(Long id, AuthorityType type) {
        this.id = id;
        this.type = type;
    }

    public void addMemberAuthority(MemberAuthority memberAuthority) {
        memberAuthorities.add(memberAuthority);
    }
}
