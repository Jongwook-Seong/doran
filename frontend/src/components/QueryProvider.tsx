'use client';

import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { ReactQueryDevtools } from '@tanstack/react-query-devtools';
import { useState } from 'react';

// QueryProvider 컴포넌트
// React Query를 앱 전체에서 사용할 수 있도록 Provider로 감싸는 컴포넌트입니다
export function QueryProvider({ children }: { children: React.ReactNode }) {
  // QueryClient 인스턴스를 생성합니다
  // useState를 사용하여 컴포넌트가 리렌더링되어도 동일한 인스턴스를 유지합니다
  const [queryClient] = useState(() => new QueryClient({
    defaultOptions: {
      queries: {
        // 쿼리 결과를 5분간 캐시합니다
        staleTime: 5 * 60 * 1000,
        // 백그라운드에서 데이터를 다시 가져오는 간격을 10분으로 설정합니다
        gcTime: 10 * 60 * 1000,
        // 네트워크 오류 시 자동으로 재시도합니다
        retry: 1,
        // 윈도우가 포커스될 때 데이터를 다시 가져옵니다
        refetchOnWindowFocus: false,
      },
      mutations: {
        // 뮤테이션 실패 시 재시도하지 않습니다
        retry: false,
      },
    },
  }));

  return (
    <QueryClientProvider client={queryClient}>
      {children}
      {/* 개발 환경에서만 React Query DevTools를 표시합니다 */}
      <ReactQueryDevtools initialIsOpen={false} />
    </QueryClientProvider>
  );
}

