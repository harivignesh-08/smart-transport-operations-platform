# TransitOps AI Prediction Service

TransitOps AI is a production-ready FastAPI microservice designed to serve as the core prediction engine for the **TransitOps AI Smart Fleet & Transport Management System**. It evaluates real-time vehicle telemetry, routing profiles, and driver logs to calculate arrival times, route optimization, predictive maintenance, vehicle health, driver behaviors, delay risks, and fuel consumption.

The service is fully independent and communicates with Java Spring Boot microservices via REST endpoints utilizing native **camelCase JSON formatting** for simple API integrations.

---

## 🛠️ Technology Stack

- **Python 3.12+**
- **FastAPI**: Modern, high-performance web framework.
- **Uvicorn**: ASG server for execution.
- **Scikit-learn**: For predictive maintenance and fuel regressions.
- **Joblib**: Model serialization and persistence.
- **Pandas & NumPy**: Telemetry data processing.
- **Pydantic v2**: High-speed JSON schema validation.

---

## 📂 Project Directory Structure

```text
ai-service/
│
├── app.py                # Main entry point registering routers & HTML dashboard
├── requirements.txt      # Python dependencies
├── test_endpoints.py     # Unit testing suite
│
├── config/
│   ├── __init__.py
│   └── settings.py       # Global environment configurations
│
├── api/
│   ├── __init__.py
│   └── endpoints/        # Endpoint router controllers
│       ├── __init__.py
│       ├── delay.py
│       ├── driver.py
│       ├── eta.py
│       ├── fuel.py
│       ├── health.py
│       ├── maintenance.py
│       ├── route.py
│       └── vehicle_health.py
│
├── services/             # Predictor service business logic
│   ├── __init__.py
│   ├── delay_service.py
│   ├── driver_service.py
│   ├── eta_service.py
│   ├── fuel_service.py
│   ├── maintenance_service.py
│   ├── route_service.py
│   └── vehicle_health_service.py
│
├── schemas/              # Pydantic v2 request & response schemas
│   ├── __init__.py
│   ├── base.py           # CamelCase naming converter base model
│   ├── delay.py
│   ├── driver.py
│   ├── eta.py
│   ├── fuel.py
│   ├── health.py
│   ├── maintenance.py
│   ├── route.py
│   └── vehicle_health.py
│
├── trained_models/       # Persistent Scikit-learn models (joblib format)
└── data/                 # Data storage directory
```

---

## 🚀 Getting Started

### 1. Set Up Environment & Install Dependencies

It is recommended to run inside a virtual environment:

```bash
# Create virtual environment
python -m venv venv

# Activate on Windows (PowerShell)
.\venv\Scripts\Activate.ps1

# Activate on Unix/macOS
source venv/bin/activate

# Install dependencies
pip install -r requirements.txt
```

### 2. Run the Microservice

Start the application with Uvicorn in reload mode:

```bash
uvicorn app:app --reload
```

