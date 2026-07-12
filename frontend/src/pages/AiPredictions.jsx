import React, { useState } from 'react';
import { Brain, HelpCircle, Activity, ShieldAlert, Cpu } from 'lucide-react';
import { api } from '../services/api';

export default function AiPredictions() {
  const [activeEngine, setActiveEngine] = useState('eta');
  const [loading, setLoading] = useState(false);

  // ETA Engine Form States
  const [distance, setDistance] = useState('45');
  const [traffic, setTraffic] = useState('MEDIUM');
  const [etaResult, setEtaResult] = useState(null);

  // Maintenance Engine Form States
  const [vehicleId, setVehicleId] = useState('1');
  const [maintenanceResult, setMaintenanceResult] = useState(null);

  // Delay Engine Form States
  const [tripId, setTripId] = useState('101');
  const [delayResult, setDelayResult] = useState(null);

  const runPrediction = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      if (activeEngine === 'eta') {
        const res = await api.ai.predictEta({ distance, traffic });
        setEtaResult(res);
      } else if (activeEngine === 'maintenance') {
        const res = await api.ai.predictMaintenance(vehicleId);
        setMaintenanceResult(res);
      } else if (activeEngine === 'delay') {
        const res = await api.ai.predictDelay({ tripId });
        setDelayResult(res);
      }
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="page-container">
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 3fr', gap: '32px' }}>
        {/* Left Column: Model Toggles */}
        <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
          <div className="glass-panel" style={{ padding: '16px' }}>
            <h3 style={{ fontSize: '14px', fontWeight: '700', color: 'var(--text-secondary)', marginBottom: '16px', textTransform: 'uppercase' }}>
              Select AI Engine
            </h3>
            
            <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
              <button 
                className={`btn ${activeEngine === 'eta' ? 'btn-primary' : 'btn-secondary'}`}
                style={{ width: '100%', justifyContent: 'flex-start' }}
                onClick={() => setActiveEngine('eta')}
              >
                <Brain size={16} />
                <span>ETA Optimisation</span>
              </button>

              <button 
                className={`btn ${activeEngine === 'maintenance' ? 'btn-primary' : 'btn-secondary'}`}
                style={{ width: '100%', justifyContent: 'flex-start' }}
                onClick={() => setActiveEngine('maintenance')}
              >
                <Cpu size={16} />
                <span>Predictive Maintenance</span>
              </button>

              <button 
                className={`btn ${activeEngine === 'delay' ? 'btn-primary' : 'btn-secondary'}`}
                style={{ width: '100%', justifyContent: 'flex-start' }}
                onClick={() => setActiveEngine('delay')}
              >
                <ShieldAlert size={16} />
                <span>Delay Risk Score</span>
              </button>
            </div>
          </div>
        </div>

        {/* Right Column: Execution Form & Results */}
        <div className="glass-panel" style={{ padding: '32px' }}>
          <h2 style={{ fontSize: '22px', fontWeight: '700', display: 'flex', alignItems: 'center', gap: '10px' }}>
            <Activity size={20} style={{ color: 'var(--accent-cyan)' }} />
            <span>
              {activeEngine === 'eta' && 'Route ETA Optimizer Model'}
              {activeEngine === 'maintenance' && 'Predictive Fleet Maintenance Model'}
              {activeEngine === 'delay' && 'Trip Delay & Disruption Analyzer'}
            </span>
          </h2>

          <form onSubmit={runPrediction} className="pred-form" style={{ maxWidth: '480px' }}>
            {activeEngine === 'eta' && (
              <>
                <div className="input-group">
                  <span className="input-label">Trip Distance (miles)</span>
                  <input 
                    type="number" 
                    value={distance} 
                    onChange={e => setDistance(e.target.value)} 
                    placeholder="Enter trip distance" 
                    required 
                  />
                </div>
                <div className="input-group">
                  <span className="input-label">Traffic Condition</span>
                  <select value={traffic} onChange={e => setTraffic(e.target.value)}>
                    <option value="LIGHT">Light Traffic Flow</option>
                    <option value="MEDIUM">Medium / Intermittent Traffic</option>
                    <option value="HEAVY">Heavy Congestion / Rush Hour</option>
                  </select>
                </div>
              </>
            )}

            {activeEngine === 'maintenance' && (
              <div className="input-group">
                <span className="input-label">Select Vehicle Asset ID</span>
                <select value={vehicleId} onChange={e => setVehicleId(e.target.value)}>
                  <option value="1">Semi-Truck 101</option>
                  <option value="2">Delivery Van 204</option>
                  <option value="3">Heavy Hauler 305</option>
                  <option value="4">Reefer Truck 401</option>
                </select>
              </div>
            )}

            {activeEngine === 'delay' && (
              <div className="input-group">
                <span className="input-label">Active Trip Job ID</span>
                <input 
                  type="text" 
                  value={tripId} 
                  onChange={e => setTripId(e.target.value)} 
                  placeholder="Enter active trip reference ID" 
                  required 
                />
              </div>
            )}

            <button 
              type="submit" 
              className="btn btn-primary" 
              style={{ width: '200px', height: '44px' }}
              disabled={loading}
            >
              {loading ? 'Evaluating Model...' : 'Execute AI Model'}
            </button>
          </form>

          {/* Results Display */}
          {activeEngine === 'eta' && etaResult && (
            <div className="pred-result">
              <h4 style={{ fontWeight: '700', fontSize: '15px', color: 'var(--text-primary)', marginBottom: '8px' }}>
                ETA Analysis Complete
              </h4>
              <p style={{ fontSize: '14px', color: 'var(--text-secondary)' }}>
                Predicted Travel Time: <span style={{ color: 'var(--accent-cyan)', fontWeight: '700' }}>{etaResult.etaMinutes} minutes</span><br />
                Routing Recommendation: <span style={{ fontWeight: '600' }}>{etaResult.routeSelected}</span>
              </p>
            </div>
          )}

          {activeEngine === 'maintenance' && maintenanceResult && (
            <div className="pred-result" style={{ borderLeftColor: maintenanceResult.maintenanceRecommended ? 'var(--accent-amber)' : 'var(--accent-emerald)' }}>
              <h4 style={{ fontWeight: '700', fontSize: '15px', color: 'var(--text-primary)', marginBottom: '8px' }}>
                Maintenance Diagnostics Report
              </h4>
              <p style={{ fontSize: '14px', color: 'var(--text-secondary)' }}>
                Recommendation:{' '}
                <span style={{ 
                  color: maintenanceResult.maintenanceRecommended ? 'var(--accent-amber)' : 'var(--accent-emerald)', 
                  fontWeight: '700' 
                }}>
                  {maintenanceResult.maintenanceRecommended ? 'MAINTENANCE RECOMMENDED' : 'NO IMMEDIATE MAINTENANCE REQUIRED'}
                </span>
                <br />
                Model Confidence Rating: <span style={{ fontWeight: '600' }}>{maintenanceResult.confidence}%</span>
                {maintenanceResult.nextPartsNeedCheck && (
                  <span style={{ display: 'block', marginTop: '6px' }}>
                    Critical Diagnostics Needed for: <strong>{maintenanceResult.nextPartsNeedCheck.join(', ')}</strong>
                  </span>
                )}
              </p>
            </div>
          )}

          {activeEngine === 'delay' && delayResult && (
            <div className="pred-result" style={{ borderLeftColor: delayResult.delayRisk === 'HIGH' ? 'var(--accent-rose)' : 'var(--accent-emerald)' }}>
              <h4 style={{ fontWeight: '700', fontSize: '15px', color: 'var(--text-primary)', marginBottom: '8px' }}>
                Trip Disruption Analytics
              </h4>
              <p style={{ fontSize: '14px', color: 'var(--text-secondary)' }}>
                Delay Risk: <span style={{ color: delayResult.delayRisk === 'HIGH' ? 'var(--accent-rose)' : 'var(--accent-emerald)', fontWeight: '700' }}>{delayResult.delayRisk}</span><br />
                Predicted Incident Delay: <span style={{ fontWeight: '600' }}>{delayResult.delayMinutes} mins</span><br />
                Weather Factor Impact: <span style={{ color: 'var(--accent-cyan)' }}>{delayResult.weatherImpact}</span>
              </p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
