'use client';

import { useAuth } from '@/contexts/AuthContext';
import { useRouter } from 'next/navigation';
import { useEffect } from 'react';

interface ProtectedRouteProps {
  children: React.ReactNode;
  redirectTo?: string;
}

export default function ProtectedRoute({ 
  children, 
  redirectTo = '/login' 
}: ProtectedRouteProps) {
  const { isAuthenticated, loading } = useAuth();
  const router = useRouter();

  useEffect(() => {
    if (!loading && !isAuthenticated) {
      // 로그인이 필요한 서비스라는 메시지와 함께 로그인 페이지로 리다이렉션
      alert('로그인이 필요한 서비스입니다.');
      router.push(redirectTo);
    }
  }, [isAuthenticated, loading, router, redirectTo]);

  // 로딩 중이거나 인증되지 않은 경우 로딩 화면 표시
  if (loading || !isAuthenticated) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600 mx-auto"></div>
          <p className="mt-4 text-gray-600">인증 확인 중...</p>
        </div>
      </div>
    );
  }

  // 인증된 사용자에게만 컨텐츠 표시
  return <>{children}</>;
}



