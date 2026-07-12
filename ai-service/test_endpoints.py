from fastapi.testclient import TestClient
from app import app

client = TestClient(app)

def test_health():
    response = client.get("/health")
    assert response.status_code == 200
    data = response.json()
    assert data["status"] == "UP"
    assert data["service"] == "AI Service"

def test_predict_eta():
    payload = {
        "currentLatitude": 12.9716,
        "currentLongitude": 77.5946,
        "destinationLatitude": 13.0827,
        "destinationLongitude": 80.2707,
        "currentSpeed": 60.0
    }
    response = client.post("/predict/eta", json=payload)
    assert response.status_code == 200
    data = response.json()
    assert "estimatedArrivalTime" in data
    assert "remainingDistance" in data
    assert data["remainingDistance"] > 0

def test_predict_route():
    payload = {
        "source": "Bangalore",
        "destination": "Chennai",
        "vehicleType": "Heavy Truck"
    }
    response = client.post("/predict/route", json=payload)
    assert response.status_code == 200
    data = response.json()
    assert "optimizedRoute" in data
    assert "estimatedDistance" in data
    assert "estimatedTime" in data

def test_predict_maintenance():
    payload = {
        "vehicleId": "TRUCK-992",
        "totalDistance": 180000.0,
        "engineHours": 4500.0,
        "fuelConsumption": 22000.0,
        "lastServiceDate": "2025-10-15"
    }
    response = client.post("/predict/maintenance", json=payload)
    assert response.status_code == 200
    data = response.json()
    assert "maintenanceRequired" in data
    assert "maintenanceScore" in data
    assert "suggestedMaintenance" in data
    assert isinstance(data["suggestedMaintenance"], list)

def test_predict_fuel():
    payload = {
        "distance": 350.0,
        "vehicleType": "Truck",
        "averageSpeed": 75.0,
        "loadWeight": 5000.0
    }
    response = client.post("/predict/fuel", json=payload)
    assert response.status_code == 200
    data = response.json()
    assert "predictedFuelConsumption" in data
    assert data["predictedFuelConsumption"] > 0

def test_predict_driver():
    payload = {
        "driverId": "DRV-3810",
        "averageSpeed": 92.5,
        "harshBraking": 4,
        "harshAcceleration": 2,
        "overspeedCount": 3
    }
    response = client.post("/predict/driver", json=payload)
    assert response.status_code == 200
    data = response.json()
    assert "driverScore" in data
    assert "riskLevel" in data
    assert "recommendation" in data

def test_predict_delay():
    payload = {
        "currentSpeed": 45.0,
        "remainingDistance": 85.0,
        "trafficLevel": "Heavy",
        "weather": "Rainy"
    }
    response = client.post("/predict/delay", json=payload)
    assert response.status_code == 200
    data = response.json()
    assert "delayMinutes" in data
    assert "arrivalStatus" in data

def test_predict_vehicle_health():
    payload = {
        "engineTemperature": 102.5,
        "mileage": 120000.0,
        "oilLevel": 35.0,
        "batteryVoltage": 11.9
    }
    response = client.post("/predict/vehicle-health", json=payload)
    assert response.status_code == 200
    data = response.json()
    assert "healthScore" in data
    assert "healthStatus" in data
