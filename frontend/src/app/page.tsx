import Link from "next/link";

// 도란 쇼핑몰 메인 페이지 컴포넌트
export default function Home() {
  return (
    <div className="min-h-screen bg-gray-50">
      {/* 헤더 영역 */}
      <header className="bg-white shadow-sm">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center py-6">
            {/* 로고 */}
            <div className="flex items-center">
              <h1 className="text-2xl font-bold text-indigo-600">도란 쇼핑몰</h1>
            </div>
            
            {/* 네비게이션 메뉴 */}
            <nav className="flex space-x-8">
              <Link 
                href="/" 
                className="text-gray-900 hover:text-indigo-600 px-3 py-2 text-sm font-medium"
              >
                홈
              </Link>
              <Link 
                href="/products" 
                className="text-gray-500 hover:text-indigo-600 px-3 py-2 text-sm font-medium"
              >
                상품
              </Link>
              <Link 
                href="/cart" 
                className="text-gray-500 hover:text-indigo-600 px-3 py-2 text-sm font-medium"
              >
                장바구니
              </Link>
              <Link 
                href="/orders" 
                className="text-gray-500 hover:text-indigo-600 px-3 py-2 text-sm font-medium"
              >
                주문내역
              </Link>
            </nav>

            {/* 로그인/회원가입 버튼 */}
            <div className="flex items-center space-x-4">
              <Link
                href="/login"
                className="text-gray-500 hover:text-indigo-600 px-3 py-2 text-sm font-medium"
              >
                로그인
              </Link>
              <Link
                href="/signup"
                className="bg-indigo-600 hover:bg-indigo-700 text-white px-4 py-2 rounded-md text-sm font-medium"
              >
                회원가입
              </Link>
            </div>
          </div>
        </div>
      </header>

      {/* 메인 콘텐츠 영역 */}
      <main className="max-w-7xl mx-auto py-12 px-4 sm:px-6 lg:px-8">
        {/* 히어로 섹션 */}
        <div className="text-center">
          <h2 className="text-4xl font-extrabold text-gray-900 sm:text-5xl md:text-6xl">
            <span className="block">도란 쇼핑몰에</span>
            <span className="block text-indigo-600">오신 것을 환영합니다</span>
          </h2>
          <p className="mt-3 max-w-md mx-auto text-base text-gray-500 sm:text-lg md:mt-5 md:text-xl md:max-w-3xl">
            최고의 상품과 서비스로 여러분의 쇼핑을 더욱 즐겁게 만들어드립니다.
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

      {/* 푸터 영역 */}
      <footer className="bg-white border-t border-gray-200 mt-24">
        <div className="max-w-7xl mx-auto py-12 px-4 sm:px-6 lg:px-8">
          <div className="text-center">
            <p className="text-base text-gray-400">
              &copy; 2024 도란 쇼핑몰. All rights reserved.
            </p>
          </div>
        </div>
      </footer>
    </div>
  );
}
