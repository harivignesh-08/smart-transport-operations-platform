from fastapi import APIRouter, Depends, HTTPException
from schemas.maintenance import MaintenanceRequest, MaintenanceResponse
from services.maintenance_service import MaintenancePredictionService

router = APIRouter()

@router.post("/maintenance", response_model=MaintenanceResponse, summary="Predictive Maintenance", description="Calculate failure probability score and provide recommendations using engine history.")
def predict_maintenance(request: MaintenanceRequest, service: MaintenancePredictionService = Depends()):
    try:
        return service.predict_maintenance(request)
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Internal maintenance ML classifier error: {str(e)}")
