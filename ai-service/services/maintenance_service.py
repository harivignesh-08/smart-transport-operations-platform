import os
import joblib
import pandas as pd
import numpy as np
from datetime import date
from sklearn.linear_model import LogisticRegression
from schemas.maintenance import MaintenanceRequest, MaintenanceResponse
from utils.logging_config import logger

class MaintenancePredictionService:
    def __init__(self):
        # Build path relative to project root
        self.model_path = os.path.join(
            os.path.dirname(os.path.dirname(os.path.abspath(__file__))),
            "trained_models",
            "maintenance_model.joblib"
        )
        self.ensure_model_trained()
        self.model = joblib.load(self.model_path)

    def ensure_model_trained(self):
        # Create parent directory structure if missing
        os.makedirs(os.path.dirname(self.model_path), exist_ok=True)
        if not os.path.exists(self.model_path):
            logger.info("Maintenance model joblib file not found. Training synthetic model...")
            
            # Generate dummy dataset
            np.random.seed(42)
            num_samples = 500
            
            total_distance = np.random.uniform(1000.0, 350000.0, num_samples)
            engine_hours = total_distance / np.random.uniform(40.0, 70.0, num_samples)
            fuel_consumption = total_distance * np.random.uniform(0.08, 0.16, num_samples)
            days_since_last_service = np.random.uniform(5.0, 365.0, num_samples)
            
            # Formulate logic: higher values in distance/hours/days lead to higher probability
            risk = (
                (total_distance / 250000.0) * 0.35 +
                (engine_hours / 6000.0) * 0.25 +
                (days_since_last_service / 200.0) * 0.40
            )
            # Add normal distribution noise
            risk += np.random.normal(0, 0.08, num_samples)
            
            # Determine maintenance label: 1 if maintenance needed, 0 otherwise
            y = (risk > 0.6).astype(int)
            
            X = pd.DataFrame({
                "total_distance": total_distance,
                "engine_hours": engine_hours,
                "fuel_consumption": fuel_consumption,
                "days_since_last_service": days_since_last_service
            })
            
            model = LogisticRegression(max_iter=1000)
            model.fit(X, y)
            
            joblib.dump(model, self.model_path)
            logger.info(f"Maintenance dummy model trained and saved to {self.model_path}")

    def predict_maintenance(self, request: MaintenanceRequest) -> MaintenanceResponse:
        logger.info(f"Evaluating predictive maintenance for vehicle ID: '{request.vehicle_id}'")
        
        # Calculate days since last service
        today = date.today()
        days_since_service = (today - request.last_service_date).days
        if days_since_service < 0:
            days_since_service = 0
            
        # Format input DataFrame for scikit-learn prediction
        input_data = pd.DataFrame([{
            "total_distance": request.total_distance,
            "engine_hours": request.engine_hours,
            "fuel_consumption": request.fuel_consumption,
            "days_since_last_service": float(days_since_service)
        }])
        
        # Get probability prediction
        probs = self.model.predict_proba(input_data)[0]
        maintenance_score = float(probs[1] * 100.0)  # Convert to percentage
        
        # Threshold at 50% probability
        maintenance_required = bool(maintenance_score >= 50.0)
        
        # Generate dynamic recommendations
        suggested_maintenance = []
        if request.total_distance > 150000.0:
            suggested_maintenance.append("Inspect transmission, drive belts, and wheel alignments.")
        if days_since_service > 180:
            suggested_maintenance.append("Perform routine engine oil and filter change.")
        if request.engine_hours > 4000.0:
            suggested_maintenance.append("Conduct comprehensive electrical alternator and starter check.")
            
        if maintenance_required and not suggested_maintenance:
            suggested_maintenance.append("Standard diagnostic testing of engine control unit (ECU).")
            
        if not suggested_maintenance:
            suggested_maintenance.append("No immediate maintenance actions recommended.")
            
        return MaintenanceResponse(
            maintenance_required=maintenance_required,
            maintenance_score=round(maintenance_score, 2),
            suggested_maintenance=suggested_maintenance
        )
