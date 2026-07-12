from fastapi import APIRouter, Depends, HTTPException
from schemas.route import RouteRequest, RouteResponse
from services.route_service import RouteOptimizationService

router = APIRouter()

@router.post("/route", response_model=RouteResponse, summary="Route Optimization", description="Generate optimized path checkpoints, distance, and estimated travel time.")
def predict_route(request: RouteRequest, service: RouteOptimizationService = Depends()):
    try:
        return service.optimize_route(request)
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Internal routing logic error: {str(e)}")
