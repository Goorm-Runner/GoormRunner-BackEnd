package goorm_runner.backend.member.security.dto;

import goorm_runner.backend.member.domain.Member;
import goorm_runner.backend.member.domain.Sex;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class MemberResponse {
    private String nickname;
    private Sex sex;
    private LocalDate birth;
    private Long teamId;

    public MemberResponse(Member member) {
        this.nickname = member.getNickname();
        this.sex = member.getSex();
        this.birth = member.getBirth();
        this.teamId = member.getTeamId();
    }
}