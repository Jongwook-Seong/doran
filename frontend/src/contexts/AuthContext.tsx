'use client';

import React, { createContext, useContext, useState, useEffect, ReactNode } from 'react';

// 사용자 정보 타입 정의
interface User {
  userUuid: string;
  userId: string;
  username: string;
  email: string;
  phoneNumber: string;
}

// 인증 컨텍스트 타입 정의
interface AuthContextType {
  user: User | null;
  token: string | null;
  isAuthenticated: boolean;
  login: (userData: User, token: string, refreshToken?: string) => void;
  logout: () => void;
  loading: boolean;
}

// 인증 컨텍스트 생성
const AuthContext = createContext<AuthContextType | undefined>(undefined);

// 인증 프로바이더 컴포넌트
export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<User | null>(null);
  const [token, setToken] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);

  // 컴포넌트 마운트 시 로컬 스토리지에서 인증 정보 복원
  useEffect(() => {
    const storedToken = localStorage.getItem('token');
    const storedUserUuid = localStorage.getItem('userUuid');
    const storedUserId = localStorage.getItem('userId');
    const storedUsername = localStorage.getItem('username');
    const storedEmail = localStorage.getItem('email');
    const storedPhoneNumber = localStorage.getItem('phoneNumber');

    if (storedToken && storedUserUuid && storedUserId && storedUsername && storedPhoneNumber) {
      setToken(storedToken);
      setUser({
        userUuid: storedUserUuid,
        userId: storedUserId,
        username: storedUsername,
        email: storedEmail || '',
        phoneNumber: storedPhoneNumber || ''
      });
    }
    setLoading(false);
  }, []);

  // 로그인 함수
  const login = (userData: User, authToken: string, refreshToken?: string) => {
    setUser(userData);
    setToken(authToken);
    
    // 로컬 스토리지에 저장
    localStorage.setItem('token', authToken);
    localStorage.setItem('userUuid', userData.userUuid);
    localStorage.setItem('userId', userData.userId);
    localStorage.setItem('username', userData.username);
    localStorage.setItem('email', userData.email);
    localStorage.setItem('phoneNumber', userData.phoneNumber);
    
    if (refreshToken) {
      localStorage.setItem('refreshToken', refreshToken);
    }
  };

  // 로그아웃 함수
  const logout = () => {
    setUser(null);
    setToken(null);
    
    // 로컬 스토리지에서 제거
    localStorage.removeItem('token');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('userUuid');
    localStorage.removeItem('userId');
    localStorage.removeItem('username');
    localStorage.removeItem('email');
    localStorage.removeItem('phoneNumber');
  };

  const value: AuthContextType = {
    user,
    token,
    isAuthenticated: !!token && !!user,
    login,
    logout,
    loading
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
}

// 인증 컨텍스트 사용 훅
export function useAuth() {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
}








