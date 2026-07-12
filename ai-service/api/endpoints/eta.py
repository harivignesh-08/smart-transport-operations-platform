from fastapi import APIRouter, Depends, HTTPException
from schemas.eta import ETARequest, ETAResponse
from services.eta_service import ETAService

router = APIRouter()

@router.post("/eta", response_model=ETAResponse, summary="Predict ETA", description="Calculate distance and ETA based on current coordinates, destination coordinates, and speed.")
def predict_eta(request: ETARequest, service: ETAService = Depends()):
    try:
        return service.predict_eta(request)
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Internal model processing error: {str(e)}")
