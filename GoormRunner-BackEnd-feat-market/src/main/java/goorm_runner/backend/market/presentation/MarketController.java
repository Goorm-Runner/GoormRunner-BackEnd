package goorm_runner.backend.market.presentation;

import goorm_runner.backend.market.application.MarketReadService;
import goorm_runner.backend.market.application.MarketService;
import goorm_runner.backend.market.domain.Market;
import goorm_runner.backend.market.dto.*;
import goorm_runner.backend.member.application.MemberService;
import goorm_runner.backend.member.security.SecurityMember;
import goorm_runner.backend.post.dto.ResponseMetaData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/market")
@RequiredArgsConstructor
public class MarketController {

    private final MarketService marketService;
    private final MarketReadService marketReadService;
    private final MemberService memberService;


    @PostMapping("/categories/{categoryName}/items")
    public ResponseEntity<MarketCreateResponse> createMarket(
            @AuthenticationPrincipal SecurityMember securityMember,
            @PathVariable String categoryName,
            @RequestParam String statusTitle,
            @RequestBody MarketCreateRequest request,
            @RequestParam("image") MultipartFile image) throws IOException {


        try {
            String username = securityMember.getUsername();
            Long memberId = memberService.getMemberIdByUsername(username);


            Market market = marketService.create(request, memberId, categoryName.toUpperCase(), statusTitle.toUpperCase(), image);
            MarketCreateResponse response = getCreateResponse(market);


            URI location = newUri(response);
            return ResponseEntity.created(location).body(response);
        } catch (IOException e) {
        return ResponseEntity.status(500).body(null);
    }
    }


    @GetMapping("/categories/{categoryName}/items/{marketId}")
    public ResponseEntity<MarketReadResponse> readMarket(@PathVariable String categoryName, @PathVariable Long marketId) {


        Market market = marketReadService.readMarket(marketId);
        MarketReadResponse response = getReadResponse(categoryName.toUpperCase(), market);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/categories/{categoryName}/items")
    public ResponseEntity<MarketReadPageResponse> readPage(@PathVariable String categoryName, @RequestParam int pageNumber, @RequestParam int pageSize) {


        Page<Market> markets = marketReadService.readPage(categoryName.toUpperCase(), PageRequest.of(pageNumber, pageSize));

        List<MarketOverview> overviews = markets.stream()
                .map(MarketOverview::from)
                .toList();

        ResponseMetaData responseMetaData = ResponseMetaData.of(markets);

        MarketReadPageResponse response = new MarketReadPageResponse(overviews, responseMetaData);
        return ResponseEntity.ok(response);
    }


    @PutMapping("/categories/{categoryName}/items/{marketId}")
    public ResponseEntity<MarketReadResponse> updateMarket(
            @PathVariable String categoryName,
            @PathVariable String statusTitle,
            @PathVariable Long marketId,
            @RequestBody MarketUpdateRequest request,
            @RequestParam("image") MultipartFile image) throws IOException {


        Market market = marketService.update(request, marketId, categoryName, statusTitle, image);
        MarketReadResponse response = getReadResponse(categoryName.toUpperCase(), market);

        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/categories/{ignoredCategoryName}/items/{marketId}")
    public ResponseEntity<Void> deleteMarket(@PathVariable String ignoredCategoryName, @PathVariable Long marketId) {
        marketService.delete(marketId);
        return ResponseEntity.noContent().build();
    }


    private MarketCreateResponse getCreateResponse(Market market) {
        return MarketCreateResponse.builder()
                .categoryName(market.getCategory().name())
                .marketId(market.getId())
                .title(market.getTitle())
                .content(market.getContent())
                .createdAt(market.getCreatedAt().toString())
                .build();
    }

    private MarketReadResponse getReadResponse(String categoryName, Market market) {
        return new MarketReadResponse(
                categoryName,
                market.getId(),
                market.getTitle(),
                market.getContent(),
                market.getPrice(),
                market.getLikeCount(),
                market.getStatus(),
                market.getCreatedAt(),
                market.getUpdatedAt()
        );
    }

    private URI newUri(MarketCreateResponse response) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getMarketId())
                .toUri();
    }
}