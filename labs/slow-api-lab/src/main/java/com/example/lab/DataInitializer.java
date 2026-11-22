package com.example.lab;

import com.example.lab.domain.Member;
import com.example.lab.domain.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import org.hibernate.SessionFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/** 서버 시작 시 팀 200개 x 멤버 15명 = 3,000명의 테스트 데이터를 넣는다. */
@Component
public class DataInitializer implements ApplicationRunner {

    private static final int TEAM_COUNT = 200;
    private static final int MEMBERS_PER_TEAM = 15;

    @PersistenceContext
    private EntityManager em;

    private final EntityManagerFactory emf;

    public DataInitializer(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        for (int t = 1; t <= TEAM_COUNT; t++) {
            Team team = new Team("team-" + t);
            em.persist(team);
            for (int m = 1; m <= MEMBERS_PER_TEAM; m++) {
                em.persist(new Member("member-" + t + "-" + m, team));
            }
        }
        em.flush();
        em.clear();
        // 초기 데이터 적재에 쓰인 SQL은 카운트에서 제외
        emf.unwrap(SessionFactory.class).getStatistics().clear();
    }
}
