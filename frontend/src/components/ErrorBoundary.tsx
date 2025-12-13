'use client';

import { Component, ReactNode } from 'react';

// 에러 바운더리 Props 타입
interface ErrorBoundaryProps {
  children: ReactNode;
  fallback?: ReactNode;
}

// 에러 바운더리 State 타입
interface ErrorBoundaryState {
  hasError: boolean;
  error?: Error;
}

// 에러 바운더리 컴포넌트 - 예상치 못한 에러를 캐치하여 사용자에게 친화적인 에러 메시지 표시
export default class ErrorBoundary extends Component<ErrorBoundaryProps, ErrorBoundaryState> {
  constructor(props: ErrorBoundaryProps) {
    super(props);
    this.state = { hasError: false };
  }

  // 에러가 발생했을 때 state를 업데이트하여 fallback UI를 렌더링
  static getDerivedStateFromError(error: Error): ErrorBoundaryState {
    return { hasError: true, error };
  }

  // 에러 정보를 로깅하거나 에러 리포팅 서비스에 전송
  componentDidCatch(error: Error, errorInfo: any) {
    console.error('ErrorBoundary caught an error:', error, errorInfo);
  }

  render() {
    if (this.state.hasError) {
      // 커스텀 fallback UI가 있으면 사용, 없으면 기본 에러 메시지 표시
      if (this.props.fallback) {
        return this.props.fallback;
      }

      return (
        <div className="min-h-screen bg-gray-50 flex items-center justify-center">
          <div className="text-center">
            <h2 className="text-2xl font-bold text-gray-900 mb-4">
              문제가 발생했습니다
            </h2>
            <p className="text-gray-600 mb-8">
              예상치 못한 오류가 발생했습니다. 잠시 후 다시 시도해주세요.
            </p>
            <button
              onClick={() => window.location.reload()}
              className="bg-indigo-600 text-white px-6 py-3 rounded-md hover:bg-indigo-700"
            >
              페이지 새로고침
            </button>
          </div>
        </div>
      );
    }

    return this.props.children;
  }
}






