# Openeye - Smart Transport Operations Platform
## Frontend API Integration Documentation

### Overview
Openeye is a comprehensive fleet management and transport operations platform built with React and Vite. The frontend integrates with a backend API gateway running on `http://localhost:8080` and provides a real-time operations command center for fleet management, driver coordination, emergency response, and AI-powered predictions.

---

## 📡 API Base URL
```
http://localhost:8080
```

---

## 🔐 Authentication Service

### Login
**Endpoint:** `POST /api/auth/login`
- **Request Body:** `{ username: string, password: string }`
- **Response:** `{ token: string, username: string, role: string }`
- **Usage:** Authenticates user and returns JWT token stored in localStorage

### Register
**Endpoint:** `POST /api/auth/register`
- **Request Body:** User registration data
- **Response:** Registration confirmation

### Get All Users
**Endpoint:** `GET /api/users`
- **Response:** Array of user objects

### Logout
- **Action:** Removes token from localStorage

---

## 🚚 Vehicle Management Service

### Get All Vehicles
**Endpoint:** `GET /api/vehicles`
- **Response:** Array of vehicle objects
- **Fields:** id, name, type, status, fuelLevel, nextMaintenance, mileage

### Get Vehicle by ID
**Endpoint:** `GET /api/vehicles/{id}`
- **Response:** Single vehicle object

### Create Vehicle
**Endpoint:** `POST /api/vehicles`
- **Request Body:** Vehicle data
- **Response:** Created vehicle object

### Update Vehicle
**Endpoint:** `PUT /api/vehicles/{id}`
- **Request Body:** Updated vehicle data
- **Response:** Updated vehicle object

### Delete Vehicle
**Endpoint:** `DELETE /api/vehicles/{id}`
- **Response:** Deletion confirmation

### Update Vehicle Status
**Endpoint:** `PUT /api/vehicles/{id}/status`
- **Request Body:** `{ status: string }`
- **Statuses:** ACTIVE, MAINTENANCE, OUT_OF_SERVICE

### Update Vehicle Availability
**Endpoint:** `PUT /api/vehicles/{id}/availability`
- **Request Body:** `{ availability: string }`

---

## 👥 Driver Management Service

### Get All Drivers
**Endpoint:** `GET /api/drivers`
- **Response:** Array of driver objects
- **Fields:** id, name, license, rating, status, trips

### Create Driver
**Endpoint:** `POST /api/drivers`
- **Request Body:** Driver data
- **Response:** Created driver object

### Update Driver
**Endpoint:** `PUT /api/drivers/{id}`
- **Request Body:** Updated driver data
- **Response:** Updated driver object

### Delete Driver
**Endpoint:** `DELETE /api/drivers/{id}`
- **Response:** Deletion confirmation

---

## 🛣️ Trip Management Service

### Get All Trips
**Endpoint:** `GET /api/trips`
- **Response:** Array of trip objects

### Create Trip
**Endpoint:** `POST /api/trips`
- **Request Body:** Trip data
- **Response:** Created trip object

### Update Trip
**Endpoint:** `PUT /api/trips/{id}`
- **Request Body:** Updated trip data
- **Response:** Updated trip object

### Delete Trip
**Endpoint:** `DELETE /api/trips/{id}`
- **Response:** Deletion confirmation

---

## 📍 Tracking & Telemetry Service

### Get Live Tracking Data
**Endpoint:** `GET /api/tracking/live`
- **Response:** Real-time vehicle positions and telemetry

### Report Vehicle Location
**Endpoint:** `POST /api/tracking/location`
- **Request Body:** Location data (lat, lng, speed, bearing)
- **Response:** Location update confirmation

### WebSocket Connection
**Endpoint:** `WS /ws/tracking`
- **Protocol:** WebSocket STOMP
- **Purpose:** Real-time vehicle tracking updates
- **Status:** Connected/Disconnected indicator shown in Fleet Tracking UI

---

## 📢 Notifications Service

### Get All Notifications
**Endpoint:** `GET /api/notifications`
- **Response:** Array of notification objects

---

## 🗺️ Map Service

### Get Live Map Data
**Endpoint:** `GET /api/map/live`
- **Response:** Live map data with vehicle positions and routes

---

## 📁 File Management Service

### Upload File
**Endpoint:** `POST /files/upload`
- **Request:** FormData with file
- **Response:** File metadata and upload confirmation

### Get All Files
**Endpoint:** `GET /files`
- **Response:** Array of file objects

---

## 🚨 Emergency & Incident Service

### Get All Emergencies
**Endpoint:** `GET /emergencies`
- **Response:** Array of emergency/incident objects
- **Fields:** id, vehicleName, type, severity, status, timestamp

### Report Emergency
**Endpoint:** `POST /emergencies`
- **Request Body:** `{ vehicleName, type, severity, status }`
- **Types:** ENGINE_OVERHEAT, FLAT_TIRE, COLLISION, FUEL_LEAK
- **Severity:** CRITICAL, HIGH, MEDIUM
- **Status:** PENDING, DISPATCHED, RESOLVED

---

## 🔮 Digital Twins Service

### Get All Digital Twins
**Endpoint:** `GET /digitaltwins`
- **Response:** Array of digital twin objects

---

## 🤖 AI Prediction Engine Service

### ETA Prediction
**Endpoint:** `GET /predict/eta`
- **Query Params:** `?distance={miles}&traffic={LIGHT|MEDIUM|HEAVY}`
- **Response:** `{ etaMinutes: number, routeSelected: string }`

