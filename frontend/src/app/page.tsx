'use client';

import Link from "next/link";
import { useAuth } from "@/contexts/AuthContext";
import { useQuery } from '@tanstack/react-query';
import { getBestItems } from "@/utils/api";
import { ShoppingCart, Star } from 'lucide-react';
import Header from "@/components/Header";
import Footer from "@/components/Footer";

// 도란 온라인 서가 메인 페이지 컴포넌트
export default function Home() {
  const { user, isAuthenticated, logout } = useAuth();
  
  // 베스트 아이템 목록을 가져오는 쿼리
  // React Query를 사용하여 API 호출을 관리합니다
  const { data: bestItems, isLoading, error } = useQuery({
    queryKey: ['bestItems'], // 쿼리 키 - 캐싱과 무효화에 사용됩니다
    queryFn: getBestItems, // API 호출 함수
    staleTime: 5 * 60 * 1000, // 5분간 캐시 유지
  });
  
  return (
    <div className="min-h-screen bg-gray-50">
      {/* 헤더 컴포넌트 사용 */}
      <Header />

      {/* 메인 콘텐츠 영역 */}
      <main className="max-w-7xl mx-auto py-12 px-4 sm:px-6 lg:px-8">
        {/* 히어로 섹션 */}
        <div className="text-center">
          <h2 className="text-4xl font-extrabold text-gray-900 sm:text-5xl md:text-6xl">
            <span className="block">도란 온라인 서가에</span>
            <span className="block text-indigo-600">오신 것을 환영합니다</span>
          </h2>
          <p className="mt-3 max-w-md mx-auto text-base text-gray-500 sm:text-lg md:mt-5 md:text-xl md:max-w-3xl">
            다양한 책과 예술에 대해 도란도란 여러분을 더욱 즐겁게 만들어드립니다.
          </p>
          <div className="mt-5 max-w-md mx-auto sm:flex sm:justify-center md:mt-8">
            <div className="rounded-md shadow">
              <Link
                href="/signup"
                className="w-full flex items-center justify-center px-8 py-3 border border-transparent text-base font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 md:py-4 md:text-lg md:px-10"
              >
                지금 시작하기
              </Link>
            </div>
            <div className="mt-3 rounded-md shadow sm:mt-0 sm:ml-3">
              <Link
                href="/products"
                className="w-full flex items-center justify-center px-8 py-3 border border-transparent text-base font-medium rounded-md text-indigo-600 bg-white hover:bg-gray-50 md:py-4 md:text-lg md:px-10"
              >
                상품 둘러보기
              </Link>
            </div>
          </div>
        </div>

        {/* 베스트 아이템 섹션 */}
        <div className="mt-24">
          <div className="text-center">
            <h3 className="text-3xl font-bold text-gray-900">베스트 아이템</h3>
            <p className="mt-2 text-lg text-gray-600">인기 있는 상품들을 만나보세요</p>
          </div>
          
          {isLoading ? (
            <div className="mt-8 grid grid-cols-1 gap-8 sm:grid-cols-2 lg:grid-cols-4">
              {[...Array(4)].map((_, i) => (
                <div key={i} className="bg-white rounded-lg shadow-md p-6 animate-pulse">
                  <div className="w-full h-48 bg-gray-200 rounded-md mb-4"></div>
                  <div className="h-4 bg-gray-200 rounded mb-2"></div>
                  <div className="h-4 bg-gray-200 rounded w-3/4"></div>
                </div>
              ))}
            </div>
          ) : error ? (
            <div className="mt-8 text-center">
              <p className="text-red-600">상품을 불러오는데 실패했습니다.</p>
            </div>
          ) : bestItems?.success && bestItems.data && bestItems.data.length > 0 ? (
            <div className="mt-8 grid grid-cols-1 gap-8 sm:grid-cols-2 lg:grid-cols-4">
              {bestItems.data.slice(0, 4).map((item: any) => (
                <div key={item.itemUuid} className="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition-shadow">
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
                    <h4 className="text-lg font-semibold text-gray-900 mb-2">{item.itemName}</h4>
                    <div className="flex items-center justify-between">
                      <span className="text-xl font-bold text-indigo-600">
                        {item.itemPrice?.toLocaleString()}원
                      </span>
                      <div className="flex items-center">
                        <Star className="w-4 h-4 text-yellow-400 fill-current" />
                        <span className="text-sm text-gray-600 ml-1">{item.salesCount || 0}</span>
                      </div>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <div className="mt-8 text-center">
              <p className="text-gray-600">베스트 아이템이 없습니다.</p>
            </div>
          )}
        </div>

        {/* 서비스 소개 섹션 */}
        <div className="mt-24">
          <div className="grid grid-cols-1 gap-8 sm:grid-cols-2 lg:grid-cols-3">
            {/* 회원가입 서비스 */}
            <div className="bg-white overflow-hidden shadow rounded-lg">
              <div className="p-6">
                <div className="flex items-center">
                  <div className="flex-shrink-0">
                    <div className="w-8 h-8 bg-indigo-500 rounded-md flex items-center justify-center">
                      <svg className="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                      </svg>
                    </div>
                  </div>
                  <div className="ml-4">
                    <h3 className="text-lg font-medium text-gray-900">간편한 회원가입</h3>
                    <p className="text-sm text-gray-500">빠르고 안전한 회원가입으로 쇼핑을 시작하세요</p>
                  </div>
                </div>
                <div className="mt-4">
                  <Link
                    href="/signup"
                    className="text-indigo-600 hover:text-indigo-500 text-sm font-medium"
                  >
                    회원가입 하기 →
                  </Link>
                </div>
              </div>
            </div>

            {/* 상품 관리 서비스 */}
            <div className="bg-white overflow-hidden shadow rounded-lg">
              <div className="p-6">
                <div className="flex items-center">
                  <div className="flex-shrink-0">
                    <div className="w-8 h-8 bg-green-500 rounded-md flex items-center justify-center">
                      <svg className="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4" />
                      </svg>
                    </div>
                  </div>
                  <div className="ml-4">
                    <h3 className="text-lg font-medium text-gray-900">다양한 상품</h3>
                    <p className="text-sm text-gray-500">최신 트렌드의 다양한 상품을 만나보세요</p>
                  </div>
                </div>
                <div className="mt-4">
                  <Link
                    href="/products"
                    className="text-green-600 hover:text-green-500 text-sm font-medium"
                  >
                    상품 보기 →
                  </Link>
                </div>
              </div>
            </div>

            {/* 주문 관리 서비스 */}
            <div className="bg-white overflow-hidden shadow rounded-lg">
              <div className="p-6">
                <div className="flex items-center">
                  <div className="flex-shrink-0">
                    <div className="w-8 h-8 bg-yellow-500 rounded-md flex items-center justify-center">
                      <svg className="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                      </svg>
                    </div>
                  </div>
                  <div className="ml-4">
                    <h3 className="text-lg font-medium text-gray-900">안전한 주문</h3>
                    <p className="text-sm text-gray-500">안전하고 빠른 배송으로 만족스러운 쇼핑을 경험하세요</p>
                  </div>
                </div>
                <div className="mt-4">
                  <Link
                    href="/orders"
                    className="text-yellow-600 hover:text-yellow-500 text-sm font-medium"
                  >
                    주문내역 보기 →
                  </Link>
                </div>
              </div>
            </div>
          </div>
        </div>
      </main>

      {/* 푸터 컴포넌트 사용 */}
      <Footer />
    </div>
  );
}
