package goorm_runner.backend.market.application;

import goorm_runner.backend.market.domain.MarketCategory;
import goorm_runner.backend.market.domain.MarketStatus;
import goorm_runner.backend.market.dto.MarketCreateRequest;
import goorm_runner.backend.market.domain.Market;
import goorm_runner.backend.market.dto.MarketUpdateRequest;
import goorm_runner.backend.market.domain.MarketRepository;
import goorm_runner.backend.market.exception.MarketNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Service
@RequiredArgsConstructor
@Transactional
public class MarketService {

    private final MarketRepository marketRepository;
    private final MarketImageStorageService imageStorageService;

    private static final String IMAGE_DIRECTORY = "uploaded-images/";

    public Market create(MarketCreateRequest request, Long memberId,  String categoryName, String statustitle,MultipartFile image) throws IOException {
        validateRequests(request.title(), request.content(), request.price(), request.delivery(), image.getOriginalFilename());

        String imageUrl = imageStorageService.saveImage(image);

        MarketCategory category = toMarketCategory(categoryName);
        MarketStatus status = toMarketStatus(statustitle);int initialLikeCount = 0; // 기본 likeCount 값을 설정
        Market market = getMarket(request, memberId, category, status, initialLikeCount, imageUrl);



        return marketRepository.save(market);
    }

    public Market update(MarketUpdateRequest request, Long marketId, String categoryName, String statustitle, MultipartFile image)throws IOException {
        validateRequests(request.title(), request.content(), request.price(), request.delivery(), image.getOriginalFilename());

        Market market = marketRepository.findById(marketId)
                .orElseThrow(() -> new MarketNotFoundException("해당 상품을 찾지 못했습니다."));

        MarketCategory category = toMarketCategory(categoryName);
        MarketStatus status = toMarketStatus(statustitle);

        String imageUrl = imageStorageService.saveImage(image);

        market.update(request.title(), request.content(), request.price(),category,status, request.delivery(), imageUrl);
        return market;
    }

    public void delete(Long marketId) {
        Market market = marketRepository.findById(marketId)
                .orElseThrow(() -> new MarketNotFoundException("해당 상품을 찾지 못했습니다."));
        marketRepository.delete(market);
    }

    private void validateRequests(String title, String content, Integer price, Integer delivery, String fileName) {
        if (!StringUtils.hasText(title) || title.length() > 100) {
            throw new IllegalArgumentException("제목을 입력하거나 너무 길지 않게 하세요.");
        }

        if (!StringUtils.hasText(content) || content.length() > 1000) {
            throw new IllegalArgumentException("본문 내용을 입력하거나 너무 길지 않게 하세요.");
        }

        if (price == null || price <= 0) {
            throw new IllegalArgumentException("상품 가격은 0보다 커야 합니다.");
        }

        if (delivery == null || delivery < 0) {
            throw new IllegalArgumentException("배송비는 0 이상이어야 합니다.");
        }

        if (!StringUtils.hasText(fileName) || !isValidImageExtension(fileName)) {
            throw new IllegalArgumentException("지원하지 않는 이미지 파일 형식입니다.");
        }
    }

    private boolean isValidImageExtension(String fileName) {
        String lowerCaseFileName = fileName.toLowerCase();
        return lowerCaseFileName.endsWith(".jpg") || lowerCaseFileName.endsWith(".jpeg") ||
                lowerCaseFileName.endsWith(".png") || lowerCaseFileName.endsWith(".gif");
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

    private Market getMarket(MarketCreateRequest request, Long memberId,  MarketCategory category, MarketStatus status, int likeCount, String imageUrl) {
        return Market.builder()
                .memberId(memberId)
                .title(request.title())
                .content(request.content())
                .price(request.price())
                .category(category)
                .status(status)
                .delivery(request.delivery())
                .likeCount(likeCount)
                .imageUrl(imageUrl)
                .build();
    }
}
