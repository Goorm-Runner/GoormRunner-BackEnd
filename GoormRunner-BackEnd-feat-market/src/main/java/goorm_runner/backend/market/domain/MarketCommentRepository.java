package goorm_runner.backend.market.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarketCommentRepository extends JpaRepository<MarketComment, Long> {
    List<MarketComment> findByMarketId(Long marketId);
}
