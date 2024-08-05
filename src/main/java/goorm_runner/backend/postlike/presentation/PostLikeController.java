package goorm_runner.backend.postlike.presentation;

import goorm_runner.backend.member.application.MemberService;
import goorm_runner.backend.member.security.SecurityMember;
import goorm_runner.backend.postlike.application.PostLikeService;
import goorm_runner.backend.postlike.dto.PostLikeCountResponse;
import goorm_runner.backend.postlike.dto.PostLikeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PostLikeController {

    private final MemberService memberService;
    private final PostLikeService postLikeService;

    @PostMapping("/likes/posts/{postId}")
    public ResponseEntity<PostLikeResponse> createPostLike(@AuthenticationPrincipal SecurityMember securityMember, @PathVariable Long postId) {

        String username = securityMember.getUsername();
        Long memberId = memberService.findMemberIdByUsername(username);

        postLikeService.likePost(postId, memberId);

        return ResponseEntity.ok(PostLikeResponse.succeed());

    }

    @GetMapping("/likes/posts/{postId}")
    public ResponseEntity<PostLikeCountResponse> getPostLikeCount(@PathVariable Long postId) {

        int likes = postLikeService.countPostLikes(postId);

        return ResponseEntity.ok(new PostLikeCountResponse(postId, likes));

    }

    @DeleteMapping("/likes/posts/{postId}")
    public ResponseEntity<PostLikeResponse> deletePostLike(@AuthenticationPrincipal SecurityMember securityMember, @PathVariable Long postId) {

        String username = securityMember.getUsername();
        Long memberId = memberService.findMemberIdByUsername(username);

        postLikeService.deletePostLike(postId, memberId);

        return ResponseEntity.ok(PostLikeResponse.succeed());

    }
}
