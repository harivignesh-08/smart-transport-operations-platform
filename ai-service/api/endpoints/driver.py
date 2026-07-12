from fastapi import APIRouter, Depends, HTTPException
from schemas.driver import DriverRequest, DriverResponse
from services.driver_service import DriverAnalysisService

router = APIRouter()

@router.post("/driver", response_model=DriverResponse, summary="Driver Behaviour Analysis", description="Evaluate a driver's score and safety risk level using acceleration, braking, and speeding metrics.")
def predict_driver(request: DriverRequest, service: DriverAnalysisService = Depends()):
    try:
        return service.analyze_driver_behavior(request)
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Internal driver behavior analysis logic error: {str(e)}")
