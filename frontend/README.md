# 도란 쇼핑몰 프론트엔드

Next.js를 사용한 도란 쇼핑몰의 프론트엔드 애플리케이션입니다. 이 프로젝트는 Spring Cloud 기반의 마이크로서비스 아키텍처로 구성된 백엔드 서비스들과 통신합니다.

## 기술 스택

- **Framework**: Next.js 15.5.2 (React 19)
- **Language**: TypeScript
- **Styling**: Tailwind CSS 4
- **State Management**: React Query (@tanstack/react-query)
- **Form Handling**: React Hook Form + Zod
- **HTTP Client**: Axios
- **Icons**: Lucide React

## 프로젝트 구조

```
frontend/
├── src/
│   ├── app/                    # Next.js App Router 페이지들
│   │   ├── cart/              # 장바구니 페이지
│   │   ├── login/             # 로그인 페이지
│   │   ├── orders/            # 주문 관련 페이지
│   │   ├── products/          # 상품 관련 페이지
│   │   ├── signup/            # 회원가입 페이지
│   │   ├── layout.tsx         # 루트 레이아웃
│   │   └── page.tsx           # 메인 페이지
│   ├── components/            # 재사용 가능한 컴포넌트들
│   │   ├── ErrorBoundary.tsx # 에러 바운더리
│   │   ├── Footer.tsx         # 푸터 컴포넌트
│   │   ├── Header.tsx         # 헤더 컴포넌트
│   │   ├── LoadingSpinner.tsx # 로딩 스피너
│   │   ├── ProtectedRoute.tsx # 인증 라우트 보호
│   │   └── QueryProvider.tsx  # React Query 프로바이더
│   ├── contexts/              # React Context들
│   │   └── AuthContext.tsx    # 인증 컨텍스트
│   ├── lib/                   # 라이브러리 설정
│   │   └── queryClient.ts     # React Query 클라이언트 설정
│   └── utils/                 # 유틸리티 함수들
│       └── api.ts             # API 호출 함수들
├── public/                    # 정적 파일들
├── package.json               # 프로젝트 의존성
└── tsconfig.json             # TypeScript 설정
```

## 주요 기능

### 1. 인증 기능 (Auth Service)
- **회원가입** (`/signup`): 새로운 사용자 등록
  - 사용자 ID, 비밀번호, 이름, 생년월일, 이메일, 연락처 입력
  - API: `POST /auth-service/api/join`
- **로그인** (`/login`): 사용자 로그인 및 JWT 토큰 발급
  - API: `POST /auth-service/api/login`
  - 로컬 스토리지에 토큰 저장
- **로그아웃**: JWT 토큰 무효화
  - API: `DELETE /auth-service/api/logout`

### 2. 상품 관리 기능 (Item Service)
- **메인 페이지** (`/`): 베스트 상품 표시 및 서비스 소개
  - API: `GET /item-service/bestitems`
- **상품 목록** (`/products`): 전체 상품 조회 및 검색
  - 베스트 상품 목록 표시
  - 키워드로 상품 검색
  - API: `GET /item-service/book/search?keywords={keywords}`
- **상품 상세** (`/products/[id]`): 상품 상세 정보 조회
  - 상품 이미지, 이름, 가격, 설명 표시
  - 수량 선택 및 장바구니 담기
  - API: `GET /item-service/book/detail?itemUuid={itemUuid}`

### 3. 장바구니 기능 (Member Service)
- **장바구니 페이지** (`/cart`): 장바구니 관리
  - 장바구니 아이템 목록 조회
  - API: `GET /member-service/basket/list`
  - 아이템 선택 및 삭제
  - API: `DELETE /member-service/basket/deleteItem?itemUuid={itemUuid}`
  - 총 금액 계산 및 주문하기
- **장바구니 추가**: 상품 페이지에서 장바구니에 상품 추가
  - API: `POST /member-service/basket/addItem`

### 4. 주문 관리 기능 (Order Service)
- **주문 목록** (`/orders`): 사용자의 주문 내역 조회
  - 주문 상태별 필터링 (전체, 주문확인중, 주문확인됨, 배송중, 배송완료, 주문취소)
  - API: `GET /order-service/list`
- **주문 상세** (`/orders/[id]`): 주문 상세 정보 조회
  - 주문 상품, 배송지, 결제 정보 표시
  - API: `GET /order-service/detail?orderUuid={orderUuid}`
