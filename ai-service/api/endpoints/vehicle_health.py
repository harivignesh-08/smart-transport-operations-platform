from fastapi import APIRouter, Depends, HTTPException
from schemas.vehicle_health import VehicleHealthRequest, VehicleHealthResponse
from services.vehicle_health_service import VehicleHealthService

router = APIRouter()

@router.post("/vehicle-health", response_model=VehicleHealthResponse, summary="Vehicle Health Score", description="Determine the overall vehicle health score and status from engine temperature, mileage, oil, and battery.")
def predict_vehicle_health(request: VehicleHealthRequest, service: VehicleHealthService = Depends()):
    try:
        return service.calculate_health_score(request)
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Internal vehicle health assessment error: {str(e)}")
