/** @type {import('next').NextConfig} */
const nextConfig = {
  // API 라우트에 CORS 헤더 추가
  async headers() {
    return [
      {
        // 모든 API 라우트에 적용
        source: '/api/:path*',
        headers: [
          {
            key: 'Access-Control-Allow-Origin',
            value: '*', // 개발 환경에서는 모든 도메인 허용
          },
          {
            key: 'Access-Control-Allow-Methods',
            value: 'GET, POST, PUT, DELETE, OPTIONS',
          },
          {
            key: 'Access-Control-Allow-Headers',
            value: 'Content-Type, Authorization',
          },
        ],
      },
    ];
  },
};

module.exports = nextConfig;

