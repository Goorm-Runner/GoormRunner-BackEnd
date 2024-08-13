package goorm_runner.backend.post.application.comment;

import goorm_runner.backend.global.ErrorCode;
import goorm_runner.backend.global.PageMetaData;
import goorm_runner.backend.member.application.exception.MemberException;
import goorm_runner.backend.member.domain.Member;
import goorm_runner.backend.member.domain.MemberRepository;
import goorm_runner.backend.post.application.comment.dto.CommentPageResult;
import goorm_runner.backend.post.application.comment.dto.CommentReadResult;
import goorm_runner.backend.post.application.comment.dto.CommentResultOverview;
import goorm_runner.backend.post.domain.CommentQueryRepository;
import goorm_runner.backend.post.domain.exception.CommentException;
import goorm_runner.backend.post.domain.model.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentReadService {

    private final CommentQueryRepository commentQueryRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public CommentReadResult read(Long commentId) {
        Comment comment = commentQueryRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(ErrorCode.COMMENT_NOT_FOUND));

        return CommentReadResult.from(comment);
    }

    @Transactional(readOnly = true)
    public CommentPageResult readPage(Long postId, Pageable pageable) {
        Page<Comment> comments = commentQueryRepository.findByPostId(postId, pageable);

        List<CommentResultOverview> overview = comments.stream()
                .map(comment -> CommentResultOverview.of(postId, comment, getAuthorName(comment.getAuthorId())))
                .toList();

        PageMetaData pageMetaData = PageMetaData.from(comments);

        return new CommentPageResult(overview, pageMetaData);
    }

    private String getAuthorName(Long authorId) {
        Member member = memberRepository.findById(authorId)
                .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));

        return member.getNickname();
    }
}
