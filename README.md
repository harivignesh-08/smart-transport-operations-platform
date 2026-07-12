# 🌐 Openeye AI - Smart Transport Operations Platform

**Real-time Fleet Management | AI-Powered Predictions | Emergency Response Coordination**

AI-powered Smart Transport Operations Platform for Odoo Hackathon 2026

---

## 🎯 Platform Overview

**Openeye** is a comprehensive smart transport operations platform that combines real-time fleet management, AI-powered predictive analytics, emergency incident management, and intelligent route optimization. The platform features a modern React-based frontend (Openeye UI) and a robust microservices backend architecture.

### Key Features
- ✅ Real-time vehicle tracking with live GPS positioning
- ✅ AI-powered predictive maintenance and vehicle health monitoring
- ✅ Intelligent ETA and route optimization with traffic analysis
- ✅ Emergency incident management and dispatcher coordination
- ✅ Driver performance analytics and behavior prediction
- ✅ Fuel consumption optimization and cost analysis
- ✅ Digital twin vehicle simulation
- ✅ WebSocket real-time telemetry streaming

---

## 🏗️ Architecture

### Backend Services

| Service | Technology | Port | Purpose |
| :--- | :--- | :--- | :--- |
| `service-registry` | Spring Boot / Eureka | 8761 | Service discovery |
| `api-gateway` | Spring Boot / Gateway | 8080 | API routing & aggregation |
| `auth-service` | Spring Boot | 8081 | JWT authentication |
| `vehicle-service` | Spring Boot | 8082 | Vehicle management |
| `driver-service` | Spring Boot | 8083 | Driver management |
| `ai-service` | Python / FastAPI | 8084 | AI predictions (ETA, maintenance, etc.) |
| `trip-service` | Spring Boot | 8085 | Trip management |
| `tracking-service` | Spring Boot | 8086 | Real-time tracking & telemetry |
| `map-service` | Spring Boot | 8087 | Map data & route visualization |
| `digital-twin-service` | Spring Boot | 8088 | Vehicle digital twins |
| `emergency-service` | Spring Boot | 8089 | Emergency incident management |
| `notification-service` | Spring Boot | 8090 | Notifications & alerts |
| `file-service` | Spring Boot | 8091 | File upload/management |

### Frontend

| Component | Technology | Port | Purpose |
| :--- | :--- | :--- | :--- |
| **Openeye UI** | React 19.2 + Vite | 5173 | Web-based operations console |

---

## 🚀 Quick Start

### Frontend Setup (Openeye UI)

```bash
# Navigate to frontend directory
cd frontend

# Install dependencies
npm install

# Start development server
npm run dev
# Access at: http://localhost:5173
```

### Login Credentials
```
Username: admin or dispatcher
Password: Any password (demo mode)
```

### Build for Production
```bash
npm run build
npm run preview
```

---

## 📡 API Gateway

The API Gateway routes all frontend requests to backend microservices.

**Base URL:** `http://localhost:8080`

### Available Endpoints

#### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration
- `GET /api/users` - Get all users

#### Vehicle Management
- `GET /api/vehicles` - List vehicles
- `GET /api/vehicles/{id}` - Get vehicle details
- `POST /api/vehicles` - Create vehicle
- `PUT /api/vehicles/{id}` - Update vehicle
- `DELETE /api/vehicles/{id}` - Delete vehicle
- `PUT /api/vehicles/{id}/status` - Update status
- `PUT /api/vehicles/{id}/availability` - Update availability

#### Driver Management
- `GET /api/drivers` - List drivers
- `POST /api/drivers` - Create driver
- `PUT /api/drivers/{id}` - Update driver
- `DELETE /api/drivers/{id}` - Delete driver

#### Trip Management
- `GET /api/trips` - List trips
- `POST /api/trips` - Create trip
- `PUT /api/trips/{id}` - Update trip
- `DELETE /api/trips/{id}` - Delete trip

#### Real-Time Tracking
- `GET /api/tracking/live` - Live tracking data
- `POST /api/tracking/location` - Report location
- `WS /ws/tracking` - WebSocket tracking stream

