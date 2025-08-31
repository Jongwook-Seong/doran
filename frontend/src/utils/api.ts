// API 엔드포인트 설정
const API_ENDPOINTS = {
  // auth-simple-service API 엔드포인트
  AUTH: {
    SIGNUP: 'http://localhost:8000/auth-simple-service/users',
    LOGIN: 'http://localhost:8000/auth-simple-service/login',
    USERS: 'http://localhost:8000/auth-simple-service/users',
  },
  // member-service API 엔드포인트 (향후 확장)
  MEMBER: {
    PROFILE: 'http://localhost:8081/profile',
    CART: 'http://localhost:8081/cart',
  },
  // item-service API 엔드포인트 (향후 확장)
  ITEM: {
    PRODUCTS: 'http://localhost:8082/products',
    PRODUCT_DETAIL: 'http://localhost:8082/products',
  },
  // order-service API 엔드포인트 (향후 확장)
  ORDER: {
    ORDERS: 'http://localhost:8083/orders',
    ORDER_DETAIL: 'http://localhost:8083/orders',
  },
};

// API 응답 타입 정의
export interface ApiResponse<T = any> {
  success: boolean;
  data?: T;
  message?: string;
  error?: string;
}

// HTTP 메서드 타입
type HttpMethod = 'GET' | 'POST' | 'PUT' | 'DELETE' | 'PATCH';

// 기본 API 호출 함수
async function apiCall<T>(
  url: string,
  method: HttpMethod = 'GET',
  data?: any,
  headers?: Record<string, string>
): Promise<ApiResponse<T>> {
  try {
    const requestHeaders: Record<string, string> = {
      'Content-Type': 'application/json',
      ...headers,
    };

    const requestOptions: RequestInit = {
      method,
      headers: requestHeaders,
    };

    // POST, PUT, PATCH 요청의 경우 body 추가
    if (data && ['POST', 'PUT', 'PATCH'].includes(method)) {
      requestOptions.body = JSON.stringify(data);
    }

    const response = await fetch(url, requestOptions);
    const responseData = await response.json();

    if (response.ok) {
      return {
        success: true,
        data: responseData,
      };
    } else {
      return {
        success: false,
        error: responseData.message || '요청이 실패했습니다.',
      };
    }
  } catch (error) {
    console.error('API 호출 에러:', error);
    return {
      success: false,
      error: '네트워크 오류가 발생했습니다.',
    };
  }
}

// 회원가입 API 호출 함수
export async function signUp(userData: {
  userId: string;
  password: string;
  username: string;
  birthDate: Date;
  email: string;
  phoneNumber: string;
}): Promise<ApiResponse> {
  return apiCall(API_ENDPOINTS.AUTH.SIGNUP, 'POST', userData);
}

// 로그인 API 호출 함수 (향후 구현)
export async function login(credentials: {
  userId: string;
  password: string;
}): Promise<ApiResponse> {
  return apiCall(API_ENDPOINTS.AUTH.LOGIN, 'POST', credentials);
}

// 사용자 목록 조회 API 호출 함수 (향후 구현)
export async function getUsers(): Promise<ApiResponse> {
  return apiCall(API_ENDPOINTS.AUTH.USERS, 'GET');
}

// API 엔드포인트 내보내기
export { API_ENDPOINTS };
