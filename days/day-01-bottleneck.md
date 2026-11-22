# Day 1. 부하 걸고 병목 찾기

- 날짜: 2026-11-18
- 사용한 시간: 분
- 컴퓨터: (집 / 회사)

## 1. 오늘의 주제와 핵심 용어

부하 테스트, 지연시간(latency), 처리량(TPS), 백분위수(p50/p95/p99), 병목, N+1 문제, 지연 로딩, 커넥션 풀

(정의는 공부 노트의 Day 1 탭 1장 참고)

## 2. 실행 전 예측 (실행하기 전에 먼저 적는다)

1. `DataInitializer`의 `TEAM_COUNT`:
2. `/api/teams` 1번 호출 시 SQL 개수 예측:
   - 그렇게 생각한 이유 (어느 줄 때문에?):
3. 동시 사용자 1명 -> 20명일 때 p95 변화 (1배 근처 / 2~5배 / 10배 이상):
   - 이유:

## 3. 실행과 측정

| 구분 | 동시 사용자 | TPS (req/s) | p50 (ms) | p95 (ms) | p99 (ms) | 요청 1건당 SQL 수 |
| --- | --- | --- | --- | --- | --- | --- |
| 내 예측 | 20 | | | | | |
| 실측 1 | 1 | | | | | |
| 실측 2 | 20 | | | | | |

실행한 명령:

```bash
cd labs/slow-api-lab
mvn spring-boot:run

curl -X POST http://localhost:8080/api/stats/reset
curl http://localhost:8080/api/teams > /dev/null
curl http://localhost:8080/api/stats

java loadtest/LoadTest.java http://localhost:8080/api/teams 1 10
java loadtest/LoadTest.java http://localhost:8080/api/teams 20 10
```

## 4. 예측과 실제의 차이

- 예측:
- 실제:
- 차이가 난 이유:

## 5. 오늘 정리할 질문 (내 말로 3~5줄, AI 없이)

1. N+1 문제는 무엇이고, 이 프로젝트에서는 코드의 어느 부분 때문에 생기는가?
2. 평균 응답시간 대신 p95를 보는 이유는 무엇인가?
3. 동시 사용자가 늘면 응답시간이 왜 늘어날 수 있는가? 오늘 측정에서 근거가 된 숫자는 무엇인가?

## 6. 막힌 점 / 내일 확인할 것

-
