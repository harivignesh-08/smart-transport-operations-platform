import React, { useState } from 'react';
import { Shield, Key, User as UserIcon, AlertCircle } from 'lucide-react';
import { api } from '../services/api';

export default function LoginPage({ setLoggedIn }) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      await api.auth.login(username, password);
      setLoggedIn(true);
    } catch (err) {
      setError(err.message || 'Login failed. Please verify credentials.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-bg">
      <div className="glow-orb orb-cyan" />
      <div className="glow-orb orb-blue" />
      
      <div className="glass-panel login-card">
        <div style={{ textAlign: 'center', marginBottom: '32px' }}>
          <div style={{ 
            display: 'inline-flex', 
            padding: '12px', 
            borderRadius: '12px', 
            background: 'rgba(56, 189, 248, 0.08)',
            color: 'var(--accent-cyan)',
            marginBottom: '16px',
            boxShadow: 'var(--glow-cyan)'
          }}>
            <Shield size={32} />
          </div>
          <h1 style={{ fontSize: '28px' }}>Openeye Control</h1>
          <p style={{ color: 'var(--text-secondary)', fontSize: '13px', marginTop: '6px' }}>
            Enter dispatch credentials to access fleet operations.
          </p>
        </div>

        {error && (
          <div style={{ 
            display: 'flex', 
            alignItems: 'center', 
            gap: '10px', 
            padding: '12px', 
            borderRadius: '8px', 
            background: 'rgba(239, 68, 68, 0.1)', 
            border: '1px solid rgba(239, 68, 68, 0.2)',
            color: 'var(--accent-rose)',
            fontSize: '13px',
            marginBottom: '20px'
          }}>
            <AlertCircle size={16} flexShrink={0} />
            <span>{error}</span>
          </div>
        )}

        <form onSubmit={handleSubmit}>
          <div className="input-group">
            <span className="input-label">Username</span>
            <div style={{ position: 'relative' }}>
              <input 
                type="text" 
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                placeholder="admin / dispatcher" 
                required 
                style={{ paddingLeft: '40px' }}
              />
              <UserIcon size={16} style={{ position: 'absolute', left: '14px', top: '50%', transform: 'translateY(-50%)', color: 'var(--text-muted)' }} />
            </div>
          </div>

          <div className="input-group" style={{ marginBottom: '28px' }}>
            <span className="input-label">Security Password</span>
            <div style={{ position: 'relative' }}>
              <input 
                type="password" 
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="••••••••" 
                required 
                style={{ paddingLeft: '40px' }}
              />
              <Key size={16} style={{ position: 'absolute', left: '14px', top: '50%', transform: 'translateY(-50%)', color: 'var(--text-muted)' }} />
            </div>
          </div>

          <button 
            type="submit" 
            className="btn btn-primary" 
            style={{ width: '100%', height: '46px' }}
            disabled={loading}
          >
            {loading ? 'Authorizing Dispatch...' : 'Access Console'}
          </button>
        </form>
      </div>
    </div>
  );
}
