package goorm_runner.backend.market.repository;

import goorm_runner.backend.market.entity.MarketEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarketRepository extends JpaRepository<MarketEntity, Long> {
}
