'use client';

import { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { getOrderList } from '@/utils/api';
import { useAuth } from '@/contexts/AuthContext';
import { Package, Truck, CheckCircle, Clock, XCircle } from 'lucide-react';
import Link from 'next/link';

// 주문 상태 타입 정의
type OrderStatus = 'PENDING' | 'CONFIRMED' | 'SHIPPING' | 'DELIVERED' | 'CANCELLED';

// 주문 아이템 타입 정의
interface OrderItem {
  itemUuid: string;
  itemName: string;
  itemPrice: number;
  quantity: number;
  itemImageUrl?: string;
}

// 주문 정보 타입 정의
interface Order {
  orderUuid: string;
  orderDate: string;
  status: OrderStatus;
  totalAmount: number;
  items: OrderItem[];
  deliveryAddress?: string;
  deliveryMessage?: string;
}

// 주문 상태별 아이콘과 색상 매핑
const getStatusInfo = (status: OrderStatus) => {
  switch (status) {
    case 'PENDING':
      return { icon: Clock, color: 'text-yellow-600', bgColor: 'bg-yellow-100', text: '주문 확인중' };
    case 'CONFIRMED':
      return { icon: CheckCircle, color: 'text-blue-600', bgColor: 'bg-blue-100', text: '주문 확인됨' };
    case 'SHIPPING':
      return { icon: Truck, color: 'text-indigo-600', bgColor: 'bg-indigo-100', text: '배송중' };
    case 'DELIVERED':
      return { icon: Package, color: 'text-green-600', bgColor: 'bg-green-100', text: '배송완료' };
    case 'CANCELLED':
      return { icon: XCircle, color: 'text-red-600', bgColor: 'bg-red-100', text: '주문 취소' };
    default:
      return { icon: Clock, color: 'text-gray-600', bgColor: 'bg-gray-100', text: '알 수 없음' };
  }
};

// 주문 내역 페이지 컴포넌트
export default function OrdersPage() {
  const { isAuthenticated, user } = useAuth();
  const [selectedStatus, setSelectedStatus] = useState<OrderStatus | 'ALL'>('ALL');

  // 주문 목록을 가져오는 쿼리
  const { data: ordersData, isLoading, error } = useQuery({
    queryKey: ['orderList', user?.userUuid],
    queryFn: () => getOrderList(user?.userUuid || ''),
    enabled: !!user?.userUuid && isAuthenticated,
    staleTime: 2 * 60 * 1000,
  });

  // 필터링된 주문 목록
  const filteredOrders = ordersData?.success 
    ? (ordersData.data || []).filter((order: Order) => 
        selectedStatus === 'ALL' || order.status === selectedStatus
      )
    : [];

  if (!isAuthenticated) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <div className="text-center">
          <h2 className="text-2xl font-bold text-gray-900 mb-4">로그인이 필요합니다</h2>
          <p className="text-gray-600 mb-8">주문 내역을 확인하려면 로그인해주세요.</p>
          <div className="space-x-4">
            <Link
              href="/login"
              className="bg-indigo-600 text-white px-6 py-3 rounded-md hover:bg-indigo-700"
            >
              로그인하기
            </Link>
            <Link
              href="/signup"
              className="bg-gray-600 text-white px-6 py-3 rounded-md hover:bg-gray-700"
            >
              회원가입하기
            </Link>
          </div>
        </div>
      </div>
    );
  }

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
              <Link href="/orders" className="text-gray-900 hover:text-indigo-600 px-3 py-2 text-sm font-medium">
                주문내역
              </Link>
            </nav>
          </div>
        </div>
      </header>

      {/* 메인 콘텐츠 */}
      <main className="max-w-7xl mx-auto py-8 px-4 sm:px-6 lg:px-8">
        {/* 페이지 제목 */}
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900">주문 내역</h1>
          <p className="mt-2 text-gray-600">
            {user?.username}님의 주문 내역을 확인하실 수 있습니다.
          </p>
        </div>

        {/* 상태 필터 */}
        <div className="mb-8">
          <div className="flex flex-wrap gap-2">
            {['ALL', 'PENDING', 'CONFIRMED', 'SHIPPING', 'DELIVERED', 'CANCELLED'].map((status) => {
              const statusInfo = getStatusInfo(status as OrderStatus);
              const isSelected = selectedStatus === status;
              
              return (
                <button
                  key={status}
                  onClick={() => setSelectedStatus(status as OrderStatus | 'ALL')}
                  className={`px-4 py-2 rounded-full text-sm font-medium transition-colors ${
                    isSelected
                      ? 'bg-indigo-600 text-white'
                      : 'bg-white text-gray-700 border border-gray-300 hover:bg-gray-50'
                  }`}
                >
                  {status === 'ALL' ? '전체' : statusInfo.text}
                </button>
              );
            })}
          </div>
        </div>

        {/* 주문 목록 */}
        {isLoading ? (
          <div className="space-y-6">
            {[...Array(3)].map((_, i) => (
              <div key={i} className="bg-white rounded-lg shadow-sm p-6 animate-pulse">
                <div className="h-6 bg-gray-200 rounded w-1/4 mb-4"></div>
                <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                  <div className="h-32 bg-gray-200 rounded"></div>
                  <div className="md:col-span-2 space-y-3">
                    <div className="h-4 bg-gray-200 rounded w-3/4"></div>
                    <div className="h-4 bg-gray-200 rounded w-1/2"></div>
                    <div className="h-4 bg-gray-200 rounded w-1/4"></div>
                  </div>
                </div>
              </div>
            ))}
          </div>
        ) : error ? (
          <div className="text-center py-12">
            <Package className="mx-auto h-24 w-24 text-gray-400 mb-4" />
            <h3 className="text-lg font-medium text-gray-900 mb-2">주문 내역을 불러올 수 없습니다</h3>
            <p className="text-gray-600">잠시 후 다시 시도해주세요.</p>
          </div>
        ) : filteredOrders.length === 0 ? (
          <div className="text-center py-12">
            <Package className="mx-auto h-24 w-24 text-gray-400 mb-4" />
            <h3 className="text-lg font-medium text-gray-900 mb-2">주문 내역이 없습니다</h3>
            <p className="text-gray-600 mb-8">아직 주문한 상품이 없습니다.</p>
            <Link
              href="/products"
              className="bg-indigo-600 text-white px-6 py-3 rounded-md hover:bg-indigo-700"
            >
              상품 둘러보기
            </Link>
          </div>
        ) : (
          <div className="space-y-6">
            {filteredOrders.map((order: Order) => (
              <OrderCard key={order.orderUuid} order={order} />
            ))}
          </div>
        )}
      </main>
    </div>
  );
}

