import React, { useEffect, useState } from 'react';
import { 
  Truck, 
  Users, 
  Activity, 
  AlertOctagon, 
  ArrowUpRight, 
  ArrowDownRight,
  ShieldCheck,
  Fuel,
  Compass
} from 'lucide-react';
import { api } from '../services/api';

export default function Dashboard({ setCurrentTab }) {
  const [vehicles, setVehicles] = useState([]);
  const [drivers, setDrivers] = useState([]);
  const [emergencies, setEmergencies] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function loadDashboardData() {
      try {
        const [vList, dList, eList] = await Promise.all([
          api.vehicles.getAll(),
          api.drivers.getAll(),
          api.emergencies.getAll()
        ]);
        setVehicles(vList);
        setDrivers(dList);
        setEmergencies(eList);
      } catch (err) {
        console.error('Error loading dashboard stats:', err);
      } finally {
        setLoading(false);
      }
    }
    loadDashboardData();
  }, []);

  if (loading) {
    return <div style={{ padding: '32px', color: 'var(--text-secondary)' }}>Loading Dashboard Operations...</div>;
  }

  const activeVehicles = vehicles.filter(v => v.status === 'ACTIVE').length;
  const activeEmergencies = emergencies.filter(e => e.status !== 'RESOLVED').length;
  const avgRating = (drivers.reduce((acc, curr) => acc + curr.rating, 0) / drivers.length).toFixed(1);

  return (
    <div className="page-container">
      <div style={{ marginBottom: '32px' }}>
        <h1>Operations Command Center</h1>
        <p style={{ color: 'var(--text-secondary)', fontSize: '14px', marginTop: '4px' }}>
          Real-time fleet statistics, incident alerts, and AI prediction models dispatch console.
        </p>
      </div>

      {/* Stats Grid */}
      <div className="stats-grid">
        <div className="glass-panel stat-card">
          <div className="stat-card-header">
            <span>ACTIVE VEHICLES</span>
            <Truck size={18} style={{ color: 'var(--accent-cyan)' }} />
          </div>
          <div className="stat-card-value">
            {activeVehicles} <span style={{ fontSize: '16px', fontWeight: '500', color: 'var(--text-secondary)' }}>/ {vehicles.length}</span>
          </div>
          <div className="stat-card-trend trend-up">
            <ArrowUpRight size={14} />
            <span>+4.2% Fleet Utility</span>
          </div>
        </div>

        <div className="glass-panel stat-card">
          <div className="stat-card-header">
            <span>ACTIVE DRIVERS</span>
            <Users size={18} style={{ color: 'var(--accent-blue)' }} />
          </div>
          <div className="stat-card-value">
            {drivers.filter(d => d.status === 'ON_TRIP').length} <span style={{ fontSize: '16px', fontWeight: '500', color: 'var(--text-secondary)' }}>/ {drivers.length}</span>
          </div>
          <div className="stat-card-trend trend-up">
            <ShieldCheck size={14} />
            <span>{avgRating} Avg Safety Rating</span>
          </div>
        </div>

        <div className="glass-panel stat-card">
          <div className="stat-card-header">
            <span>FUEL EFFICIENCY</span>
            <Fuel size={18} style={{ color: 'var(--accent-emerald)' }} />
          </div>
          <div className="stat-card-value">
            8.4 <span style={{ fontSize: '14px', fontWeight: '500', color: 'var(--text-secondary)' }}>mpg (predicted)</span>
          </div>
          <div className="stat-card-trend trend-down" style={{ color: 'var(--accent-emerald)' }}>
            <ArrowDownRight size={14} />
            <span>-2.1% consumption</span>
          </div>
        </div>

        <div className="glass-panel stat-card" style={{ borderColor: activeEmergencies > 0 ? 'rgba(239, 68, 68, 0.3)' : 'var(--border-color)' }}>
          <div className="stat-card-header">
            <span>PENDING EMERGENCIES</span>
            <AlertOctagon size={18} style={{ color: activeEmergencies > 0 ? 'var(--accent-rose)' : 'var(--text-muted)' }} />
          </div>
          <div className="stat-card-value" style={{ color: activeEmergencies > 0 ? 'var(--accent-rose)' : 'var(--text-primary)' }}>
            {activeEmergencies}
          </div>
          <div className="stat-card-trend" style={{ color: activeEmergencies > 0 ? 'var(--accent-rose)' : 'var(--text-muted)' }}>
            {activeEmergencies > 0 ? 'Action required in Dispatch Console' : 'All systems clear'}
          </div>
        </div>
      </div>

      {/* Main Grid Section */}
      <div style={{ display: 'grid', gridTemplateColumns: '2fr 1fr', gap: '32px', minHeight: '400px' }}>
        {/* Left Column: Recent Trips & Operations Status */}
        <div className="glass-panel" style={{ padding: '24px' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
            <h3 style={{ fontSize: '18px', fontWeight: '600' }}>Active Fleet Status Logs</h3>
            <button className="btn btn-secondary" style={{ padding: '6px 12px', fontSize: '12px' }} onClick={() => setCurrentTab('fleet')}>
              View All Fleet
            </button>
          </div>

          <div className="table-container">
            <table>
              <thead>
                <tr>
                  <th>Vehicle</th>
                  <th>Type</th>
                  <th>Mileage</th>
                  <th>Fuel Level</th>
                  <th>Status</th>
                </tr>
              </thead>
              <tbody>
                {vehicles.slice(0, 4).map(v => (
                  <tr key={v.id}>
                    <td style={{ fontWeight: '600', color: 'var(--text-primary)' }}>{v.name}</td>
                    <td style={{ color: 'var(--text-secondary)' }}>{v.type}</td>
                    <td style={{ color: 'var(--text-secondary)' }}>{v.mileage.toLocaleString()} mi</td>
                    <td>
                      <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                        <div style={{ width: '80px', height: '6px', background: 'rgba(255,255,255,0.06)', borderRadius: '3px', overflow: 'hidden' }}>
                          <div style={{ 
                            width: `${v.fuelLevel}%`, 
                            height: '100%', 
                            background: v.fuelLevel > 40 ? 'var(--accent-emerald)' : v.fuelLevel > 15 ? 'var(--accent-amber)' : 'var(--accent-rose)'
                          }} />
                        </div>
                        <span style={{ fontSize: '12px' }}>{v.fuelLevel}%</span>
                      </div>
                    </td>
                    <td>
                      <span className={`badge ${
                        v.status === 'ACTIVE' ? 'badge-success' : v.status === 'MAINTENANCE' ? 'badge-warning' : 'badge-danger'
                      }`}>
                        {v.status}
                      </span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>

        {/* Right Column: AI Analytics Quick-Stats */}
        <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
          {/* Quick AI Advisor */}
          <div className="glass-panel" style={{ padding: '24px', flexGrow: 1 }}>
            <h3 style={{ fontSize: '18px', fontWeight: '600', marginBottom: '16px', display: 'flex', alignItems: 'center', gap: '10px' }}>
              <Compass size={18} style={{ color: 'var(--accent-cyan)' }} />
              <span>AI Operations Advisor</span>
            </h3>
            
            <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
              <div style={{ 
                padding: '12px', 
                background: 'rgba(56, 189, 248, 0.04)', 
                borderLeft: '3px solid var(--accent-cyan)',
                borderRadius: '4px' 
              }}>
                <div style={{ fontSize: '12px', color: 'var(--accent-cyan)', fontWeight: '600', marginBottom: '4px' }}>PREDICTIVE MAINTENANCE</div>
                <div style={{ fontSize: '13px', color: 'var(--text-primary)' }}>
                  Heavy Hauler 305 health score is falling. Scheduling brake diagnostic is recommended.
                </div>
              </div>

              <div style={{ 
                padding: '12px', 
                background: 'rgba(16, 185, 129, 0.04)', 
                borderLeft: '3px solid var(--accent-emerald)',
                borderRadius: '4px' 
              }}>
                <div style={{ fontSize: '12px', color: 'var(--accent-emerald)', fontWeight: '600', marginBottom: '4px' }}>ROUTE OPTIMIZATION</div>
                <div style={{ fontSize: '13px', color: 'var(--text-primary)' }}>
                  Route B selected for Semi-Truck 101 saves 18 minutes on traffic delay checks.
                </div>
              </div>

              <button 
                className="btn btn-primary" 
                style={{ width: '100%', marginTop: '8px' }}
                onClick={() => setCurrentTab('predictions')}
              >
                Launch AI Engine
              </button>
            </div>
          </div>

          {/* Quick incident alert */}
          {activeEmergencies > 0 && (
            <div className="glass-panel" style={{ padding: '20px', borderLeft: '4px solid var(--accent-rose)', background: 'rgba(239, 68, 68, 0.03)' }}>
              <h4 style={{ color: 'var(--accent-rose)', fontWeight: '700', fontSize: '14px', marginBottom: '6px' }}>Active Fleet Incident Warnings</h4>
              <p style={{ fontSize: '12px', color: 'var(--text-secondary)' }}>
                {emergencies[0].vehicleName} reported a critical {emergencies[0].type} warning.
              </p>
              <button 
                className="btn btn-danger" 
                style={{ width: '100%', padding: '6px 12px', fontSize: '11px', marginTop: '12px', fontWeight: '700' }}
                onClick={() => setCurrentTab('emergencies')}
              >
                Dispatch Incident Console
              </button>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
