import axios from 'axios';

const client = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080',
  headers: { 'Content-Type': 'application/json' },
  timeout: 15000,
});

client.interceptors.request.use((config) => {
  const token = localStorage.getItem('expense_token');
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

client.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401 && !String(error.config?.url || '').includes('/api/v1/auth/login')) {
      localStorage.removeItem('expense_token');
      window.dispatchEvent(new Event('expense:unauthorized'));
    }
    return Promise.reject(error);
  }
);
export default client;
