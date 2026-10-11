import client from './client';
export const categoryApi = {
  list: async () => (await client.get('/api/v1/category')).data,
  getById: async (id) => (await client.get(`/api/v1/category/${id}`)).data,
  create: async (payload) => (await client.post('/api/v1/category', payload)).data,
  update: async (id, payload) => (await client.put(`/api/v1/category/${id}`, payload)).data,
  remove: async (id) => client.delete(`/api/v1/category/${id}`),
  getMonthlyTotal: async (id) => (await client.get(`/api/v1/category/month/total-amount/${id}`)).data,
};
