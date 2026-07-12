import math
from datetime import datetime, timedelta, timezone
from schemas.eta import ETARequest, ETAResponse
from utils.logging_config import logger

class ETAService:
    @staticmethod
    def calculate_haversine_distance(lat1: float, lon1: float, lat2: float, lon2: float) -> float:
        # Earth radius in kilometers
        R = 6371.0
        
        d_lat = math.radians(lat2 - lat1)
        d_lon = math.radians(lon2 - lon1)
        
        a = (math.sin(d_lat / 2) ** 2 + 
             math.cos(math.radians(lat1)) * math.cos(math.radians(lat2)) * math.sin(d_lon / 2) ** 2)
        c = 2 * math.atan2(math.sqrt(a), math.sqrt(1 - a))
        
        return R * c

    def predict_eta(self, request: ETARequest) -> ETAResponse:
        logger.info(
            f"Calculating ETA between coordinates: ({request.current_latitude}, {request.current_longitude}) "
            f"and ({request.destination_latitude}, {request.destination_longitude}) at speed {request.current_speed} km/h"
        )
        
        remaining_distance = self.calculate_haversine_distance(
            request.current_latitude, request.current_longitude,
            request.destination_latitude, request.destination_longitude
        )
        
        # Fallback speed if vehicle is stationary or invalid speed given
        speed = request.current_speed if request.current_speed > 2.0 else 30.0
        
        # Calculate time in hours
        travel_time_hours = remaining_distance / speed
        
        estimated_arrival = datetime.now(timezone.utc) + timedelta(hours=travel_time_hours)
        
        return ETAResponse(
            estimated_arrival_time=estimated_arrival,
            remaining_distance=round(remaining_distance, 2)
        )
