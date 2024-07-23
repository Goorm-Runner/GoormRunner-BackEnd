package goorm_runner.backend.domain.member;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="member")
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

    @Column(nullable = false,length =1)
    private String sex;


    @Column(nullable = false)
    private LocalDate birth;

    private Long teamId;

    private String profilePictureUrl;

    @Builder
    public Member(String loginId, String username, String password, Role role, String sex, LocalDate birth, Long teamId, String profilePictureUrl) {
        this.loginId = loginId;
        this.username = username;
        this.password = password;
        this.role = role;
        this.sex = sex;
        this.birth = birth;
        this.teamId = teamId;
        this.profilePictureUrl = profilePictureUrl;
    }
}

