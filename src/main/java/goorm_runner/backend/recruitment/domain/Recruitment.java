package goorm_runner.backend.recruitment.domain;

import goorm_runner.backend.ballpark.domain.Ballpark;
import goorm_runner.backend.common.BaseTimeEntity;
import goorm_runner.backend.team.domain.Team;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recruitment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "gathering_id", nullable = false)
    private Gathering gathering;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne
    @JoinColumn(name = "ballpark_id", nullable = false)
    private Ballpark ballpark;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private LocalDateTime meetTime;

    @Column
    private LocalDateTime deletedAt;

    @Column(nullable = false)
    private int maxParticipants;

    @Builder
    public Recruitment(Gathering gathering, Team team, Ballpark ballpark, String title,
                       String content, String address, LocalDateTime meetTime, int maxParticipants) {
        this.gathering = gathering;
        this.team = team;
        this.ballpark = ballpark;
        this.title = title;
        this.content = content;
        this.address = address;
        this.meetTime = meetTime;
        this.maxParticipants = maxParticipants;
    }

    public void update(String title, String content, String address, LocalDateTime meetTime, int maxParticipants) {
        this.title = title;
        this.content = content;
        this.address = address;
        this.meetTime = meetTime;
        this.maxParticipants = maxParticipants;
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }
}
