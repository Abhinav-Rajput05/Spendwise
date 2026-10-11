import client from './client';
export const authApi = {
  register: async (payload) => (await client.post('/api/v1/auth/register', payload)).data,
  login: async (payload) => (await client.post('/api/v1/auth/login', payload)).data,
};
