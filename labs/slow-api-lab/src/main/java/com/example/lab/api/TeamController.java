package com.example.lab.api;

import jakarta.persistence.EntityManagerFactory;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TeamController {

    private final TeamService teamService;
    private final Statistics statistics;

    public TeamController(TeamService teamService, EntityManagerFactory emf) {
        this.teamService = teamService;
        this.statistics = emf.unwrap(SessionFactory.class).getStatistics();
    }

    /** 팀 목록과 각 팀의 멤버 이름을 반환한다. */
    @GetMapping("/teams")
    public List<TeamResponse> teams() {
        return teamService.findAllTeams();
    }

    /** 지금까지 DB로 나간 SQL(PreparedStatement) 개수. */
    @GetMapping("/stats")
    public Map<String, Object> stats() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("sqlStatementCount", statistics.getPrepareStatementCount());
        return result;
    }

    /** 카운터를 0으로 되돌린다. */
    @PostMapping("/stats/reset")
    public Map<String, Object> reset() {
        statistics.clear();
        return stats();
    }
}
