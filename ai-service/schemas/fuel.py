from pydantic import Field
from schemas.base import CamelModel

class FuelRequest(CamelModel):
    distance: float = Field(..., description="Planned travel distance in kilometers", ge=0.0)
    vehicle_type: str = Field(..., description="Type of vehicle (e.g. 'Truck', 'Bus', 'Van', 'Sedan')")
    average_speed: float = Field(..., description="Expected average speed of the vehicle in km/h", ge=0.0)
    load_weight: float = Field(..., description="Weight of the load/cargo in kg", ge=0.0)

class FuelResponse(CamelModel):
    predicted_fuel_consumption: float = Field(..., description="Predicted fuel consumption in liters", ge=0.0)
