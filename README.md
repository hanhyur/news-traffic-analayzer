# 뉴스 트래픽 분석 시스템

> Kafka + Redis 기반 실시간 뉴스 트래픽 수집 및 분석 플랫폼  
> 클린 아키텍처 · DDD · TDD 기반으로 설계된 사이드 프로젝트

---

## 주요 기능

-  기사 클릭, 좋아요 등 사용자 활동 수집 API
-  Kafka를 활용한 비동기 이벤트 처리 구조
-  Redis 기반 기사별 실시간 통계 저장 및 조회
-  (선택) 클릭 수 상위 기사 요약 (LLM API 연동)
-  클린 아키텍처 기반 설계 및 테스트 커버리지 확보

---

## 기술 스택

| 범주 | 기술 |
|------|------|
| 언어/프레임워크 | Kotlin / Java, Spring Boot 3 |
| 메시징 | Apache Kafka |
| 실시간 저장소 | Redis |
| DB | PostgreSQL |
| 테스트 | JUnit5, Mockito, Testcontainers |
| 문서화 | SpringDoc OpenAPI (Swagger) |
| 배포 | Docker, Docker Compose |
| AI 연동 | OpenAI GPT or HuggingFace API (옵션) |

---

## 시스템 아키텍처

```plaintext
[Client] → [Spring Web API] → [Kafka Producer] → Kafka Topic
		                                              ↓
		                                        [Kafka Consumer]
			                                          ↓
			                                  [Redis ←→ PostgreSQL]
		                                              ↓
		                                      [통계 조회 API (REST)]
```

---

## 폴더 구조

```scss
src
├── domain
│   └── traffic
│       ├── model
│       └── service
├── application
│   └── usecase
├── adapter
│   ├── in
│   │   └── web(API)
│   └── out
│       ├── kafka
│       └── redis 
├── config
└── support
```

---

## API 예시

### 트래픽 수집

`POST /api/v1/traffic`

```json
{
  "articleId": "1234",
  "eventType": "CLICK"
}
```

### 통계 조회

`GET /api/v1/stats/1234`

```json
{
  "articleId": "1234",
  "clickCount": 120,
  "likeCount": 5
}
```

## 테스트

-  단위 테스트 : UseCase, Domain, Repository
-  통합 테스트 : Kafka → Redis 흐름 검증
-  테스트 도구 : JUnit5, Mockito, Testcontainers

## 실행 방법

```bash
# Kafka / Redis / Postgres 실행
docker-compose up -d

# 애플리케이션 실행
./gradlew bootRun
```

## 확장 아이디어

-  뉴스 크롤링 및 기사 등록 자동화
-  사용자 세션 기반 추천 알고리즘
-  LLM 기반 뉴스 요약 기능 고도화
-  Prometheus + Grafana 모니터링 연동
---