// 주문 카드 컴포넌트
function OrderCard({ order }: { order: Order }) {
  const statusInfo = getStatusInfo(order.status);

  return (
    <div className="bg-white rounded-lg shadow-sm border border-gray-200">
      {/* 주문 헤더 */}
      <div className="px-6 py-4 border-b border-gray-200">
        <div className="flex items-center justify-between">
          <div>
            <h3 className="text-lg font-semibold text-gray-900">
              주문번호: {order.orderUuid}
            </h3>
            <p className="text-sm text-gray-600">
              주문일: {new Date(order.orderDate).toLocaleDateString('ko-KR')}
            </p>
          </div>
          <div className="flex items-center space-x-3">
            <div className={`flex items-center px-3 py-1 rounded-full ${statusInfo.bgColor}`}>
              <statusInfo.icon className={`w-4 h-4 mr-2 ${statusInfo.color}`} />
              <span className={`text-sm font-medium ${statusInfo.color}`}>
                {statusInfo.text}
              </span>
            </div>
            <span className="text-lg font-bold text-gray-900">
              {order.totalAmount.toLocaleString()}원
            </span>
          </div>
        </div>
      </div>

      {/* 주문 아이템들 */}
      <div className="px-6 py-4">
        <div className="space-y-4">
          {order.items.map((item, index) => (
            <div key={index} className="flex items-center space-x-4">
              <div className="flex-shrink-0 w-16 h-16 bg-gray-200 rounded-md flex items-center justify-center overflow-hidden">
                {item.itemImageUrl ? (
                  <img
                    src={item.itemImageUrl}
                    alt={item.itemName}
                    className="w-full h-full object-cover"
                  />
                ) : (
                  <Package className="w-6 h-6 text-gray-400" />
                )}
              </div>
              <div className="flex-1 min-w-0">
                <h4 className="text-sm font-medium text-gray-900 truncate">
                  {item.itemName}
                </h4>
                <p className="text-sm text-gray-600">
                  {item.itemPrice.toLocaleString()}원 × {item.quantity}개
                </p>
              </div>
              <div className="text-sm font-medium text-gray-900">
                {(item.itemPrice * item.quantity).toLocaleString()}원
              </div>
            </div>
          ))}
        </div>

        {/* 배송 정보 */}
        {order.deliveryAddress && (
          <div className="mt-4 pt-4 border-t border-gray-200">
            <h5 className="text-sm font-medium text-gray-900 mb-1">배송지</h5>
            <p className="text-sm text-gray-600">{order.deliveryAddress}</p>
            {order.deliveryMessage && (
              <p className="text-sm text-gray-600 mt-1">
                배송 요청사항: {order.deliveryMessage}
              </p>
            )}
          </div>
        )}

        {/* 액션 버튼들 */}
        <div className="mt-4 pt-4 border-t border-gray-200 flex justify-end space-x-3">
          <Link
            href={`/orders/${order.orderUuid}`}
            className="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 hover:bg-gray-50"
          >
            상세보기
          </Link>
          {order.status === 'PENDING' && (
            <button className="px-4 py-2 bg-red-600 text-white rounded-md text-sm font-medium hover:bg-red-700">
              주문 취소
            </button>
          )}
          {order.status === 'DELIVERED' && (
            <button className="px-4 py-2 bg-indigo-600 text-white rounded-md text-sm font-medium hover:bg-indigo-700">
              배송 추적
            </button>
          )}
        </div>
      </div>
    </div>
  );
}


