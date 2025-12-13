'use client';

import { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { getBasketList, deleteFromBasket, createOrder, BasketItem, OrderCreateRequest, ItemSimpleInfo, TransceiverInfo, Address } from '@/utils/api';
import { useAuth } from '@/contexts/AuthContext';
import { ShoppingCart, Trash2, Package, ArrowRight } from 'lucide-react';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import Header from '@/components/Header';
import Footer from '@/components/Footer';

// 장바구니 페이지 컴포넌트
export default function CartPage() {
  const { isAuthenticated, user } = useAuth();
  const router = useRouter();
  const queryClient = useQueryClient();
  
  // 선택된 아이템들을 관리하는 상태
  const [selectedItems, setSelectedItems] = useState<Set<string>>(new Set());

  // 장바구니 목록을 가져오는 쿼리
  const { data: basketData, isLoading, error } = useQuery({
    queryKey: ['basketList', user?.userUuid],
    queryFn: () => getBasketList(user?.userUuid || ''),
    enabled: !!user?.userUuid && isAuthenticated,
    staleTime: 1 * 60 * 1000, // 1분간 캐시 유지
  });

  // 장바구니 아이템 삭제 mutation
  const deleteMutation = useMutation({
    mutationFn: (itemUuid: string) => deleteFromBasket(user?.userUuid || '', itemUuid),
    onSuccess: () => {
      // 삭제 성공 시 장바구니 목록 다시 가져오기
      queryClient.invalidateQueries({ queryKey: ['basketList'] });
      alert('장바구니에서 삭제되었습니다.');
    },
    onError: () => {
      alert('삭제에 실패했습니다. 다시 시도해주세요.');
    },
  });

  // 아이템 선택/해제 함수
  const toggleItemSelection = (itemUuid: string) => {
    setSelectedItems(prev => {
      const newSet = new Set(prev);
      if (newSet.has(itemUuid)) {
        newSet.delete(itemUuid);
      } else {
        newSet.add(itemUuid);
      }
      return newSet;
    });
  };

  // 전체 선택/해제 함수
  const toggleAllSelection = () => {
    if (basketItems.length === selectedItems.size) {
      setSelectedItems(new Set());
    } else {
      setSelectedItems(new Set(basketItems.map((item: BasketItem) => item.itemUuid)));
    }
  };

  // 선택된 아이템 삭제 함수
  const handleDeleteSelected = () => {
    if (selectedItems.size === 0) {
      alert('삭제할 상품을 선택해주세요.');
      return;
    }

    if (confirm(`${selectedItems.size}개의 상품을 장바구니에서 삭제하시겠습니까?`)) {
      selectedItems.forEach(itemUuid => {
        deleteMutation.mutate(itemUuid);
      });
      setSelectedItems(new Set());
    }
  };

  // 주문 생성 mutation
  const orderMutation = useMutation({
    mutationFn: (orderData: OrderCreateRequest) => createOrder(user?.userUuid || '', orderData),
    onSuccess: () => {
      // 주문 성공 시 장바구니 목록 다시 가져오기
      queryClient.invalidateQueries({ queryKey: ['basketList'] });
      queryClient.invalidateQueries({ queryKey: ['orderList'] });
      alert('주문이 완료되었습니다!');
      // 선택된 아이템 초기화
      setSelectedItems(new Set());
      // 주문 내역 페이지로 이동
      router.push('/orders');
    },
    onError: (error: any) => {
      console.error('주문 실패:', error);
      alert('주문에 실패했습니다. 다시 시도해주세요.');
    },
  });

  // 주문하기 함수
  const handleOrder = () => {
    if (selectedItems.size === 0) {
      alert('주문할 상품을 선택해주세요.');
      return;
    }

    // 선택된 아이템들의 정보를 기반으로 주문 데이터 생성
    const selectedBasketItems = basketItems.filter(item => selectedItems.has(item.itemUuid));
    
    // ItemSimpleInfo 리스트 생성
    const itemSimpleInfoList: ItemSimpleInfo[] = selectedBasketItems.map(item => ({
      itemUuid: item.itemUuid,
      count: item.count,
      price: item.price,
    }));

    // 기본 주문자 및 수신자 정보 (실제로는 사용자 입력을 받아야 하지만, 간단하게 기본값 사용)
    const transceiverInfo: TransceiverInfo = {
      ordererName: user?.username || '',
      receiverName: user?.username || '',
      receiverPhoneNumber: user?.email || '',
    };

    // 기본 주소 정보 (실제로는 사용자 입력을 받아야 하지만, 간단하게 기본값 사용)
    const address: Address = {
      city: '서울특별시',
      street: '주소를 입력해주세요',
      details: '상세주소를 입력해주세요',
      zipcode: '12345',
    };

    // OrderCreateRequest 생성
    const orderData: OrderCreateRequest = {
      itemSimpleInfoList,
      transceiverInfo,
      address,
    };

    // 주문 확인 메시지
    if (confirm(`${selectedItems.size}개의 상품을 주문하시겠습니까?\n\n주의: 현재는 기본 주소 정보로 주문됩니다.`)) {
      orderMutation.mutate(orderData);
    }
  };

  // 장바구니 아이템 목록
  const basketItems: BasketItem[] = basketData?.success ? (basketData.data || []) : [];

  // 선택된 아이템들의 총 금액 계산
  const selectedTotalPrice = basketItems
    .filter(item => selectedItems.has(item.itemUuid))
    .reduce((sum, item) => sum + (item.price * item.count), 0);

  // 로그인하지 않은 경우
  if (!isAuthenticated) {
    return (
      <div className="min-h-screen bg-gray-50">
        <Header />
        <main className="flex-1 flex items-center justify-center py-20">
          <div className="text-center">
            <ShoppingCart className="mx-auto h-24 w-24 text-gray-400 mb-4" />
            <h2 className="text-2xl font-bold text-gray-900 mb-4">로그인이 필요합니다</h2>
            <p className="text-gray-600 mb-8">장바구니를 확인하려면 로그인해주세요.</p>
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
        </main>
        <Footer />
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50 flex flex-col">
      {/* 헤더 컴포넌트 사용 */}
      <Header />

      {/* 메인 콘텐츠 */}
      <main className="flex-1 max-w-7xl mx-auto w-full py-8 px-4 sm:px-6 lg:px-8">
        {/* 페이지 제목 */}
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900">장바구니</h1>
          <p className="mt-2 text-gray-600">
            {user?.username}님의 장바구니입니다.
          </p>
        </div>

        {/* 로딩 상태 */}
        {isLoading ? (
          <div className="space-y-4">
            {[...Array(3)].map((_, i) => (
              <div key={i} className="bg-white rounded-lg shadow-sm p-6 animate-pulse">
                <div className="flex items-center space-x-4">
                  <div className="w-20 h-20 bg-gray-200 rounded"></div>
                  <div className="flex-1 space-y-2">
                    <div className="h-4 bg-gray-200 rounded w-3/4"></div>
                    <div className="h-4 bg-gray-200 rounded w-1/2"></div>
                  </div>
                </div>
              </div>
            ))}
          </div>
        ) : error ? (
          // 에러 상태
          <div className="text-center py-12">
            <ShoppingCart className="mx-auto h-24 w-24 text-gray-400 mb-4" />
            <h3 className="text-lg font-medium text-gray-900 mb-2">장바구니를 불러올 수 없습니다</h3>
            <p className="text-gray-600">잠시 후 다시 시도해주세요.</p>
          </div>
        ) : basketItems.length === 0 ? (
          // 빈 장바구니
          <div className="text-center py-12">
            <ShoppingCart className="mx-auto h-24 w-24 text-gray-400 mb-4" />
            <h3 className="text-lg font-medium text-gray-900 mb-2">장바구니가 비어있습니다</h3>
            <p className="text-gray-600 mb-8">상품을 담아보세요!</p>
            <Link
              href="/products"
              className="bg-indigo-600 text-white px-6 py-3 rounded-md hover:bg-indigo-700"
            >
              상품 둘러보기
            </Link>
          </div>
        ) : (
          // 장바구니 아이템 목록
          <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
            {/* 장바구니 아이템 목록 */}
            <div className="lg:col-span-2 space-y-4">
              {/* 전체 선택 체크박스 */}
              <div className="bg-white rounded-lg shadow-sm p-4">
                <div className="flex items-center justify-between">
                  <label className="flex items-center space-x-2 cursor-pointer">
                    <input
                      type="checkbox"
                      checked={basketItems.length === selectedItems.size}
                      onChange={toggleAllSelection}
                      className="w-5 h-5 rounded border-gray-300 text-indigo-600 focus:ring-indigo-500"
                    />
                    <span className="text-sm font-medium text-gray-900">
                      전체 선택 ({selectedItems.size}/{basketItems.length})
                    </span>
                  </label>
                  <button
                    onClick={handleDeleteSelected}
                    className="text-sm text-red-600 hover:text-red-500 flex items-center"
                  >
                    <Trash2 className="w-4 h-4 mr-1" />
                    선택 삭제
                  </button>
                </div>
              </div>

              {/* 장바구니 아이템 카드들 */}
              {basketItems.map((item: BasketItem) => (
                <div key={item.itemUuid} className="bg-white rounded-lg shadow-sm p-6">
                  <div className="flex items-center space-x-4">
                    {/* 선택 체크박스 */}
                    <input
                      type="checkbox"
                      checked={selectedItems.has(item.itemUuid)}
                      onChange={() => toggleItemSelection(item.itemUuid)}
                      className="w-5 h-5 rounded border-gray-300 text-indigo-600 focus:ring-indigo-500"
                    />

                    {/* 상품 이미지 */}
                    <div className="flex-shrink-0 w-20 h-20 bg-gray-200 rounded-md flex items-center justify-center overflow-hidden">
                      {item.itemImageUrl ? (
                        <img
                          src={item.itemImageUrl}
                          alt={item.itemName}
                          className="w-full h-full object-cover"
                        />
                      ) : (
                        <Package className="w-8 h-8 text-gray-400" />
                      )}
                    </div>

                    {/* 상품 정보 */}
                    <div className="flex-1 min-w-0">
                      <h3 className="text-lg font-medium text-gray-900 truncate">
                        {item.itemName}
                      </h3>
                                             <p className="text-gray-600">
                         {item.price.toLocaleString()}원 × {item.count}개
                       </p>
                       <p className="text-lg font-semibold text-indigo-600">
                         {(item.price * item.count).toLocaleString()}원
                       </p>
                    </div>

                    {/* 삭제 버튼 */}
                    <button
                      onClick={() => {
                        if (confirm('이 상품을 장바구니에서 삭제하시겠습니까?')) {
                          deleteMutation.mutate(item.itemUuid);
                        }
                      }}
                      disabled={deleteMutation.isPending}
                      className="text-red-600 hover:text-red-500 disabled:opacity-50"
                    >
                      <Trash2 className="w-5 h-5" />
                    </button>
                  </div>
                </div>
              ))}
            </div>

            {/* 주문 요약 */}
            <div className="lg:col-span-1">
              <div className="bg-white rounded-lg shadow-sm p-6 sticky top-8">
                <h2 className="text-lg font-semibold text-gray-900 mb-4">주문 요약</h2>
                
                <div className="space-y-3 mb-6">
                  <div className="flex justify-between text-gray-600">
                    <span>선택한 상품 ({selectedItems.size}개)</span>
                    <span>{selectedTotalPrice.toLocaleString()}원</span>
                  </div>
                  <div className="flex justify-between text-gray-600">
                    <span>배송비</span>
                    <span>0원</span>
                  </div>
                  <div className="border-t pt-3">
                    <div className="flex justify-between">
                      <span className="text-lg font-semibold text-gray-900">총 결제금액</span>
                      <span className="text-lg font-bold text-indigo-600">
                        {selectedTotalPrice.toLocaleString()}원
                      </span>
                    </div>
                  </div>
                </div>

                                 <button
                   onClick={handleOrder}
                   disabled={selectedItems.size === 0 || orderMutation.isPending}
                   className="w-full bg-indigo-600 text-white py-3 px-6 rounded-md hover:bg-indigo-700 transition-colors flex items-center justify-center disabled:opacity-50 disabled:cursor-not-allowed"
                 >
                   <span>{orderMutation.isPending ? '주문 처리 중...' : `${selectedItems.size}개 상품 주문하기`}</span>
                   {!orderMutation.isPending && <ArrowRight className="w-5 h-5 ml-2" />}
                 </button>

                <Link
                  href="/products"
                  className="block w-full text-center mt-3 text-indigo-600 hover:text-indigo-500 py-2"
                >
                  쇼핑 계속하기
                </Link>
              </div>
            </div>
          </div>
        )}
      </main>

      {/* 푸터 컴포넌트 사용 */}
      <Footer />
    </div>
  );
}





