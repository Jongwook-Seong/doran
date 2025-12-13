'use client';

import { useState } from 'react';
import { useParams } from 'next/navigation';
import { useQuery } from '@tanstack/react-query';
import { getOrderDetail, getDeliveryTracking, OrderDetailResponse, DeliveryStatus } from '@/utils/api';
import { useAuth } from '@/contexts/AuthContext';
import { Package, Truck, CheckCircle, Clock, ArrowLeft, MapPin, User, Phone, Search } from 'lucide-react';
import Link from 'next/link';
import Header from '@/components/Header';
import Footer from '@/components/Footer';

// 배송 상태별 정보
const getDeliveryStatusInfo = (status: DeliveryStatus) => {
  switch (status) {
    case 'READY':
      return { 
        icon: Clock, 
        color: 'text-yellow-600', 
        bgColor: 'bg-yellow-100', 
        text: '배송 준비중',
        description: '상품이 배송 준비 중입니다.'
      };
    case 'DELIVER':
      return { 
        icon: Truck, 
        color: 'text-indigo-600', 
        bgColor: 'bg-indigo-100', 
        text: '배송중',
        description: '상품이 배송 중입니다.'
      };
    case 'COMPLETE':
      return { 
        icon: CheckCircle, 
        color: 'text-green-600', 
        bgColor: 'bg-green-100', 
        text: '배송 완료',
        description: '상품이 배송 완료되었습니다.'
      };
    default:
      return { 
        icon: Clock, 
        color: 'text-gray-600', 
        bgColor: 'bg-gray-100', 
        text: '알 수 없음',
        description: '상태를 확인할 수 없습니다.'
      };
  }
};

