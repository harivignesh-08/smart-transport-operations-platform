# Smart Transport Operations Platform - Complete API Endpoints Reference

**Base URL:** `http://localhost:8080`

---

## 📋 Authentication Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|----------------|
| POST | `/api/auth/login` | User login | No |
| POST | `/api/auth/register` | User registration | No |
| GET | `/api/users` | Get all users | Yes |

---

## 🚚 Vehicle Management Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|----------------|
| GET | `/api/vehicles` | Get all vehicles | Yes |
| GET | `/api/vehicles/{id}` | Get vehicle by ID | Yes |
| POST | `/api/vehicles` | Create new vehicle | Yes |
| PUT | `/api/vehicles/{id}` | Update vehicle | Yes |
| DELETE | `/api/vehicles/{id}` | Delete vehicle | Yes |
| PUT | `/api/vehicles/{id}/status` | Update vehicle status | Yes |
| PUT | `/api/vehicles/{id}/availability` | Update vehicle availability | Yes |

**Vehicle Object Example:**
```json
{
  "id": 1,
  "name": "Semi-Truck 101",
  "type": "Freightliner Cascadia",
  "status": "ACTIVE",
  "fuelLevel": 78,
  "nextMaintenance": "2026-07-28",
  "mileage": 12450
}
```

**Vehicle Status Values:** `ACTIVE`, `MAINTENANCE`, `OUT_OF_SERVICE`

---

## 👥 Driver Management Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|----------------|
| GET | `/api/drivers` | Get all drivers | Yes |
| POST | `/api/drivers` | Create new driver | Yes |
| PUT | `/api/drivers/{id}` | Update driver | Yes |
| DELETE | `/api/drivers/{id}` | Delete driver | Yes |

**Driver Object Example:**
```json
{
  "id": 101,
  "name": "Alex Johnson",
  "license": "CDL-A",
  "rating": 4.8,
  "status": "ON_TRIP",
  "trips": 142
}
```

**Driver Status Values:** `AVAILABLE`, `ON_TRIP`, `OFF_DUTY`

---

## 🛣️ Trip Management Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|----------------|
| GET | `/api/trips` | Get all trips | Yes |
| POST | `/api/trips` | Create new trip | Yes |
| PUT | `/api/trips/{id}` | Update trip | Yes |
| DELETE | `/api/trips/{id}` | Delete trip | Yes |

**Trip Object Example:**
```json
{
  "id": 1001,
  "startLocation": "New York, NY",
  "endLocation": "Los Angeles, CA",
  "driverId": 101,
  "vehicleId": 1,
  "status": "IN_PROGRESS",
  "distance": 2800,
  "estimatedDuration": 2880
}
```

---

## 📍 Tracking & Telemetry Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|----------------|
| GET | `/api/tracking/live` | Get live tracking data | Yes |
| POST | `/api/tracking/location` | Report vehicle location | Yes |
| WS | `/ws/tracking` | WebSocket tracking stream | Yes |

**Location Update Request:**
```json
{
  "vehicleId": 1,
  "latitude": 40.7128,
  "longitude": -74.0060,
  "speed": 55,
  "bearing": 90,
  "timestamp": "2026-07-12T16:30:00Z"
}
```

**Tracking Data Response:**
```json
{
  "vehicleId": 1,
  "latitude": 40.7128,
  "longitude": -74.0060,
  "speed": 55,
  "bearing": 90,
  "lastUpdate": "2026-07-12T16:30:00Z"
}
```

---

## 📢 Notification Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|----------------|
| GET | `/api/notifications` | Get all notifications | Yes |

**Notification Object Example:**
```json
{
  "id": 1,
  "type": "ALERT",
  "title": "Vehicle Maintenance Due",
  "message": "Semi-Truck 101 maintenance due on 2026-07-28",
  "severity": "HIGH",
  "timestamp": "2026-07-12T16:00:00Z",
  "read": false
}
```

---

## 🗺️ Map Service Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|----------------|
| GET | `/api/map/live` | Get live map data | Yes |

**Map Data Response:**
```json
{
  "vehicles": [
    {
      "id": 1,
      "lat": 40.7128,
      "lng": -74.0060,
      "status": "ACTIVE"
    }
  ],
  "routes": [],
  "trafficIncidents": []
}
```

---

## 📁 File Management Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|----------------|
| POST | `/files/upload` | Upload file | Yes |
| GET | `/files` | Get all files | Yes |

**Upload Request:** FormData with `file` field
**Upload Response:**
```json
{
  "fileId": "f123456",
  "filename": "maintenance_report.pdf",
  "size": 2048576,
  "uploadedAt": "2026-07-12T16:00:00Z"
}
```

---

## 🚨 Emergency Management Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|----------------|
| GET | `/emergencies` | Get all emergencies | Yes |
| POST | `/emergencies` | Report emergency | Yes |

**Create Emergency Request:**
```json
{
  "vehicleName": "Semi-Truck 101",
  "type": "ENGINE_OVERHEAT",
  "severity": "HIGH",
  "description": "Engine temperature exceeding safe limits",
  "location": "40.7128, -74.0060"
}
```

**Emergency Object:**
```json
{
  "id": 2001,
  "vehicleName": "Semi-Truck 101",
  "type": "ENGINE_OVERHEAT",
  "severity": "HIGH",
  "status": "PENDING",
  "timestamp": "2026-07-12T16:15:00Z"
}
```

**Emergency Type Values:** `ENGINE_OVERHEAT`, `FLAT_TIRE`, `COLLISION`, `FUEL_LEAK`, `BRAKE_FAILURE`, `ACCIDENT`

**Severity Values:** `CRITICAL`, `HIGH`, `MEDIUM`, `LOW`

