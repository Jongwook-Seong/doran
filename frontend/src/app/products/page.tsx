'use client';

import { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { getBestItems, searchBooks, addToBasket } from '@/utils/api';
import { ShoppingCart, Search, Star } from 'lucide-react';
import Link from 'next/link';
import { useAuth } from '@/contexts/AuthContext';

// 상품 목록 페이지 컴포넌트
export default function ProductsPage() {
  const { isAuthenticated, user } = useAuth();
  const queryClient = useQueryClient();
  
  // 검색 키워드 상태 관리
  const [searchKeyword, setSearchKeyword] = useState('');
  const [searchInput, setSearchInput] = useState('');

  // 베스트 아이템 목록을 가져오는 쿼리
  const { data: bestItems, isLoading: bestItemsLoading, error: bestItemsError } = useQuery({
    queryKey: ['bestItems'],
    queryFn: getBestItems,
    staleTime: 5 * 60 * 1000,
  });

  // 검색 결과를 가져오는 쿼리 (검색 키워드가 있을 때만 실행)
  const { data: searchResults, isLoading: searchLoading, error: searchError } = useQuery({
    queryKey: ['searchBooks', searchKeyword],
    queryFn: () => searchBooks(searchKeyword),
    enabled: !!searchKeyword, // 검색 키워드가 있을 때만 쿼리 실행
    staleTime: 2 * 60 * 1000,
  });

  // 검색 실행 함수
  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    setSearchKeyword(searchInput);
  };

  // 검색어 초기화 함수
  const handleClearSearch = () => {
    setSearchInput('');
    setSearchKeyword('');
  };

  // 장바구니 추가 mutation
  const addToBasketMutation = useMutation({
    mutationFn: ({ itemUuid }: { itemUuid: string }) => 
      addToBasket(user?.userUuid || '', itemUuid, 1),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['basketList'] });
      alert('장바구니에 추가되었습니다.');
    },
    onError: () => {
      alert('장바구니 추가에 실패했습니다.');
    },
  });

  // 장바구니 담기 핸들러
  const handleAddToCart = (itemUuid: string) => {
    if (!isAuthenticated) {
      alert('로그인이 필요합니다.');
      return;
    }
    addToBasketMutation.mutate({ itemUuid });
  };

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
              <Link href="/products" className="text-gray-900 hover:text-indigo-600 px-3 py-2 text-sm font-medium">
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
        {/* 페이지 제목 및 검색 */}
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900 mb-6">상품 목록</h1>
          
          {/* 검색 폼 */}
          <form onSubmit={handleSearch} className="max-w-md mx-auto">
            <div className="flex">
              <div className="relative flex-1">
                <input
                  type="text"
                  value={searchInput}
                  onChange={(e) => setSearchInput(e.target.value)}
                  placeholder="상품명을 검색하세요..."
                  className="w-full px-4 py-2 border border-gray-300 rounded-l-md focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 text-gray-900"
                />
              </div>
              <button
                type="submit"
                className="px-4 py-2 bg-indigo-600 text-white rounded-r-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"
              >
                <Search className="w-5 h-5" />
              </button>
            </div>
          </form>

          {/* 검색 키워드가 있을 때 초기화 버튼 표시 */}
          {searchKeyword && (
            <div className="mt-4 text-center">
              <span className="text-sm text-gray-600">
                "{searchKeyword}" 검색 결과
              </span>
              <button
                onClick={handleClearSearch}
                className="ml-2 text-sm text-indigo-600 hover:text-indigo-500"
              >
                전체 상품 보기
              </button>
            </div>
          )}
        </div>

        {/* 상품 목록 */}
        {searchKeyword ? (
          // 검색 결과 표시
          <div>
            {searchLoading ? (
              <div className="grid grid-cols-1 gap-8 sm:grid-cols-2 lg:grid-cols-4">
                {[...Array(8)].map((_, i) => (
                  <div key={i} className="bg-white rounded-lg shadow-md p-6 animate-pulse">
                    <div className="w-full h-48 bg-gray-200 rounded-md mb-4"></div>
                    <div className="h-4 bg-gray-200 rounded mb-2"></div>
                    <div className="h-4 bg-gray-200 rounded w-3/4"></div>
                  </div>
                ))}
              </div>
            ) : searchError ? (
              <div className="text-center py-12">
                <p className="text-red-600">검색 중 오류가 발생했습니다.</p>
              </div>
            ) : searchResults?.success && searchResults.data?.length > 0 ? (
              <div className="grid grid-cols-1 gap-8 sm:grid-cols-2 lg:grid-cols-4">
                {searchResults.data.map((item: any) => (
                  <ProductCard key={item.itemUuid} item={item} onAddToCart={handleAddToCart} />
                ))}
              </div>
            ) : (
              <div className="text-center py-12">
                <p className="text-gray-600">검색 결과가 없습니다.</p>
              </div>
            )}
          </div>
        ) : (
          // 베스트 아이템 표시
          <div>
            <h2 className="text-2xl font-bold text-gray-900 mb-6">베스트 상품</h2>
            {bestItemsLoading ? (
              <div className="grid grid-cols-1 gap-8 sm:grid-cols-2 lg:grid-cols-4">
                {[...Array(8)].map((_, i) => (
                  <div key={i} className="bg-white rounded-lg shadow-md p-6 animate-pulse">
                    <div className="w-full h-48 bg-gray-200 rounded-md mb-4"></div>
                    <div className="h-4 bg-gray-200 rounded mb-2"></div>
                    <div className="h-4 bg-gray-200 rounded w-3/4"></div>
                  </div>
                ))}
              </div>
            ) : bestItemsError ? (
              <div className="text-center py-12">
                <p className="text-red-600">상품을 불러오는데 실패했습니다.</p>
              </div>
            ) : bestItems?.success && bestItems.data?.length > 0 ? (
              <div className="grid grid-cols-1 gap-8 sm:grid-cols-2 lg:grid-cols-4">
                {bestItems.data.map((item: any) => (
                  <ProductCard key={item.itemUuid} item={item} onAddToCart={handleAddToCart} />
                ))}
              </div>
            ) : (
              <div className="text-center py-12">
                <p className="text-gray-600">등록된 상품이 없습니다.</p>
              </div>
            )}
          </div>
        )}
      </main>
    </div>
  );
}

