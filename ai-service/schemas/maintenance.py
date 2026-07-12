from datetime import date
from typing import List
from pydantic import Field
from schemas.base import CamelModel

class MaintenanceRequest(CamelModel):
    vehicle_id: str = Field(..., description="Unique identifier of the vehicle")
    total_distance: float = Field(..., description="Total distance accumulated in kilometers", ge=0.0)
    engine_hours: float = Field(..., description="Total engine operational hours", ge=0.0)
    fuel_consumption: float = Field(..., description="Total fuel consumption in liters", ge=0.0)
    last_service_date: date = Field(..., description="Date of last professional service/maintenance")

class MaintenanceResponse(CamelModel):
    maintenance_required: bool = Field(..., description="True if maintenance check is recommended immediately")
    maintenance_score: float = Field(..., description="Maintenance urgency score (0-100), higher means higher risk of failure", ge=0.0, le=100.0)
    suggested_maintenance: List[str] = Field(..., description="Recommended list of actions/servicing items")
