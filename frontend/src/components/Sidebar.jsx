import React from 'react';
import { 
  LayoutDashboard, 
  MapPin, 
  BrainCircuit, 
  AlertTriangle, 
  Truck, 
  LogOut 
} from 'lucide-react';
import { api } from '../services/api';

export default function Sidebar({ currentTab, setCurrentTab, setLoggedIn }) {
  const menuItems = [
    { id: 'dashboard', label: 'Operations Center', icon: LayoutDashboard },
    { id: 'tracking', label: 'Live GPS Fleet', icon: MapPin },
    { id: 'predictions', label: 'AI Predictor Hub', icon: BrainCircuit },
    { id: 'emergencies', label: 'Incident Console', icon: AlertTriangle },
    { id: 'fleet', label: 'Fleet & Drivers', icon: Truck },
  ];

  const handleLogout = () => {
    api.auth.logout();
    setLoggedIn(false);
  };

  return (
    <aside className="sidebar">
      <div style={{ padding: '24px', display: 'flex', alignItems: 'center', gap: '10px', borderBottom: '1px solid var(--border-color)' }}>
        <div style={{ 
          background: 'linear-gradient(135deg, var(--accent-cyan) 0%, var(--accent-blue) 100%)', 
          width: '32px', 
          height: '32px', 
          borderRadius: '8px',
          boxShadow: 'var(--glow-cyan)' 
        }} />
        <span style={{ fontWeight: '800', fontSize: '18px', color: 'var(--text-primary)', letterSpacing: '-0.5px' }}>
          Openeye <span style={{ color: 'var(--accent-cyan)' }}>AI</span>
        </span>
      </div>

      <nav style={{ flexGrow: 1, marginTop: '24px' }}>
        {menuItems.map((item) => {
          const Icon = item.icon;
          return (
            <div
              key={item.id}
              className={`nav-item ${currentTab === item.id ? 'active' : ''}`}
              onClick={() => setCurrentTab(item.id)}
            >
              <Icon size={18} />
              <span>{item.label}</span>
            </div>
          );
        })}
      </nav>

      <div style={{ padding: '20px', borderTop: '1px solid var(--border-color)' }}>
        <button 
          onClick={handleLogout}
          className="btn btn-secondary" 
          style={{ width: '100%', display: 'flex', justifyContent: 'center', gap: '10px' }}
        >
          <LogOut size={16} />
          <span>Sign Out</span>
        </button>
      </div>
    </aside>
  );
}
