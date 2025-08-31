import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  /* config options here */
  
  // CORS 헤더 설정 - 백엔드 API와의 통신을 위해 필요
  async headers() {
    return [
      {
        // 모든 API 라우트에 대해 CORS 헤더 적용
        source: '/api/:path*',
        headers: [
          { key: 'Access-Control-Allow-Origin', value: '*' },
          { key: 'Access-Control-Allow-Methods', value: 'GET, POST, PUT, DELETE, OPTIONS' },
          { key: 'Access-Control-Allow-Headers', value: 'Content-Type, Authorization' },
        ],
      },
    ];
  },
};

export default nextConfig;