#### AI Predictions
- `GET /predict/eta` - ETA prediction
- `GET /predict/maintenance` - Maintenance prediction
- `GET /predict/fuel` - Fuel consumption prediction
- `GET /predict/delay` - Delay risk prediction
- `GET /predict/driver` - Driver behavior prediction
- `GET /predict/health` - Vehicle health prediction

#### Emergency Management
- `GET /emergencies` - Get emergencies
- `POST /emergencies` - Report emergency

#### Additional Services
- `GET /api/notifications` - Get notifications
- `GET /api/map/live` - Live map data
- `GET /api/notifications` - Get notifications
- `POST /files/upload` - Upload file
- `GET /files` - Get files
- `GET /digitaltwins` - Get digital twins
- `GET /health` - System health check

**For complete API reference, see:** [API_ENDPOINTS_REFERENCE.md](API_ENDPOINTS_REFERENCE.md)

---

## 🎨 Frontend Features

### 📊 Operations Center Dashboard
- Real-time fleet statistics (active vehicles, drivers, fuel)
- Pending emergency alerts with severity indicators
- Active fleet status logs with maintenance schedules
- AI Operations Advisor with recommendations
- Quick incident dispatch system

### 📍 Live GPS Fleet Tracking
- Interactive map with vehicle positions
- WebSocket real-time updates
- Vehicle telemetry display (speed, bearing, coordinates)
- Route visualization
- Vehicle detail panel with live metrics

### 🤖 AI Predictor Hub
- ETA optimization with traffic analysis
- Predictive maintenance alerts
- Delay risk assessment
- Fuel consumption prediction
- Driver behavior analysis
- Vehicle health monitoring

### 🚛 Fleet & Drivers Directory
- Complete vehicle inventory with maintenance schedules
- Driver directory with safety ratings
- Status monitoring (ACTIVE, MAINTENANCE, AVAILABLE, ON_TRIP)
- Quick access to asset information

### 🚨 Emergency Incident Console
- Emergency log with severity filtering
- Incident reporting form
- Status workflow (PENDING → DISPATCHED → RESOLVED)
- Color-coded severity indicators
- Real-time incident updates

---

## 📚 Documentation

### Frontend Documentation
- **Openeye Frontend Integration:** [frontend/API_INTEGRATION.md](frontend/API_INTEGRATION.md)
- **Frontend Update Summary:** [FRONTEND_UPDATE_SUMMARY.md](FRONTEND_UPDATE_SUMMARY.md)
- **Complete API Reference:** [API_ENDPOINTS_REFERENCE.md](API_ENDPOINTS_REFERENCE.md)

### Backend Documentation
- **AI Service Setup:** [ai-service/README.md](ai-service/README.md)

---

## 🔐 Authentication

### JWT Token Flow
1. User logs in at `/api/auth/login`
2. Backend returns JWT token
3. Token stored in browser localStorage
4. Token included in all subsequent requests as Bearer token
5. API Gateway validates token before routing requests

### Headers
```
Authorization: Bearer {token}
Content-Type: application/json
```

---

## 🌟 Frontend Branding: Openeye

The frontend has been completely redesigned and rebranded as **Openeye AI**:

- **Modern UI:** Glassmorphic design with dark theme
- **Real-time Updates:** WebSocket integration for live data
- **AI Integration:** Multi-model predictions and analytics
- **Emergency Response:** Quick incident management
- **Fleet Operations:** Comprehensive asset management
- **Responsive Design:** Works on desktop, tablet, and mobile

### Access the Frontend
```
Frontend: http://localhost:5173
API Gateway: http://localhost:8080
```

---

## 🚀 Running the Full Stack

### Terminal 1: Start Backend Services
```bash
# Start API Gateway and microservices
# Follow backend setup instructions in respective service READMEs
```

### Terminal 2: Start Frontend
```bash
cd frontend
npm install
npm run dev
```

### Terminal 3: Monitor Services (Optional)
```bash
# Monitor Eureka service registry
# Access at: http://localhost:8761
```

---

