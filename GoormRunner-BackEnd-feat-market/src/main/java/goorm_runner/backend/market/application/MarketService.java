package goorm_runner.backend.market.application;

import goorm_runner.backend.market.domain.MarketCategory;
import goorm_runner.backend.market.domain.MarketStatus;
import goorm_runner.backend.market.dto.MarketCreateRequest;
import goorm_runner.backend.market.domain.Market;
import goorm_runner.backend.market.dto.MarketUpdateRequest;
import goorm_runner.backend.market.domain.MarketRepository;
import goorm_runner.backend.member.application.MemberRepository;
import goorm_runner.backend.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service
@RequiredArgsConstructor
@Transactional
public class MarketService {

    private final MarketRepository marketRepository;
    private final MemberRepository memberRepository;
    public Market create(MarketCreateRequest request, Long memberId,  String categoryName, String statustitle) {
        validateOfRequests(request.title(), request.content(), request.price(), request.delivery(), request.imageUrl());

        MarketCategory category = toMarketCategory(categoryName);
        MarketStatus status = toMarketStatus(statustitle);
        Market market = getMarket(request, memberId, category, status);
        return marketRepository.save(market);
    }

    public Market update(MarketUpdateRequest request, Long marketId) {
        validateOfRequests(request.title(), request.content(), request.price(), request.delivery(), request.imageUrl());

        Market market = marketRepository.findById(marketId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품을 찾지 못했습니다."));

        market.update(request.title(), request.content(), request.price(), request.delivery(), request.imageUrl());
        return market;
    }

    public void delete(Long marketId) {
        marketRepository.deleteById(marketId);
    }

    private void validateOfRequests(String title, String content, Integer price,  Integer delivery, String imageUrl) {
        if (!StringUtils.hasText(title)) {
            throw new IllegalArgumentException("제목을 입력해주세요.");
        }

        if (!StringUtils.hasText(content)) {
            throw new IllegalArgumentException("본문 내용을 입력해주세요.");
        }

        if (price == null || price <= 0) {
            throw new IllegalArgumentException("상품 가격은 0보다 커야 합니다.");
        }

        if (delivery == null || delivery < 0) {
            throw new IllegalArgumentException("배송비는 0 이상이어야 합니다.");
        }

        if (!StringUtils.hasText(imageUrl)) {
            throw new IllegalArgumentException("사진이 첨부되지 않았습니다.");
        }
    }

    private MarketCategory toMarketCategory(String category) {
        try {
            return MarketCategory.valueOf(category);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 카테고리: " + category);
        }
    }

    private MarketStatus toMarketStatus(String status) {
        try {
            return MarketStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 상품상태: " + status);
        }
    }

    private Market getMarket(MarketCreateRequest request, Long memberId,  MarketCategory category, MarketStatus status) {
        return Market.builder()
                .memberId(memberId)
                .title(request.title())
                .content(request.content())
                .price(request.price())
                .likeCount((Integer) 0)
                .category(category)
                .status(status)
                .delivery(request.delivery())
                .imageUrl(request.imageUrl())
                .build();
    }
}