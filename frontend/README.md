# 도란 쇼핑몰 프론트엔드

이 프로젝트는 도란 쇼핑몰의 프론트엔드 애플리케이션입니다. Next.js 15와 TypeScript, Tailwind CSS를 사용하여 구축되었습니다.

## 🚀 기술 스택

- **Framework**: Next.js 15 (App Router)
- **Language**: TypeScript
- **Styling**: Tailwind CSS
- **Package Manager**: npm

## 📁 프로젝트 구조

```
frontend/
├── src/
│   └── app/
│       ├── page.tsx          # 메인 홈페이지
│       ├── layout.tsx        # 루트 레이아웃
│       ├── globals.css       # 전역 스타일
│       └── signup/
│           └── page.tsx      # 회원가입 페이지
├── public/                   # 정적 파일들
├── package.json
├── next.config.ts           # Next.js 설정
└── tailwind.config.ts       # Tailwind CSS 설정
```

## 🛠️ 설치 및 실행

### 1. 의존성 설치
```bash
npm install
```

### 2. 개발 서버 실행
```bash
npm run dev
```

개발 서버는 기본적으로 `http://localhost:3000`에서 실행됩니다.

### 3. 프로덕션 빌드
```bash
npm run build
npm start
```

## 🔗 백엔드 서비스 연결

이 프론트엔드는 다음 마이크로서비스들과 연동됩니다:

- **auth-simple-service**: 회원가입, 로그인 기능
- **member-service**: 회원 관리, 장바구니 기능
- **item-service**: 상품 관리 기능
- **order-service**: 주문 및 배송 관리 기능

## 📱 주요 기능

### 현재 구현된 기능
- ✅ 회원가입 페이지 (`/signup`)
- ✅ 반응형 디자인
- ✅ 폼 유효성 검사
- ✅ 에러 처리

### 향후 구현 예정 기능
- 🔄 로그인 페이지
- 🔄 상품 목록/상세 페이지
- 🔄 장바구니 기능
- 🔄 주문 관리
- 🔄 사용자 프로필

## 🎨 UI/UX 특징

- **반응형 디자인**: 모바일, 태블릿, 데스크톱 모든 기기에서 최적화
- **모던한 디자인**: Tailwind CSS를 활용한 깔끔하고 현대적인 UI
- **사용자 친화적**: 직관적인 네비게이션과 명확한 CTA 버튼
- **접근성**: 웹 접근성 가이드라인 준수

## 🔧 개발 가이드

### 컴포넌트 작성 시 주의사항
- 모든 컴포넌트에 상세한 주석 추가
- TypeScript 타입 정의 필수
- 반응형 디자인 고려
- 에러 처리 및 로딩 상태 관리

### API 호출 패턴
```typescript
// 예시: 회원가입 API 호출
const response = await fetch('http://localhost:8080/users', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify(formData),
});
```

## 🚨 주의사항

1. **CORS 설정**: 백엔드 서비스에서 CORS 설정이 필요할 수 있습니다.
2. **환경 변수**: API 엔드포인트는 환경 변수로 관리하는 것을 권장합니다.
3. **에러 처리**: 모든 API 호출에 적절한 에러 처리가 필요합니다.

## 📝 라이센스

이 프로젝트는 MIT 라이센스 하에 배포됩니다.

## 🤝 기여하기

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

**도란 쇼핑몰 팀** 🛒