## 📊 System Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    Openeye Frontend (React)                 │
│                  http://localhost:5173                      │
└──────────────────────────┬──────────────────────────────────┘
                           │
                 ┌─────────▼─────────┐
                 │   API Gateway     │
                 │ http://8080       │
                 └─────────┬─────────┘
                           │
        ┌──────────────────┼──────────────────┐
        │                  │                  │
    ┌───▼────┐        ┌────▼────┐       ┌────▼────┐
    │ Auth   │        │ Vehicle │       │  Driver │
    │Service │        │ Service │       │Service  │
    │ :8081  │        │  :8082  │       │  :8083  │
    └────────┘        └────────┘       └────────┘
        
        ┌────────────────┐  ┌───────────────┐
        │  AI Service    │  │ Tracking      │
        │  Python :8084  │  │ Service :8086 │
        └────────────────┘  └───────────────┘
```

---

## 🔄 Development Workflow

### Adding New Frontend Feature
1. Create component in `frontend/src/components/` or `frontend/src/pages/`
2. Import and use API client from `frontend/src/services/api.js`
3. Test with mock data fallback
4. Verify API integration with backend

### Adding New API Endpoint
1. Update backend microservice
2. Add route in API Gateway
3. Update frontend API client (`frontend/src/services/api.js`)
4. Add UI component to consume endpoint
5. Test end-to-end flow

---

## 🐛 Troubleshooting

### Frontend Won't Start
```bash
cd frontend
rm -rf node_modules
npm install
npm run dev
```

### API Connection Issues
```bash
# Check if gateway is running
curl http://localhost:8080/health

# Check frontend console (F12)
# Look for error messages and failed requests
```

### WebSocket Connection Failed
- Verify `/ws/tracking` endpoint is available
- Check that authorization token is valid
- Ensure WebSocket protocol is supported
- Check network tab for failed connections

---

## 📈 Performance

### Frontend Optimization
- Efficient React hooks for rendering
- CSS Grid for fast layouts
- Lazy loading of routes
- WebSocket for real-time updates
- Minimal dependencies

### Backend Optimization
- Microservices architecture
- Service registry for load balancing
- Caching strategies
- Database optimization
- API rate limiting

---

## 🔮 Future Enhancements

### Phase 2
- Mobile app (iOS/Android)
- Advanced analytics dashboards
- Machine learning optimization
- Third-party integration
- Voice commands

### Phase 3
- Autonomous vehicle support
- Blockchain tracking
- AR training interface
- API marketplace
- Advanced digital twins

---

## 📝 Documentation Files

| File | Purpose |
| :--- | :--- |
| [API_ENDPOINTS_REFERENCE.md](API_ENDPOINTS_REFERENCE.md) | Complete API endpoint reference |
| [frontend/API_INTEGRATION.md](frontend/API_INTEGRATION.md) | Frontend-API integration guide |
| [FRONTEND_UPDATE_SUMMARY.md](FRONTEND_UPDATE_SUMMARY.md) | Frontend update details |
| [ai-service/README.md](ai-service/README.md) | AI service setup & docs |

---

## 🤝 Contributing

### Frontend Changes
```bash
cd frontend
npm run lint  # Check code style
npm run build # Build for production
```

### Code Style
- Use functional components with React hooks
- Follow ESLint configuration
- Add JSDoc comments
- Write descriptive commit messages

---

## 📞 Support & Resources

- Frontend Documentation: [frontend/API_INTEGRATION.md](frontend/API_INTEGRATION.md)
- API Reference: [API_ENDPOINTS_REFERENCE.md](API_ENDPOINTS_REFERENCE.md)
- Backend Services: Check individual service READMEs
- AI Service: [ai-service/README.md](ai-service/README.md)

---

## 📅 Version History

### v1.0.0 - July 12, 2026
- ✅ Complete frontend redesign with Openeye branding
- ✅ Full API integration with all endpoints
- ✅ Real-time tracking with WebSocket
- ✅ AI prediction models integration
- ✅ Emergency management system
- ✅ Comprehensive documentation
- ✅ Production-ready deployment

---

## 📜 License

MIT License - See LICENSE file for details

---

## 🙏 Acknowledgments

Built with:
- React 19.2 & Vite
- Lucide React Icons
- Spring Boot & Eureka
- Python FastAPI
- WebSocket Technologies

---

<div align="center">

**Openeye AI - Smart Transport Operations Platform**

*Empowering logistics with real-time intelligence*

**Frontend:** http://localhost:5173 | **API:** http://localhost:8080

© 2026 Openeye. All rights reserved.

</div>
