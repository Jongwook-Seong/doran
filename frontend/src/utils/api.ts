// API 게이트웨이를 통한 엔드포인트 설정
const API_BASE_URL = 'http://localhost:8000';

const API_ENDPOINTS = {
  // auth-service API 엔드포인트
  AUTH: {
    SIGNUP: `${API_BASE_URL}/auth-service/api/join`,
    LOGIN: `${API_BASE_URL}/auth-service/api/login`,
    REISSUE: `${API_BASE_URL}/auth-service/api/reissue`,
    LOGOUT: `${API_BASE_URL}/auth-service/api/logout`,
    USERS: `${API_BASE_URL}/auth-service/api/users`,
    VALIDATE: `${API_BASE_URL}/auth-service/api/validate`,
  },
  // member-service API 엔드포인트
  MEMBER: {
    MEMBERS: `${API_BASE_URL}/member-service/members`,
    JOIN: `${API_BASE_URL}/member-service/join`,
    DELETE: `${API_BASE_URL}/member-service/delete`,
    ORDER_LIST: `${API_BASE_URL}/member-service/order/list`,
    ORDER_DETAIL: `${API_BASE_URL}/member-service/order/detail`,
    DELIVERY_TRACKING: `${API_BASE_URL}/member-service/delivery/tracking`,
    BASKET_ADD: `${API_BASE_URL}/member-service/basket/addItem`,
    BASKET_DELETE: `${API_BASE_URL}/member-service/basket/deleteItem`,
    BASKET_LIST: `${API_BASE_URL}/member-service/basket/list`,
  },
  // item-service API 엔드포인트
  ITEM: {
    BEST_ITEMS: `${API_BASE_URL}/item-service/bestitems`,
    BOOK_DETAIL: `${API_BASE_URL}/item-service/book/detail`,
    BOOK_BASKET: `${API_BASE_URL}/item-service/book/basket`,
    ORDER_ITEMS: `${API_BASE_URL}/item-service/orderitems`,
    BOOK_SEARCH: `${API_BASE_URL}/item-service/book/search`,
    BOOK_REGISTER: `${API_BASE_URL}/item-service/book/register`,
    DELETE: `${API_BASE_URL}/item-service/delete`,
    ITEMS_SIMPLE: `${API_BASE_URL}/item-service/items/simple`,
  },
  // order-service API 엔드포인트
  ORDER: {
    ORDERS: `${API_BASE_URL}/order-service/orders`,
    CANCEL: `${API_BASE_URL}/order-service/cancel`,
    LIST: `${API_BASE_URL}/order-service/list`,
    DETAIL: `${API_BASE_URL}/order-service/detail`,
    DETAIL_ASYNC: `${API_BASE_URL}/order-service/detail/async`,
    DELIVERY_TRACKING: `${API_BASE_URL}/order-service/delivery/tracking`,
    DELIVERY_STATUS: `${API_BASE_URL}/order-service/delivery/status`,
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
  headers?: Record<string, string>,
  requireAuth: boolean = false
): Promise<ApiResponse<T>> {
  try {
    const requestHeaders: Record<string, string> = {
      'Content-Type': 'application/json',
      ...headers,
    };

    // 인증이 필요한 요청인 경우 JWT 토큰 추가
    if (requireAuth && typeof window !== 'undefined') {
      const token = localStorage.getItem('token');
      const refreshToken = localStorage.getItem('refreshToken');
      if (token) {
        requestHeaders['Authorization'] = token.startsWith('Bearer ') ? token : `Bearer ${token}`;
      }
      if (refreshToken) {
        requestHeaders['refreshToken'] = refreshToken;
      }
    }

    const requestOptions: RequestInit = {
      method,
      headers: requestHeaders,
    };

    // POST, PUT, PATCH 요청의 경우 body 추가
    if (data && ['POST', 'PUT', 'PATCH'].includes(method)) {
      requestOptions.body = JSON.stringify(data);
    }

    const response = await fetch(url, requestOptions);
    console.log('response', response);
    
    // 응답이 비어있는지 확인
    const responseText = await response.text();
    console.log('responseText', responseText);
    
    let responseData;
    try {
      // 응답이 JSON인지 확인하고 파싱
      responseData = responseText ? JSON.parse(responseText) : {};
    } catch (parseError) {
      console.error('JSON 파싱 에러:', parseError);
      // JSON이 아닌 경우 빈 객체로 처리
      responseData = {};
    }

    if (response.ok) {
      return {
        success: true,
        data: responseData,
      };
    } else {
      return {
        success: false,
        error: responseData.message || `요청이 실패했습니다. (${response.status})`,
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

// ===== 타입 정의 =====
export interface UserJoinRequest {
  userId: string;
  password: string;
  username: string;
  birthDate: string;
  email: string;
  phoneNumber: string;
}

export interface UserLoginRequest {
  userId: string;
  password: string;
}

export interface UserLoginResponse {
  accessToken: string;
  refreshToken: string;
  userUuid: string;
}

export interface UserResponse {
  userUuid: string;
  userId: string;
  username: string;
  birthDate: string;
  email: string;
  phoneNumber: string;
}

export interface ItemSimpleResponse {
  itemUuid: string;
  itemName: string;
  price: number;
  itemImageUrl: string;
}

export interface BestItem {
  itemUuid: string;
  itemName: string;
  price: number;
  itemImageUrl: string;
  salesCount: number;
}

export interface ItemDetail {
  itemUuid: string;
  itemName: string;
  price: number;
  itemImageUrl?: string;
  itemDetail?: string;
  itemCount?: number;
  category?: string;
  salesCount?: number;
}

export interface BasketItem {
  itemUuid: string;
  itemName: string;
  price: number;
  count: number;
  itemImageUrl?: string;
}

export interface BasketItemResponse {
  itemUuid: string;
  message: string;
}

export interface MemberResponse {
  userUuid: string;
  username: string;
  message: string;
}

// 주문에 필요한 상품 간단 정보
export interface ItemSimpleInfo {
  itemUuid: string;
  count: number;
  price: number;
}

// 주문자 및 수신자 정보
export interface TransceiverInfo {
  ordererName: string;      // 주문자 이름
  receiverName: string;      // 수신자 이름
  receiverPhoneNumber: string; // 수신자 전화번호
}

// 배송 주소 정보
export interface Address {
  city: string;      // 시/도
  street: string;    // 도로명 주소
  details: string;   // 상세 주소
  zipcode: string;   // 우편번호
}

// 주문 생성 요청 타입
export interface OrderCreateRequest {
  itemSimpleInfoList: ItemSimpleInfo[];
  transceiverInfo: TransceiverInfo;
  address: Address;
}

export interface Order {
  orderUuid: string;
  orderDate: string;
  status: string;
  totalAmount: number;
  items: OrderItem[];
  deliveryAddress?: string;
  deliveryMessage?: string;
  deliveryFee?: number;
}

export interface OrderItem {
  itemUuid: string;
  itemName: string;
  itemPrice: number;
  quantity: number;
  itemImageUrl?: string;
}

export interface OrderListResponse {
  orders: Order[];
}

// 주문 상품 간단 정보
export interface OrderItemSimple {
  itemUuid: string;
  itemName?: string;
  itemImageUrl?: string;
  count: number;
  price: number;
}

// 배송 상태 타입
export type DeliveryStatus = 'READY' | 'DELIVER' | 'COMPLETE';

// 주문 상세 응답 타입
export interface OrderDetailResponse {
  orderItemSimpleList: OrderItemSimple[];
  orderDate: string;  // ISO 날짜 문자열
  deliveryStatus: DeliveryStatus;
  transceiverInfo: TransceiverInfo;
  address: Address;
}

export interface TokenDto {
  accessToken: string;
  refreshToken: string;
}

// ===== Auth Service API 함수들 =====
// 회원가입 API 호출 함수
export async function signUp(userData: UserJoinRequest): Promise<ApiResponse<void>> {
  return apiCall<void>(API_ENDPOINTS.AUTH.SIGNUP, 'POST', userData, {}, false);
}

// 로그인 API 호출 함수
export async function login(credentials: UserLoginRequest): Promise<ApiResponse<UserLoginResponse>> {
  return apiCall<UserLoginResponse>(API_ENDPOINTS.AUTH.LOGIN, 'POST', credentials, {}, false);
}

// 토큰 재발급 API 호출 함수
export async function reissueToken(refreshToken: string): Promise<ApiResponse<TokenDto>> {
  return apiCall<TokenDto>(API_ENDPOINTS.AUTH.REISSUE, 'POST', {}, { refreshToken }, true);
}

// 로그아웃 API 호출 함수
export async function logout(accessToken: string, refreshToken: string): Promise<ApiResponse<void>> {
  return apiCall<void>(API_ENDPOINTS.AUTH.LOGOUT, 'DELETE', {}, { 
    Authorization: accessToken,
    refreshToken 
  }, false);
}

// 사용자 목록 조회 API 호출 함수 (인증 필요)
export async function getUsers(): Promise<ApiResponse<UserResponse[]>> {
  return apiCall<UserResponse[]>(API_ENDPOINTS.AUTH.USERS, 'GET', undefined, {}, true);
}

// 특정 사용자 정보 조회 API 호출 함수 (인증 필요)
export async function getUserByUuid(userUuid: string, accessToken?: string, refreshToken?: string): Promise<ApiResponse<UserResponse>> {
  const authHeaders: Record<string, string> = {};
  
  if (accessToken) {
    authHeaders['Authorization'] = accessToken.startsWith('Bearer ') ? accessToken : `Bearer ${accessToken}`;
  }
  if (refreshToken) {
    authHeaders['refreshToken'] = refreshToken;
  }
  
  return apiCall<UserResponse>(`${API_ENDPOINTS.AUTH.USERS}/${userUuid}`, 'GET', undefined, authHeaders, !!accessToken);
}

// 사용자 정보 수정 API 호출 함수 (인증 필요)
export async function modifyUserInfo(userUuid: string, password: string): Promise<ApiResponse<void>> {
  return apiCall<void>(API_ENDPOINTS.AUTH.USERS, 'PUT', { password }, { userUuid }, true);
}

// 사용자 삭제 API 호출 함수 (인증 필요)
export async function deleteUser(userUuid: string): Promise<ApiResponse<void>> {
  return apiCall<void>(API_ENDPOINTS.AUTH.USERS, 'DELETE', {}, { userUuid }, true);
}

// ===== Member Service API 함수들 =====
// 멤버 목록 조회 API 호출 함수
export async function getMembers(): Promise<ApiResponse<UserResponse[]>> {
  return apiCall<UserResponse[]>(API_ENDPOINTS.MEMBER.MEMBERS, 'GET', undefined, {}, false);
}

// 멤버 가입 API 호출 함수
export async function joinMember(userUuid: string, username: string): Promise<ApiResponse<MemberResponse>> {
  return apiCall<MemberResponse>(API_ENDPOINTS.MEMBER.JOIN, 'POST', undefined, { userUuid }, false);
}

// 멤버 삭제 API 호출 함수
export async function deleteMember(userUuid: string): Promise<ApiResponse<MemberResponse>> {
  return apiCall<MemberResponse>(API_ENDPOINTS.MEMBER.DELETE, 'DELETE', undefined, { userUuid }, false);
}

// 장바구니에 아이템 추가 API 호출 함수
export async function addToBasket(userUuid: string, itemUuid: string, count: number): Promise<ApiResponse<BasketItemResponse>> {
  return apiCall<BasketItemResponse>(API_ENDPOINTS.MEMBER.BASKET_ADD, 'POST', { itemUuid, count }, { userUuid }, false);
}

// 장바구니에서 아이템 삭제 API 호출 함수
export async function deleteFromBasket(userUuid: string, itemUuid: string): Promise<ApiResponse<BasketItemResponse>> {
  return apiCall<BasketItemResponse>(`${API_ENDPOINTS.MEMBER.BASKET_DELETE}?itemUuid=${itemUuid}`, 'DELETE', undefined, { userUuid }, false);
}

// 장바구니 목록 조회 API 호출 함수
export async function getBasketList(userUuid: string): Promise<ApiResponse<BasketItem[]>> {
  return apiCall<BasketItem[]>(API_ENDPOINTS.MEMBER.BASKET_LIST, 'GET', undefined, { userUuid }, false);
}

// ===== Item Service API 함수들 =====
// 베스트 아이템 목록 조회 API 호출 함수
export async function getBestItems(): Promise<ApiResponse<BestItem[]>> {
  return apiCall<BestItem[]>(API_ENDPOINTS.ITEM.BEST_ITEMS, 'GET', undefined, {}, false);
}

// 책 상세 정보 조회 API 호출 함수
export async function getBookDetail(itemUuid: string): Promise<ApiResponse<ItemDetail>> {
  return apiCall<ItemDetail>(`${API_ENDPOINTS.ITEM.BOOK_DETAIL}?itemUuid=${itemUuid}`, 'GET', undefined, {}, false);
}

// 책 검색 API 호출 함수
export async function searchBooks(keywords: string): Promise<ApiResponse<ItemSimpleResponse[]>> {
  return apiCall<ItemSimpleResponse[]>(`${API_ENDPOINTS.ITEM.BOOK_SEARCH}?keywords=${encodeURIComponent(keywords)}`, 'GET', undefined, {}, false);
}

// ===== Order Service API 함수들 =====
// 주문 생성 API 호출 함수
export async function createOrder(userUuid: string, orderData: OrderCreateRequest): Promise<ApiResponse<void>> {
  return apiCall<void>(API_ENDPOINTS.ORDER.ORDERS, 'POST', orderData, { userUuid }, false);
}

// 주문 목록 조회 API 호출 함수
export async function getOrderList(userUuid: string): Promise<ApiResponse<OrderListResponse>> {
  return apiCall<OrderListResponse>(API_ENDPOINTS.ORDER.LIST, 'GET', undefined, { userUuid }, false);
}

// 주문 상세 조회 API 호출 함수
export async function getOrderDetail(userUuid: string, orderUuid: string): Promise<ApiResponse<OrderDetailResponse>> {
  return apiCall<OrderDetailResponse>(`${API_ENDPOINTS.ORDER.DETAIL}?orderUuid=${orderUuid}`, 'GET', undefined, { userUuid }, false);
}

// 주문 취소 API 호출 함수
export async function cancelOrder(userUuid: string, orderUuid: string): Promise<ApiResponse<void>> {
  return apiCall<void>(`${API_ENDPOINTS.ORDER.CANCEL}?orderUuid=${orderUuid}`, 'PUT', undefined, { userUuid }, false);
}

// 배송 추적 조회 API 호출 함수
export interface DeliveryTrackingInfo {
  courier: string;           // 택배사
  contactNumber: string;     // 연락처
  postLocation: string;      // 배송 위치
  postDateTime: string;      // 배송 시간 (ISO 날짜 문자열)
}

export interface DeliveryTrackingResponse {
  deliveryTrackingInfoList: DeliveryTrackingInfo[];
  deliveryStatus: DeliveryStatus;
}

export async function getDeliveryTracking(userUuid: string, orderUuid: string): Promise<ApiResponse<DeliveryTrackingResponse>> {
  return apiCall<DeliveryTrackingResponse>(`${API_ENDPOINTS.ORDER.DELIVERY_TRACKING}?orderUuid=${orderUuid}`, 'GET', undefined, { userUuid }, false);
}

// API 엔드포인트 내보내기
export { API_ENDPOINTS };
