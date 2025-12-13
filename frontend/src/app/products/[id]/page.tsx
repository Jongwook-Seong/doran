'use client';

import { useState } from 'react';
import { useParams, useRouter } from 'next/navigation';
import { useQuery } from '@tanstack/react-query';
import { getBookDetail, createOrder, ItemSimpleInfo, TransceiverInfo, Address, OrderCreateRequest, getOrderList } from '@/utils/api';
import { ShoppingCart, ArrowLeft, Star, X } from 'lucide-react';
import Link from 'next/link';
import { useAuth } from '@/contexts/AuthContext';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { addToBasket } from '@/utils/api';

// 주문 폼 데이터 타입 정의
interface OrderFormData {
  ordererName: string;
  receiverName: string;
  receiverPhoneNumber: string;
  city: string;
  street: string;
  details: string;
  zipcode: string;
}

// 상품 상세 페이지 컴포넌트
export default function ProductDetailPage() {
  const params = useParams();
  const router = useRouter();
  const { isAuthenticated, user } = useAuth();
  const itemUuid = params.id as string;
  const queryClient = useQueryClient();
  
  // 수량 선택 상태
  const [quantity, setQuantity] = useState(1);
  
  // 주문 폼 모달 표시 여부 상태
  const [showOrderModal, setShowOrderModal] = useState(false);
  
  // 주문 폼 데이터 상태
  const [orderFormData, setOrderFormData] = useState<OrderFormData>({
    ordererName: user?.username || '',
    receiverName: user?.username || '',
    receiverPhoneNumber: user?.phoneNumber || '',
    city: '',
    street: '',
    details: '',
    zipcode: '',
  });

  // 상품 상세 정보를 가져오는 쿼리
  const { data: product, isLoading, error } = useQuery({
    queryKey: ['productDetail', itemUuid],
    queryFn: () => getBookDetail(itemUuid),
    enabled: !!itemUuid,
    staleTime: 5 * 60 * 1000,
  });

  // 장바구니 추가 mutation
  const addToBasketMutation = useMutation({
    mutationFn: () => addToBasket(user?.userUuid || '', itemUuid, quantity),
    onSuccess: () => {
      // 장바구니 목록 캐시 무효화
      queryClient.invalidateQueries({ queryKey: ['basketList'] });
      alert(`${quantity}개가 장바구니에 추가되었습니다.`);
    },
    onError: () => {
      alert('장바구니 추가에 실패했습니다. 다시 시도해주세요.');
    },
  });

  // 수량 변경 함수
  const handleQuantityChange = (newQuantity: number) => {
    if (newQuantity >= 1) {
      setQuantity(newQuantity);
    }
  };

  // 장바구니 담기 함수
  const handleAddToCart = () => {
    if (!isAuthenticated) {
      alert('로그인이 필요합니다.');
      return;
    }
    addToBasketMutation.mutate();
  };

  // 주문 폼 입력 필드 변경 핸들러
  const handleOrderFormChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setOrderFormData(prev => ({
      ...prev,
      [name]: value,
    }));
  };

  // 주문 생성 mutation
  const createOrderMutation = useMutation({
    mutationFn: (orderData: OrderCreateRequest) => createOrder(user?.userUuid || '', orderData),
    onSuccess: async () => {
      // 주문 성공 시 주문 목록 캐시 무효화
      queryClient.invalidateQueries({ queryKey: ['orderList'] });
      
      // 주문 모달 닫기
      setShowOrderModal(false);
      
      // 주문 목록을 가져와서 가장 최신 주문의 orderUuid 찾기
      try {
        const orderListResult = await getOrderList(user?.userUuid || '');
        if (orderListResult.success && orderListResult.data?.orders && orderListResult.data.orders.length > 0) {
          // 주문 목록을 날짜순으로 정렬하여 가장 최신 주문 찾기
          const sortedOrders = [...orderListResult.data.orders].sort((a, b) => 
            new Date(b.orderDate).getTime() - new Date(a.orderDate).getTime()
          );
          const latestOrderUuid = sortedOrders[0].orderUuid;
          
          // 주문 상세 페이지로 이동
          router.push(`/orders/${latestOrderUuid}`);
        } else {
          // 주문 목록을 가져오지 못한 경우 주문 내역 페이지로 이동
          alert('주문이 완료되었습니다!');
          router.push('/orders');
        }
      } catch (error) {
        console.error('주문 목록 조회 실패:', error);
        alert('주문이 완료되었습니다!');
        router.push('/orders');
      }
    },
    onError: (error: any) => {
      console.error('주문 실패:', error);
      alert('주문에 실패했습니다. 다시 시도해주세요.');
    },
  });

  // 바로 주문하기 함수
  const handleBuyNow = () => {
    if (!isAuthenticated) {
      alert('로그인이 필요합니다.');
      return;
    }
    // 주문 폼 데이터 초기화 (사용자 정보가 있으면 기본값 설정)
    setOrderFormData({
      ordererName: user?.username || '',
      receiverName: user?.username || '',
      receiverPhoneNumber: user?.phoneNumber || '',
      city: '',
      street: '',
      details: '',
      zipcode: '',
    });
    // 주문 모달 표시
    setShowOrderModal(true);
  };

  // 주문 폼 제출 핸들러
  const handleOrderSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    
    // 필수 필드 검증
    if (!orderFormData.ordererName || !orderFormData.receiverName || 
        !orderFormData.receiverPhoneNumber || !orderFormData.city || 
        !orderFormData.street || !orderFormData.details || !orderFormData.zipcode) {
      alert('모든 필드를 입력해주세요.');
      return;
    }

    if (!product?.success || !item) {
      alert('상품 정보를 불러올 수 없습니다.');
      return;
    }

    // ItemSimpleInfo 리스트 생성
    const itemSimpleInfoList: ItemSimpleInfo[] = [{
      itemUuid: item.itemUuid,
      count: quantity,
      price: item.price || 0,
    }];

    // TransceiverInfo 생성
    const transceiverInfo: TransceiverInfo = {
      ordererName: orderFormData.ordererName,
      receiverName: orderFormData.receiverName,
      receiverPhoneNumber: orderFormData.receiverPhoneNumber,
    };

    // Address 생성
    const address: Address = {
      city: orderFormData.city,
      street: orderFormData.street,
      details: orderFormData.details,
      zipcode: orderFormData.zipcode,
    };

    // OrderCreateRequest 생성
    const orderData: OrderCreateRequest = {
      itemSimpleInfoList,
      transceiverInfo,
      address,
    };

    // 주문 생성 API 호출
    createOrderMutation.mutate(orderData);
  };

  if (isLoading) {
    return (
      <div className="min-h-screen bg-gray-50">
        <div className="max-w-7xl mx-auto py-8 px-4 sm:px-6 lg:px-8">
          <div className="animate-pulse">
            <div className="h-8 bg-gray-200 rounded w-1/4 mb-8"></div>
            <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
              <div className="h-96 bg-gray-200 rounded"></div>
              <div className="space-y-4">
                <div className="h-8 bg-gray-200 rounded w-3/4"></div>
                <div className="h-6 bg-gray-200 rounded w-1/2"></div>
                <div className="h-4 bg-gray-200 rounded w-full"></div>
                <div className="h-4 bg-gray-200 rounded w-2/3"></div>
                <div className="h-12 bg-gray-200 rounded w-full"></div>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }

  if (error || !product?.success || !product?.data) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <div className="text-center">
          <h2 className="text-2xl font-bold text-gray-900 mb-4">상품을 찾을 수 없습니다</h2>
          <p className="text-gray-600 mb-8">요청하신 상품이 존재하지 않거나 삭제되었습니다.</p>
          <Link
            href="/products"
            className="bg-indigo-600 text-white px-6 py-3 rounded-md hover:bg-indigo-700"
          >
            상품 목록으로 돌아가기
          </Link>
        </div>
      </div>
    );
  }

  const item = product.data;

  return (
    <div className="min-h-screen bg-gray-50">
      {/* 헤더 */}
      <header className="bg-white shadow-sm">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center py-6">
            <Link href="/" className="text-2xl font-bold text-indigo-600">
              도란 온라인 서가
            </Link>
            <nav className="flex space-x-8">
              <Link href="/" className="text-gray-500 hover:text-indigo-600 px-3 py-2 text-sm font-medium">
                홈
              </Link>
              <Link href="/products" className="text-gray-500 hover:text-indigo-600 px-3 py-2 text-sm font-medium">
                상품
              </Link>
              <Link href="/cart" className="text-gray-500 hover:text-indigo-600 px-3 py-2 text-sm font-medium">
                장바구니
              </Link>
              <Link href="/orders" className="text-gray-500 hover:text-indigo-600 px-3 py-2 text-sm font-medium">
                주문내역
              </Link>
            </nav>
          </div>
        </div>
      </header>

      {/* 메인 콘텐츠 */}
      <main className="max-w-7xl mx-auto py-8 px-4 sm:px-6 lg:px-8">
        {/* 뒤로가기 버튼 */}
        <div className="mb-6">
          <Link
            href="/products"
            className="inline-flex items-center text-indigo-600 hover:text-indigo-500"
          >
            <ArrowLeft className="w-5 h-5 mr-2" />
            상품 목록으로 돌아가기
          </Link>
        </div>

        {/* 상품 정보 */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-12">
          {/* 상품 이미지 */}
          <div className="aspect-w-1 aspect-h-1">
            <div className="w-full h-96 bg-gray-200 rounded-lg flex items-center justify-center overflow-hidden">
              {item.itemImageUrl ? (
                <img
                  src={item.itemImageUrl}
                  alt={item.itemName}
                  className="w-full h-full object-cover"
                />
              ) : (
                <ShoppingCart className="w-24 h-24 text-gray-400" />
              )}
            </div>
          </div>

          {/* 상품 정보 */}
          <div className="space-y-6">
            <div>
              <h1 className="text-3xl font-bold text-gray-900 mb-4">{item.itemName}</h1>
              <div className="flex items-center space-x-4 mb-4">
                <span className="text-3xl font-bold text-indigo-600">
                  {item.price?.toLocaleString()}원
                </span>
                {item.salesCount && (
                  <div className="flex items-center">
                    <Star className="w-5 h-5 text-yellow-400 fill-current" />
                    <span className="text-sm text-gray-600 ml-1">
                      판매량: {item.salesCount}
                    </span>
                  </div>
                )}
              </div>
              <p className="text-gray-600">
                {item.itemDetail || '상품에 대한 자세한 설명이 없습니다.'}
              </p>
            </div>

            {/* 수량 선택 */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                수량
              </label>
              <div className="flex items-center space-x-3">
                <button
                  onClick={() => handleQuantityChange(quantity - 1)}
                  className="w-8 h-8 rounded-full border border-gray-600 text-gray-600 flex items-center justify-center hover:bg-gray-50"
                  disabled={quantity <= 1}
                >
                  -
                </button>
                <span className="w-12 text-center font-medium text-gray-600">{quantity}</span>
                <button
                  onClick={() => handleQuantityChange(quantity + 1)}
                  className="w-8 h-8 rounded-full border border-gray-600 text-gray-600 flex items-center justify-center hover:bg-gray-50"
                >
                  +
                </button>
              </div>
            </div>

            {/* 총 가격 */}
            <div className="border-t pt-4">
              <div className="flex justify-between items-center text-lg font-semibold">
                <span className="text-gray-900">총 가격:</span>
                <span className="text-indigo-600">
                  {(item.price * quantity)?.toLocaleString()}원
                </span>
              </div>
            </div>

            {/* 버튼들 */}
            <div className="space-y-3">
              <button
                onClick={handleAddToCart}
                disabled={addToBasketMutation.isPending}
                className="w-full bg-indigo-600 text-white py-3 px-6 rounded-md hover:bg-indigo-700 transition-colors flex items-center justify-center disabled:opacity-50 disabled:cursor-not-allowed"
              >
                <ShoppingCart className="w-5 h-5 mr-2" />
                {addToBasketMutation.isPending ? '담는 중...' : '장바구니 담기'}
              </button>
              <button
                onClick={handleBuyNow}
                className="w-full bg-green-600 text-white py-3 px-6 rounded-md hover:bg-green-700 transition-colors"
              >
                바로 주문하기
              </button>
            </div>
          </div>
        </div>

        {/* 상품 상세 정보 */}
        <div className="mt-16">
          <div className="bg-white rounded-lg shadow-sm p-8">
            <h2 className="text-2xl font-bold text-gray-900 mb-6">상품 상세 정보</h2>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div>
                <dt className="text-sm font-medium text-gray-500">상품명</dt>
                <dd className="mt-1 text-sm text-gray-900">{item.itemName}</dd>
              </div>
              <div>
                <dt className="text-sm font-medium text-gray-500">가격</dt>
                <dd className="mt-1 text-sm text-gray-900">{item.price?.toLocaleString()}원</dd>
              </div>
              <div>
                <dt className="text-sm font-medium text-gray-500">재고</dt>
                <dd className="mt-1 text-sm text-gray-900">{item.itemCount || '재고 정보 없음'}</dd>
              </div>
              <div>
                <dt className="text-sm font-medium text-gray-500">카테고리</dt>
                <dd className="mt-1 text-sm text-gray-900">{item.category || '카테고리 정보 없음'}</dd>
              </div>
            </div>
          </div>
        </div>
      </main>

      {/* 주문 폼 모달 */}
      {showOrderModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg shadow-xl max-w-2xl w-full mx-4 max-h-[90vh] overflow-y-auto">
            {/* 모달 헤더 */}
            <div className="flex justify-between items-center p-6 border-b">
              <h2 className="text-2xl font-bold text-gray-900">주문 정보 입력</h2>
              <button
                onClick={() => setShowOrderModal(false)}
                className="text-gray-400 hover:text-gray-600"
              >
                <X className="w-6 h-6" />
              </button>
            </div>

            {/* 모달 본문 */}
            <form onSubmit={handleOrderSubmit} className="p-6 space-y-6">
              {/* 주문 상품 정보 */}
              <div className="bg-gray-50 rounded-lg p-4">
                <h3 className="text-lg font-semibold text-gray-900 mb-3">주문 상품</h3>
                <div className="flex items-center space-x-4">
                  {item.itemImageUrl && (
                    <img
                      src={item.itemImageUrl}
                      alt={item.itemName}
                      className="w-20 h-20 object-cover rounded"
                    />
                  )}
                  <div className="flex-1">
                    <p className="font-medium text-gray-900">{item.itemName}</p>
                    <p className="text-sm text-gray-600">수량: {quantity}개</p>
                    <p className="text-lg font-semibold text-indigo-600">
                      {(item.price * quantity)?.toLocaleString()}원
                    </p>
                  </div>
                </div>
              </div>

              {/* 주문자 정보 */}
              <div>
                <h3 className="text-lg font-semibold text-gray-900 mb-4">주문자 정보</h3>
                <div className="space-y-4">
                  <div>
                    <label htmlFor="ordererName" className="block text-sm font-medium text-gray-700 mb-1">
                      주문자 이름 *
                    </label>
                    <input
                      type="text"
                      id="ordererName"
                      name="ordererName"
                      required
                      value={orderFormData.ordererName}
                      onChange={handleOrderFormChange}
                      className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 text-gray-900"
                      placeholder="주문자 이름을 입력해주세요"
                    />
                  </div>
                </div>
              </div>

              {/* 수신자 정보 */}
              <div>
                <h3 className="text-lg font-semibold text-gray-900 mb-4">수신자 정보</h3>
                <div className="space-y-4">
                  <div>
                    <label htmlFor="receiverName" className="block text-sm font-medium text-gray-700 mb-1">
                      수신자 이름 *
                    </label>
                    <input
                      type="text"
                      id="receiverName"
                      name="receiverName"
                      required
                      value={orderFormData.receiverName}
                      onChange={handleOrderFormChange}
                      className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 text-gray-900"
                      placeholder="수신자 이름을 입력해주세요"
                    />
                  </div>
                  <div>
                    <label htmlFor="receiverPhoneNumber" className="block text-sm font-medium text-gray-700 mb-1">
                      수신자 전화번호 *
                    </label>
                    <input
                      type="tel"
                      id="receiverPhoneNumber"
                      name="receiverPhoneNumber"
                      required
                      value={orderFormData.receiverPhoneNumber}
                      onChange={handleOrderFormChange}
                      className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 text-gray-900"
                      placeholder="010-1234-5678"
                    />
                  </div>
                </div>
              </div>

              {/* 배송 주소 정보 */}
              <div>
                <h3 className="text-lg font-semibold text-gray-900 mb-4">배송 주소</h3>
                <div className="space-y-4">
                  <div>
                    <label htmlFor="city" className="block text-sm font-medium text-gray-700 mb-1">
                      시/도 *
                    </label>
                    <input
                      type="text"
                      id="city"
                      name="city"
                      required
                      value={orderFormData.city}
                      onChange={handleOrderFormChange}
                      className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 text-gray-900"
                      placeholder="서울특별시"
                    />
                  </div>
                  <div>
                    <label htmlFor="street" className="block text-sm font-medium text-gray-700 mb-1">
                      도로명 주소 *
                    </label>
                    <input
                      type="text"
                      id="street"
                      name="street"
                      required
                      value={orderFormData.street}
                      onChange={handleOrderFormChange}
                      className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 text-gray-900"
                      placeholder="테헤란로 123"
                    />
                  </div>
                  <div>
                    <label htmlFor="details" className="block text-sm font-medium text-gray-700 mb-1">
                      상세 주소 *
                    </label>
                    <input
                      type="text"
                      id="details"
                      name="details"
                      required
                      value={orderFormData.details}
                      onChange={handleOrderFormChange}
                      className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 text-gray-900"
                      placeholder="101동 101호"
                    />
                  </div>
                  <div>
                    <label htmlFor="zipcode" className="block text-sm font-medium text-gray-700 mb-1">
                      우편번호 *
                    </label>
                    <input
                      type="text"
                      id="zipcode"
                      name="zipcode"
                      required
                      value={orderFormData.zipcode}
                      onChange={handleOrderFormChange}
                      className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 text-gray-900"
                      placeholder="12345"
                    />
                  </div>
                </div>
              </div>

              {/* 모달 푸터 */}
              <div className="flex justify-end space-x-3 pt-4 border-t">
                <button
                  type="button"
                  onClick={() => setShowOrderModal(false)}
                  className="px-6 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50"
                >
                  취소
                </button>
                <button
                  type="submit"
                  disabled={createOrderMutation.isPending}
                  className="px-6 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  {createOrderMutation.isPending ? '주문 처리 중...' : '주문하기'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}

