'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import { signUp } from '@/utils/api';

// 회원가입 폼의 데이터 타입 정의
interface SignupFormData {
  userId: string;
  password: string;
  username: string;
  birthDate: string;
  email: string;
  phoneNumber: string;
}

// 회원가입 페이지 컴포넌트
export default function SignupPage() {
  // Next.js의 라우터 훅 사용
  const router = useRouter();
  
  // 폼 데이터를 관리하는 상태
  const [formData, setFormData] = useState<SignupFormData>({
    userId: '',
    password: '',
    username: '',
    birthDate: '',
    email: '',
    phoneNumber: ''
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
      // API 유틸리티 함수를 사용하여 회원가입 요청
      const result = await signUp({
        ...formData,
        // birthDate를 Date 객체로 변환
        birthDate: new Date(formData.birthDate)
      });

      if (result.success) {
        // 회원가입 성공 시 메인 페이지로 이동
        alert('회원가입이 완료되었습니다!');
        router.push('/');
      } else {
        // 에러 응답 처리
        setErrorMessage(result.error || '회원가입에 실패했습니다.');
      }
    } catch (error) {
      // 네트워크 에러 등 예외 처리
      console.error('회원가입 에러:', error);
      setErrorMessage('서버 연결에 실패했습니다. 다시 시도해주세요.');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-50 flex flex-col justify-center py-12 sm:px-6 lg:px-8">
      <div className="sm:mx-auto sm:w-full sm:max-w-md">
        <h2 className="mt-6 text-center text-3xl font-extrabold text-gray-900">
          회원가입
        </h2>
        <p className="mt-2 text-center text-sm text-gray-600">
          도란 쇼핑몰에 오신 것을 환영합니다
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
                  placeholder="3글자 이상 입력해주세요"
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
                  placeholder="4글자 이상 입력해주세요"
                />
              </div>
            </div>

            {/* 이름 입력 필드 */}
            <div>
              <label htmlFor="username" className="block text-sm font-medium text-gray-700">
                이름
              </label>
              <div className="mt-1">
                <input
                  id="username"
                  name="username"
                  type="text"
                  required
                  minLength={2}
                  value={formData.username}
                  onChange={handleInputChange}
                  className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm text-gray-900"
                  placeholder="2글자 이상 입력해주세요"
                />
              </div>
            </div>

            {/* 생년월일 입력 필드 */}
            <div>
              <label htmlFor="birthDate" className="block text-sm font-medium text-gray-700">
                생년월일
              </label>
              <div className="mt-1">
                <input
                  id="birthDate"
                  name="birthDate"
                  type="date"
                  required
                  value={formData.birthDate}
                  onChange={handleInputChange}
                  className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm text-gray-900"
                />
              </div>
            </div>

            {/* 이메일 입력 필드 */}
            <div>
              <label htmlFor="email" className="block text-sm font-medium text-gray-700">
                이메일
              </label>
              <div className="mt-1">
                <input
                  id="email"
                  name="email"
                  type="email"
                  required
                  minLength={3}
                  value={formData.email}
                  onChange={handleInputChange}
                  className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm text-gray-900"
                  placeholder="이메일을 입력해주세요"
                />
              </div>
            </div>

            {/* 연락처 입력 필드 */}
            <div>
              <label htmlFor="phoneNumber" className="block text-sm font-medium text-gray-700">
                연락처
              </label>
              <div className="mt-1">
                <input
                  id="phoneNumber"
                  name="phoneNumber"
                  type="tel"
                  required
                  value={formData.phoneNumber}
                  onChange={handleInputChange}
                  className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm text-gray-900"
                  placeholder="010-1234-5678"
                />
              </div>
            </div>

            {/* 에러 메시지 표시 */}
            {errorMessage && (
              <div className="text-red-600 text-sm text-center">
                {errorMessage}
              </div>
            )}

            {/* 회원가입 버튼 */}
            <div>
              <button
                type="submit"
                disabled={isLoading}
                className="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {isLoading ? '처리중...' : '회원가입'}
              </button>
            </div>
          </form>

          {/* 로그인 페이지 링크 */}
          <div className="mt-6">
            <div className="relative">
              <div className="relative flex justify-center text-sm">
                <span className="px-2 bg-white text-gray-500">
                  이미 계정이 있으신가요?{' '}
                  <a href="/login" className="font-medium text-indigo-600 hover:text-indigo-500">
                    로그인하기
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