// 상품 카드 컴포넌트
function ProductCard({ item, onAddToCart }: { item: any; onAddToCart: (itemUuid: string) => void }) {
  return (
    <div className="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition-shadow">
      <div className="w-full h-48 bg-gray-200 flex items-center justify-center">
        {item.itemImageUrl ? (
          <img 
            src={item.itemImageUrl} 
            alt={item.itemName}
            className="w-full h-full object-cover"
          />
        ) : (
          <ShoppingCart className="w-12 h-12 text-gray-400" />
        )}
      </div>
      <div className="p-4">
        <h3 className="text-lg font-semibold text-gray-900 mb-2 line-clamp-2">
          {item.itemName}
        </h3>
        <div className="flex items-center justify-between mb-2">
          <span className="text-xl font-bold text-indigo-600">
            {item.price?.toLocaleString()}원
          </span>
          {item.salesCount && (
            <div className="flex items-center">
              <Star className="w-4 h-4 text-yellow-400 fill-current" />
              <span className="text-sm text-gray-600 ml-1">{item.salesCount}</span>
            </div>
          )}
        </div>
        <div className="flex space-x-2">
          <button 
            onClick={() => onAddToCart(item.itemUuid)}
            className="flex-1 bg-indigo-600 text-white py-2 px-4 rounded-md hover:bg-indigo-700 transition-colors text-sm"
          >
            장바구니
          </button>
          <Link
            href={`/products/${item.itemUuid}`}
            className="flex-1 bg-gray-200 text-gray-700 py-2 px-4 rounded-md hover:bg-gray-300 transition-colors text-center text-sm"
          >
            상세보기
          </Link>
        </div>
      </div>
    </div>
  );
}

