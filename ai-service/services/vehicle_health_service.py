from schemas.vehicle_health import VehicleHealthRequest, VehicleHealthResponse
from utils.logging_config import logger

class VehicleHealthService:
    def calculate_health_score(self, request: VehicleHealthRequest) -> VehicleHealthResponse:
        logger.info(
            f"Evaluating vehicle health: Temp={request.engine_temperature} C, Mileage={request.mileage} km, "
            f"Oil={request.oil_level}%, Battery={request.battery_voltage} V"
        )
        
        # Base health score starts at 100
        score = 100.0
        
        # 1. Engine Temperature Penalty (Optimal range: 80 C to 98 C)
        temp = request.engine_temperature
        if temp > 98.0:
            overheat_diff = temp - 98.0
            score -= overheat_diff * 1.8  # Strong penalty for overheating
        elif temp < 70.0:
            underheat_diff = 70.0 - temp
            score -= underheat_diff * 0.5  # Soft penalty for running cold
            
        # 2. Oil Level Penalty (Optimal range: 45% to 100%)
        oil = request.oil_level
        if oil < 45.0:
            oil_shortage = 45.0 - oil
            score -= oil_shortage * 1.2
            
        # 3. Battery Voltage Penalty (Optimal range: 12.2V to 14.6V)
        voltage = request.battery_voltage
        if voltage < 12.2:
            undervolt = 12.2 - voltage
            score -= undervolt * 20.0  # High penalty for weak battery
        elif voltage > 14.6:
            overvolt = voltage - 14.6
            score -= overvolt * 10.0  # Penalty for overcharging
            
        # 4. Mileage wear penalty
        # Soft aging penalty: 1 point lost per 25,000 km, capped at 15 points
        mileage_penalty = min(15.0, request.mileage / 25000.0)
        score -= mileage_penalty
        
        # Ensure score stays bounded between 0 and 100
        score = max(0.0, min(100.0, score))
        
        # Classify health status
        if score >= 85.0:
            status = "Excellent"
        elif score >= 70.0:
            status = "Good"
        elif score >= 50.0:
            status = "Fair"
        else:
            status = "Critical"
            
        return VehicleHealthResponse(
            health_score=round(score, 2),
            health_status=status
        )
