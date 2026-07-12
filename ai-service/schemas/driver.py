from pydantic import Field
from schemas.base import CamelModel

class DriverRequest(CamelModel):
    driver_id: str = Field(..., description="Unique driver identifier")
    average_speed: float = Field(..., description="Average speed in km/h recorded over the tracking period", ge=0.0)
    harsh_braking: int = Field(..., description="Count of harsh braking events", ge=0)
    harsh_acceleration: int = Field(..., description="Count of harsh acceleration events", ge=0)
    overspeed_count: int = Field(..., description="Number of times the speed limit was exceeded", ge=0)

class DriverResponse(CamelModel):
    driver_score: float = Field(..., description="Computed safety score (0-100), where 100 is perfect and safe", ge=0.0, le=100.0)
    risk_level: str = Field(..., description="Driving risk categorization (e.g. 'Low', 'Medium', 'High')")
    recommendation: str = Field(..., description="Coaching recommendation or tips based on behavior metrics")
