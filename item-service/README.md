<div align="center">
<h1>Item Service</h1>
<h3>상품 등록 및 관리를 담당하는 서비스</h3>
</div>

<br>
<br>

## ERD
<img width="9116" alt="상품서비스ERD" src="https://github.com/user-attachments/assets/f4552c17-f0d7-4a62-8fe6-a4d33e5d5c05">

<br>
<br>

## Architecture
<img width="9116" alt="상품서비스아키텍처" src="https://github.com/user-attachments/assets/2968949b-0b67-490d-8814-c4f945cc35ea">

<img width="9116" alt="상품서비스아키텍처" src="https://github.com/user-attachments/assets/b8ebab5a-23bf-4647-8fb4-a6f038f1dcd8">

<br>
<br>

## Description

### 회원정보/장바구니 상품목록 CRUD
상품 등록, 조회, 수정 및 삭제가 가능하다.

### QueryDSL 사용
타입 안정성 및 동적쿼리 생성 등을 고려하여 QueryDSL을 활용하였다.

### Resilience4J CircuitBreaker & Retry
MSA 애플리케이션의 정상적인 사용을 위해 서비스간 통신에서는 여러 장애대응 조치를 취할 수 있다. 그러한 조치로 서킷브레이커와 리트라이 정책을 설정해주는 것이 대표적이다. 일부 서비스가 비정상이라고 하더라도 서킷브레이커가 적용된 해당 서비스에 한해서만 접근을 차단함으로써 문제가 번지는 상황을 방지하고 다른 서비스에 대해서는 정상 수행하도록 하며, 일시적 오류일 가능성에 대비해 요청을 제한적으로 재시도하도록 함으로써 안정성과 가용성을 높인다. 만약 데이터의 수정 및 저장 과정이 포함되는 로직에서는 중간에 에러가 발생할 경우 @Transactional을 통해 연쇄된 전체 트랜잭션을 롤백 및 대체 로직을 수행함으로써 서비스간 통신에서 발생하는 데이터 부정합 문제를 방지한다.

### Kafka 비동기 처리
프로세스의 지연 및 부하는 서비스의 자원을 쉽게 고갈시키고 사용성을 저하시킬 수 있다. 이때 DB 접근에 대한 비동기 처리를 통해 기능을 보다 원활히 사용할 수 있다. 상품 테이블에 업데이트가 발생하면 해당 메세지 데이터를 Kafka Server로 발행한다. 발행된 메세지는 바로 Consume 되어 MongoDB로 저장함으로써 데이터 조회가 가능하도록 한다.

### Redis Cache Server
베스트 상품 리스트 조회 기능의 경우, 사용자가 동일한 데이터를 반복해서 조회할 가능성이 높은 기능이므로 캐시를 적용하였다. 분산환경에서는 로컬 캐시 적용이 부적절하므로 원격으로 사용이 가능하고 속도가 빠른 Redis를 선택하였다.