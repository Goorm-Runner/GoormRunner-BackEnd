package goorm_runner.backend.post.application.post;

import goorm_runner.backend.global.ErrorCode;
import goorm_runner.backend.global.PageMetaData;
import goorm_runner.backend.member.domain.Member;
import goorm_runner.backend.member.domain.MemberRepository;
import goorm_runner.backend.post.application.post.dto.PostReadPageResult;
import goorm_runner.backend.post.application.post.dto.PostResultOverview;
import goorm_runner.backend.post.application.post.exception.PostException;
import goorm_runner.backend.post.domain.PostQueryRepository;
import goorm_runner.backend.post.domain.model.Category;
import goorm_runner.backend.post.domain.model.Post;
import goorm_runner.backend.postlike.domain.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PostReadService {

    private final PostQueryRepository postQueryRepository;
    private final MemberRepository memberRepository;
    private final PostLikeRepository postLikeRepository;

    public PostReadPageResult readPage(Category category, Pageable pageable) {
        Page<Post> posts = postQueryRepository.findByCategory(category, pageable);

        List<PostResultOverview> overviews = posts.stream()
                .map(post -> PostResultOverview.from(post, getAuthorName(post.getAuthorId()), countPostLikes(post.getId())))
                .toList();

        return new PostReadPageResult(overviews, PageMetaData.from(posts));
    }

    private String getAuthorName(Long authorId) {
        Member member = memberRepository.findById(authorId)
                .orElseThrow(() -> new PostException(ErrorCode.MEMBER_NOT_FOUND));
        return member.getNickname();
    }

    private int countPostLikes(Long postId) {
        return postLikeRepository.countByPostId(postId);
    }
}
