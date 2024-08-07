<div align="center">
<h1>API Gateway Service</h1>
<h3>요청에 대한 서비스 라우팅 및 로드밸런싱하는 API 게이트웨이 서비스</h3>
</div>

<br>
<br>

## Architecture
<img width="9116" alt="API게이트웨이서비스아키텍처" src="https://github.com/user-attachments/assets/328341c3-a404-4c9b-ace9-1083cd0296af">

<br>
<br>

## Description

### Resilience4J CircuitBreaker & Retry
MSA 애플리케이션의 정상적인 사용을 위해 서비스간 통신에서는 여러 장애대응 조치를 취할 수 있다. 그러한 조치로 서킷브레이커와 리트라이 정책을 설정해주는 것이 대표적이다. 일부 서비스가 비정상이라고 하더라도 서킷브레이커가 적용된 해당 서비스에 한해서만 접근을 차단함으로써 문제가 번지는 상황을 방지하고 다른 서비스에 대해서는 정상 수행하도록 하며, 일시적 오류일 가능성에 대비해 요청을 제한적으로 재시도하도록 함으로써 안정성과 가용성을 높인다.

이때, 다른 마이크로서비스와 별도로 여기서도 Resilience4J를 사용한 이유는 서비스 안정성 향상과 관리 비용 감소를 위함이다. 다시 말해, 기본적으로는 각 서비스의 특성과 요구사항에 맞게 서킷브레이커와 리트라이 정책을 세밀하고 적절하게 개별 적용함으로써 설정을 최적화할 필요가 있지만, 공통 설정이 무방한 서비스에 대해서는 API 게이트웨이 서비스의 설정 방식을 따르도록 하여 관리의 편의성을 높일 수 있기 때문이다. 이로써 전체 시스템의 가용성과 안정성을 향상시킬 수 있다.