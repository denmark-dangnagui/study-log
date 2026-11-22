# study-log

서버 개발 12주 공부 기록. 하루 30분, 직접 실행하고 측정한 것을 남긴다.

## 구조

```
study-log/
├── README.md
├── days/
│   ├── _template.md          # 매일 복사해서 쓰는 기록 양식
│   └── day-01-bottleneck.md  # 1일차 기록
└── labs/
    └── slow-api-lab/         # 실습 프로젝트 (Spring Boot + H2)
```

- `days/` : 그날의 예측, 측정값, 배운 점. 노션/블로그 정리의 원본이 된다.
- `labs/` : 실습 코드. 고친 코드는 커밋 메시지에 "무엇을 왜 고쳤는지"를 적는다.

## 매일 루틴 (두 컴퓨터에서 이어서 하기)

```bash
# 시작할 때: 다른 컴퓨터에서 한 작업을 먼저 받아온다
git pull

# 끝낼 때
git add -A
git commit -m "day01: N+1 측정, SQL 1+N회 확인"
git push
```

커밋 메시지 예시
- `day01: 부하 1명/20명 측정 기록`
- `day02: fetch join으로 N+1 해결, SQL 201회 -> 1회`

## 지킬 것

- **회사 코드, 회사 데이터, 사내 URL, 계정 정보는 절대 넣지 않는다.** 여기에는 실습용 가짜 데이터만 둔다.
- 저장소는 **private**으로 만든다.
- 회사 컴퓨터에서는 이 저장소의 커밋 이메일을 개인 이메일로 지정한다.
  ```bash
  git config user.name  "내 이름"
  git config user.email "개인 이메일"
  ```
  (`--global`을 붙이지 않으면 이 저장소에만 적용된다.)