**Status Values:** `PENDING`, `DISPATCHED`, `IN_PROGRESS`, `RESOLVED`

---

## 🔮 Digital Twins Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|----------------|
| GET | `/digitaltwins` | Get all digital twins | Yes |

**Digital Twin Object Example:**
```json
{
  "id": "dt-1",
  "vehicleId": 1,
  "vehicleName": "Semi-Truck 101",
  "status": "ACTIVE",
  "healthScore": 87,
  "maintenanceNeeded": false,
  "lastSync": "2026-07-12T16:30:00Z"
}
```

---

## 🤖 AI Prediction Endpoints

### ETA Prediction
**Endpoint:** `GET /predict/eta`
**Query Parameters:**
- `distance` (number): Distance in miles
- `traffic` (string): Traffic condition - `LIGHT`, `MEDIUM`, `HEAVY`

**Response:**
```json
{
  "etaMinutes": 45,
  "routeSelected": "Optimized Route A",
  "confidence": 92.5,
  "trafficDelay": 8
}
```

### Maintenance Prediction
**Endpoint:** `GET /predict/maintenance`
**Query Parameters:**
- `vehicleId` (number): Vehicle ID

**Response:**
```json
{
  "maintenanceRecommended": true,
  "confidence": 84.5,
  "nextPartsNeedCheck": ["Brake Pads", "Air Filter"],
  "estimatedCost": 450.00,
  "recommendedDate": "2026-07-15"
}
```

### Fuel Consumption Prediction
**Endpoint:** `GET /predict/fuel`
**Query Parameters:**
- `tripId` (number): Trip ID

**Response:**
```json
{
  "predictedConsumption": 8.4,
  "unit": "Gallons/100mi",
  "estimatedCost": 245.00,
  "efficiency": "AVERAGE"
}
```

### Delay Risk Prediction
**Endpoint:** `GET /predict/delay`
**Query Parameters:**
- `tripId` (number): Trip ID

**Response:**
```json
{
  "delayRisk": "HIGH",
  "delayMinutes": 15,
  "weatherImpact": "Minor Rain",
  "congestionLevel": "HEAVY",
  "confidence": 87.5
}
```

### Driver Behavior Prediction
**Endpoint:** `GET /predict/driver`
**Query Parameters:**
- `driverId` (number): Driver ID

**Response:**
```json
{
  "behaviourScore": 85,
  "safetyRating": 4.8,
  "alertsTriggered": ["Hard Braking (2x)", "Speed Violation"],
  "recommendation": "SAFE"
}
```

### Vehicle Health Prediction
**Endpoint:** `GET /predict/health`
**Query Parameters:**
- `vehicleId` (number): Vehicle ID

**Response:**
```json
{
  "healthScore": 87,
  "overallStatus": "GOOD",
  "criticalFaultsDetected": 0,
  "warningFaultsDetected": 2,
  "faultDetails": ["Engine Oil Low", "Brake Wear"]
}
```

---

## ❤️ Health Check Endpoint

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|----------------|
| GET | `/health` | System health status | No |

**Response:**
```json
{
  "status": "OK",
  "timestamp": "2026-07-12T16:30:00Z",
  "services": {
    "database": "healthy",
    "cache": "healthy",
    "messageQueue": "healthy"
  },
  "uptime": 86400
}
```

---

## 🔐 Authentication

### Login Request
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "password123"
  }'
```

### Login Response
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "admin",
  "role": "ADMIN",
  "expiresIn": 86400
}
```

### Using Token in Requests
```bash
curl -X GET http://localhost:8080/api/vehicles \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

---

## 📊 Response Codes

| Code | Meaning |
|------|---------|
| 200 | OK - Request successful |
| 201 | Created - Resource created |
| 400 | Bad Request - Invalid parameters |
| 401 | Unauthorized - Missing/invalid token |
| 403 | Forbidden - Insufficient permissions |
| 404 | Not Found - Resource doesn't exist |
| 500 | Internal Server Error |

---

## 🔄 WebSocket Connection

### Connecting to Tracking WebSocket
```javascript
const ws = new WebSocket('ws://localhost:8080/ws/tracking');

ws.onopen = () => {
  console.log('Connected to tracking stream');
};

ws.onmessage = (event) => {
  const trackingData = JSON.parse(event.data);
  console.log('Vehicle position:', trackingData);
};

ws.onerror = (error) => {
  console.error('WebSocket error:', error);
};

ws.onclose = () => {
  console.log('Disconnected from tracking stream');
};
```

---

## 📝 Error Response Format

```json
{
  "error": "Invalid vehicle ID",
  "code": "INVALID_VEHICLE",
  "statusCode": 400,
  "timestamp": "2026-07-12T16:30:00Z"
}
```

---

## 🚀 Rate Limiting

- **Default:** 100 requests per minute per user
- **Burst:** 1000 requests per 10 minutes
- **Headers:** `X-RateLimit-Limit`, `X-RateLimit-Remaining`, `X-RateLimit-Reset`

---

## 📱 Request/Response Format

### Default Content-Type
```
Content-Type: application/json
```

### All Requests Include
```
Authorization: Bearer {token}
User-Agent: Openeye-Client/1.0
```

---

## 🔗 CORS Configuration

**Allowed Origins:** `http://localhost:5173`, `http://localhost:3000`

**Allowed Methods:** GET, POST, PUT, DELETE, OPTIONS

**Allowed Headers:** Content-Type, Authorization

---

## 📚 Additional Resources

- Full API Integration: See `API_INTEGRATION.md`
- Frontend Update Summary: See `FRONTEND_UPDATE_SUMMARY.md`
- Openeye Frontend: Visit `http://localhost:5173`

---

**API Version:** 1.0.0
**Last Updated:** July 12, 2026
**Status:** Production Ready
