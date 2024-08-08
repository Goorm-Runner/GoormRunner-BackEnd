package goorm_runner.backend.recruitment.dto;

import goorm_runner.backend.ballpark.domain.Ballpark;
import goorm_runner.backend.team.domain.Team;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RecruitmentResponse {
    private Long id;
    private String title;
    private String content;
    private String address;
    private LocalDateTime meetTime;
    private int maxParticipants;
    private String teamName;
    private String ballparkName;

    public RecruitmentResponse(Long id, String title, String content, String address,
                               LocalDateTime meetTime, int maxParticipants, Team team, Ballpark ballpark) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.address = address;
        this.meetTime = meetTime;
        this.maxParticipants = maxParticipants;
        this.teamName = team.getName();
        this.ballparkName = ballpark.getName();
    }
}