- **주문 취소**: 주문 상태가 'PENDING'일 때 취소 가능
  - API: `PUT /order-service/cancel?orderUuid={orderUuid}`

## API Gateway 통신

모든 API 요청은 API Gateway (`http://localhost:8000`)를 통해 라우팅됩니다:

```typescript
// API 엔드포인트 예시
const API_BASE_URL = 'http://localhost:8000';

// Auth Service
POST /auth-service/api/join         → http://localhost:8000/auth-service/api/join
POST /auth-service/api/login        → http://localhost:8000/auth-service/api/login

// Member Service
POST /member-service/basket/addItem → http://localhost:8000/member-service/basket/addItem
GET  /member-service/basket/list    → http://localhost:8000/member-service/basket/list

// Item Service
GET  /item-service/bestitems        → http://localhost:8000/item-service/bestitems
GET  /item-service/book/detail      → http://localhost:8000/item-service/book/detail

// Order Service
POST /order-service/orders          → http://localhost:8000/order-service/orders
GET  /order-service/list            → http://localhost:8000/order-service/list
```

## 시작하기

### 필수 요구사항

- Node.js 20 이상
- npm 또는 yarn

### 설치

```bash
# 의존성 설치
npm install

# 또는
yarn install
```

### 개발 서버 실행

```bash
# 개발 서버 시작 (Turbopack 사용)
npm run dev

# 또는
yarn dev
```

브라우저에서 [http://localhost:3000](http://localhost:3000)을 열어 애플리케이션을 확인하세요.

### 빌드

```bash
# 프로덕션 빌드
npm run build

# 프로덕션 서버 실행
npm run start
```

## 주요 컴포넌트 설명

### AuthContext
사용자 인증 상태를 관리하는 React Context입니다.
- 로그인/로그아웃 기능
- 사용자 정보 저장 (로컬 스토리지)
- 인증 상태 확인

```typescript
const { user, isAuthenticated, login, logout } = useAuth();
```

### API 유틸리티 (`utils/api.ts`)
백엔드 API와 통신하는 함수들을 제공합니다.
- `apiCall()`: 기본 API 호출 함수
- 각 서비스별 API 함수들 (인증, 상품, 장바구니, 주문 등)
- 자동 JWT 토큰 처리

```typescript
// 예시: 로그인
const result = await login({ userId, password });

// 예시: 장바구니에 추가
const result = await addToBasket(userUuid, itemUuid, count);
```

### React Query
서버 상태 관리를 위해 React Query를 사용합니다.
- 자동 캐싱
- 백그라운드 refetch
- 낙관적 업데이트

```typescript
// 예시: 상품 목록 조회
const { data, isLoading, error } = useQuery({
  queryKey: ['bestItems'],
  queryFn: getBestItems,
  staleTime: 5 * 60 * 1000, // 5분
});
```

## 코드 주석 가이드

이 프로젝트의 모든 주요 컴포넌트와 함수에는 React와 Next.js 초보자를 위한 상세한 주석이 포함되어 있습니다:

- **컴포넌트 설명**: 각 컴포넌트의 역할과 책임
- **상태 관리**: useState, useQuery 등의 훅 사용법
- **이벤트 핸들러**: 사용자 액션 처리 방법
- **API 호출**: 백엔드와의 통신 방법
- **조건부 렌더링**: 다양한 상태에 따른 UI 표시

## 스타일링

Tailwind CSS를 사용하여 유틸리티 기반 스타일링을 적용했습니다:

```jsx
<button className="bg-indigo-600 text-white px-4 py-2 rounded-md hover:bg-indigo-700">
  클릭하기
</button>
```

## 향후 개선 사항

- [ ] 주문 생성 기능 완성
- [ ] 배송 추적 기능 구현
- [ ] 사용자 프로필 페이지
- [ ] 상품 리뷰 및 평점 시스템
- [ ] 이미지 업로드 기능
- [ ] 페이지네이션 개선
- [ ] 반응형 디자인 최적화
- [ ] 다크 모드 지원

## 트러블슈팅

### CORS 에러
API Gateway에서 CORS 설정이 필요합니다. API Gateway의 설정을 확인하세요.

### 인증 토큰 만료
토큰이 만료된 경우 자동으로 갱신되지 않습니다. 다시 로그인해야 합니다.

### 로컬 스토리지 문제
브라우저 개발자 도구 → Application → Local Storage에서 저장된 데이터를 확인할 수 있습니다.

## 라이선스

이 프로젝트는 학습 목적으로 제작되었습니다.
