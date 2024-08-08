package goorm_runner.backend.market.application;

import goorm_runner.backend.market.domain.Comment;
import goorm_runner.backend.market.domain.CommentRepository;
import goorm_runner.backend.market.domain.Market;
import goorm_runner.backend.market.domain.MarketRepository;
import goorm_runner.backend.market.dto.CommentCreateRequest;
import goorm_runner.backend.market.dto.CommentUpdateRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final MarketRepository marketRepository;

    public Comment createComment(CommentCreateRequest request, Long marketId, Long memberId) {
        Market market = marketRepository.findById(marketId)
                .orElseThrow(() -> new EntityNotFoundException("해당 상품을 찾을 수 없습니다."));

        Comment comment = Comment.builder()
                .market(market)
                .memberId(memberId)
                .content(request.getContent())
                .build();

        return commentRepository.save(comment);
    }

    public Comment updateComment(Long commentId, CommentUpdateRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("해당 댓글을 찾을 수 없습니다."));

        comment.updateContent(request.getContent());
        return comment;
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    public List<Comment> getCommentsByMarketId(Long marketId) {
        return commentRepository.findByMarketId(marketId);
    }
}

