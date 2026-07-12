import React, { useState, useEffect } from 'react';
import { Compass, Navigation, Radio, MapPin, CheckCircle } from 'lucide-react';
import { api } from '../services/api';

export default function FleetTracking() {
  const [vehicles, setVehicles] = useState([]);
  const [selectedVehicle, setSelectedVehicle] = useState(null);
  const [telemetry, setTelemetry] = useState({});
  const [wsStatus, setWsStatus] = useState('CONNECTING');

  // SVG Map Path definitions representing routes
  const routePaths = {
    1: 'M 100 250 L 300 250 L 500 150 L 700 250 L 900 250',
    2: 'M 150 100 L 350 100 L 450 300 L 650 300 L 850 150',
    4: 'M 100 350 L 300 350 L 500 250 L 700 350 L 900 100'
  };

  useEffect(() => {
    async function loadVehicles() {
      try {
        const list = await api.vehicles.getAll();
        setVehicles(list.filter(v => v.status === 'ACTIVE'));
        if (list.length > 0) {
          setSelectedVehicle(list[0]);
        }
      } catch (err) {
        console.error('Error loading tracking vehicles:', err);
      }
    }
    loadVehicles();

    // Simulate WebSocket connection to API Gateway websocket endpoint: /ws/tracking
    const wsTimer = setTimeout(() => {
      setWsStatus('CONNECTED');
    }, 1500);

    return () => clearTimeout(wsTimer);
  }, []);

  // Telemetry loop: periodically animate vehicle positions along SVG paths
  useEffect(() => {
    if (vehicles.length === 0) return;

    const interval = setInterval(() => {
      const updatedTelemetry = {};
      const time = Date.now() / 8000; // Speed control

      vehicles.forEach(vehicle => {
        const id = vehicle.id;
        const path = routePaths[id] || routePaths[1];
        
        // Approximate animation coordinates using trigonometric paths corresponding to paths
        let progress = (time + (id * 0.2)) % 1.0;
        let x = 100 + progress * 800;
        let y = 250 + Math.sin(progress * Math.PI * 2) * 80;

        // Map specific routes coordinates
        if (id === 1) {
          y = 250 + (progress > 0.25 && progress < 0.75 ? (progress - 0.25) * -100 : 0);
        } else if (id === 2) {
          y = 100 + (progress > 0.25 ? 100 : 0) + (progress > 0.5 ? 50 : 0);
        } else if (id === 4) {
          y = 350 - progress * 200;
        }

        updatedTelemetry[id] = {
          x: Math.min(Math.max(x, 50), 950),
          y: Math.min(Math.max(y, 50), 450),
          speed: Math.floor(Math.sin(time + id) * 15 + 50), // Live fluctuating speed
          bearing: Math.floor((Math.sin(time * 2) * 45) + 90),
          lastUpdate: new Date().toLocaleTimeString()
        };
      });

      setTelemetry(updatedTelemetry);
    }, 200);

    return () => clearInterval(interval);
  }, [vehicles]);

  return (
    <div className="page-container">
      <div style={{ display: 'grid', gridTemplateColumns: '3fr 1fr', gap: '32px' }}>
        {/* Left Column: Live Map */}
        <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
          {/* Map Header with Connection Status */}
          <div className="glass-panel" style={{ padding: '16px 24px', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
              <Radio size={16} className={wsStatus === 'CONNECTED' ? 'trend-up' : 'trend-down'} style={{ animation: 'pulse 1.5s infinite' }} />
              <span style={{ fontSize: '14px', fontWeight: '600' }}>
                Gateway STOMP Socket status:{' '}
                <span style={{ color: wsStatus === 'CONNECTED' ? 'var(--accent-emerald)' : 'var(--accent-amber)' }}>
                  {wsStatus}
                </span>
              </span>
            </div>
            <div style={{ fontSize: '12px', color: 'var(--text-secondary)' }}>
              Routing live via: <code style={{ background: 'rgba(255,255,255,0.04)', padding: '2px 6px', borderRadius: '4px' }}>/ws/tracking</code>
            </div>
          </div>

          {/* Interactive SVG Map */}
          <div className="glass-panel map-container">
            <svg className="map-svg" viewBox="0 0 1000 500">
              {/* Grid Background Lines */}
              <defs>
                <pattern id="grid" width="40" height="40" patternUnits="userSpaceOnUse">
                  <path d="M 40 0 L 0 0 0 40" fill="none" stroke="rgba(255,255,255,0.015)" strokeWidth="1" />
                </pattern>
              </defs>
              <rect width="1000" height="500" fill="url(#grid)" />

              {/* Roads / Routes */}
              <path d={routePaths[1]} className="map-road" />
              <path d={routePaths[2]} className="map-road" />
              <path d={routePaths[4]} className="map-road" />

              {/* Active Route Highlight for Selected Vehicle */}
              {selectedVehicle && (
                <path 
                  d={routePaths[selectedVehicle.id] || routePaths[1]} 
                  className="map-road-active"
                />
              )}

              {/* Vehicle Markers */}
              {vehicles.map(vehicle => {
                const pos = telemetry[vehicle.id] || { x: 150, y: 150 };
                const isSelected = selectedVehicle && selectedVehicle.id === vehicle.id;
                
                return (
                  <g 
                    key={vehicle.id} 
                    transform={`translate(${pos.x}, ${pos.y})`}
                    className="map-marker"
                    onClick={() => setSelectedVehicle(vehicle)}
                    style={{ cursor: 'pointer' }}
                  >
                    {/* Ripple Glow circle */}
                    {isSelected && (
                      <circle 
                        r="20" 
                        fill="rgba(56, 189, 248, 0.2)" 
                        className="map-marker-pulse"
                      />
                    )}

                    {/* Center Core dot */}
                    <circle 
                      r="7" 
                      fill={isSelected ? 'var(--accent-cyan)' : 'var(--accent-blue)'}
                      stroke="var(--bg-dark)"
                      strokeWidth="2"
                    />

                    {/* Vehicle Name text tag */}
                    <text 
                      y="-14" 
                      textAnchor="middle" 
                      fill="var(--text-secondary)" 
                      fontSize="10" 
                      fontWeight="600"
                      style={{ pointerEvents: 'none', background: 'black' }}
                    >
                      {vehicle.name}
                    </text>
                  </g>
                );
              })}
            </svg>
          </div>
        </div>

        {/* Right Column: Telemetry & Vehicle Detail Panel */}
        <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
          {selectedVehicle ? (
            <div className="glass-panel" style={{ padding: '24px', height: '100%' }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: '10px', marginBottom: '20px' }}>
                <Navigation size={20} style={{ color: 'var(--accent-cyan)', transform: `rotate(${(telemetry[selectedVehicle.id]?.bearing || 0)}deg)` }} />
                <h3 style={{ fontSize: '18px', fontWeight: '700' }}>{selectedVehicle.name}</h3>
              </div>

              <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
                <div style={{ borderBottom: '1px solid var(--border-color)', paddingBottom: '12px' }}>
                  <div style={{ fontSize: '11px', color: 'var(--text-muted)' }}>VEHICLE MODEL</div>
                  <div style={{ fontSize: '14px', fontWeight: '500' }}>{selectedVehicle.type}</div>
                </div>

                <div style={{ borderBottom: '1px solid var(--border-color)', paddingBottom: '12px' }}>
                  <div style={{ fontSize: '11px', color: 'var(--text-muted)' }}>CURRENT SPEED</div>
                  <div style={{ fontSize: '20px', fontWeight: '700', color: 'var(--text-primary)' }}>
                    {telemetry[selectedVehicle.id]?.speed || 0} <span style={{ fontSize: '13px', fontWeight: '500', color: 'var(--text-secondary)' }}>mph</span>
                  </div>
                </div>

                <div style={{ borderBottom: '1px solid var(--border-color)', paddingBottom: '12px' }}>
                  <div style={{ fontSize: '11px', color: 'var(--text-muted)' }}>GPS COORDINATES</div>
                  <div style={{ fontSize: '13px', color: 'var(--text-primary)', fontFamily: 'monospace' }}>
                    Lat: {(40.7128 + (telemetry[selectedVehicle.id]?.x || 0) * 0.0001).toFixed(4)}<br />
                    Lng: {(-74.0060 - (telemetry[selectedVehicle.id]?.y || 0) * 0.0001).toFixed(4)}
                  </div>
                </div>

                <div style={{ borderBottom: '1px solid var(--border-color)', paddingBottom: '12px' }}>
                  <div style={{ fontSize: '11px', color: 'var(--text-muted)' }}>FUEL STATUS</div>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '10px', marginTop: '4px' }}>
                    <div style={{ flexGrow: 1, height: '8px', background: 'rgba(255,255,255,0.06)', borderRadius: '4px', overflow: 'hidden' }}>
                      <div style={{ width: `${selectedVehicle.fuelLevel}%`, height: '100%', background: 'var(--accent-cyan)' }} />
                    </div>
                    <span style={{ fontSize: '12px', fontWeight: '600' }}>{selectedVehicle.fuelLevel}%</span>
                  </div>
                </div>

                <div>
                  <div style={{ fontSize: '11px', color: 'var(--text-muted)' }}>LAST TELEMETRY UPDATE</div>
                  <div style={{ fontSize: '13px', color: 'var(--accent-emerald)', fontWeight: '600', display: 'flex', alignItems: 'center', gap: '4px', marginTop: '4px' }}>
                    <CheckCircle size={12} />
                    <span>Active ({telemetry[selectedVehicle.id]?.lastUpdate || '...'})</span>
                  </div>
                </div>
              </div>
            </div>
          ) : (
            <div className="glass-panel" style={{ padding: '24px', display: 'flex', alignItems: 'center', justifyContent: 'center', height: '100%', color: 'var(--text-muted)' }}>
              Select a vehicle to track
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
