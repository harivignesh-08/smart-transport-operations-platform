from typing import List
from pydantic import Field
from schemas.base import CamelModel

class RouteRequest(CamelModel):
    source: str = Field(..., description="Source location name or coordinates (e.g. 'Warehouse A' or '12.97,77.59')")
    destination: str = Field(..., description="Destination location name or coordinates (e.g. 'Hub B' or '13.08,80.27')")
    vehicle_type: str = Field(..., description="Type of vehicle (e.g. 'Heavy Truck', 'Electric Van', 'Bus')")

class RouteResponse(CamelModel):
    optimized_route: List[str] = Field(..., description="Waypoints representing the optimized route sequence")
    estimated_distance: float = Field(..., description="Estimated distance in kilometers")
    estimated_time: float = Field(..., description="Estimated time in minutes")
