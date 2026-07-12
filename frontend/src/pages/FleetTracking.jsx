import React, { useState, useEffect } from 'react';
import { Compass, Navigation, Radio, MapPin, CheckCircle, AlertCircle } from 'lucide-react';
import { api } from '../services/api';

export default function FleetTracking() {
  const [vehicles, setVehicles] = useState([]);
  const [selectedVehicle, setSelectedVehicle] = useState(null);
  const [telemetry, setTelemetry] = useState({});
  const [wsStatus, setWsStatus] = useState('CONNECTING');
  const [userLocation, setUserLocation] = useState(null);
  const [locationError, setLocationError] = useState('');

  useEffect(() => {
    // Request user's geolocation
    if (navigator.geolocation) {
      navigator.geolocation.watchPosition(
        (position) => {
          const { latitude, longitude } = position.coords;
          setUserLocation({ lat: latitude, lng: longitude });
          setLocationError('');
        },
        (error) => {
          console.error('Geolocation error:', error);
          setLocationError('Unable to access device location. Enable location permissions.');
        },
        { enableHighAccuracy: true, timeout: 5000, maximumAge: 0 }
      );
    }
  }, []);

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

    // Connect to WebSocket for real-time tracking
    const connectWebSocket = () => {
      try {
        const ws = api.tracking.connectWebSocket();
        ws.onopen = () => {
          setWsStatus('CONNECTED');
        };
        ws.onmessage = (event) => {
          const data = JSON.parse(event.data);
          setTelemetry(prev => ({ ...prev, ...data }));
        };
        ws.onerror = () => {
          setWsStatus('ERROR');
        };
        ws.onclose = () => {
          setWsStatus('DISCONNECTED');
          // Attempt to reconnect after 3 seconds
          setTimeout(connectWebSocket, 3000);
        };
      } catch (err) {
        console.error('WebSocket connection error:', err);
        setWsStatus('ERROR');
      }
    };

    connectWebSocket();

    return () => {
      setWsStatus('DISCONNECTED');
    };
  }, []);

  // Fetch live tracking data from API
  useEffect(() => {
    const fetchLiveTracking = async () => {
      try {
        const liveData = await api.tracking.getLiveTracking();
        if (liveData && liveData.length > 0) {
          const telemetryMap = {};
          liveData.forEach(vehicle => {
            telemetryMap[vehicle.id] = {
              lat: vehicle.latitude || userLocation?.lat || 40.7128,
              lng: vehicle.longitude || userLocation?.lng || -74.0060,
              speed: vehicle.speed || 0,
              bearing: vehicle.bearing || 0,
              lastUpdate: new Date().toLocaleTimeString()
            };
          });
          setTelemetry(telemetryMap);
        }
      } catch (err) {
        console.error('Error fetching live tracking:', err);
      }
    };

    const interval = setInterval(fetchLiveTracking, 3000);
    fetchLiveTracking(); // Initial fetch

    return () => clearInterval(interval);
  }, [userLocation]);

  // Convert coordinates to map pixels (simple mercator-like projection)
  const lngToPixel = (lng) => {
    return ((lng + 180) / 360) * 1000;
  };

  const latToPixel = (lat) => {
    return ((90 - lat) / 180) * 500;
  };

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
                <span style={{ color: wsStatus === 'CONNECTED' ? 'var(--accent-emerald)' : wsStatus === 'ERROR' ? 'var(--accent-rose)' : 'var(--accent-amber)' }}>
                  {wsStatus}
                </span>
              </span>
            </div>
            <div style={{ fontSize: '12px', color: 'var(--text-secondary)' }}>
              {userLocation ? `Location: ${userLocation.lat.toFixed(4)}, ${userLocation.lng.toFixed(4)}` : 'Requesting location...'}
            </div>
          </div>

          {locationError && (
            <div style={{ 
              display: 'flex', 
              alignItems: 'center', 
              gap: '10px', 
              padding: '12px', 
              borderRadius: '8px', 
              background: 'rgba(239, 68, 68, 0.1)', 
              border: '1px solid rgba(239, 68, 68, 0.2)',
              color: 'var(--accent-rose)',
              fontSize: '12px'
            }}>
              <AlertCircle size={14} />
              <span>{locationError}</span>
            </div>
          )}

          {/* Live Map Display */}
          <div className="glass-panel map-container">
            <svg className="map-svg" viewBox="0 0 1000 500">
              {/* Grid Background Lines */}
              <defs>
                <pattern id="grid" width="40" height="40" patternUnits="userSpaceOnUse">
                  <path d="M 40 0 L 0 0 0 40" fill="none" stroke="rgba(255,255,255,0.015)" strokeWidth="1" />
                </pattern>
              </defs>
              <rect width="1000" height="500" fill="url(#grid)" />

              {/* User Location Marker */}
              {userLocation && (
                <g transform={`translate(${lngToPixel(userLocation.lng)}, ${latToPixel(userLocation.lat)})`}>
                  <circle 
                    r="12" 
                    fill="rgba(56, 189, 248, 0.15)" 
                    stroke="var(--accent-cyan)"
                    strokeWidth="2"
                  />
                  <circle 
                    r="4" 
                    fill="var(--accent-cyan)"
                  />
                  <text 
                    y="-18" 
                    textAnchor="middle" 
                    fill="var(--accent-cyan)" 
                    fontSize="9" 
                    fontWeight="700"
                  >
                    YOUR LOCATION
                  </text>
                </g>
              )}

              {/* Vehicle Markers */}
              {vehicles.map(vehicle => {
                const pos = telemetry[vehicle.id];
                if (!pos) return null;
                
                const x = lngToPixel(pos.lng || -74.0060);
                const y = latToPixel(pos.lat || 40.7128);
                const isSelected = selectedVehicle && selectedVehicle.id === vehicle.id;
                
                return (
                  <g 
                    key={vehicle.id} 
                    transform={`translate(${x}, ${y})`}
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
                      style={{ pointerEvents: 'none' }}
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
                    Lat: {(telemetry[selectedVehicle.id]?.lat || 40.7128).toFixed(6)}<br />
                    Lng: {(telemetry[selectedVehicle.id]?.lng || -74.0060).toFixed(6)}
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
