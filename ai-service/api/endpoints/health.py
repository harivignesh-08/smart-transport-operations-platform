from fastapi import APIRouter
from schemas.health import HealthResponse

router = APIRouter()

@router.get("/health", response_model=HealthResponse, summary="Service Health Status", description="Get health status. Utilized by orchestration engines or OpenFeign client checks.")
def check_health():
    return HealthResponse(status="UP", service="AI Service")
