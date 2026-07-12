import time
from fastapi import FastAPI, Request
from fastapi.responses import HTMLResponse
from fastapi.middleware.cors import CORSMiddleware
from config.settings import settings
from utils.logging_config import setup_logging, logger
from api.endpoints.eta import router as eta_router
from api.endpoints.route import router as route_router
from api.endpoints.maintenance import router as maintenance_router
from api.endpoints.fuel import router as fuel_router
from api.endpoints.driver import router as driver_router
from api.endpoints.delay import router as delay_router
from api.endpoints.vehicle_health import router as vehicle_health_router
from api.endpoints.health import router as health_router

# Set up logging configuration
setup_logging()
logger.info("Initializing TransitOps AI Microservice...")

# Initialize FastAPI App
app = FastAPI(
    title=settings.APP_NAME,
    description="Python FastAPI Prediction Engine for the TransitOps AI Smart Fleet & Transport Management System.",
    version=settings.VERSION,
    docs_url="/docs",
    redoc_url="/redoc"
)

# Configure CORS Middleware for Spring Boot & Frontend access
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Middleware for request tracing, logging, and response time metrics
@app.middleware("http")
async def log_requests_and_latency(request: Request, call_next):
    start_time = time.time()
    path = request.url.path
    method = request.method
    logger.info(f"Incoming: {method} {path}")
    
    try:
        response = await call_next(request)
        process_time = (time.time() - start_time) * 1000
        response.headers["X-Process-Time-Ms"] = f"{process_time:.2f}"
        logger.info(f"Completed: {method} {path} - Status: {response.status_code} - Latency: {process_time:.2f}ms")
        return response
    except Exception as e:
        process_time = (time.time() - start_time) * 1000
        logger.error(f"Failed: {method} {path} - Error: {str(e)} - Latency: {process_time:.2f}ms", exc_info=True)
        raise e

# Include Prediction Routers with the '/predict' prefix
app.include_router(eta_router, prefix="/predict", tags=["ETA Prediction"])
app.include_router(route_router, prefix="/predict", tags=["Route Optimization"])
app.include_router(maintenance_router, prefix="/predict", tags=["Predictive Maintenance"])
app.include_router(fuel_router, prefix="/predict", tags=["Fuel Consumption Prediction"])
app.include_router(driver_router, prefix="/predict", tags=["Driver Behaviour Analysis"])
app.include_router(delay_router, prefix="/predict", tags=["Delay Prediction"])
app.include_router(vehicle_health_router, prefix="/predict", tags=["Vehicle Health Score"])

# Include Health Router directly
app.include_router(health_router, tags=["System Health"])

