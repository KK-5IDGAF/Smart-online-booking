import { createContext, useContext, useMemo, useState } from 'react';
import { authApi } from '../api/services';

const AuthContext = createContext();

export function AuthProvider({ children }) {
  const [token, setToken] = useState(() => localStorage.getItem('sob_token'));
  const [user, setUser] = useState(() => {
    const raw = localStorage.getItem('sob_user');
    return raw ? JSON.parse(raw) : null;
  });

  const persist = (nextToken, nextUser) => {
    setToken(nextToken);
    setUser(nextUser);
    localStorage.setItem('sob_token', nextToken);
    localStorage.setItem('sob_user', JSON.stringify(nextUser));
  };

  const login = async (payload) => {
    const data = await authApi.login(payload);
    persist(data.token, data.user);
    return data.user;
  };

  const register = async (payload) => {
    const data = await authApi.register(payload);
    persist(data.token, data.user);
    return data.user;
  };

  const logout = () => {
    setToken(null);
    setUser(null);
    localStorage.removeItem('sob_token');
    localStorage.removeItem('sob_user');
  };

  const value = useMemo(
    () => ({
      token,
      user,
      isAuthenticated: Boolean(token),
      login,
      register,
      logout,
    }),
    [token, user]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  return useContext(AuthContext);
}
