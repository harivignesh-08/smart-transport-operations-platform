import React, { useState, useEffect } from 'react';
import { Star, AlertCircle, Wrench, Shield } from 'lucide-react';
import { api } from '../services/api';

export default function FleetManagement() {
  const [vehicles, setVehicles] = useState([]);
  const [drivers, setDrivers] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function loadData() {
      try {
        const [vList, dList] = await Promise.all([
          api.vehicles.getAll(),
          api.drivers.getAll()
        ]);
        setVehicles(vList);
        setDrivers(dList);
      } catch (err) {
        console.error(err);
      } finally {
        setLoading(false);
      }
    }
    loadData();
  }, []);

  if (loading) {
    return <div style={{ padding: '32px', color: 'var(--text-secondary)' }}>Loading Fleet Assets Directory...</div>;
  }

  return (
    <div className="page-container">
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '32px' }}>
        {/* Left Column: Fleet Vehicles List */}
        <div className="glass-panel" style={{ padding: '24px' }}>
          <h3 style={{ fontSize: '18px', fontWeight: '700', marginBottom: '20px', display: 'flex', alignItems: 'center', gap: '8px' }}>
            <Wrench size={18} style={{ color: 'var(--accent-cyan)' }} />
            <span>Vehicles Inventory</span>
          </h3>

          <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
            {vehicles.map(v => (
              <div 
                key={v.id}
                className="glass-panel"
                style={{ padding: '16px 20px', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}
              >
                <div>
                  <div style={{ fontWeight: '600', color: 'var(--text-primary)', fontSize: '15px' }}>{v.name}</div>
                  <div style={{ fontSize: '12px', color: 'var(--text-secondary)' }}>Model: {v.type}</div>
                  <div style={{ fontSize: '11px', color: 'var(--text-muted)', marginTop: '4px' }}>
                    Odometer: <strong>{v.mileage.toLocaleString()} mi</strong> | Next service: {v.nextMaintenance}
                  </div>
                </div>

                <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'flex-end', gap: '6px' }}>
                  <span className={`badge ${
                    v.status === 'ACTIVE' ? 'badge-success' : v.status === 'MAINTENANCE' ? 'badge-warning' : 'badge-danger'
                  }`}>
                    {v.status.replace('_', ' ')}
                  </span>
                  <div style={{ fontSize: '11px', color: 'var(--text-secondary)' }}>
                    Fuel: <strong>{v.fuelLevel}%</strong>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* Right Column: Drivers List */}
        <div className="glass-panel" style={{ padding: '24px' }}>
          <h3 style={{ fontSize: '18px', fontWeight: '700', marginBottom: '20px', display: 'flex', alignItems: 'center', gap: '8px' }}>
            <Shield size={18} style={{ color: 'var(--accent-blue)' }} />
            <span>Active Drivers Directory</span>
          </h3>

          <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
            {drivers.map(d => (
              <div 
                key={d.id}
                className="glass-panel"
                style={{ padding: '16px 20px', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}
              >
                <div>
                  <div style={{ fontWeight: '600', color: 'var(--text-primary)', fontSize: '15px' }}>{d.name}</div>
                  <div style={{ fontSize: '12px', color: 'var(--text-secondary)' }}>License: {d.license}</div>
                  <div style={{ fontSize: '11px', color: 'var(--text-muted)', marginTop: '4px' }}>
                    Total Trips Completed: <strong>{d.trips}</strong>
                  </div>
                </div>

                <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'flex-end', gap: '6px' }}>
                  <span className={`badge ${
                    d.status === 'AVAILABLE' ? 'badge-success' : d.status === 'ON_TRIP' ? 'badge-info' : 'badge-danger'
                  }`}>
                    {d.status.replace('_', ' ')}
                  </span>
                  
                  <div style={{ display: 'flex', alignItems: 'center', gap: '4px', fontSize: '12px', color: 'var(--accent-amber)', fontWeight: '600' }}>
                    <Star size={12} fill="currentColor" />
                    <span>{d.rating}</span>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}
