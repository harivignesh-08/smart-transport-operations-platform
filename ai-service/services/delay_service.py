from schemas.delay import DelayRequest, DelayResponse
from utils.logging_config import logger

class DelayPredictionService:
    def predict_delay(self, request: DelayRequest) -> DelayResponse:
        logger.info(
            f"Predicting delay. Speed: {request.current_speed} km/h, remaining distance: {request.remaining_distance} km, "
            f"traffic: '{request.traffic_level}', weather: '{request.weather}'"
        )
        
        # Calculate base travel time in minutes
        speed = request.current_speed if request.current_speed > 5.0 else 40.0
        base_time_mins = (request.remaining_distance / speed) * 60.0
        
        delay_minutes = 0.0
        
        # Traffic factors
        traffic = request.traffic_level.lower()
        if "heavy" in traffic:
            delay_minutes += (base_time_mins * 0.40) + 15.0
        elif "mod" in traffic or "medium" in traffic:
            delay_minutes += (base_time_mins * 0.15) + 5.0
        elif "low" in traffic:
            delay_minutes += 0.0
            
        # Weather factors
        weather = request.weather.lower()
        if "rain" in weather:
            delay_minutes += (base_time_mins * 0.10) + 3.0
        elif "snow" in weather:
            delay_minutes += (base_time_mins * 0.35) + 12.0
        elif "fog" in weather:
            delay_minutes += (base_time_mins * 0.25) + 8.0
            
        # Classify arrival status based on delay duration
        if delay_minutes > 15.0:
            arrival_status = "Delayed"
        elif delay_minutes > 5.0:
            # Let's say 5 to 15 mins is a minor delay
            arrival_status = "Delayed"
        elif delay_minutes <= 1.0 and request.current_speed > 70.0:
            arrival_status = "On Time"
        else:
            arrival_status = "On Time"
            
        return DelayResponse(
            delay_minutes=round(delay_minutes, 2),
            arrival_status=arrival_status
        )