// 주문 상세 페이지 컴포넌트
export default function OrderDetailPage() {
  const params = useParams();
  const { isAuthenticated, user } = useAuth();
  const orderUuid = params.id as string;
  const [showDeliveryTracking, setShowDeliveryTracking] = useState(false);

  // 주문 상세 정보를 가져오는 쿼리
  const { data: orderData, isLoading, error } = useQuery({
    queryKey: ['orderDetail', orderUuid],
    queryFn: () => getOrderDetail(user?.userUuid || '', orderUuid),
    enabled: !!user?.userUuid && !!orderUuid && isAuthenticated,
    staleTime: 2 * 60 * 1000,
  });

  // 배송 추적 정보를 가져오는 쿼리 (배송 조회 버튼 클릭 시 활성화)
  const { data: deliveryTrackingData, isLoading: isTrackingLoading } = useQuery({
    queryKey: ['deliveryTracking', orderUuid],
    queryFn: () => getDeliveryTracking(user?.userUuid || '', orderUuid),
    enabled: showDeliveryTracking && !!user?.userUuid && !!orderUuid && isAuthenticated,
    staleTime: 1 * 60 * 1000,
  });

  if (!isAuthenticated) {
    return (
      <div className="min-h-screen bg-gray-50">
        <Header />
        <main className="flex-1 flex items-center justify-center py-20">
          <div className="text-center">
            <h2 className="text-2xl font-bold text-gray-900 mb-4">로그인이 필요합니다</h2>
            <Link
              href="/login"
              className="bg-indigo-600 text-white px-6 py-3 rounded-md hover:bg-indigo-700"
            >
              로그인하기
            </Link>
          </div>
        </main>
        <Footer />
      </div>
    );
  }

  if (isLoading) {
    return (
      <div className="min-h-screen bg-gray-50">
        <Header />
        <main className="max-w-4xl mx-auto py-8 px-4 sm:px-6 lg:px-8">
          <div className="animate-pulse space-y-6">
            <div className="h-8 bg-gray-200 rounded w-1/4"></div>
            <div className="bg-white rounded-lg shadow-sm p-6">
              <div className="h-6 bg-gray-200 rounded w-1/3 mb-4"></div>
              <div className="space-y-3">
                <div className="h-4 bg-gray-200 rounded w-full"></div>
                <div className="h-4 bg-gray-200 rounded w-2/3"></div>
                <div className="h-4 bg-gray-200 rounded w-1/2"></div>
              </div>
            </div>
          </div>
        </main>
        <Footer />
      </div>
    );
  }

  if (error || !orderData?.success || !orderData?.data) {
    return (
      <div className="min-h-screen bg-gray-50">
        <Header />
        <main className="flex-1 flex items-center justify-center py-20">
          <div className="text-center">
            <h2 className="text-2xl font-bold text-gray-900 mb-4">주문을 찾을 수 없습니다</h2>
            <p className="text-gray-600 mb-8">요청하신 주문이 존재하지 않습니다.</p>
            <Link
              href="/orders"
              className="bg-indigo-600 text-white px-6 py-3 rounded-md hover:bg-indigo-700"
            >
              주문 내역으로 돌아가기
            </Link>
          </div>
        </main>
        <Footer />
      </div>
    );
  }

  const order: OrderDetailResponse = orderData.data;
  const statusInfo = getDeliveryStatusInfo(order.deliveryStatus);
  
  // 총 주문 금액 계산
  const totalAmount = order.orderItemSimpleList.reduce((sum, item) => sum + (item.price * item.count), 0);

  return (
    <div className="min-h-screen bg-gray-50 flex flex-col">
      {/* 헤더 컴포넌트 사용 */}
      <Header />

      {/* 메인 콘텐츠 */}
      <main className="flex-1 max-w-4xl mx-auto w-full py-8 px-4 sm:px-6 lg:px-8">
        {/* 뒤로가기 버튼 */}
        <div className="mb-6">
          <Link
            href="/orders"
            className="inline-flex items-center text-indigo-600 hover:text-indigo-500"
          >
            <ArrowLeft className="w-5 h-5 mr-2" />
            주문 내역으로 돌아가기
          </Link>
        </div>

        {/* 주문 완료 성공 메시지 */}
        <div className="bg-green-50 border border-green-200 rounded-lg p-4 mb-6">
          <div className="flex items-center">
            <CheckCircle className="w-6 h-6 text-green-600 mr-3" />
            <div>
              <h3 className="text-lg font-semibold text-green-900">주문이 완료되었습니다!</h3>
              <p className="text-green-700">주문번호: {orderUuid}</p>
            </div>
          </div>
        </div>

        {/* 주문 정보 헤더 */}
        <div className="bg-white rounded-lg shadow-sm p-6 mb-6">
          <div className="flex items-center justify-between mb-4">
            <div>
              <h1 className="text-2xl font-bold text-gray-900">주문 상세</h1>
              <p className="text-gray-600 mt-1">주문번호: {orderUuid}</p>
            </div>
            <div className={`flex items-center px-4 py-2 rounded-full ${statusInfo.bgColor}`}>
              <statusInfo.icon className={`w-5 h-5 mr-2 ${statusInfo.color}`} />
              <span className={`font-medium ${statusInfo.color}`}>
                {statusInfo.text}
              </span>
            </div>
          </div>
          
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mt-4">
            <div>
              <h3 className="text-sm font-medium text-gray-500 mb-1">주문일</h3>
              <p className="text-gray-900">
                {new Date(order.orderDate).toLocaleDateString('ko-KR', {
                  year: 'numeric',
                  month: 'long',
                  day: 'numeric',
                  hour: '2-digit',
                  minute: '2-digit'
                })}
              </p>
            </div>
            <div>
              <h3 className="text-sm font-medium text-gray-500 mb-1">배송 상태</h3>
              <p className="text-gray-900">{statusInfo.description}</p>
            </div>
          </div>
        </div>

        {/* 주문 상품 목록 */}
        <div className="bg-white rounded-lg shadow-sm p-6 mb-6">
          <h2 className="text-lg font-semibold text-gray-900 mb-4">주문 상품</h2>
          <div className="space-y-4">
            {order.orderItemSimpleList.map((item, index) => (
              <div key={index} className="flex items-center space-x-4 p-4 border border-gray-200 rounded-lg">
                <div className="flex-shrink-0 w-20 h-20 bg-gray-200 rounded-md flex items-center justify-center overflow-hidden">
                  {item.itemImageUrl ? (
                    <img
                      src={item.itemImageUrl}
                      alt={item.itemName || '상품 이미지'}
                      className="w-full h-full object-cover"
                    />
                  ) : (
                    <Package className="w-8 h-8 text-gray-400" />
                  )}
                </div>
                <div className="flex-1 min-w-0">
                  <h3 className="text-lg font-medium text-gray-900">
                    {item.itemName || `상품 ${index + 1}`}
                  </h3>
                  <p className="text-gray-600">단가: {item.price.toLocaleString()}원</p>
                  <p className="text-gray-600">수량: {item.count}개</p>
                </div>
                <div className="text-right">
                  <p className="text-lg font-semibold text-gray-900">
                    {(item.price * item.count).toLocaleString()}원
                  </p>
                </div>
              </div>
            ))}
          </div>
          
          {/* 총 주문 금액 */}
          <div className="mt-6 pt-6 border-t">
            <div className="flex justify-between items-center">
              <span className="text-lg font-semibold text-gray-900">총 주문 금액</span>
              <span className="text-2xl font-bold text-indigo-600">
                {totalAmount.toLocaleString()}원
              </span>
            </div>
          </div>
        </div>

        {/* 배송 정보 */}
        <div className="bg-white rounded-lg shadow-sm p-6 mb-6">
          <h2 className="text-lg font-semibold text-gray-900 mb-4">배송 정보</h2>
          <div className="space-y-4">
            {/* 수신자 정보 */}
            <div className="flex items-start space-x-3">
              <User className="w-5 h-5 text-gray-400 mt-0.5" />
              <div>
                <h3 className="text-sm font-medium text-gray-500">수신자</h3>
                <p className="text-gray-900">{order.transceiverInfo.receiverName}</p>
              </div>
            </div>
            
            <div className="flex items-start space-x-3">
              <Phone className="w-5 h-5 text-gray-400 mt-0.5" />
              <div>
                <h3 className="text-sm font-medium text-gray-500">연락처</h3>
                <p className="text-gray-900">{order.transceiverInfo.receiverPhoneNumber}</p>
              </div>
            </div>

            {/* 배송 주소 */}
            <div className="flex items-start space-x-3">
              <MapPin className="w-5 h-5 text-gray-400 mt-0.5" />
              <div>
                <h3 className="text-sm font-medium text-gray-500">배송지</h3>
                <p className="text-gray-900">
                  {order.address.zipcode}
                  <br />
                  {order.address.city} {order.address.street}
                  <br />
                  {order.address.details}
                </p>
              </div>
            </div>

            {/* 배송 상태 */}
            <div className="flex items-start space-x-3">
              <Truck className="w-5 h-5 text-gray-400 mt-0.5" />
              <div>
                <h3 className="text-sm font-medium text-gray-500">배송 상태</h3>
                <p className="text-gray-900">{statusInfo.description}</p>
              </div>
            </div>
          </div>
        </div>

        {/* 배송 추적 정보 (배송 조회 버튼 클릭 시 표시) */}
        {showDeliveryTracking && (
          <div className="bg-white rounded-lg shadow-sm p-6 mb-6">
            <h2 className="text-lg font-semibold text-gray-900 mb-4">배송 추적</h2>
            {isTrackingLoading ? (
              <div className="animate-pulse space-y-3">
                <div className="h-4 bg-gray-200 rounded w-3/4"></div>
                <div className="h-4 bg-gray-200 rounded w-1/2"></div>
              </div>
            ) : deliveryTrackingData?.success && deliveryTrackingData.data ? (
              <div className="space-y-4">
                {deliveryTrackingData.data.deliveryTrackingInfoList.length > 0 ? (
                  deliveryTrackingData.data.deliveryTrackingInfoList.map((tracking, index) => (
                    <div key={index} className="border-l-4 border-indigo-500 pl-4 py-2">
                      <div className="flex justify-between items-start">
                        <div>
                          <p className="font-medium text-gray-900">{tracking.postLocation}</p>
                          <p className="text-sm text-gray-600">
                            {tracking.courier && `택배사: ${tracking.courier}`}
                            {tracking.contactNumber && ` | 연락처: ${tracking.contactNumber}`}
                          </p>
                          <p className="text-sm text-gray-500">
                            {new Date(tracking.postDateTime).toLocaleString('ko-KR')}
                          </p>
                        </div>
                      </div>
                    </div>
                  ))
                ) : (
                  <p className="text-gray-600">배송 추적 정보가 없습니다.</p>
                )}
              </div>
            ) : (
              <p className="text-gray-600">배송 추적 정보를 불러올 수 없습니다.</p>
            )}
          </div>
        )}

        {/* 배송 조회 버튼 */}
        <div className="flex justify-end space-x-3">
          <button
            onClick={() => setShowDeliveryTracking(!showDeliveryTracking)}
            className="px-6 py-2 bg-indigo-600 text-white rounded-md hover:bg-indigo-700 transition-colors flex items-center"
          >
            <Search className="w-5 h-5 mr-2" />
            {showDeliveryTracking ? '배송 조회 닫기' : '배송 조회'}
          </button>
        </div>
      </main>

      {/* 푸터 컴포넌트 사용 */}
      <Footer />
    </div>
  );
}
