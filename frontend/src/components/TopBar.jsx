import React from 'react';
import { Bell, Shield } from 'lucide-react';

export default function TopBar({ currentTab }) {
  const username = localStorage.getItem('username') || 'Dispatcher';

  const tabTitles = {
    dashboard: 'Operations Center Dashboard',
    tracking: 'Live GPS Fleet Tracking Map',
    predictions: 'AI Predictive Analysis Engine',
    emergencies: 'Emergency Incident Dispatch Console',
    fleet: 'Fleet Vehicles & Drivers Directory'
  };

  return (
    <header className="topbar">
      <div>
        <h2 style={{ margin: 0, fontSize: '20px', fontWeight: '700' }}>
          {tabTitles[currentTab] || 'Openeye Operations Portal'}
        </h2>
      </div>

      <div style={{ display: 'flex', alignItems: 'center', gap: '20px' }}>
        <button 
          style={{ 
            background: 'none', 
            border: 'none', 
            color: 'var(--text-secondary)', 
            cursor: 'pointer',
            position: 'relative' 
          }}
        >
          <Bell size={20} />
          <span style={{ 
            position: 'absolute', 
            top: '-2px', 
            right: '-2px', 
            width: '8px', 
            height: '8px', 
            background: 'var(--accent-rose)', 
            borderRadius: '50%',
            boxShadow: 'var(--glow-rose)'
          }} />
        </button>

        <div style={{ height: '24px', width: '1px', background: 'var(--border-color)' }} />

        <div className="user-profile">
          <div style={{ textAlign: 'right' }}>
            <div style={{ fontWeight: '600', fontSize: '14px', color: 'var(--text-primary)' }}>{username}</div>
            <div style={{ fontSize: '11px', color: 'var(--accent-cyan)', display: 'flex', alignItems: 'center', gap: '4px', justifyContent: 'flex-end' }}>
              <Shield size={10} />
              <span>Fleet Admin</span>
            </div>
          </div>
          <div className="avatar">
            {username.charAt(0).toUpperCase()}
          </div>
        </div>
      </div>
    </header>
  );
}
