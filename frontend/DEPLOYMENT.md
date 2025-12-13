# 배포 가이드

이 문서는 도란 쇼핑몰 프론트엔드를 프로덕션 환경에 배포하는 방법을 설명합니다.

## 배포 전 체크리스트

- [ ] 모든 기능이 정상 작동함 (TESTING_GUIDE.md 참조)
- [ ] 환경 변수 설정 완료
- [ ] 빌드 테스트 완료
- [ ] API Gateway URL 확인
- [ ] 백엔드 서비스 실행 확인

## 환경 변수 설정

### 개발 환경

프로젝트 루트에 `.env.local` 파일 생성:

```env
# API Gateway URL
NEXT_PUBLIC_API_BASE_URL=http://localhost:8000

# 환경
NODE_ENV=development
```

### 프로덕션 환경

프로덕션 서버에 맞게 API Gateway URL을 변경:

```env
# API Gateway URL (프로덕션)
NEXT_PUBLIC_API_BASE_URL=https://api.your-domain.com

# 환경
NODE_ENV=production
```

**⚠️ 주의**: 현재 코드에서는 `utils/api.ts`에 하드코딩된 URL을 사용하고 있습니다. 환경 변수를 사용하려면 다음과 같이 수정해야 합니다:

```typescript
// utils/api.ts
const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL || 'http://localhost:8000';
```

## 빌드

### 1. 의존성 설치

```bash
npm install
# 또는
yarn install
```

### 2. 프로덕션 빌드

```bash
npm run build
# 또는
yarn build
```

빌드가 성공하면 `.next` 디렉토리가 생성됩니다.

### 3. 빌드 테스트

```bash
npm run start
# 또는
yarn start
```

브라우저에서 http://localhost:3000 접속하여 정상 작동 확인

## 배포 옵션

### 옵션 1: Vercel (권장)

Next.js를 만든 Vercel에서 제공하는 배포 플랫폼입니다.

#### 단계:

1. **Vercel 계정 생성**: https://vercel.com/signup

2. **프로젝트 연결**:
   ```bash
   npm install -g vercel
   vercel login
   vercel
   ```

3. **환경 변수 설정**:
   - Vercel 대시보드 → 프로젝트 → Settings → Environment Variables
   - `NEXT_PUBLIC_API_BASE_URL` 추가

4. **배포**:
   ```bash
   vercel --prod
   ```

#### 장점:
- 자동 SSL 인증서
- 글로벌 CDN
- 자동 스케일링
- Git 통합 (푸시 시 자동 배포)
- 무료 플랜 제공

### 옵션 2: Docker

Docker를 사용하여 컨테이너로 배포할 수 있습니다.

#### Dockerfile 생성:

```dockerfile
# frontend/Dockerfile
FROM node:20-alpine AS builder

WORKDIR /app

# 의존성 설치
COPY package*.json ./
RUN npm ci

# 소스 복사 및 빌드
COPY . .
RUN npm run build

# 프로덕션 이미지
FROM node:20-alpine AS runner

WORKDIR /app

ENV NODE_ENV production

# 필요한 파일만 복사
COPY --from=builder /app/next.config.js ./
COPY --from=builder /app/public ./public
COPY --from=builder /app/.next ./.next
COPY --from=builder /app/node_modules ./node_modules
COPY --from=builder /app/package.json ./package.json

EXPOSE 3000

CMD ["npm", "start"]
```

#### Docker Compose에 추가:

프로젝트 루트의 `docker-compose.yml`에 추가:

```yaml
services:
  frontend:
    build:
      context: ./frontend
      dockerfile: Dockerfile
    ports:
      - "3000:3000"
    environment:
      - NEXT_PUBLIC_API_BASE_URL=http://apigateway-service:8000
    depends_on:
      - apigateway-service
    networks:
      - doran-network

  # 기존 서비스들...
```

#### 빌드 및 실행:

```bash
# 프로젝트 루트에서
docker-compose up -d frontend
```

### 옵션 3: 전통적인 Node.js 서버

#### 단계:

1. **빌드**:
   ```bash
   npm run build
   ```

2. **프로덕션 서버에 파일 전송**:
   ```bash
   # SCP를 사용하여 전송
   scp -r .next node_modules package.json user@server:/path/to/app
   ```

3. **서버에서 실행**:
   ```bash
   npm start
   ```

4. **PM2를 사용하여 프로세스 관리** (권장):
   ```bash
   # PM2 설치
   npm install -g pm2

   # 앱 시작
   pm2 start npm --name "doran-frontend" -- start

   # 자동 재시작 설정
   pm2 startup
   pm2 save
   ```

#### PM2 명령어:

```bash
# 상태 확인
pm2 status

# 로그 확인
pm2 logs doran-frontend

# 재시작
pm2 restart doran-frontend

# 중지
pm2 stop doran-frontend

# 삭제
pm2 delete doran-frontend
```

### 옵션 4: Nginx 리버스 프록시

Nginx를 사용하여 Next.js 앱을 프록시할 수 있습니다.

#### Nginx 설정:

```nginx
# /etc/nginx/sites-available/doran-frontend
server {
    listen 80;
    server_name your-domain.com;

    location / {
        proxy_pass http://localhost:3000;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host $host;
        proxy_cache_bypass $http_upgrade;
    }
}
```

#### 설정 활성화:

