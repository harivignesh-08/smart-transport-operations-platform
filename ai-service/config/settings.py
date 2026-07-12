import os

class Settings:
    APP_NAME: str = "TransitOps AI Service"
    VERSION: str = "1.0.0"
    ENV: str = os.getenv("ENV", "development")
    LOG_LEVEL: str = os.getenv("LOG_LEVEL", "INFO")
    HOST: str = os.getenv("HOST", "0.0.0.0")
    PORT: int = int(os.getenv("PORT", "8084"))

settings = Settings()
