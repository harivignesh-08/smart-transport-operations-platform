import logging
import sys
from config.settings import settings

def setup_logging():
    log_format = "%(asctime)s - %(levelname)s - [%(name)s] - %(message)s"
    logging.basicConfig(
        level=getattr(logging, settings.LOG_LEVEL.upper(), logging.INFO),
        format=log_format,
        handlers=[
            logging.StreamHandler(sys.stdout)
        ]
    )
    # Configure fastapi and uvicorn logging to use setup configuration
    for logger_name in ("uvicorn", "uvicorn.error", "uvicorn.access", "fastapi"):
        l = logging.getLogger(logger_name)
        l.handlers = []
        l.propagate = True

logger = logging.getLogger("TransitOpsAI")
