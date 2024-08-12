package goorm_runner.backend.member.security.dto;

import goorm_runner.backend.recruitment.dto.RecruitmentResponse;
import lombok.Getter;

import java.util.List;

@Getter
public class MemberRecruitmentResponse {
    private MemberResponse member;
    private List<RecruitmentResponse> recruitments;

    public MemberRecruitmentResponse(MemberResponse member, List<RecruitmentResponse> recruitments) {
        this.member = member;
        this.recruitments = recruitments;
    }
}
