import axios from 'axios'

const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'

const api = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
})

// Add token to requests
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

// Handle responses
api.interceptors.response.use(
  (response) => response.data,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('role')
      localStorage.removeItem('username')
      window.location.href = '/login'
    }
    return Promise.reject(error.response?.data?.message || 'An error occurred')
  }
)

export const authAPI = {
  login: (username, password) => api.post('/api/auth/login', { username, password }),
  register: (data) => api.post('/api/auth/register', data),
  getUsers: () => api.get('/api/users'),
}

export const vehicleAPI = {
  getAll: () => api.get('/api/vehicles'),
  getById: (id) => api.get(`/api/vehicles/${id}`),
  create: (data) => api.post('/api/vehicles', data),
  update: (id, data) => api.put(`/api/vehicles/${id}`, data),
  delete: (id) => api.delete(`/api/vehicles/${id}`),
  updateStatus: (id, status) => api.put(`/api/vehicles/${id}/status`, { status }),
  updateAvailability: (id, availability) => api.put(`/api/vehicles/${id}/availability`, { availability }),
}

export const driverAPI = {
  getAll: () => api.get('/api/drivers'),
  create: (data) => api.post('/api/drivers', data),
  update: (id, data) => api.put(`/api/drivers/${id}`, data),
  delete: (id) => api.delete(`/api/drivers/${id}`),
}

export const tripAPI = {
  getAll: () => api.get('/api/trips'),
  create: (data) => api.post('/api/trips', data),
  update: (id, data) => api.put(`/api/trips/${id}`, data),
  delete: (id) => api.delete(`/api/trips/${id}`),
}

export const trackingAPI = {
  getLive: () => api.get('/api/tracking/live'),
  reportLocation: (data) => api.post('/api/tracking/location', data),
  connectWebSocket: () => new WebSocket(`${API_BASE_URL.replace('http', 'ws')}/ws/tracking`),
}

export const notificationAPI = {
  getAll: () => api.get('/api/notifications'),
}

export const mapAPI = {
  getLive: () => api.get('/api/map/live'),
}

export const fileAPI = {
  upload: (formData) => api.post('/files/upload', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  }),
  getAll: () => api.get('/files'),
}

export const emergencyAPI = {
  getAll: () => api.get('/emergencies'),
  create: (data) => api.post('/emergencies', data),
}

export const digitalTwinAPI = {
  getAll: () => api.get('/digitaltwins'),
}

export const aiAPI = {
  predictEta: (params) => api.get('/predict/eta', { params }),
  predictMaintenance: (params) => api.get('/predict/maintenance', { params }),
  predictFuel: (params) => api.get('/predict/fuel', { params }),
  predictDelay: (params) => api.get('/predict/delay', { params }),
  predictDriver: (params) => api.get('/predict/driver', { params }),
  predictHealth: (params) => api.get('/predict/health', { params }),
}

export const healthAPI = {
  check: () => api.get('/health'),
}

export default api
