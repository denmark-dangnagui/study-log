package com.example.lab.api;

import com.example.lab.domain.Member;
import com.example.lab.domain.TeamRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TeamService {

    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Transactional(readOnly = true)
    public List<TeamResponse> findAllTeams() {
        return teamRepository.findAll().stream()
                .map(team -> new TeamResponse(
                        team.getName(),
                        team.getMembers().stream().map(Member::getName).toList()))
                .toList();
    }
}
