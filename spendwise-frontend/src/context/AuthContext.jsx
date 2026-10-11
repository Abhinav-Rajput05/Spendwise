import { createContext, useContext, useEffect, useMemo, useState } from 'react';
import { authApi } from '../api/authApi';
const AuthContext = createContext(null);
export function AuthProvider({ children }) {
  const [token, setToken] = useState(() => localStorage.getItem('expense_token'));
  const [busy, setBusy] = useState(false);
  const logout = () => { localStorage.removeItem('expense_token'); setToken(null); };
  useEffect(() => { const handler = () => logout(); window.addEventListener('expense:unauthorized', handler); return () => window.removeEventListener('expense:unauthorized', handler); }, []);
  const login = async (payload) => { setBusy(true); try { const response = await authApi.login(payload); if (!response?.token) throw new Error('Login response did not contain a token.'); localStorage.setItem('expense_token', response.token); setToken(response.token); return response; } finally { setBusy(false); } };
  const register = async (payload) => { setBusy(true); try { return await authApi.register(payload); } finally { setBusy(false); } };
  const value = useMemo(() => ({ token, isAuthenticated: Boolean(token), busy, login, register, logout }), [token, busy]);
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}
export const useAuth = () => useContext(AuthContext);
