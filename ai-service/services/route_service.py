import hashlib
from schemas.route import RouteRequest, RouteResponse
from utils.logging_config import logger

class RouteOptimizationService:
    def optimize_route(self, request: RouteRequest) -> RouteResponse:
        logger.info(
            f"Optimizing route from '{request.source}' to '{request.destination}' "
            f"for vehicle type '{request.vehicle_type}'"
        )
        
        # Produce a consistent value from the source and destination for testing/mock consistency
        hash_input = f"{request.source}-{request.destination}-{request.vehicle_type}".encode("utf-8")
        val = int(hashlib.md5(hash_input).hexdigest(), 16)
        
        # Calculate mock distance (between 15 and 200 km)
        estimated_distance = 15.0 + (val % 185)
        
        # Speed varies depending on the vehicle type
        speed_kph = 50.0
        v_type = request.vehicle_type.lower()
        if "truck" in v_type:
            speed_kph = 45.0
        elif "car" in v_type:
            speed_kph = 65.0
        elif "van" in v_type:
            speed_kph = 55.0
            
        # Add a traffic factor
        traffic_multiplier = 1.0 + ((val % 5) * 0.05) # 1.0 to 1.2x
        estimated_time_hours = (estimated_distance / speed_kph) * traffic_multiplier
        estimated_time_minutes = estimated_time_hours * 60.0
        
        # Generate generic but structured waypoint sequence
        optimized_route = [
            request.source,
            f"Waystation-{1 + (val % 3)}",
            f"State Highway Link-{(val % 100) + 1}",
            f"City Gate Control Point",
            request.destination
        ]
        
        return RouteResponse(
            optimized_route=optimized_route,
            estimated_distance=round(estimated_distance, 2),
            estimated_time=round(estimated_time_minutes, 1)
        )
