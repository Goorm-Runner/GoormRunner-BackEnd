package goorm_runner.backend.post.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PostCreateRequest {
    private final String title;
    private final String content;
}
