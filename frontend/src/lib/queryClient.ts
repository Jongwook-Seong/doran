'use client';

import { QueryClient } from '@tanstack/react-query';

// React Query 클라이언트 설정
// React Query는 서버 상태 관리 라이브러리로, API 호출을 캐싱하고 동기화합니다
export const queryClient = new QueryClient({
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
});

