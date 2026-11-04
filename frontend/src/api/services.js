import client from './client';

export const authApi = {
  login: (payload) => client.post('/auth/login', payload).then((r) => r.data),
  register: (payload) => client.post('/auth/register', payload).then((r) => r.data),
};

export const userApi = {
  profile: () => client.get('/users/profile').then((r) => r.data),
  updateProfile: (payload) => client.put('/users/profile', payload).then((r) => r.data),
};

export const resourceApi = {
  list: (search) => client.get('/resources', { params: { search } }).then((r) => r.data),
  get: (id) => client.get(`/resources/${id}`).then((r) => r.data),
  create: (payload) => client.post('/resources', payload).then((r) => r.data),
  update: (id, payload) => client.put(`/resources/${id}`, payload).then((r) => r.data),
  remove: (id) => client.delete(`/resources/${id}`),
};

export const slotApi = {
  list: (params) => client.get('/slots', { params }).then((r) => r.data),
  available: () => client.get('/slots/available').then((r) => r.data),
  create: (payload) => client.post('/slots', payload).then((r) => r.data),
  update: (id, payload) => client.put(`/slots/${id}`, payload).then((r) => r.data),
};

export const bookingApi = {
  create: (slotId) => client.post('/bookings', { slotId }).then((r) => r.data),
  mine: () => client.get('/bookings/my').then((r) => r.data),
  cancel: (id) => client.delete(`/bookings/${id}`).then((r) => r.data),
};

export const adminApi = {
  dashboard: () => client.get('/admin/dashboard').then((r) => r.data),
  bookings: () => client.get('/admin/bookings').then((r) => r.data),
  users: () => client.get('/admin/users').then((r) => r.data),
};
