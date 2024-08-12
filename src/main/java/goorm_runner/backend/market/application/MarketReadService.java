package goorm_runner.backend.market.application;

import goorm_runner.backend.market.domain.Market;
import goorm_runner.backend.market.domain.MarketCategory;
import goorm_runner.backend.market.domain.MarketRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MarketReadService {
    private final MarketRepository marketRepository;

    public Market readMarket(Long marketId) {
        return marketRepository.findById(marketId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
    }

    public Page<Market> readPage(String categoryName, Pageable pageable) {
        MarketCategory category = toCategory(categoryName);
        return marketRepository.findByCategory(category, pageable);
    }

    private MarketCategory toCategory(String category) {
        try {
            return MarketCategory.valueOf(category);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 카테고리: " + category);
        }
    }
}