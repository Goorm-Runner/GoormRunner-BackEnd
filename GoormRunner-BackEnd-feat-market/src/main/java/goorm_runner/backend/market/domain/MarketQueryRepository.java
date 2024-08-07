package goorm_runner.backend.market.domain;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarketQueryRepository extends JpaRepository<Market, Long> {
    Page<Market> findByCategory(MarketCategory category, Pageable pageable);
}