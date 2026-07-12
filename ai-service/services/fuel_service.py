import os
import joblib
import pandas as pd
import numpy as np
from sklearn.linear_model import LinearRegression
from schemas.fuel import FuelRequest, FuelResponse
from utils.logging_config import logger

class FuelPredictionService:
    def __init__(self):
        # Build path relative to project root
        self.model_path = os.path.join(
            os.path.dirname(os.path.dirname(os.path.abspath(__file__))),
            "trained_models",
            "fuel_model.joblib"
        )
        self.ensure_model_trained()
        self.model = joblib.load(self.model_path)

    def _encode_vehicle_type(self, vehicle_type: str) -> int:
        mapping = {"sedan": 0, "van": 1, "bus": 2, "truck": 3}
        return mapping.get(vehicle_type.lower(), 1)  # Default to 1 (van) if unknown

    def ensure_model_trained(self):
        # Create directories if needed
        os.makedirs(os.path.dirname(self.model_path), exist_ok=True)
        if not os.path.exists(self.model_path):
            logger.info("Fuel model joblib file not found. Training synthetic model...")
            
            # Generate synthetic data
            np.random.seed(101)
            num_samples = 500
            
            distance = np.random.uniform(5.0, 800.0, num_samples)
            load_weight = np.random.uniform(0.0, 8000.0, num_samples)
            average_speed = np.random.uniform(30.0, 110.0, num_samples)
            vehicle_type_code = np.random.choice([0, 1, 2, 3], size=num_samples)
            
            # Define consumption relationship (Liters / 100km):
            # Sedan: 7L, Van: 11L, Bus: 20L, Truck: 30L base rate
            base_rates = np.array([7.0, 11.0, 20.0, 30.0])
            rates = base_rates[vehicle_type_code]
            
            # Add weight penalty (+0.8L per 1000kg)
            rates += (load_weight / 1000.0) * 0.8
            
            # Add speed variance (drag increases above 80 km/h or runs inefficiently below 50 km/h)
            speed_deviation = np.maximum(0.0, average_speed - 80.0) * 0.12 + np.maximum(0.0, 50.0 - average_speed) * 0.08
            rates += speed_deviation
            
            # Total consumption = distance * rates / 100
            fuel_consumed = (distance * rates) / 100.0
            
            X = pd.DataFrame({
                "distance": distance,
                "load_weight": load_weight,
                "average_speed": average_speed,
                "vehicle_type_code": vehicle_type_code
            })
            
            model = LinearRegression()
            model.fit(X, fuel_consumed)
            
            joblib.dump(model, self.model_path)
            logger.info(f"Fuel dummy model trained and saved to {self.model_path}")

    def predict_fuel(self, request: FuelRequest) -> FuelResponse:
        logger.info(
            f"Predicting fuel for distance: {request.distance} km, vehicle: '{request.vehicle_type}', "
            f"avg speed: {request.average_speed} km/h, cargo weight: {request.load_weight} kg"
        )
        
        vehicle_code = self._encode_vehicle_type(request.vehicle_type)
        
        input_data = pd.DataFrame([{
            "distance": request.distance,
            "load_weight": request.load_weight,
            "average_speed": request.average_speed,
            "vehicle_type_code": float(vehicle_code)
        }])
        
        predicted = float(self.model.predict(input_data)[0])
        
        # Ensure predicted value is non-negative and realistic
        predicted = max(0.5, predicted)
        
        return FuelResponse(
            predicted_fuel_consumption=round(predicted, 2)
        )
