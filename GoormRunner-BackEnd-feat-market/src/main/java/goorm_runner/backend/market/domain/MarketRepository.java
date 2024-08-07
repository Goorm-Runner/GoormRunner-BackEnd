package goorm_runner.backend.market.domain;

import goorm_runner.backend.market.domain.Market;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarketRepository extends JpaRepository<Market, Long> {
}