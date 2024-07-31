package goorm_runner.backend.member.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String loginId;

    @Column(nullable = false)
    private String username;

    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)//역할군 열거형으로 표현
    private Role role;

    @Column(nullable = false, length = 1)
    private Sex sex;

    @Column(nullable = false)
    private LocalDate birth;

    private Long teamId;

    private String profilePictureUrl;

    @OneToMany(mappedBy = "member")
    private Set<MemberAuthority> memberAuthorities = new HashSet<>();

    @Builder
    public Member(String loginId, String username, String password, Role role, Sex sex, LocalDate birth, Long teamId, String profilePictureUrl, Set<MemberAuthority> memberAuthorities) {
        this.loginId = loginId;
        this.username = username;
        this.password = password;
        this.role = role;
        this.sex = sex;
        this.birth = birth;
        this.teamId = teamId;
        this.profilePictureUrl = profilePictureUrl;
    }

    public void updateProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public void removeProfilePictureUrl() {
        this.profilePictureUrl = null;
    }

    public void addMemberAuthority(MemberAuthority memberAuthority) {
        memberAuthorities.add(memberAuthority);
    }
}

