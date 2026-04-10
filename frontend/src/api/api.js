import axios from 'axios';

const API = axios.create({
  baseURL: '',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor to add JWT token
API.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Response interceptor for auth errors
API.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401 || error.response?.status === 403) {
      // Only redirect if not on auth pages
      if (!window.location.pathname.startsWith('/login') && !window.location.pathname.startsWith('/register')) {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        window.location.href = '/login';
      }
    }
    return Promise.reject(error);
  }
);

// Auth APIs
export const authAPI = {
  register: (data) => API.post('/auth/register', data),
  login: (data) => API.post('/auth/login', data),
};

// Test APIs
export const testAPI = {
  start: (userId) => API.get(`/api/test/start?userId=${userId}`),
  submit: (userId, data) => API.post(`/api/test/submit?userId=${userId}`, data),
};

// Analytics APIs
export const analyticsAPI = {
  getPerformance: (userId) => API.get(`/api/performance/${userId}`),
  getSkillGaps: (userId) => API.get(`/api/skill-gap/${userId}`),
  getDashboard: (userId) => API.get(`/api/dashboard/${userId}`),
};

// Roadmap APIs
export const roadmapAPI = {
  getRoadmap: (userId) => API.get(`/api/roadmap/${userId}`),
  generate: (userId) => API.post(`/api/roadmap/generate/${userId}`),
};

// Recommendation APIs
export const recommendationAPI = {
  get: (userId) => API.get(`/api/recommendations/${userId}`),
};

// Question APIs (admin)
export const questionAPI = {
  getAll: () => API.get('/api/questions'),
  create: (data) => API.post('/api/questions', data),
  update: (id, data) => API.put(`/api/questions/${id}`, data),
  delete: (id) => API.delete(`/api/questions/${id}`),
};

export default API;
