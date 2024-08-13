package goorm_runner.backend.market.application;

import goorm_runner.backend.market.domain.Market;
import goorm_runner.backend.market.domain.MarketLike;
import goorm_runner.backend.market.domain.MarketLikeRepository;
import goorm_runner.backend.market.domain.MarketRepository;
import goorm_runner.backend.member.domain.Member;
import goorm_runner.backend.member.domain.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MarketLikeService {

    private final MarketLikeRepository marketLikeRepository;
    private final MarketRepository marketRepository;
    private final MemberRepository memberRepository;

    public void toggleLike(Long marketId, Long memberId) {
        Market market = marketRepository.findById(marketId)
                .orElseThrow(() -> new EntityNotFoundException("해당 상품을 찾을 수 없습니다."));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("해당 사용자를 찾을 수 없습니다."));

        if (marketLikeRepository.existsByMemberAndMarket(member, market)) {
            marketLikeRepository.deleteByMemberAndMarket(member, market);
        } else {
            MarketLike marketlike = new MarketLike(member, market);
            marketLikeRepository.save(marketlike);
        }

        updateLikeCount(market);
    }

    private void updateLikeCount(Market market) {
        int likeCount = marketLikeRepository.countByMarket(market);
        market.setLikeCount(likeCount);
    }
}
