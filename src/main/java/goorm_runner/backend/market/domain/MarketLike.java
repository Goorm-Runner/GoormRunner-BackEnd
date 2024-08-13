package goorm_runner.backend.market.domain;

import goorm_runner.backend.common.BaseTimeEntity;
import goorm_runner.backend.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MarketLike extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "market_like_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "market_id", nullable = false)
    private Market market;

    public MarketLike(Member member, Market market) {
        this.member = member;
        this.market = market;
    }
}
