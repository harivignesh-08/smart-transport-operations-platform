// API integration client for Smart Transport Operations Platform
// Interacts with API Gateway at port 8080 or falls back to local simulation data

const API_BASE_URL = 'http://localhost:8080';

// Fallback Mock Data for standalone operations (in case gateway is not running)
export const MOCK_DATA = {
  vehicles: [
    { id: 1, name: 'Semi-Truck 101', type: 'Freightliner Cascadia', status: 'ACTIVE', fuelLevel: 78, nextMaintenance: '2026-07-28', mileage: 12450 },
    { id: 2, name: 'Delivery Van 204', type: 'Ford E-Transit', status: 'ACTIVE', fuelLevel: 92, nextMaintenance: '2026-08-02', mileage: 4120 },
    { id: 3, name: 'Heavy Hauler 305', type: 'Volvo FH16', status: 'MAINTENANCE', fuelLevel: 45, nextMaintenance: '2026-07-15', mileage: 48900 },
    { id: 4, name: 'Reefer Truck 401', type: 'Peterbilt 579', status: 'ACTIVE', fuelLevel: 62, nextMaintenance: '2026-07-24', mileage: 18320 },
    { id: 5, name: 'Service Van 12', type: 'Mercedes Sprinter', status: 'OUT_OF_SERVICE', fuelLevel: 0, nextMaintenance: '2026-07-12', mileage: 65100 }
  ],
  drivers: [
    { id: 101, name: 'Alex Johnson', license: 'CDL-A', rating: 4.8, status: 'ON_TRIP', trips: 142 },
    { id: 102, name: 'Sarah Miller', license: 'CDL-B', rating: 4.9, status: 'AVAILABLE', trips: 89 },
    { id: 103, name: 'Marcus Vance', license: 'CDL-A', rating: 4.2, status: 'ON_TRIP', trips: 231 },
    { id: 104, name: 'Emily Davis', license: 'CDL-A', rating: 4.7, status: 'OFF_DUTY', trips: 115 },
    { id: 105, name: 'David Cho', license: 'Class C', rating: 4.5, status: 'AVAILABLE', trips: 64 }
  ],
  emergencies: [
    { id: 2001, vehicleName: 'Reefer Truck 401', type: 'ENGINE_OVERHEAT', severity: 'HIGH', status: 'PENDING', timestamp: '16:14' },
    { id: 2002, vehicleName: 'Delivery Van 204', type: 'FLAT_TIRE', severity: 'MEDIUM', status: 'RESOLVED', timestamp: '14:22' },
    { id: 2003, vehicleName: 'Semi-Truck 101', type: 'COLLISION', severity: 'CRITICAL', status: 'DISPATCHED', timestamp: '15:45' }
  ]
};

async function handleRequest(url, options = {}) {
  try {
    const res = await fetch(`${API_BASE_URL}${url}`, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token') || ''}`
      },
      ...options
    });
    if (!res.ok) throw new Error(`HTTP error! Status: ${res.status}`);
    return await res.json();
  } catch (error) {
    console.warn(`Gateway Request failed, using simulation mock data. URL: ${url}`, error);
    // Simulate endpoints based on route
    if (url.startsWith('/api/vehicles')) return MOCK_DATA.vehicles;
    if (url.startsWith('/api/drivers')) return MOCK_DATA.drivers;
    if (url.startsWith('/emergencies')) return MOCK_DATA.emergencies;
    if (url.startsWith('/predict/eta')) {
      return { etaMinutes: Math.floor(Math.random() * 45) + 15, routeSelected: 'Optimized Route A' };
    }
    if (url.startsWith('/predict/maintenance')) {
      return { maintenanceRecommended: Math.random() > 0.5, confidence: 84.5, nextPartsNeedCheck: ['Brake Pads', 'Air Filter'] };
    }
    if (url.startsWith('/predict/delay')) {
      return { delayRisk: Math.random() > 0.6 ? 'HIGH' : 'LOW', delayMinutes: Math.floor(Math.random() * 20), weatherImpact: 'Minor Rain' };
    }
    if (url.startsWith('/predict/fuel')) {
      return { predictedConsumption: (Math.random() * 5 + 7).toFixed(2), unit: 'Gallons/100mi' };
    }
    if (url.startsWith('/predict/driver')) {
      return { behaviourScore: Math.floor(Math.random() * 30) + 70, alertsTriggered: ['Hard Braking (2x)'] };
    }
    if (url.startsWith('/predict/health')) {
      return { healthScore: Math.floor(Math.random() * 25) + 75, criticalFaultsDetected: 0 };
    }
    throw error;
  }
}

export const api = {
  auth: {
    login: async (username, password) => {
      try {
        const res = await fetch(`${API_BASE_URL}/api/auth/login`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ username, password })
        });
        if (!res.ok) throw new Error('Invalid credentials');
        const data = await res.json();
        localStorage.setItem('token', data.token);
        localStorage.setItem('username', data.username);
        return data;
      } catch (err) {
        // Fallback for demo logins
        if (username && password) {
          const fakeData = { token: 'demo-jwt-token-12345', username, role: 'ADMIN' };
          localStorage.setItem('token', fakeData.token);
          localStorage.setItem('username', fakeData.username);
          return fakeData;
        }
        throw err;
      }
    },
    logout: () => {
      localStorage.removeItem('token');
      localStorage.removeItem('username');
    }
  },
  vehicles: {
    getAll: () => handleRequest('/api/vehicles'),
    getById: (id) => handleRequest(`/api/vehicles/${id}`)
  },
  drivers: {
    getAll: () => handleRequest('/api/drivers')
  },
  emergencies: {
    getAll: () => handleRequest('/emergencies'),
    report: (data) => handleRequest('/emergencies', {
      method: 'POST',
      body: JSON.stringify(data)
    })
  },
  ai: {
    predictEta: (params) => handleRequest(`/predict/eta?distance=${params.distance}&traffic=${params.traffic}`),
    predictMaintenance: (vehicleId) => handleRequest(`/predict/maintenance?vehicleId=${vehicleId}`),
    predictDelay: (params) => handleRequest(`/predict/delay?tripId=${params.tripId}`),
    predictFuel: (params) => handleRequest(`/predict/fuel?tripId=${params.tripId}`),
    predictDriver: (driverId) => handleRequest(`/predict/driver?driverId=${driverId}`),
    predictHealth: (vehicleId) => handleRequest(`/predict/health?vehicleId=${vehicleId}`)
  }
};