```bash
# 심볼릭 링크 생성
sudo ln -s /etc/nginx/sites-available/doran-frontend /etc/nginx/sites-enabled/

# Nginx 테스트
sudo nginx -t

# Nginx 재시작
sudo systemctl restart nginx
```

## SSL/TLS 설정 (HTTPS)

### Let's Encrypt 사용 (무료)

```bash
# Certbot 설치
sudo apt-get update
sudo apt-get install certbot python3-certbot-nginx

# SSL 인증서 발급
sudo certbot --nginx -d your-domain.com

# 자동 갱신 테스트
sudo certbot renew --dry-run
```

## 성능 최적화

### 1. 이미지 최적화

Next.js의 Image 컴포넌트를 사용하면 자동으로 최적화됩니다:

```tsx
import Image from 'next/image';

<Image 
  src={item.itemImageUrl} 
  alt={item.itemName}
  width={200}
  height={200}
  priority={false} // 우선순위가 낮은 이미지
/>
```

### 2. 코드 스플리팅

Next.js는 자동으로 페이지별 코드 스플리팅을 수행합니다. 추가로 동적 import를 사용할 수 있습니다:

```tsx
import dynamic from 'next/dynamic';

// 컴포넌트를 필요할 때만 로드
const DynamicComponent = dynamic(() => import('./HeavyComponent'), {
  loading: () => <LoadingSpinner />,
  ssr: false, // SSR 비활성화 (클라이언트에서만 로드)
});
```

### 3. 캐싱 전략

React Query 설정 조정:

```typescript
// lib/queryClient.ts
const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      staleTime: 5 * 60 * 1000,  // 5분
      gcTime: 10 * 60 * 1000,    // 10분
      refetchOnWindowFocus: false,
    },
  },
});
```

## 모니터링

### 1. 로그 수집

프로덕션 환경에서는 적절한 로깅이 필요합니다:

```typescript
// utils/logger.ts
export const logger = {
  error: (message: string, error: any) => {
    if (process.env.NODE_ENV === 'production') {
      // 프로덕션에서는 로깅 서비스로 전송
      // 예: Sentry, Datadog 등
      console.error(message, error);
    } else {
      console.error(message, error);
    }
  },
  info: (message: string, data?: any) => {
    if (process.env.NODE_ENV !== 'production') {
      console.log(message, data);
    }
  },
};
```

### 2. 에러 추적

Sentry 통합:

```bash
npm install @sentry/nextjs
```

```typescript
// sentry.client.config.ts
import * as Sentry from '@sentry/nextjs';

Sentry.init({
  dsn: process.env.NEXT_PUBLIC_SENTRY_DSN,
  environment: process.env.NODE_ENV,
  tracesSampleRate: 1.0,
});
```

## 백업 및 롤백

### Git 태그 사용

```bash
# 배포 전 태그 생성
git tag -a v1.0.0 -m "Release version 1.0.0"
git push origin v1.0.0

# 롤백이 필요한 경우
git checkout v1.0.0
npm run build
```

## 트러블슈팅

### 빌드 실패

**문제**: `npm run build` 실패

**해결책**:
1. `node_modules` 및 `.next` 삭제 후 재시도:
   ```bash
   rm -rf node_modules .next
   npm install
   npm run build
   ```

2. Node.js 버전 확인:
   ```bash
   node -v  # v20 이상 권장
   ```

### 메모리 부족

**문제**: 빌드 중 메모리 부족 에러

**해결책**:
```bash
# Node.js 메모리 제한 증가
NODE_OPTIONS="--max-old-space-size=4096" npm run build
```

### API 연결 실패

**문제**: 프로덕션에서 API 호출 실패

**해결책**:
1. API Gateway URL 확인
2. CORS 설정 확인 (백엔드)
3. Network 탭에서 실제 요청 URL 확인

### CORS 에러

**문제**: 브라우저에서 CORS 에러 발생

**해결책**:
1. API Gateway에서 CORS 헤더 추가:
   ```java
   @CrossOrigin(origins = "https://your-frontend-domain.com")
   ```

2. 또는 API Gateway 설정에서 전역 CORS 설정

## 보안 고려사항

### 1. 환경 변수 보호

- `.env.local` 파일을 `.gitignore`에 추가
- 민감한 정보는 환경 변수로만 관리
- 서버 측 환경 변수는 `NEXT_PUBLIC_` 접두사 사용하지 않음

### 2. XSS 방지

React는 기본적으로 XSS를 방지하지만, `dangerouslySetInnerHTML`을 사용하지 않도록 주의

### 3. CSRF 방지

API Gateway에서 CSRF 토큰 사용 권장

### 4. HTTPS 사용

프로덕션 환경에서는 항상 HTTPS 사용

## 유지보수

### 의존성 업데이트

```bash
# 보안 업데이트 확인
npm audit

# 의존성 업데이트
npm update

# 메이저 버전 업데이트 (주의 필요)
npx npm-check-updates -u
npm install
```

### 정기 점검 항목

- [ ] 월 1회: 의존성 업데이트 및 보안 패치
- [ ] 주 1회: 로그 확인 및 에러 모니터링
- [ ] 일 1회: 서버 상태 및 디스크 용량 확인

## 추가 리소스

- [Next.js 배포 문서](https://nextjs.org/docs/deployment)
- [Vercel 문서](https://vercel.com/docs)
- [PM2 문서](https://pm2.keymetrics.io/docs/usage/quick-start/)
- [Nginx 문서](https://nginx.org/en/docs/)


















