from schemas.driver import DriverRequest, DriverResponse
from utils.logging_config import logger

class DriverAnalysisService:
    def analyze_driver_behavior(self, request: DriverRequest) -> DriverResponse:
        logger.info(f"Analyzing driver behavior for driver ID: '{request.driver_id}'")
        
        # Base safety score starts at 100
        score = 100.0
        
        # Deduct penalties for unsafe driving practices
        score -= request.harsh_braking * 6.0
        score -= request.harsh_acceleration * 4.0
        score -= request.overspeed_count * 8.0
        
        # High average speed penalty (e.g. above 85 km/h for fleet safety)
        if request.average_speed > 85.0:
            speed_excess = request.average_speed - 85.0
            score -= speed_excess * 0.5
            
        # Ensure score stays bounded between 0 and 100
        score = max(0.0, min(100.0, score))
        
        # Evaluate risk level based on score ranges
        if score >= 80.0:
            risk_level = "Low"
            recommendation = "Excellent performance. Maintain current safe driving practices."
        elif score >= 50.0:
            risk_level = "Medium"
            
            # Formulate targeted coaching tip
            issues = []
            if request.harsh_braking > 3:
                issues.append("reduce sudden braking events")
            if request.harsh_acceleration > 3:
                issues.append("smooth out accelerations")
            if request.overspeed_count > 1:
                issues.append("comply with posted speed limits")
                
            tip_detail = " and ".join(issues) if issues else "exercise general caution"
            recommendation = f"Adequate behavior, but recommended to {tip_detail} to improve efficiency and safety."
        else:
            risk_level = "High"
            recommendation = (
                "Critical Risk Warning: High frequency of unsafe driving behaviors. "
                "Driver should be scheduled for defensive driving training immediately."
            )
            
        return DriverResponse(
            driver_score=round(score, 2),
            risk_level=risk_level,
            recommendation=recommendation
        )
