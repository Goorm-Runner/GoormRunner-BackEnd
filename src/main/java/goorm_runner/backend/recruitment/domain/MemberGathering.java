package goorm_runner.backend.recruitment.domain;

import goorm_runner.backend.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberGathering {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "gathering_id", nullable = false)
    private Gathering gathering;

    @ManyToOne
    @JoinColumn(name = "guest_id")
    private Member guest;

//    @Column(nullable = false)
//    private boolean approved = false;

    @Builder
    public MemberGathering(Gathering gathering, Member guest) {
        this.gathering = gathering;
        this.guest = guest;
    }

//    public void approve() {
//        approved = true;
//    }
}
