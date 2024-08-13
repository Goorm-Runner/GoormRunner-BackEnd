package goorm_runner.backend.post.application.post;

import goorm_runner.backend.post.application.post.dto.PostCreateResult;
import goorm_runner.backend.post.application.post.dto.PostUpdateResult;
import goorm_runner.backend.post.domain.PostRepository;
import goorm_runner.backend.post.domain.exception.PostException;
import goorm_runner.backend.post.domain.model.Category;
import goorm_runner.backend.post.domain.model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static goorm_runner.backend.global.ErrorCode.ACCESS_DENIED;
import static goorm_runner.backend.global.ErrorCode.POST_NOT_FOUND;

@RequiredArgsConstructor
@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;

    public PostCreateResult create(String title, String content, Long authorId, Category category) {
        Post post = getPost(title, content, authorId, category);
        Post saved = postRepository.save(post);
        return PostCreateResult.from(saved);
    }

    public PostUpdateResult update(String title, String content, Long postId, Long loginId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));

        checkAuthor(loginId, post);

        post.update(title, content);
        return PostUpdateResult.from(post);
    }

    public void delete(Long postId, Long loginId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));

        checkAuthor(loginId, post);

        post.delete();
    }

    private void checkAuthor(Long loginId, Post post) {
        if (!post.getAuthorId().equals(loginId)) {
            throw new PostException(ACCESS_DENIED);
        }
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
