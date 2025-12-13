# 책과 예술을 파는 온라인 서가 DORAN
우리는 전시회나 카페에서 작품에 대해 감상하거나 사람들과 이야기하곤 합니다. 그리고 여기, 온라인 서가에서도 책과 예술에 대해 감상하며 사람들과 "도란도란" 대화하는 장이 열리기를 꿈꿉니다.

## About the Project
책과 예술작품을 판매하는 이커머스 플랫폼입니다. 서비스에 등록된 상품을 회원이 주문을 하거나 장바구니에 담을 수 있고, 주문 후에는 주문내역과 배송현황 조회가 가능합니다. 특별하게 일일 주문량이 가장 많은 10개의 상품은 베스트 상품으로 선정되어 보다 활발한 소비를 유도합니다.

## Period / Personnel
- 설계 및 개발 : 24.03. ~ 24.07. / 5개월
- 성능테스트 및 리팩토링 : 24.06. ~ 24.07. / 2개월
- 프론트엔드 개발 : 25.01. / 1개월
- BE 1인 개인프로젝트 (프론트엔드 포함)

## Blog
Velog : https://velog.io/@xav/posts?tag=Doran

## Features
- 회원가입 및 로그인
- 상품 등록 및 삭제
- 상품 장바구니 등록 및 삭제
- 장바구니 리스트 조회
- 상품 주문 및 주문 취소
- 주문 내역 조회
- 최근 주문 리스트 조회
- 주문 배송 업데이트 및 조회
- 베스트 상품 리스트 조회

## Technologies

