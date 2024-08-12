package goorm_runner.backend.market.domain;

import goorm_runner.backend.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketLikeRepository extends JpaRepository<MarketLike, Long> {
    boolean existsByMemberAndMarket(Member member, Market market);
    void deleteByMemberAndMarket(Member member, Market market);
    int countByMarket(Market market);
}
