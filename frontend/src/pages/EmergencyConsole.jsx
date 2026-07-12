import React, { useState, useEffect } from 'react';
import { AlertOctagon, RefreshCw, Send, ShieldAlert, CheckCircle } from 'lucide-react';
import { api } from '../services/api';

export default function EmergencyConsole() {
  const [emergencies, setEmergencies] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filterSeverity, setFilterSeverity] = useState('ALL');

  // Incident reporting form states
  const [vehicleName, setVehicleName] = useState('Semi-Truck 101');
  const [type, setType] = useState('ENGINE_OVERHEAT');
  const [severity, setSeverity] = useState('HIGH');
  const [submitting, setSubmitting] = useState(false);

  async function loadEmergencies() {
    setLoading(true);
    try {
      const list = await api.emergencies.getAll();
      setEmergencies(list);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadEmergencies();
  }, []);

  const reportIncident = async (e) => {
    e.preventDefault();
    setSubmitting(true);
    try {
      const newEmergency = {
        id: Math.floor(Math.random() * 1000) + 3000,
        vehicleName,
        type,
        severity,
        status: 'PENDING',
        timestamp: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
      };
      await api.emergencies.report(newEmergency);
      setEmergencies([newEmergency, ...emergencies]);
    } catch (err) {
      console.error(err);
    } finally {
      setSubmitting(false);
    }
  };

  const updateStatus = (id, newStatus) => {
    setEmergencies(emergencies.map(e => e.id === id ? { ...e, status: newStatus } : e));
  };

  const filteredEmergencies = filterSeverity === 'ALL' 
    ? emergencies 
    : emergencies.filter(e => e.severity === filterSeverity);

  return (
    <div className="page-container">
      <div style={{ display: 'grid', gridTemplateColumns: '2fr 1.2fr', gap: '32px' }}>
        {/* Left Column: List of Incidents */}
        <div className="glass-panel" style={{ padding: '24px' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
            <h3 style={{ fontSize: '18px', fontWeight: '700' }}>Active Fleet Emergency Logs</h3>
            
            <div style={{ display: 'flex', gap: '10px' }}>
              <select 
                value={filterSeverity} 
                onChange={e => setFilterSeverity(e.target.value)}
                style={{ width: '130px', padding: '6px 12px', fontSize: '12px' }}
              >
                <option value="ALL">All Severities</option>
                <option value="CRITICAL">Critical Only</option>
                <option value="HIGH">High Only</option>
                <option value="MEDIUM">Medium Only</option>
              </select>
              
              <button 
                className="btn btn-secondary" 
                style={{ padding: '6px 12px' }}
                onClick={loadEmergencies}
              >
                <RefreshCw size={14} />
              </button>
            </div>
          </div>

          {loading ? (
            <div style={{ color: 'var(--text-secondary)' }}>Loading emergencies...</div>
          ) : filteredEmergencies.length === 0 ? (
            <div style={{ padding: '40px', textAlign: 'center', color: 'var(--text-muted)' }}>
              No active incidents logged.
            </div>
          ) : (
            <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
              {filteredEmergencies.map(e => (
                <div 
                  key={e.id}
                  className="glass-panel"
                  style={{ 
                    padding: '16px 20px', 
                    display: 'flex', 
                    justifyContent: 'space-between', 
                    alignItems: 'center',
                    borderLeft: `4px solid ${
                      e.severity === 'CRITICAL' ? 'var(--accent-rose)' : e.severity === 'HIGH' ? 'var(--accent-amber)' : 'var(--accent-blue)'
                    }`,
                    background: e.status === 'RESOLVED' ? 'rgba(255,255,255,0.01)' : 'rgba(239, 68, 68, 0.02)'
                  }}
                >
                  <div>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '4px' }}>
                      <span style={{ fontWeight: '700', fontSize: '15px', color: 'var(--text-primary)' }}>
                        {e.vehicleName}
                      </span>
                      <span className={`badge ${
                        e.severity === 'CRITICAL' ? 'badge-danger' : e.severity === 'HIGH' ? 'badge-warning' : 'badge-info'
                      }`}>
                        {e.severity}
                      </span>
                    </div>
                    <div style={{ fontSize: '13px', color: 'var(--text-secondary)' }}>
                      Incident: <strong style={{ color: 'var(--text-primary)' }}>{e.type.replace('_', ' ')}</strong> ({e.timestamp})
                    </div>
                  </div>

                  <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
                    <span style={{ 
                      fontSize: '12px', 
                      fontWeight: '700',
                      color: e.status === 'RESOLVED' ? 'var(--accent-emerald)' : 'var(--accent-rose)'
                    }}>
                      {e.status}
                    </span>

                    {e.status === 'PENDING' && (
                      <button 
                        className="btn btn-primary"
                        style={{ padding: '6px 12px', fontSize: '11px' }}
                        onClick={() => updateStatus(e.id, 'DISPATCHED')}
                      >
                        Dispatch Rescuer
                      </button>
                    )}

                    {e.status === 'DISPATCHED' && (
                      <button 
                        className="btn btn-secondary"
                        style={{ padding: '6px 12px', fontSize: '11px', color: 'var(--accent-emerald)', borderColor: 'var(--accent-emerald)' }}
                        onClick={() => updateStatus(e.id, 'RESOLVED')}
                      >
                        Mark Resolved
                      </button>
                    )}
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>

        {/* Right Column: Report New Incident Form */}
        <div className="glass-panel" style={{ padding: '24px' }}>
          <h3 style={{ fontSize: '18px', fontWeight: '700', marginBottom: '16px', display: 'flex', alignItems: 'center', gap: '8px' }}>
            <AlertOctagon size={18} style={{ color: 'var(--accent-rose)' }} />
            <span>Log Fleet Incident</span>
          </h3>

          <form onSubmit={reportIncident} style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
            <div className="input-group">
              <span className="input-label">Select Fleet Vehicle</span>
              <select value={vehicleName} onChange={e => setVehicleName(e.target.value)}>
                <option value="Semi-Truck 101">Semi-Truck 101</option>
                <option value="Delivery Van 204">Delivery Van 204</option>
                <option value="Heavy Hauler 305">Heavy Hauler 305</option>
                <option value="Reefer Truck 401">Reefer Truck 401</option>
              </select>
            </div>

            <div className="input-group">
              <span className="input-label">Incident Type</span>
              <select value={type} onChange={e => setType(e.target.value)}>
                <option value="ENGINE_OVERHEAT">Engine Overheat Alert</option>
                <option value="FLAT_TIRE">Flat Tire / Tire Pressure</option>
                <option value="COLLISION">Vehicle Collision / Accident</option>
                <option value="FUEL_LEAK">Fuel Leak / Fuel Drop Warning</option>
              </select>
            </div>

            <div className="input-group">
              <span className="input-label">Severity Level</span>
              <select value={severity} onChange={e => setSeverity(e.target.value)}>
                <option value="CRITICAL">Critical Priority</option>
                <option value="HIGH">High Priority</option>
                <option value="MEDIUM">Medium Priority</option>
              </select>
            </div>

            <button 
              type="submit" 
              className="btn btn-danger" 
              style={{ width: '100%', height: '44px', display: 'flex', gap: '10px' }}
              disabled={submitting}
            >
              <Send size={16} />
              <span>{submitting ? 'Reporting...' : 'Dispatch SOS Call'}</span>
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}
