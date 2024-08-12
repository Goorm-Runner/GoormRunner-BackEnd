package goorm_runner.backend.market.domain;

import goorm_runner.backend.market.domain.Market;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarketRepository extends JpaRepository<Market, Long> {
    Page<Market> findByCategory(MarketCategory category, Pageable pageable);
}