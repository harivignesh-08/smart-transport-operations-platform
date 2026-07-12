from pydantic import Field
from schemas.base import CamelModel

class VehicleHealthRequest(CamelModel):
    engine_temperature: float = Field(..., description="Engine coolant temperature in Celsius")
    mileage: float = Field(..., description="Total vehicle mileage in kilometers", ge=0.0)
    oil_level: float = Field(..., description="Oil level as a percentage (0 to 100)", ge=0.0, le=100.0)
    battery_voltage: float = Field(..., description="Battery electrical voltage (typically 12.0 to 14.8 Volts)", ge=0.0)

class VehicleHealthResponse(CamelModel):
    health_score: float = Field(..., description="Overall computed vehicle health score (0-100)", ge=0.0, le=100.0)
    health_status: str = Field(..., description="Health category classification ('Excellent', 'Good', 'Fair', 'Critical')")