The service will boot up and be accessible locally:
- **Interactive API Dashboard**: [http://localhost:8084/](http://localhost:8084/)
- **Swagger UI Documentation**: [http://localhost:8084/docs](http://localhost:8084/docs)
- **ReDoc Documentation**: [http://localhost:8084/redoc](http://localhost:8084/redoc)

---

## 🔌 API Endpoints Reference

| Method | Endpoint | Description | Input Fields | Output Fields |
| :--- | :--- | :--- | :--- | :--- |
| **GET** | `/health` | Health status checker | None | `status`, `service` |
| **POST** | `/predict/eta` | ETA Prediction | `currentLatitude`, `currentLongitude`, `destinationLatitude`, `destinationLongitude`, `currentSpeed` | `estimatedArrivalTime`, `remainingDistance` |
| **POST** | `/predict/route` | Route Optimization | `source`, `destination`, `vehicleType` | `optimizedRoute`, `estimatedDistance`, `estimatedTime` |
| **POST** | `/predict/maintenance` | Predictive Maintenance | `vehicleId`, `totalDistance`, `engineHours`, `fuelConsumption`, `lastServiceDate` | `maintenanceRequired`, `maintenanceScore`, `suggestedMaintenance` |
| **POST** | `/predict/fuel` | Fuel Estimation | `distance`, `vehicleType`, `averageSpeed`, `loadWeight` | `predictedFuelConsumption` |
| **POST** | `/predict/driver` | Driver Analysis | `driverId`, `averageSpeed`, `harshBraking`, `harshAcceleration`, `overspeedCount` | `driverScore`, `riskLevel`, `recommendation` |
| **POST** | `/predict/delay` | Delay Estimation | `currentSpeed`, `remainingDistance`, `trafficLevel`, `weather` | `delayMinutes`, `arrivalStatus` |
| **POST** | `/predict/vehicle-health` | Mechanical Score | `engineTemperature`, `mileage`, `oilLevel`, `batteryVoltage` | `healthScore`, `healthStatus` |

---

## ☕ Spring Boot OpenFeign Integration

To consume this service inside a Java Spring Boot application, define the client interface as follows:

```java
package com.transitops.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import java.time.LocalDateTime;
import java.util.List;

@FeignClient(name = "ai-service", url = "${transitops.ai-service.url:http://localhost:8084}")
public interface AIServiceClient {

    @GetMapping("/health")
    HealthResponse checkHealth();

    @PostMapping("/predict/eta")
    ETAResponse predictETA(@RequestBody ETARequest request);

    @PostMapping("/predict/route")
    RouteResponse optimizeRoute(@RequestBody RouteRequest request);

    @PostMapping("/predict/maintenance")
    MaintenanceResponse predictMaintenance(@RequestBody MaintenanceRequest request);

    @PostMapping("/predict/fuel")
    FuelResponse predictFuel(@RequestBody FuelRequest request);

    @PostMapping("/predict/driver")
    DriverResponse analyzeDriver(@RequestBody DriverRequest request);

    @PostMapping("/predict/delay")
    DelayResponse predictDelay(@RequestBody DelayRequest request);

    @PostMapping("/predict/vehicle-health")
    VehicleHealthResponse getVehicleHealth(@RequestBody VehicleHealthRequest request);

    // Feign DTO Records
    record HealthResponse(String status, String service) {}
    
    record ETARequest(double currentLatitude, double currentLongitude, double destinationLatitude, double destinationLongitude, double currentSpeed) {}
    record ETAResponse(LocalDateTime estimatedArrivalTime, double remainingDistance) {}

    record RouteRequest(String source, String destination, String vehicleType) {}
    record RouteResponse(List<String> optimizedRoute, double estimatedDistance, double estimatedTime) {}

    record MaintenanceRequest(String vehicleId, double totalDistance, double engineHours, double fuelConsumption, String lastServiceDate) {}
    record MaintenanceResponse(boolean maintenanceRequired, double maintenanceScore, List<String> suggestedMaintenance) {}

    record FuelRequest(double distance, String vehicleType, double averageSpeed, double loadWeight) {}
    record FuelResponse(double predictedFuelConsumption) {}

    record DriverRequest(String driverId, double averageSpeed, int harshBraking, int harshAcceleration, int overspeedCount) {}
    record DriverResponse(double driverScore, String riskLevel, String recommendation) {}

    record DelayRequest(double currentSpeed, double remainingDistance, String trafficLevel, String weather) {}
    record DelayResponse(double delayMinutes, String arrivalStatus) {}

    record VehicleHealthRequest(double engineTemperature, double mileage, double oilLevel, double batteryVoltage) {}
    record VehicleHealthResponse(double healthScore, String healthStatus) {}
}
```

---

## 🧪 Testing

Run the pytest suite to verify all endpoints are operating successfully and matching the Pydantic schemas:

```bash
pytest test_endpoints.py
```
