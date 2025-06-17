# 🚘 DriveMate


**음성 분석 기반 졸음 감지 및 음성 대화를 통한 졸음 운전 예방 서비스**

---

## ✨ Project Goal
졸음 운전 사고를 예방하기 위해,
1. **운전자 음성 분석**을 통해 졸음을 감지하는 시스템 개발
2. **생성형 AI**를 활용한 개인 맞춤형 음성 대화 제공
3. **실시간 경고 및 간단한 스트레칭 음성 안내**로 졸음 운전 상황 방지

이 서비스는 사용자의 음성을 기반으로 졸음의 징후를 실시간으로 감지하며, AI와의 자연스러운 대화를 통해 운전자가 깨어 있을 수 있도록 돕는 것을 목표로 합니다.

---


## 🗂️ 프로젝트 구조 (BE)

```
src
└── main
    ├── java
    │   └── x10.drivemate
    │       ├── common               # 예외, 응답 포맷, 상태 코드 등 공통 처리
    │       ├── domain               # 기능별 도메인 계층 (chat, member 등)
    │       └── global               # 전역 설정 (보안, 설정 등)
    │           ├── security
    │           └── DrivemateApplication.java  # 메인 실행 클래스
    └── resources
        └── application.properties   # 환경설정 (DB, JWT, OAuth 등)
```
---
## 🚀 실행 환경

### 1. ✅ 사전 준비
- Java 17 이상  
- MySQL 인스턴스 (또는 로컬 설치)  
- Gradle 설치 (또는 ./gradlew 사용)  
- 다음 환경 변수 필수:  
  - OpenAI API Key  
  - Kakao OAuth 인증 정보  
  - JWT Secret Key
</br>

### 2. 환경 변수 또는 .env 설정
EC2나 로컬 IDE에서 실행 시, 아래 값을 .env 또는 시스템 환경 변수로 지정하세요.
</br>application.properties는 아래와 같은 환경 변수 의존성이 있습니다.
```bash
RDS_URL=
RDS_USERNAME=
RDS_PASSWORD=
AI_KEY=
OAUTH_REST=
OAUTH_CLIENT=
JWT_KEY=
```
</br>

### 3. 빌드 및 실행
```bash
# Gradle 빌드
./gradlew build

# 실행
java -jar build/libs/drivemate-0.0.1-SNAPSHOT.jar
```
</br>

### 4. Docker로 실행
```bash
docker build -t drivemate-server .
docker run -d -p 8080:8080 --env-file .env drivemate-server
```
</br>

---

## 🎯 API TEST 안내
DriveMate는 Kakao OAuth2 기반 인증/인가 시스템을 사용합니다.</br>
따라서 대부분의 API는 프론트엔드에서 전달받은 암호화된 카카오 ID 없이는 정상 동작하지 않습니다.</br>
API 테스트를 위해서는 Kakao 인증 절차가 선행되어야 합니다.


