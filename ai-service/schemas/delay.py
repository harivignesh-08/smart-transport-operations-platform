from pydantic import Field
from schemas.base import CamelModel

class DelayRequest(CamelModel):
    current_speed: float = Field(..., description="Current speed of the vehicle in km/h", ge=0.0)
    remaining_distance: float = Field(..., description="Remaining distance in kilometers", ge=0.0)
    traffic_level: str = Field(..., description="Traffic density descriptor ('Low', 'Moderate', 'Heavy')")
    weather: str = Field(..., description="Weather descriptor ('Clear', 'Rainy', 'Snowy', 'Foggy')")

class DelayResponse(CamelModel):
    delay_minutes: float = Field(..., description="Predicted additional delay in minutes", ge=0.0)
    arrival_status: str = Field(..., description="Predicted arrival status ('On Time', 'Delayed', 'Early')")
