from pydantic import Field
from schemas.base import CamelModel

class HealthResponse(CamelModel):
    status: str = Field(..., description="Current status of the microservice, usually 'UP'")
    service: str = Field(..., description="Name of this microservice, 'AI Service'")
