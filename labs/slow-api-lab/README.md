# slow-api-lab

서버 개발 과외용 실습 프로젝트. 일부러 병목이 들어 있는 Spring Boot API입니다.

- Java 17 이상, Maven (또는 IntelliJ 같은 IDE) 필요
- DB는 인메모리 H2라 별도 설치가 필요 없습니다.

## 실행

```bash
mvn spring-boot:run
# IDE를 쓴다면 pom.xml을 프로젝트로 열고 SlowApiLabApplication 을 실행
```

서버가 뜨면 http://localhost:8080/api/teams 를 브라우저로 열어 JSON이 나오는지 확인하세요.

## 엔드포인트

| 경로 | 설명 |
| --- | --- |
| `GET /api/teams` | 팀 목록과 각 팀의 멤버 이름 |
| `GET /api/stats` | 지금까지 DB로 나간 SQL 개수 |
| `POST /api/stats/reset` | SQL 개수를 0으로 초기화 |

## 부하 테스트 (설치 없이 Java만으로)

```bash
# 동시 사용자 1명, 10초
java loadtest/LoadTest.java http://localhost:8080/api/teams 1 10

# 동시 사용자 20명, 10초
java loadtest/LoadTest.java http://localhost:8080/api/teams 20 10
```

처음 3초는 워밍업이라 결과에서 제외됩니다.

## 주의

- `spring.jpa.show-sql=true` 는 요청 1건 확인용으로만 쓰고, 부하 테스트 전에는 반드시 false 로 되돌리세요.
- 노트북 상태(전원 연결, 다른 무거운 프로그램 종료)에 따라 숫자는 달라집니다. 절대값보다 **전후 비교**가 중요합니다.