### Maintenance Prediction
**Endpoint:** `GET /predict/maintenance`
- **Query Params:** `?vehicleId={id}`
- **Response:** `{ maintenanceRecommended: boolean, confidence: number, nextPartsNeedCheck: string[] }`

### Fuel Consumption Prediction
**Endpoint:** `GET /predict/fuel`
- **Query Params:** `?tripId={id}`
- **Response:** `{ predictedConsumption: number, unit: string }`

### Delay Risk Prediction
**Endpoint:** `GET /predict/delay`
- **Query Params:** `?tripId={id}`
- **Response:** `{ delayRisk: string, delayMinutes: number, weatherImpact: string }`

### Driver Behavior Prediction
**Endpoint:** `GET /predict/driver`
- **Query Params:** `?driverId={id}`
- **Response:** `{ behaviourScore: number, alertsTriggered: string[] }`

### Vehicle Health Prediction
**Endpoint:** `GET /predict/health`
- **Query Params:** `?vehicleId={id}`
- **Response:** `{ healthScore: number, criticalFaultsDetected: number }`

---

## ❤️ Health Check Service

### System Health Status
**Endpoint:** `GET /health`
- **Response:** System health and status information

---

## 📊 Frontend Features & Pages

### 1. **Operations Center Dashboard** (`/dashboard`)
- Real-time fleet statistics (active vehicles, drivers, fuel efficiency)
- Pending emergencies overview
- Active fleet status logs
- AI Operations Advisor with recommendations
- Quick incident alert system

### 2. **Live GPS Fleet Tracking** (`/tracking`)
- Interactive SVG map with vehicle positions
- WebSocket real-time tracking updates
- Vehicle telemetry display (speed, bearing, GPS coordinates)
- Route visualization
- Vehicle selection and detail panel

### 3. **AI Predictor Hub** (`/predictions`)
- **ETA Optimization:** Predict delivery times based on distance and traffic
- **Predictive Maintenance:** Analyze vehicle health and maintenance needs
- **Delay Risk Score:** Assess trip disruption probabilities
- **Fuel Consumption:** Predict fuel efficiency
- **Driver Behavior:** Analyze driver performance
- **Vehicle Health:** Monitor vehicle condition

### 4. **Fleet & Drivers Directory** (`/fleet`)
- Complete vehicles inventory with maintenance schedules
- Driver directory with ratings and trip history
- Status monitoring (ACTIVE, MAINTENANCE, OFF_DUTY)
- Quick vehicle/driver information display

### 5. **Incident Console** (`/emergencies`)
- Emergency log with severity filtering
- Incident report form
- Status tracking (PENDING → DISPATCHED → RESOLVED)
- Color-coded severity indicators
- Real-time incident updates

---

## 🎨 UI Components & Styling

### Design System
- **Color Palette:** Cyan (#38bdf8), Blue (#3b82f6), Emerald (#10b981), Amber (#f59e0b), Rose (#ef4444)
- **Glass Morphism:** Semi-transparent panels with backdrop blur
- **Dark Theme:** Optimized for 24/7 operations center use
- **Responsive Grid:** Auto-fit layouts for various screen sizes

### Key Components
- `Sidebar` - Navigation menu with active state indicators
- `TopBar` - Header with user profile and notifications
- `Dashboard` - Main statistics and overview
- `FleetTracking` - Interactive map with vehicle markers
- `AiPredictions` - Multi-model AI prediction interface
- `FleetManagement` - Vehicle and driver listings
- `EmergencyConsole` - Incident management and reporting
- `LoginPage` - Secure authentication interface

---

## 🔌 API Client Implementation

The frontend uses a custom API client (`src/services/api.js`) that provides:
- Centralized request handling with token authentication
- Mock data fallback for offline/demo mode
- Comprehensive endpoint coverage
- Error handling and logging

### Usage Example
```javascript
// Fetch all vehicles
const vehicles = await api.vehicles.getAll();

// Predict ETA
const eta = await api.ai.predictEta({ distance: 45, traffic: 'MEDIUM' });

// Report emergency
await api.emergencies.create({ vehicleName: 'Truck-01', type: 'ENGINE_OVERHEAT', severity: 'HIGH' });
```

---

## 🚀 Running the Frontend

```bash
# Install dependencies
npm install

# Start development server (runs on http://localhost:5173)
npm run dev

# Build for production
npm run build

# Preview production build
npm run preview
```

---

## 📝 Environment Variables

The frontend expects the API gateway to be available at:
```
http://localhost:8080
```

This can be configured in `src/services/api.js` by modifying the `API_BASE_URL` constant.

---

## 🔒 Authentication

All API requests (except login/register) include the Authorization header:
```
Authorization: Bearer {token}
```

The token is stored in `localStorage` after successful login and is automatically included in all subsequent requests.

---

## 🐛 Error Handling

The API client includes graceful fallback to mock data when:
- API gateway is not reachable
- Network errors occur
- Requests fail with error status codes

This allows the frontend to operate in demo/offline mode with simulated data.

---

## 📱 Responsive Design

The frontend is optimized for:
- Desktop (1024px+)
- Tablet (768px - 1023px)
- Mobile (< 768px)

All components use CSS Grid and Flexbox for responsive layouts.

---

## 🎯 Future Enhancements

- Real-time WebSocket notifications
- Advanced vehicle diagnostic telemetry
- Multi-user collaboration features
- Custom reporting and analytics
- Integration with third-party logistics providers
- Mobile app companion
- Voice command interface

---

## 📧 Support

For issues or questions about the Openeye frontend, please refer to the API documentation or contact the development team.

**App Name:** Openeye AI
**Status:** Production Ready
**Last Updated:** 2026-07-12