### Backend
- **Language** : ![Static Badge](https://img.shields.io/badge/java-17-ED8B00?logo=Java&label=Java&color=ED8B00)
- **Build** : ![Static Badge](https://img.shields.io/badge/maven-4.0.0-ED1B4B?logo=Maven&label=Maven&color=ED1B4B)
- **Framework** : ![Static Badge](https://img.shields.io/badge/springboot-3.2.5-6DB33F?logo=springboot&label=Spring%20Boot&color=6DB33F) ![Static Badge](https://img.shields.io/badge/springcloud-2023.0.1-6DB33F?logo=Spring&label=Spring%20Cloud&color=6DB33F)
- **ORM** : ![Static Badge](https://img.shields.io/badge/springdatajpa-3.2.5-43853D?logo=Spring&label=Spring%20Data%20JPA&color=43853D) ![Static Badge](https://img.shields.io/badge/querydsl-5.0.0-0A60A8?logo=Spring&label=QueryDSL&color=0A60A8)
- **Database/Cache** : ![Static Badge](https://img.shields.io/badge/mysql-8.0-4479A1?logo=MySQL&label=MySQL&color=4479A1) ![Static Badge](https://img.shields.io/badge/mongodb-7.0.3-47A248?logo=MongoDB&label=MongoDB&color=47A248) ![Static Badge](https://img.shields.io/badge/redis-6.0.20-FF4438?logo=Redis&label=Redis&color=FF4438)
- **Configuration Management** : ![Static Badge](https://img.shields.io/badge/github-2.44.0-231F20?logo=Github&label=Github&color=231F20)
- **Messaging System** : ![Static Badge](https://img.shields.io/badge/kafka-3.6.0-231F20?logo=ApacheKafka&label=Apache%20Kafka&color=231F20)

### Frontend
- **Language** : ![Static Badge](https://img.shields.io/badge/typescript-5-3178C6?logo=TypeScript&label=TypeScript&color=3178C6)
- **Framework** : ![Static Badge](https://img.shields.io/badge/nextjs-15.5.2-000000?logo=Next.js&label=Next.js&color=000000) ![Static Badge](https://img.shields.io/badge/react-19.1.0-61DAFB?logo=React&label=React&color=61DAFB)
- **Styling** : ![Static Badge](https://img.shields.io/badge/tailwindcss-4-38B2AC?logo=Tailwind%20CSS&label=Tailwind%20CSS&color=38B2AC)
- **State Management** : ![Static Badge](https://img.shields.io/badge/reactquery-5.87.4-FF4154?logo=React%20Query&label=React%20Query&color=FF4154)
- **Form Handling** : ![Static Badge](https://img.shields.io/badge/reacthookform-7.62.0-EC5990?logo=React%20Hook%20Form&label=React%20Hook%20Form&color=EC5990) ![Static Badge](https://img.shields.io/badge/zod-4.1.8-3E67B1?logo=Zod&label=Zod&color=3E67B1)

## Architecture
<img width="9116" alt="아키텍처" src="https://github.com/user-attachments/assets/6f1c9993-47d4-4afb-b566-c77e5d34e22e">

<img width="9116" alt="카프카 아키텍처" src="https://github.com/user-attachments/assets/f2f999ac-b9ce-46c5-953f-f96ac2abebe4">

❗마이크로서비스별 아키텍처는 각 서비스의 README를 통해 확인할 수 있습니다.

## Service Details

|     Service       | URL                                                                             | 
|:-----------------:|:--------------------------------------------------------------------------------|
|  **API Gateway**  | https://github.com/Jongwook-Seong/doran/blob/main/apigateway-service/READMD.md  |
|    **Member**     | https://github.com/Jongwook-Seong/doran/blob/main/member-service/READMD.md      |
|     **Order**     | https://github.com/Jongwook-Seong/doran/blob/main/order-service/READMD.md       |
|     **Item**      | https://github.com/Jongwook-Seong/doran/blob/main/item-service/READMD.md        |

## Performance Impovements
- DB 접근 시 비동기 처리와 CDC 및 비동기 통신 적용을 통한 서비스 안정성 및 성능 향상 / 데이터 정합성 이슈 해소 (Apache Kafka 적용)
- 서킷브레이커 및 리트라이 패턴 적용을 통한 데이터 롤백 및 서비스간 통신 장애 대응 및 데이터 롤백 (Resilience4J)
- 자주 조회되는 데이터에 대한 캐시 적용으로 조회 속도 향상 및 부하 분산 (Redis 캐시 서버 적용)
- 상품 재고수량 증감 시 동시성 이슈로 인한 데이터 불일치 가능성 해소 (Redisson 분산 락 적용)
- JPA N+1 문제 해결로 로딩 속도 및 메모리 성능 최적화
- MySQL 인덱스 설정을 통한 쿼리 속도 향상
- 객체 매핑에 대한 리팩토링을 통해 런타임 부하 해소 및 유지보수성 향상(ModelMapper → Mapstruct)

## Getting Started

### Backend

각 서비스를 실행하려면 다음 순서를 따르세요:

1. **Discovery Service** (포트 8761)
```bash
cd discovery-service
./gradlew bootRun
```

2. **API Gateway** (포트 8000)
```bash
cd apigateway-service
./gradlew bootRun
```

3. **마이크로서비스들** (순서 무관)
```bash
# Auth Service
cd auth-service
./gradlew bootRun

# Member Service
cd member-service
./gradlew bootRun

# Item Service
cd item-service
./gradlew bootRun

# Order Service
cd order-service
./gradlew bootRun
```

### Frontend

```bash
cd frontend
npm install
npm run dev
```

브라우저에서 http://localhost:3000 접속

## Project Structure

```
doran/
├── apigateway-service/     # API Gateway (Spring Cloud Gateway)
├── discovery-service/      # Service Discovery (Eureka Server)
├── auth-service/          # 인증/인가 서비스
├── member-service/        # 회원 및 장바구니 관리 서비스
├── item-service/          # 상품 관리 서비스
├── order-service/         # 주문 및 배송 관리 서비스
├── frontend/              # Next.js 프론트엔드
│   ├── src/
│   │   ├── app/          # Next.js 페이지
│   │   ├── components/   # 재사용 컴포넌트
│   │   ├── contexts/     # React Context
│   │   ├── lib/          # 라이브러리 설정
│   │   └── utils/        # 유틸리티 함수
│   ├── README.md         # 프론트엔드 상세 문서
│   ├── IMPLEMENTATION_GUIDE.md  # 구현 가이드
│   ├── TESTING_GUIDE.md  # 테스트 가이드
│   └── DEPLOYMENT.md     # 배포 가이드
└── README.md             # 이 파일
```

## Frontend Features

- **BFF (Backend for Frontend) 패턴**: API Gateway를 통한 마이크로서비스 통신
- **반응형 디자인**: 모바일, 태블릿, 데스크톱 지원
- **상태 관리**: React Query를 사용한 서버 상태 관리
- **인증**: JWT 토큰 기반 인증
- **타입 안정성**: TypeScript를 통한 타입 체크
- **자세한 주석**: React/Next.js 초보자를 위한 상세한 코드 주석

더 자세한 내용은 [Frontend README](./frontend/README.md)를 참조하세요.