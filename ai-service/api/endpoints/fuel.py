from fastapi import APIRouter, Depends, HTTPException
from schemas.fuel import FuelRequest, FuelResponse
from services.fuel_service import FuelPredictionService

router = APIRouter()

@router.post("/fuel", response_model=FuelResponse, summary="Fuel Consumption Prediction", description="Predict total fuel consumption in liters based on load weight, distance, speed, and vehicle type.")
def predict_fuel(request: FuelRequest, service: FuelPredictionService = Depends()):
    try:
        return service.predict_fuel(request)
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Internal fuel ML model error: {str(e)}")
