package goorm_runner.backend.market.application;

import goorm_runner.backend.market.domain.Market;
import goorm_runner.backend.market.domain.MarketComment;
import goorm_runner.backend.market.domain.MarketCommentRepository;
import goorm_runner.backend.market.domain.MarketRepository;
import goorm_runner.backend.market.dto.MarketCommentCreateRequest;
import goorm_runner.backend.market.dto.MarketCommentUpdateRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MarketCommentService {

    private final MarketCommentRepository commentRepository;
    private final MarketRepository marketRepository;

    public MarketComment createComment(MarketCommentCreateRequest request, Long marketId, Long memberId) {
        Market market = marketRepository.findById(marketId)
                .orElseThrow(() -> new EntityNotFoundException("해당 상품을 찾을 수 없습니다."));

        MarketComment comment = MarketComment.builder()
                .market(market)
                .memberId(memberId)
                .content(request.getContent())
                .build();

        return commentRepository.save(comment);
    }

    public MarketComment updateComment(Long commentId, MarketCommentUpdateRequest request) {
        MarketComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("해당 댓글을 찾을 수 없습니다."));

        comment.updateContent(request.getContent());
        return comment;
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    public List<MarketComment> getCommentsByMarketId(Long marketId) {
        return commentRepository.findByMarketId(marketId);
    }
}

