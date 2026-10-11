import client from './client';
export const expenseApi = {
  listCurrentMonth: async () => (await client.get('/api/v1/expenses')).data,
  listByMonth: async (year, month) => (await client.get(`/api/v1/expenses/month/${year}/${month}`)).data,
  getById: async (id) => (await client.get(`/api/v1/expenses/${id}`)).data,
  getCurrentMonthTotal: async () => (await client.get('/api/v1/expenses/current-month/total')).data,
  listByCategory: async (id) => (await client.get(`/api/v1/expenses/category/${id}/expense`)).data,
  create: async (payload) => (await client.post('/api/v1/expenses', payload)).data,
  update: async (id, payload) => (await client.put(`/api/v1/expenses/${id}`, payload)).data,
  remove: async (id) => client.delete(`/api/v1/expenses/${id}`),
};
