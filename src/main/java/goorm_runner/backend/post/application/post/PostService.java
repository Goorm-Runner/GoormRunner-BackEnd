package goorm_runner.backend.post.application.post;

import goorm_runner.backend.member.application.exception.MemberException;
import goorm_runner.backend.member.domain.Member;
import goorm_runner.backend.member.domain.MemberRepository;
import goorm_runner.backend.post.application.post.exception.PostException;
import goorm_runner.backend.post.domain.PostRepository;
import goorm_runner.backend.post.domain.model.Category;
import goorm_runner.backend.post.domain.model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static goorm_runner.backend.global.ErrorCode.MEMBER_NOT_FOUND;
import static goorm_runner.backend.global.ErrorCode.POST_NOT_FOUND;

@RequiredArgsConstructor
@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public Post create(String title, String content, Long authorId, Category category) {
        Post post = getPost(title, content, authorId, category);

        return postRepository.save(post);
    }

    public Post update(String title, String content, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));

        post.update(title, content);
        return post;
    }

    public void delete(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));
        post.delete();
    }

    public String getAuthorName(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));

        Long authorId = post.getAuthorId();
        Member author = memberRepository.findById(authorId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        return author.getNickname();
    }

    private Post getPost(String title, String content, Long authorId, Category category) {
        return Post.builder()
                .authorId(authorId)
                .title(title)
                .content(content)
                .category(category)
                .build();
    }
}