# HTML Dashboard Landing Page at Root
@app.get("/", response_class=HTMLResponse, include_in_schema=False)
def read_root():
    html_content = """
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>TransitOps AI - Microservice Dashboard</title>
        <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;600;800&display=swap" rel="stylesheet">
        <style>
            :root {
                --bg-color: #0b0f19;
                --card-bg: rgba(22, 30, 49, 0.65);
                --border-color: rgba(255, 255, 255, 0.08);
                --accent-color: #38bdf8;
                --success-color: #34d399;
                --text-main: #94a3b8;
                --text-bold: #f8fafc;
            }
            * { box-sizing: border-box; margin: 0; padding: 0; }
            body {
                font-family: 'Outfit', sans-serif;
                background-color: var(--bg-color);
                color: var(--text-main);
                min-height: 100vh;
                display: flex;
                flex-direction: column;
                align-items: center;
                justify-content: center;
                overflow-x: hidden;
                padding: 3rem 1.5rem;
                position: relative;
            }
            body::before {
                content: '';
                position: absolute;
                top: 0; left: 0; right: 0; bottom: 0;
                background-image: radial-gradient(circle at 15% 20%, rgba(56, 189, 248, 0.1) 0%, transparent 45%),
                                  radial-gradient(circle at 85% 80%, rgba(52, 211, 153, 0.08) 0%, transparent 45%);
                z-index: -1;
            }
            .container {
                max-width: 1200px;
                width: 100%;
                text-align: center;
                z-index: 10;
            }
            header {
                margin-bottom: 2.5rem;
                animation: fadeInDown 0.8s ease-out;
            }
            h1 {
                font-size: 3.5rem;
                font-weight: 800;
                color: var(--text-bold);
                margin-bottom: 0.5rem;
                background: linear-gradient(135deg, #38bdf8 0%, #a855f7 100%);
                -webkit-background-clip: text;
                -webkit-text-fill-color: transparent;
            }
            p.subtitle {
                font-size: 1.25rem;
                font-weight: 300;
                letter-spacing: 1px;
                color: #64748b;
            }
            .status-badge {
                display: inline-flex;
                align-items: center;
                gap: 0.6rem;
                background-color: rgba(52, 211, 153, 0.1);
                border: 1px solid rgba(52, 211, 153, 0.3);
                color: var(--success-color);
                padding: 0.6rem 1.6rem;
                border-radius: 50px;
                font-weight: 600;
                margin-top: 1.5rem;
                font-size: 0.95rem;
                box-shadow: 0 0 20px rgba(52, 211, 153, 0.15);
                animation: pulse 2s infinite alternate;
            }
            .status-dot {
                width: 10px;
                height: 10px;
                background-color: var(--success-color);
                border-radius: 50%;
                box-shadow: 0 0 10px var(--success-color);
            }
            .actions {
                display: flex;
                gap: 1.2rem;
                justify-content: center;
                margin-bottom: 4rem;
                animation: fadeInUp 0.8s ease-out 0.2s both;
            }
            .btn {
                text-decoration: none;
                padding: 0.85rem 2.2rem;
                font-weight: 600;
                border-radius: 8px;
                transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
                font-size: 1rem;
            }
            .btn-primary {
                background-color: var(--accent-color);
                color: #0b0f19;
                box-shadow: 0 4px 14px rgba(56, 189, 248, 0.35);
            }
            .btn-primary:hover {
                transform: translateY(-2px);
                box-shadow: 0 6px 20px rgba(56, 189, 248, 0.5);
                background-color: #7dd3fc;
            }
            .btn-secondary {
                background-color: transparent;
                color: var(--text-bold);
                border: 1px solid var(--border-color);
            }
            .btn-secondary:hover {
                transform: translateY(-2px);
                background-color: rgba(255, 255, 255, 0.05);
                border-color: #64748b;
            }
            .grid {
                display: grid;
                grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
                gap: 1.5rem;
                width: 100%;
                animation: fadeInUp 0.8s ease-out 0.4s both;
            }
            .card {
                background: var(--card-bg);
                border: 1px solid var(--border-color);
                border-radius: 12px;
                padding: 1.8rem;
                text-align: left;
                backdrop-filter: blur(12px);
                transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
                position: relative;
                overflow: hidden;
            }
            .card::before {
                content: '';
                position: absolute;
                top: 0; left: 0; width: 4px; height: 100%;
                background: var(--accent-color);
                opacity: 0;
                transition: opacity 0.3s;
            }
            .card:hover {
                transform: translateY(-5px) scale(1.02);
                border-color: rgba(56, 189, 248, 0.3);
                box-shadow: 0 10px 25px rgba(0, 0, 0, 0.4);
            }
            .card:hover::before {
                opacity: 1;
            }
            .card-header {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 1rem;
            }
            .card-title {
                font-size: 1.25rem;
                font-weight: 600;
                color: var(--text-bold);
            }
            .badge {
                font-size: 0.75rem;
                padding: 0.25rem 0.6rem;
                border-radius: 4px;
                font-weight: 700;
                text-transform: uppercase;
            }
            .badge-post { background-color: rgba(244, 63, 94, 0.15); color: #f43f5e; border: 1px solid rgba(244, 63, 94, 0.3); }
            .badge-get { background-color: rgba(52, 211, 153, 0.15); color: var(--success-color); border: 1px solid rgba(52, 211, 153, 0.3); }
            .card-path {
                font-family: monospace;
                font-size: 0.9rem;
                color: var(--accent-color);
                background-color: rgba(56, 189, 248, 0.08);
                padding: 0.25rem 0.6rem;
                border-radius: 4px;
                display: inline-block;
                margin-bottom: 1rem;
            }
            .card-desc {
                font-size: 0.92rem;
                line-height: 1.6;
                color: var(--text-main);
            }
            @keyframes fadeInDown {
                from { opacity: 0; transform: translateY(-20px); }
                to { opacity: 1; transform: translateY(0); }
            }
            @keyframes fadeInUp {
                from { opacity: 0; transform: translateY(20px); }
                to { opacity: 1; transform: translateY(0); }
            }
            @keyframes pulse {
                from { box-shadow: 0 0 10px rgba(52, 211, 153, 0.1); }
                to { box-shadow: 0 0 25px rgba(52, 211, 153, 0.4); }
            }
            footer {
                margin-top: 5rem;
                color: #64748b;
                font-size: 0.85rem;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <header>
                <h1>TransitOps AI</h1>
                <p class="subtitle">Smart Fleet & Transport Management Prediction Engine</p>
                <div class="status-badge">
                    <span class="status-dot"></span>
                    <span>SYSTEM ACTIVE & ONLINE</span>
                </div>
            </header>

            <div class="actions">
                <a href="/docs" class="btn btn-primary">Open Swagger UI Docs</a>
                <a href="/redoc" class="btn btn-secondary">Open ReDoc View</a>
            </div>

            <div class="grid">
                <div class="card">
                    <div class="card-header">
                        <span class="card-title">ETA Prediction</span>
                        <span class="badge badge-post">POST</span>
                    </div>
                    <div class="card-path">/predict/eta</div>
                    <div class="card-desc">Predict estimated arrival times and calculate distances using coordinates and velocity.</div>
                </div>
                
                <div class="card">
                    <div class="card-header">
                        <span class="card-title">Route Optimization</span>
                        <span class="badge badge-post">POST</span>
                    </div>
                    <div class="card-path">/predict/route</div>
                    <div class="card-desc">Simulate optimized routes between hubs and calculate distance and estimated transit hours.</div>
                </div>

                <div class="card">
                    <div class="card-header">
                        <span class="card-title">Predictive Maintenance</span>
                        <span class="badge badge-post">POST</span>
                    </div>
                    <div class="card-path">/predict/maintenance</div>
                    <div class="card-desc">Analyze vehicle telemetry using scikit-learn models to calculate failure risk and suggestions.</div>
                </div>

                <div class="card">
                    <div class="card-header">
                        <span class="card-title">Fuel Consumption</span>
                        <span class="badge badge-post">POST</span>
                    </div>
                    <div class="card-path">/predict/fuel</div>
                    <div class="card-desc">Predict total fuel usage based on cargo weights, distance, vehicle profiles, and average speeds.</div>
                </div>

                <div class="card">
                    <div class="card-header">
                        <span class="card-title">Driver Behaviour</span>
                        <span class="badge badge-post">POST</span>
                    </div>
                    <div class="card-path">/predict/driver</div>
                    <div class="card-desc">Evaluate driver performance indexes and safe driving metrics to produce coaching plans.</div>
                </div>

                <div class="card">
                    <div class="card-header">
                        <span class="card-title">Delay Prediction</span>
                        <span class="badge badge-post">POST</span>
                    </div>
                    <div class="card-path">/predict/delay</div>
                    <div class="card-desc">Estimate transit delay minutes caused by local weather conditions and traffic density metrics.</div>
                </div>

                <div class="card">
                    <div class="card-header">
                        <span class="card-title">Vehicle Health Score</span>
                        <span class="badge badge-post">POST</span>
                    </div>
                    <div class="card-path">/predict/vehicle-health</div>
                    <div class="card-desc">Synthesize mechanical indicators (coolant temperature, oil levels, voltage) into an overall score.</div>
                </div>

                <div class="card">
                    <div class="card-header">
                        <span class="card-title">System Health Check</span>
                        <span class="badge badge-get">GET</span>
                    </div>
                    <div class="card-path">/health</div>
                    <div class="card-desc">Simple status probe used by Spring Boot OpenFeign for service availability discovery.</div>
                </div>
            </div>

            <footer>
                TransitOps AI &bull; Version 1.0.0 &bull; Powered by FastAPI & Scikit-learn
            </footer>
        </div>
    </body>
    </html>
    """
    return HTMLResponse(content=html_content)


if __name__ == "__main__":
    import uvicorn
    uvicorn.run("app:app", host=settings.HOST, port=settings.PORT, reload=True)

