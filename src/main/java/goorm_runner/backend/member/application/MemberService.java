package goorm_runner.backend.member.application;

import goorm_runner.backend.global.ErrorCode;
import goorm_runner.backend.member.application.exception.MemberException;
import goorm_runner.backend.member.domain.Member;
import goorm_runner.backend.member.domain.MemberRepository;
import goorm_runner.backend.member.security.dto.MemberResponse;
import goorm_runner.backend.recruitment.domain.MemberGatheringRepository;
import goorm_runner.backend.recruitment.domain.Recruitment;
import goorm_runner.backend.recruitment.domain.RecruitmentRepository;
import goorm_runner.backend.recruitment.dto.RecruitmentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final RecruitmentRepository recruitmentRepository;
    private final MemberGatheringRepository memberGatheringRepository;

    public Long findMemberIdByUsername(String username) {
        Member member = memberRepository.findByLoginId(username)
                .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));
        return member.getId();
    }

    public void deleteMemberById(Long memberId){
        Member member = memberRepository.findByIdAndDeletedAtIsNull(memberId)
                .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));

        member.delete();
        memberRepository.save(member);
    }

    public void updateMemberById(Long memberId, MemberUpdateRequest request){
        Member updateMember = memberRepository.findByIdAndDeletedAtIsNull(memberId)
                .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));

        updateMember.update(request.getTeamId());
        memberRepository.save(updateMember);
    }

    public MemberResponse getMemberInfo(Long memberId){
        Member member = memberRepository.findByIdAndDeletedAtIsNull(memberId)
                .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));
        return new MemberResponse(member);
    }

    public List<RecruitmentResponse> findRecruitmentsByMember(Long memberId) {
        List<RecruitmentResponse> hostMember = recruitmentRepository.findByGathering_Host_IdAndDeletedAtIsNull(memberId)
                .stream()
                .map(this::creativeRecruitmentResponse)
                .toList();

        List<RecruitmentResponse> guestMember = recruitmentRepository.findByGuestIdAndDeletedAtIsNull(memberId)
                .stream()
                .map(this::creativeRecruitmentResponse)
                .toList();

        List<RecruitmentResponse> allRecruitments = new ArrayList<>();
        allRecruitments.addAll(hostMember);
        allRecruitments.addAll(guestMember);

        return allRecruitments;
    }

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
}
