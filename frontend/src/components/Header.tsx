'use client';

import Link from 'next/link';
import { useAuth } from '@/contexts/AuthContext';

// 헤더 컴포넌트 - 모든 페이지에서 공통으로 사용되는 네비게이션
export default function Header() {
  const { user, isAuthenticated, logout } = useAuth();

  return (
    <header className="bg-white shadow-sm">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between items-center py-6">
          {/* 로고 */}
          <Link href="/" className="text-2xl font-bold text-indigo-600">
            도란 온라인 서가
          </Link>
          
          {/* 네비게이션 메뉴 */}
          <nav className="flex space-x-8">
            <Link 
              href="/" 
              className="text-gray-500 hover:text-indigo-600 px-3 py-2 text-sm font-medium transition-colors"
            >
              홈
            </Link>
            <Link 
              href="/products" 
              className="text-gray-500 hover:text-indigo-600 px-3 py-2 text-sm font-medium transition-colors"
            >
              상품
            </Link>
            <Link 
              href="/cart" 
              className="text-gray-500 hover:text-indigo-600 px-3 py-2 text-sm font-medium transition-colors"
            >
              장바구니
            </Link>
            <Link 
              href="/orders" 
              className="text-gray-500 hover:text-indigo-600 px-3 py-2 text-sm font-medium transition-colors"
            >
              주문내역
            </Link>
          </nav>

          {/* 로그인/회원가입 버튼 또는 사용자 정보 */}
          <div className="flex items-center space-x-4">
            {isAuthenticated ? (
              <>
                <span className="text-gray-700 text-sm">
                  안녕하세요, {user?.username}님!
                </span>
                <button
                  onClick={logout}
                  className="text-gray-500 hover:text-indigo-600 px-3 py-2 text-sm font-medium transition-colors"
                >
                  로그아웃
                </button>
              </>
            ) : (
              <>
                <Link
                  href="/login"
                  className="text-gray-500 hover:text-indigo-600 px-3 py-2 text-sm font-medium transition-colors"
                >
                  로그인
                </Link>
                <Link
                  href="/signup"
                  className="bg-indigo-600 hover:bg-indigo-700 text-white px-4 py-2 rounded-md text-sm font-medium transition-colors"
                >
                  회원가입
                </Link>
              </>
            )}
          </div>
        </div>
      </div>
    </header>
  );
}




