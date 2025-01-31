package goorm_runner.backend.recruitment.presentation;

import goorm_runner.backend.member.application.MemberService;
import goorm_runner.backend.member.security.SecurityMember;
import goorm_runner.backend.recruitment.application.RecruitmentService;
import goorm_runner.backend.recruitment.domain.MemberGathering;
import goorm_runner.backend.recruitment.dto.ApproveRequest;
import goorm_runner.backend.recruitment.dto.RecruitmentRequest;
import goorm_runner.backend.recruitment.dto.RecruitmentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recruitment")
public class RecruitmentController {
    private final RecruitmentService recruitmentService;
    private final MemberService memberService;

    /**
     * 모집 글을 생성한다.
     */
    @PostMapping("/create")
    public ResponseEntity<RecruitmentResponse> createRecruitment(
            @RequestBody RecruitmentRequest request,
            @AuthenticationPrincipal SecurityMember securityMember) {

        String username = securityMember.getUsername();
        Long authorId = memberService.findMemberIdByUsername(username);

        RecruitmentResponse response = recruitmentService.createRecruitment(request, authorId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 모든 모집 글을 조회한다.
     */
//    @GetMapping
    public ResponseEntity<List<RecruitmentResponse>> getAllRecruitments() {
        List<RecruitmentResponse> recruitments = recruitmentService.findAllRecruitments();
        return ResponseEntity.ok(recruitments);
    }

    @GetMapping
    public ResponseEntity<List<RecruitmentResponse>> getRecruitments(
            @RequestParam(required = false) Long teamId,
            @RequestParam(required = false) Long ballparkId) {
        List<RecruitmentResponse> recruitments = recruitmentService.findByTeamAndBallpark(teamId, ballparkId);
        return ResponseEntity.ok(recruitments);
    }

    /**
     * 특정 모집 글을 조회한다.
     */
    @GetMapping("/{recruitmentId}")
    public ResponseEntity<RecruitmentResponse> getRecruitmentById(@PathVariable Long recruitmentId) {
        RecruitmentResponse recruitment = recruitmentService.findRecruitmentById(recruitmentId);
        return ResponseEntity.ok(recruitment);
    }

    /**
     * 모집 글의 참여 인원 수를 조회한다.
     */
    @GetMapping("/{recruitmentId}/participants/count")
    public ResponseEntity<Integer> getParticipantCount(@PathVariable Long recruitmentId) {
        int participantCount = recruitmentService.getParticipantCount(recruitmentId);
        return ResponseEntity.ok(participantCount);
    }

    /**
     * 특정 팀의 모집 글을 조회한다.
     */
//    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<RecruitmentResponse>> getRecruitmentsByTeam(@PathVariable Long teamId) {
        List<RecruitmentResponse> recruitments = recruitmentService.findRecruitmentsByTeam(teamId);
        return ResponseEntity.ok(recruitments);
    }

    /**
     * 모집 글을 수정한다.
     */
    @PutMapping("/{recruitmentId}")
    public ResponseEntity<RecruitmentResponse> updateRecruitment(
            @PathVariable Long recruitmentId,
            @RequestBody RecruitmentRequest request,
            @AuthenticationPrincipal SecurityMember securityMember) {

        String username = securityMember.getUsername();
        Long authorId = memberService.findMemberIdByUsername(username);

        RecruitmentResponse response = recruitmentService.updateRecruitment(recruitmentId, request, authorId);
        return ResponseEntity.ok(response);
    }


    /**
     * 회원이 모집 글에 참여한다.
     */
    @PostMapping("/{recruitmentId}/join")
    public ResponseEntity<MemberGathering> joinRecruitment(
            @PathVariable Long recruitmentId,
            @AuthenticationPrincipal SecurityMember securityMember) {

        String username = securityMember.getUsername();
        Long memberId = memberService.findMemberIdByUsername(username);

        MemberGathering memberGathering = recruitmentService.joinRecruitment(recruitmentId, memberId);
        return ResponseEntity.ok(memberGathering);
    }

    /**
     * 글 작성자가 참여 요청을 승인한다.
     */
//    @PostMapping("/{recruitmentId}/approve")
    public ResponseEntity<Void> approveParticipation(
            @PathVariable Long recruitmentId,
            @RequestBody ApproveRequest approveRequest,
            @AuthenticationPrincipal SecurityMember securityMember) {

        String username = securityMember.getUsername();
        Long hostId = memberService.findMemberIdByUsername(username);

        recruitmentService.approveParticipation(recruitmentId, approveRequest.getMemberId(), hostId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 회원이 참여를 취소한다.
     */
//    @DeleteMapping("/{recruitmentId}/cancel")
    public ResponseEntity<Void> cancelParticipation(
            @PathVariable Long recruitmentId,
            @AuthenticationPrincipal SecurityMember securityMember) {

        String username = securityMember.getUsername();
        Long memberId = memberService.findMemberIdByUsername(username);

        recruitmentService.cancelParticipation(recruitmentId, memberId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 모집 글을 삭제한다.
     */
    @DeleteMapping("/{recruitmentId}")
    public ResponseEntity<Void> deleteRecruitment(
            @PathVariable Long recruitmentId,
            @AuthenticationPrincipal SecurityMember securityMember) {

        String username = securityMember.getUsername();
        Long memberId = memberService.findMemberIdByUsername(username);

        recruitmentService.deleteRecruitment(recruitmentId, memberId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
