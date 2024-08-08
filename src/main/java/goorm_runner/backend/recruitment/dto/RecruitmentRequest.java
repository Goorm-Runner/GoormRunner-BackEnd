package goorm_runner.backend.recruitment.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RecruitmentRequest {
    private String title;
    private String content;
    private String address;
    private LocalDateTime meetTime;
    private Long teamId;
    private Long ballparkId;
    private int maxParticipants;
}
