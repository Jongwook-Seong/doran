'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import { login, getUserByUuid } from '@/utils/api';
import { useAuth } from '@/contexts/AuthContext';

// 로그인 폼의 데이터 타입 정의
interface LoginFormData {
  userId: string;
  password: string;
}

// 로그인 페이지 컴포넌트
export default function LoginPage() {
  // Next.js의 라우터 훅 사용
  const router = useRouter();
  const { login: authLogin } = useAuth();
  
  // 폼 데이터를 관리하는 상태
  const [formData, setFormData] = useState<LoginFormData>({
    userId: '',
    password: ''
  });

  // 로딩 상태 관리
  const [isLoading, setIsLoading] = useState(false);
  
  // 에러 메시지 상태 관리
  const [errorMessage, setErrorMessage] = useState('');

  // 입력 필드 값이 변경될 때 호출되는 함수
  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  // 폼 제출 처리 함수
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true);
    setErrorMessage('');

    try {
      // API 유틸리티 함수를 사용하여 로그인 요청
      const result = await login(formData);

      if (result.success && result.data) {
        // 로그인 성공 시 AuthContext를 통해 인증 상태 업데이트
        const loginData = result.data;
        console.log('loginData', loginData);
        
        // 로그인 성공 후 받은 토큰으로 사용자 정보를 조회
        const userResult = await getUserByUuid(
          loginData.userUuid, 
          loginData.accessToken, 
          loginData.refreshToken
        );
        console.log('userResult', userResult);
        
        if (userResult.success && userResult.data) {
          const userData = userResult.data;
          
          // AuthContext의 login 함수 호출 (refreshToken도 함께 저장)
          authLogin({
            userUuid: loginData.userUuid,
            userId: userData.userId,
            username: userData.username,
            email: userData.email
          }, loginData.accessToken, loginData.refreshToken);
          
          // 성공 메시지 표시 후 메인 페이지로 이동
          alert('로그인이 완료되었습니다!');
          router.push('/');
        } else {
          setErrorMessage('사용자 정보를 가져오는데 실패했습니다.');
        }
      } else {
        // 에러 응답 처리
        setErrorMessage(result.error || '로그인에 실패했습니다.');
      }
    } catch (error) {
      // 네트워크 에러 등 예외 처리
      console.error('로그인 에러:', error);
      setErrorMessage('서버 연결에 실패했습니다. 다시 시도해주세요.');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-50 flex flex-col justify-center py-12 sm:px-6 lg:px-8">
      <div className="sm:mx-auto sm:w-full sm:max-w-md">
        <h2 className="mt-6 text-center text-3xl font-extrabold text-gray-900">
          로그인
        </h2>
        <p className="mt-2 text-center text-sm text-gray-600">
          도란 온라인 서가에 오신 것을 환영합니다
        </p>
      </div>

      <div className="mt-8 sm:mx-auto sm:w-full sm:max-w-md">
        <div className="bg-white py-8 px-4 shadow sm:rounded-lg sm:px-10">
          <form className="space-y-6" onSubmit={handleSubmit}>
            {/* 사용자 ID 입력 필드 */}
            <div>
              <label htmlFor="userId" className="block text-sm font-medium text-gray-700">
                사용자 ID
              </label>
              <div className="mt-1">
                <input
                  id="userId"
                  name="userId"
                  type="text"
                  required
                  minLength={3}
                  value={formData.userId}
                  onChange={handleInputChange}
                  className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm text-gray-900"
                  placeholder="사용자 ID를 입력해주세요"
                />
              </div>
            </div>

            {/* 비밀번호 입력 필드 */}
            <div>
              <label htmlFor="password" className="block text-sm font-medium text-gray-700">
                비밀번호
              </label>
              <div className="mt-1">
                <input
                  id="password"
                  name="password"
                  type="password"
                  required
                  minLength={4}
                  value={formData.password}
                  onChange={handleInputChange}
                  className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm text-gray-900"
                  placeholder="비밀번호를 입력해주세요"
                />
              </div>
            </div>

            {/* 에러 메시지 표시 */}
            {errorMessage && (
              <div className="text-red-600 text-sm text-center">
                {errorMessage}
              </div>
            )}

            {/* 로그인 버튼 */}
            <div>
              <button
                type="submit"
                disabled={isLoading}
                className="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {isLoading ? '로그인 중...' : '로그인'}
              </button>
            </div>
          </form>

          {/* 회원가입 페이지 링크 */}
          <div className="mt-6">
            <div className="relative">
              <div className="relative flex justify-center text-sm">
                <span className="px-2 bg-white text-gray-500">
                  계정이 없으신가요?{' '}
                  <a href="/signup" className="font-medium text-indigo-600 hover:text-indigo-500">
                    회원가입하기
                  </a>
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
