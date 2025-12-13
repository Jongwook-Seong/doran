// 푸터 컴포넌트 - 모든 페이지에서 공통으로 사용되는 푸터
export default function Footer() {
  return (
    <footer className="bg-white border-t border-gray-200 mt-24">
      <div className="max-w-7xl mx-auto py-12 px-4 sm:px-6 lg:px-8">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-8">
          {/* 회사 정보 */}
          <div className="col-span-1 md:col-span-2">
            <h3 className="text-lg font-semibold text-gray-900 mb-4">도란 온라인 서가</h3>
            <p className="text-gray-600 mb-4">
              다양한 책과 예술에 대해 도란도란 여러분을 더욱 즐겁게 만들어드립니다.
            </p>
            <div className="text-sm text-gray-500">
              <p>사업자등록번호: 123-45-67890</p>
              <p>대표이사: 김도란</p>
              <p>주소: 서울특별시 강남구 테헤란로 123</p>
            </div>
          </div>

          {/* 고객센터 */}
          <div>
            <h3 className="text-lg font-semibold text-gray-900 mb-4">고객센터</h3>
            <div className="space-y-2 text-sm text-gray-600">
              <p>전화: 1588-1234</p>
              <p>이메일: support@doran.com</p>
              <p>운영시간: 평일 09:00-18:00</p>
              <p>주말 및 공휴일 휴무</p>
            </div>
          </div>

          {/* 빠른 링크 */}
          <div>
            <h3 className="text-lg font-semibold text-gray-900 mb-4">빠른 링크</h3>
            <div className="space-y-2 text-sm">
              <a href="/products" className="text-gray-600 hover:text-indigo-600 block">
                상품 목록
              </a>
              <a href="/cart" className="text-gray-600 hover:text-indigo-600 block">
                장바구니
              </a>
              <a href="/orders" className="text-gray-600 hover:text-indigo-600 block">
                주문내역
              </a>
              <a href="/login" className="text-gray-600 hover:text-indigo-600 block">
                로그인
              </a>
            </div>
          </div>
        </div>
        
        {/* 저작권 정보 */}
        <div className="border-t border-gray-200 mt-8 pt-8 text-center">
          <p className="text-sm text-gray-500">
            &copy; 2024 도란 온라인 서가. All rights reserved.
          </p>
        </div>
      </div>
    </footer>
  );
}




