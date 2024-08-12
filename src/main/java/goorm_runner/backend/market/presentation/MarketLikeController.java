package goorm_runner.backend.market.presentation;

import goorm_runner.backend.market.application.MarketLikeService;
import goorm_runner.backend.member.application.MemberService;
import goorm_runner.backend.member.security.SecurityMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/market/{marketId}/like")
@RequiredArgsConstructor
public class MarketLikeController {

    private final MarketLikeService marketlikeService;
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<Void> toggleLike(
            @AuthenticationPrincipal SecurityMember securityMember,
            @PathVariable Long marketId) {

        String username = securityMember.getUsername();
        Long memberId = memberService.findMemberIdByUsername(username);

        marketlikeService.toggleLike(marketId, memberId);

        return ResponseEntity.ok().build();
    }
}
