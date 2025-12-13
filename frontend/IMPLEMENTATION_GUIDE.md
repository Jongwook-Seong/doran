# 도란 쇼핑몰 프론트엔드 구현 가이드

이 문서는 Next.js와 React를 처음 접하는 개발자를 위한 상세한 구현 가이드입니다.

## 목차

1. [프로젝트 구조 이해하기](#프로젝트-구조-이해하기)
2. [핵심 개념](#핵심-개념)
3. [주요 패턴](#주요-패턴)
4. [API 통신](#api-통신)
5. [상태 관리](#상태-관리)
6. [라우팅](#라우팅)

## 프로젝트 구조 이해하기

### Next.js App Router

Next.js 13+ 버전에서는 `app` 디렉토리를 사용하여 파일 시스템 기반 라우팅을 구현합니다:

```
app/
├── page.tsx              → / (메인 페이지)
├── login/
│   └── page.tsx         → /login
├── products/
│   ├── page.tsx         → /products (상품 목록)
│   └── [id]/
│       └── page.tsx     → /products/:id (상품 상세)
└── layout.tsx           → 모든 페이지에 적용되는 레이아웃
```

### 컴포넌트 분류

1. **페이지 컴포넌트** (`app/*/page.tsx`): URL 경로에 대응하는 컴포넌트
2. **레이아웃 컴포넌트** (`app/*/layout.tsx`): 여러 페이지에서 공유하는 UI
3. **재사용 컴포넌트** (`components/`): 여러 곳에서 사용되는 컴포넌트

## 핵심 개념

### 1. 클라이언트 컴포넌트 vs 서버 컴포넌트

```typescript
'use client';  // 이 지시어가 있으면 클라이언트 컴포넌트

import { useState } from 'react';

export default function MyComponent() {
  // useState, useEffect 등의 훅을 사용하려면 클라이언트 컴포넌트여야 함
  const [count, setCount] = useState(0);
  // ...
}
```

**언제 'use client'를 사용해야 할까?**
- `useState`, `useEffect` 등 React 훅 사용 시
- 브라우저 이벤트 (onClick, onChange 등) 처리 시
- 로컬 스토리지, 세션 스토리지 접근 시

### 2. TypeScript 타입 정의

```typescript
// 인터페이스로 데이터 구조 정의
interface User {
  userUuid: string;
  userId: string;
  username: string;
  email: string;
}

// 컴포넌트 Props 타입 정의
interface ProductCardProps {
  item: Item;
  onAddToCart: (itemUuid: string) => void;
}

function ProductCard({ item, onAddToCart }: ProductCardProps) {
  // ...
}
```

### 3. React Hooks

#### useState - 상태 관리

```typescript
import { useState } from 'react';

function Counter() {
  // [현재 값, 값을 변경하는 함수] = useState(초기값)
  const [count, setCount] = useState(0);

  const increment = () => {
    setCount(count + 1);  // 또는 setCount(prev => prev + 1)
  };

  return <button onClick={increment}>Count: {count}</button>;
}
```

#### useEffect - 부수 효과 처리

```typescript
import { useEffect } from 'react';

function MyComponent() {
  useEffect(() => {
    // 컴포넌트가 마운트될 때 실행
    console.log('Component mounted');

    // cleanup 함수 (언마운트 시 실행)
    return () => {
      console.log('Component unmounted');
    };
  }, []); // 빈 배열 = 한 번만 실행

  useEffect(() => {
    // count가 변경될 때마다 실행
    console.log('Count changed');
  }, [count]); // 의존성 배열

  // ...
}
```

## 주요 패턴

### 1. API 호출 패턴 (React Query 사용)

```typescript
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';

function ProductList() {
  // 데이터 조회 (GET)
  const { data, isLoading, error } = useQuery({
    queryKey: ['products'],           // 캐시 키
    queryFn: getProducts,             // API 호출 함수
    staleTime: 5 * 60 * 1000,        // 5분간 캐시 유지
  });

  // 데이터 변경 (POST, PUT, DELETE)
  const queryClient = useQueryClient();
  const mutation = useMutation({
    mutationFn: addProduct,           // API 호출 함수
    onSuccess: () => {
      // 성공 시 캐시 무효화 (다시 불러오기)
      queryClient.invalidateQueries({ queryKey: ['products'] });
    },
  });

  // 로딩 상태 처리
  if (isLoading) return <div>Loading...</div>;
  
  // 에러 상태 처리
  if (error) return <div>Error occurred</div>;

  // 정상 상태
  return <div>{/* 데이터 렌더링 */}</div>;
}
```

### 2. 폼 처리 패턴

```typescript
function LoginForm() {
  // 폼 입력 상태 관리
  const [formData, setFormData] = useState({
    userId: '',
    password: ''
  });

  // 입력 필드 변경 처리
  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value  // 해당 필드만 업데이트
    }));
  };

  // 폼 제출 처리
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();  // 기본 폼 제출 동작 방지
    
    // API 호출
    const result = await login(formData);
    
    if (result.success) {
      // 성공 처리
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <input
        name="userId"
        value={formData.userId}
        onChange={handleInputChange}
      />
      <button type="submit">로그인</button>
    </form>
  );
}
```

### 3. 조건부 렌더링 패턴

```typescript
function ProductPage() {
  const { isLoading, error, data } = useQuery({...});

  // 패턴 1: 조기 반환 (Early Return)
  if (isLoading) return <LoadingSpinner />;
  if (error) return <ErrorMessage />;
  if (!data) return <NoData />;

  // 패턴 2: 삼항 연산자
  return (
    <div>
      {isAuthenticated ? <UserMenu /> : <LoginButton />}
    </div>
  );

  // 패턴 3: 논리 AND 연산자
  return (
    <div>
      {user && <WelcomeMessage user={user} />}
    </div>
  );
}
```

## API 통신

### API 호출 함수 작성 (`utils/api.ts`)

```typescript
// 기본 API 호출 함수
async function apiCall<T>(
  url: string,
  method: 'GET' | 'POST' | 'PUT' | 'DELETE',
  data?: any,
  headers?: Record<string, string>
): Promise<ApiResponse<T>> {
  try {
    const requestHeaders: Record<string, string> = {
      'Content-Type': 'application/json',
      ...headers,
    };

    // 인증 토큰 추가
    const token = localStorage.getItem('token');
    if (token) {
      requestHeaders['Authorization'] = `Bearer ${token}`;
    }

    const requestOptions: RequestInit = {
      method,
      headers: requestHeaders,
    };

    // POST, PUT의 경우 body 추가
    if (data && ['POST', 'PUT'].includes(method)) {
      requestOptions.body = JSON.stringify(data);
    }

    const response = await fetch(url, requestOptions);
    const responseData = await response.json();

    if (response.ok) {
      return { success: true, data: responseData };
    } else {
      return { success: false, error: responseData.message };
    }
  } catch (error) {
    return { success: false, error: '네트워크 오류' };
  }
}

// 특정 API 함수
export async function login(credentials: LoginRequest) {
  return apiCall('/auth-service/api/login', 'POST', credentials);
}
```

### API 게이트웨이 주소 변환

모든 API 요청은 API Gateway를 통해 라우팅됩니다:

```typescript
// 프론트엔드에서 호출
POST http://localhost:8000/auth-service/api/login

// API Gateway가 변환
→ POST http://auth-service-instance/api/login
```

## 상태 관리

### 1. 로컬 상태 (useState)

컴포넌트 내부에서만 사용하는 상태:

```typescript
function Counter() {
  const [count, setCount] = useState(0);
  // 이 count는 이 컴포넌트에서만 접근 가능
}
```

### 2. 전역 상태 (React Context)

여러 컴포넌트에서 공유하는 상태:

```typescript
// AuthContext.tsx
const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<User | null>(null);

  const login = (userData: User) => {
    setUser(userData);
    localStorage.setItem('user', JSON.stringify(userData));
  };

  return (
    <AuthContext.Provider value={{ user, login }}>
      {children}
    </AuthContext.Provider>
  );
}

// 다른 컴포넌트에서 사용
function Header() {
  const { user, login } = useAuth();  // Context 사용
  return <div>Welcome, {user?.name}</div>;
}
```

### 3. 서버 상태 (React Query)

서버에서 가져온 데이터의 상태:

```typescript
// 자동 캐싱, 재시도, 백그라운드 업데이트 제공
const { data, isLoading } = useQuery({
  queryKey: ['products'],
  queryFn: getProducts,
});
```

## 라우팅

### 1. 페이지 이동

```typescript
import { useRouter } from 'next/navigation';
import Link from 'next/link';

function MyComponent() {
  const router = useRouter();

  // 프로그래밍 방식 이동
  const goToProducts = () => {
    router.push('/products');
  };

  return (
    <>
      {/* 선언적 방식 (권장) */}
      <Link href="/products">상품 보기</Link>
      
      {/* 프로그래밍 방식 */}
      <button onClick={goToProducts}>상품 보기</button>
    </>
  );
}
```

### 2. 동적 라우트

```typescript
// app/products/[id]/page.tsx
import { useParams } from 'next/navigation';

export default function ProductDetail() {
  const params = useParams();
  const productId = params.id;  // URL에서 ID 추출

  // /products/123 → productId = "123"
  // /products/abc → productId = "abc"
}
```

## 스타일링 (Tailwind CSS)

### 기본 사용법

```tsx
// 유틸리티 클래스 조합
<button className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">
  클릭
</button>

// 반응형 디자인
<div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4">
  {/* 모바일: 1열, 태블릿: 2열, 데스크톱: 4열 */}
</div>

// 조건부 스타일
<button 
  className={`px-4 py-2 ${isActive ? 'bg-blue-500' : 'bg-gray-500'}`}
>
  버튼
</button>
```

## 모범 사례

### 1. 컴포넌트 분리

```typescript
// ❌ 나쁜 예: 모든 로직이 한 컴포넌트에
function ProductPage() {
  // 200줄의 코드...
}

// ✅ 좋은 예: 역할별로 분리
function ProductPage() {
  return (
    <>
      <ProductHeader />
      <ProductList />
      <ProductFooter />
    </>
  );
}
```

### 2. 타입 안정성

```typescript
// ❌ any 사용
function processData(data: any) {
  return data.name;  // 오타 발생 가능
}

// ✅ 명시적 타입
interface Product {
  id: string;
  name: string;
  price: number;
}

function processProduct(product: Product) {
  return product.name;  // 타입 체크됨
}
```

### 3. 에러 처리

```typescript
// ✅ 모든 단계에서 에러 처리
try {
  const result = await apiCall();
  
  if (!result.success) {
    // API 레벨 에러
    setError(result.error);
    return;
  }
  
  // 정상 처리
  setData(result.data);
} catch (error) {
  // 네트워크 레벨 에러
  setError('네트워크 오류가 발생했습니다.');
}
```

## 디버깅 팁

### 1. React Query DevTools

개발 환경에서 자동으로 활성화됩니다. 브라우저 하단의 아이콘을 클릭하여:
- 캐시된 데이터 확인
- 쿼리 상태 모니터링
- 수동으로 데이터 재요청

### 2. 브라우저 개발자 도구

- **Console**: 로그 및 에러 확인
- **Network**: API 요청/응답 확인
- **Application > Local Storage**: 저장된 토큰 확인
- **React DevTools**: 컴포넌트 구조 및 Props 확인

### 3. 유용한 console.log 패턴

```typescript
// 함수 시작
console.log('handleSubmit called with:', formData);

// API 응답
console.log('API response:', { success: result.success, data: result.data });

// 조건문
console.log('isAuthenticated:', isAuthenticated);
```

## 추가 학습 자료

- [Next.js 공식 문서](https://nextjs.org/docs)
- [React 공식 문서](https://react.dev/)
- [React Query 문서](https://tanstack.com/query/latest)
- [Tailwind CSS 문서](https://tailwindcss.com/docs)
- [TypeScript 핸드북](https://www.typescriptlang.org/docs/handbook/intro.html)






