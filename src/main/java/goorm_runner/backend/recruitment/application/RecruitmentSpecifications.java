package goorm_runner.backend.recruitment.application;

import goorm_runner.backend.recruitment.domain.Recruitment;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class RecruitmentSpecifications {
    public static Specification<Recruitment> byTeamAndBallpark(Long teamId, Long ballparkId) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (teamId != null) {
                predicates.add(cb.equal(root.get("team").get("id"), teamId));
            }
            if (ballparkId != null) {
                predicates.add(cb.equal(root.get("ballpark").get("id"), ballparkId));
            }

            predicates.add(cb.isNull(root.get("deletedAt")));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
