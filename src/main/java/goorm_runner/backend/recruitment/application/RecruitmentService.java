package goorm_runner.backend.recruitment.application;

import goorm_runner.backend.ballpark.domain.Ballpark;
import goorm_runner.backend.ballpark.domain.BallparkRepository;
import goorm_runner.backend.global.ErrorCode;
import goorm_runner.backend.member.application.MemberRepository;
import goorm_runner.backend.member.domain.Member;
import goorm_runner.backend.recruitment.application.exception.RecruitmentException;
import goorm_runner.backend.recruitment.domain.*;
import goorm_runner.backend.recruitment.dto.RecruitmentRequest;
import goorm_runner.backend.recruitment.dto.RecruitmentResponse;
import goorm_runner.backend.team.domain.Team;
import goorm_runner.backend.team.domain.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RecruitmentService {
    private final MemberRepository memberRepository;
    private final RecruitmentRepository recruitmentRepository;
    private final GatheringRepository gatheringRepository;
    private final MemberGatheringRepository memberGatheringRepository;
    private final TeamRepository teamRepository;
    private final BallparkRepository ballparkRepository;

    /**
     * 모집 글을 생성한다.
     */
    public RecruitmentResponse createRecruitment(RecruitmentRequest request, Long authorId) {

        if (authorId == null) {
            throw new RecruitmentException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        Member host = findByIdEntity(memberRepository, authorId, "Host");

        Gathering gathering = new Gathering(host);
        gatheringRepository.save(gathering);

        Team team = findByIdEntity(teamRepository, request.getTeamId(), "Team");
        Ballpark ballpark = findByIdEntity(ballparkRepository, request.getBallparkId(), "Ballpark");

        validateRecruitment(request, team, ballpark);

        Recruitment recruitment = Recruitment.builder()
                .gathering(gathering)
                .team(team)
                .ballpark(ballpark)
                .title(request.getTitle())
                .content(request.getContent())
                .address(request.getAddress())
                .meetTime(request.getMeetTime())
                .maxParticipants(request.getMaxParticipants())
                .build();

        recruitmentRepository.save(recruitment);

        MemberGathering memberGathering = MemberGathering.builder()
                .gathering(gathering)
                .guest(host)
                .build();
        memberGatheringRepository.save(memberGathering);

        return creativeRecruitmentResponse(recruitment);
    }

    public List<RecruitmentResponse> findByTeamAndBallpark(Long teamId, Long ballparkId) {
        Specification<Recruitment> spec = RecruitmentSpecifications.byTeamAndBallpark(teamId, ballparkId);

        return recruitmentRepository.findAll(spec).stream()
                .map(this::creativeRecruitmentResponse)
                .collect(Collectors.toList());
    }

    /**
     * 모든 모집 글을 조회한다.
     */
    public List<RecruitmentResponse> findAllRecruitments() {
        return recruitmentRepository.findByDeletedAtIsNull().stream()
                .map(this::creativeRecruitmentResponse)
                .collect(Collectors.toList());
    }

    /**
     * 특정 모집 글을 조회한다.
     */
    public RecruitmentResponse findRecruitmentById(Long recruitmentId) {
        Recruitment recruitment = recruitmentRepository.findByIdAndDeletedAtIsNull(recruitmentId)
                .orElseThrow(() -> new RecruitmentException(ErrorCode.RECRUITMENT_NOT_FOUND));

        return creativeRecruitmentResponse(recruitment);
    }

    /**
     * 모집 글의 참여 인원 수를 조회한다.
     */
    public int getParticipantCount(Long recruitmentId) {
        Recruitment recruitment = findRecruitmentByIdEntity(recruitmentId);
        return (int) memberGatheringRepository.countByGatheringId(recruitment.getGathering().getId());
    }

    /**
     * 특정 팀의 모집 글을 조회한다.
     */
    public List<RecruitmentResponse> findRecruitmentsByTeam(Long teamId) {
        Team team = findByIdEntity(teamRepository, teamId, "Team");

        return recruitmentRepository.findByTeamAndDeletedAtIsNull(team).stream()
                .map(this::creativeRecruitmentResponse)
                .collect(Collectors.toList());
    }

    /**
     * 모집 글을 수정한다.
     */
    public RecruitmentResponse updateRecruitment(Long recruitmentId, RecruitmentRequest request, Long authorId) {
        Recruitment recruitment = findRecruitmentByIdEntity(recruitmentId);

        if (!recruitment.getGathering().getHost().getId().equals(authorId)) {
            throw new RecruitmentException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        validateRecruitment(request, recruitment.getTeam(), recruitment.getBallpark());

        recruitment.update(
                request.getTitle(),
                request.getContent(),
                request.getAddress(),
                request.getMeetTime(),
                request.getMaxParticipants()
        );

        recruitmentRepository.save(recruitment);

        return creativeRecruitmentResponse(recruitment);
    }

    /**
     * 회원이 모집 글에 참여한다.
     */
    public MemberGathering joinRecruitment(Long recruitmentId, Long memberId) {
        Recruitment recruitment = findRecruitmentByIdEntity(recruitmentId);

        Member member = findByIdEntity(memberRepository, memberId, "Member");

        if (isMemberAlreadyJoined(recruitment, member)) {
            throw new RecruitmentException(ErrorCode.MEMBER_ALREADY_JOINED);
        }

        if (isRecruitmentFull(recruitment)) {
            throw new RecruitmentException(ErrorCode.RECRUITMENT_FULL);
        }

        Gathering gathering = recruitment.getGathering();

        MemberGathering memberGathering = MemberGathering.builder()
                .gathering(gathering)
                .guest(member)
                .build();

        return memberGatheringRepository.save(memberGathering);
    }

    /**
     * 글 작성자가 참여 요청을 승인한다.
     */
    public void approveParticipation(Long recruitmentId, Long memberId, Long hostId) {
        Recruitment recruitment = findRecruitmentByIdEntity(recruitmentId);

        Member host = findByIdEntity(memberRepository, hostId, "Host");

        if (!recruitment.getGathering().getHost().equals(host)) {
            throw new RecruitmentException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        MemberGathering memberGathering = memberGatheringRepository.findByGatheringAndGuestId(recruitment.getGathering(), memberId)
                .orElseThrow(() -> new RecruitmentException(ErrorCode.PARTICIPATION_REQUEST_NOT_FOUND));

//        memberGathering.approve();
        memberGatheringRepository.save(memberGathering);
    }

    /**
     * 모집 글을 삭제한다.
     */
    public void deleteRecruitment(Long recruitmentId, Long hostId) {
        Recruitment recruitment = findRecruitmentByIdEntity(recruitmentId);

        Member host = findByIdEntity(memberRepository, hostId, "Host");

        if (!recruitment.getGathering().getHost().equals(host)) {
            throw new RecruitmentException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        recruitment.delete();
        recruitmentRepository.save(recruitment);
    }

    /**
     * 회원이 참여를 취소한다.
     */
    public void cancelParticipation(Long recruitmentId, Long memberId) {
        Recruitment recruitment = findRecruitmentByIdEntity(recruitmentId);

        Member member = findByIdEntity(memberRepository, memberId, "Member");

        MemberGathering memberGathering = memberGatheringRepository.findByGatheringAndGuestId(recruitment.getGathering(), member.getId())
                .orElseThrow(() -> new RecruitmentException(ErrorCode.PARTICIPATION_REQUEST_NOT_FOUND));

        memberGatheringRepository.delete(memberGathering);
    }

    /**
     * 모집 글의 참여 인원이 최대 제한에 도달했는지 확인한다.
     */
    private boolean isRecruitmentFull(Recruitment recruitment) {
        long participantCount = memberGatheringRepository.countByGatheringId(recruitment.getGathering().getId());
        return participantCount >= recruitment.getMaxParticipants();
    }

    /**
     * 회원이 이미 해당 모집 글에 참여했는지 확인한다.
     */
    private boolean isMemberAlreadyJoined(Recruitment recruitment, Member member) {
        return memberGatheringRepository.findByGatheringAndGuestId(recruitment.getGathering(), member.getId()).isPresent();
    }

    /**
     * 특정 모집 글을 엔티티로 조회한다.
     */
    private Recruitment findRecruitmentByIdEntity(Long recruitmentId) {
        return recruitmentRepository.findByIdAndDeletedAtIsNull(recruitmentId)
                .orElseThrow(() -> new RecruitmentException(ErrorCode.RECRUITMENT_NOT_FOUND));
    }

    /**
     * RecruitmentResponse 객체를 생성한다.
     */
    private RecruitmentResponse creativeRecruitmentResponse(Recruitment recruitment) {
        return new RecruitmentResponse(
                recruitment.getId(),
                recruitment.getTitle(),
                recruitment.getContent(),
                recruitment.getAddress(),
                recruitment.getMeetTime(),
                recruitment.getMaxParticipants(),
                recruitment.getTeam(),
                recruitment.getBallpark()
        );
    }

    private <T> T findByIdEntity(JpaRepository<T, Long> repository, Long id, String entityName) {
        return repository.findById(id)
                .orElseThrow(() -> new RecruitmentException(ErrorCode.RECRUITMENT_NOT_FOUND));
    }

    private void validateRecruitment(RecruitmentRequest request, Team team, Ballpark ballpark) {
        if (team == null) {
            throw new RecruitmentException(ErrorCode.INVALID_TEAM);
        }
        if (ballpark == null) {
            throw new RecruitmentException(ErrorCode.INVALID_BALLPARK);
        }
        if (request.getTitle() == null || request.getTitle().isEmpty()) {
            throw new RecruitmentException(ErrorCode.RECRUITMENT_EMPTY_TITLE);
        }
        if (request.getContent() == null || request.getContent().isEmpty()) {
            throw new RecruitmentException(ErrorCode.RECRUITMENT_EMPTY_CONTENT);
        }
        if (request.getAddress() == null || request.getAddress().isEmpty()) {
            throw new RecruitmentException(ErrorCode.EMPTY_ADDRESS);
        }
        if (request.getMeetTime() == null) {
            throw new RecruitmentException(ErrorCode.INVALID_MEET_TIME);
        }
        if (request.getMaxParticipants() <= 1) {
            throw new RecruitmentException(ErrorCode.INVALID_MAX_PARTICIPANTS);
        }
    }
}
