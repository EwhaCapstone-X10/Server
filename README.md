# 🚘 DriveMate
**음성 분석 기반 졸음 감지 및 음성 대화를 통한 졸음 운전 예방 서비스**

---

## ✨ Project Goal
졸음 운전 사고를 예방하기 위해,
1. **운전자 음성 분석**을 통해 졸음을 감지하는 시스템 개발
2. **생성형 AI**를 활용한 개인 맞춤형 음성 대화 제공
3. **실시간 경고 및 운전 중 가능한 간단한 스트레칭 안내**로 졸음 운전 상황 방지

이 서비스는 사용자의 음성을 기반으로 졸음의 징후를 실시간으로 감지하며, AI와의 자연스러운 대화를 통해 운전자가 깨어 있을 수 있도록 돕는 것을 목표로 합니다.

---

## 👩🏻‍💻 Tech Stacks         

### FrontEnd
- React-Native: [공식 문서](https://ko.legacy.reactjs.org/)
- TypeScript: [공식 문서](https://www.typescriptlang.org/ko/)

### BackEnd
- Spring Boot: [공식 문서](https://spring.io/projects/spring-boot)
- MySQL: [공식 문서](https://www.mysql.com)

### AI
- **개인 맞춤형 대화 생성**:
  - GPT-4o Audio Preview: 자연스러운 맞춤형 음성 대화 구현
- **하품 감지**:
  - Librosa, Wav2Vec 2.0: Audio Feature 추출
  - scikit-learn RandomForestClassifier: 졸린 음성 이진 분류
  - YAMNet: 하품 감지 transfer learning을 위한 pretrained 모델
  - TensorFlow 2.0: YAMNet 및 딥러닝 모델 구현

### 배포 및 CI/CD
- GitHub Actions
- AWS EC2, RDS
- Docker

### Communication
- Notion
- Figma

<br/>
