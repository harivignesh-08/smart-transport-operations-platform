from datetime import datetime
from pydantic import Field
from schemas.base import CamelModel

class ETARequest(CamelModel):
    current_latitude: float = Field(..., description="Latitude of the vehicle's current location", ge=-90.0, le=90.0)
    current_longitude: float = Field(..., description="Longitude of the vehicle's current location", ge=-180.0, le=180.0)
    destination_latitude: float = Field(..., description="Latitude of the vehicle's destination", ge=-90.0, le=90.0)
    destination_longitude: float = Field(..., description="Longitude of the vehicle's destination", ge=-180.0, le=180.0)
    current_speed: float = Field(..., description="Current speed of the vehicle in km/h", ge=0.0)

class ETAResponse(CamelModel):
    estimated_arrival_time: datetime = Field(..., description="Estimated arrival time at destination")
    remaining_distance: float = Field(..., description="Remaining distance to destination in kilometers")
