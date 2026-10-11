export const money = (value) => new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR', maximumFractionDigits: 2 }).format(Number(value || 0));
export const dateLabel = (value) => value ? new Date(`${String(value).slice(0, 10)}T00:00:00`).toLocaleDateString('en-IN', { day: '2-digit', month: 'short', year: 'numeric' }) : '—';
export const today = () => new Date().toISOString().slice(0, 10);
export const errorMessage = (error) => {
  const data = error?.response?.data;
  if (typeof data === 'string' && data.trim()) { try { const parsed = JSON.parse(data); return parsed.error || parsed.message || data; } catch { return data; } }
  return data?.error || data?.message || (error?.response?.status === 401 ? 'Invalid credentials or session expired.' : error?.message === 'Network Error' ? 'Cannot reach the backend. Check that Spring Boot is running on port 8080.' : 'Something went wrong. Please try again.');
};
