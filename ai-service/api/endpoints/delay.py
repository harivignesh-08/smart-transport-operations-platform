from fastapi import APIRouter, Depends, HTTPException
from schemas.delay import DelayRequest, DelayResponse
from services.delay_service import DelayPredictionService

router = APIRouter()

@router.post("/delay", response_model=DelayResponse, summary="Delay Prediction", description="Predict delay minutes and arrival status using traffic level, speed, and weather condition.")
def predict_delay(request: DelayRequest, service: DelayPredictionService = Depends()):
    try:
        return service.predict_delay(request)
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Internal delay prediction logic error: {str(e)}")
