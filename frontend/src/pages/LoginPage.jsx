import React, { useState } from 'react';
import { Shield, Key, User as UserIcon, AlertCircle, Mail, Phone, Briefcase } from 'lucide-react';
import { api } from '../services/api';

export default function LoginPage({ setLoggedIn }) {
  const [isRegister, setIsRegister] = useState(false);
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [email, setEmail] = useState('');
  const [phone, setPhone] = useState('');
  const [role, setRole] = useState('DISPATCHER');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [loading, setLoading] = useState(false);

  const handleLoginSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');
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

  const handleRegisterSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');

    if (password !== confirmPassword) {
      setError('Passwords do not match.');
      return;
    }

    if (password.length < 6) {
      setError('Password must be at least 6 characters long.');
      return;
    }

    setLoading(true);

    try {
      await api.auth.register({
        username,
        password,
        email,
        phone,
        role
      });
      setSuccess('Account created successfully! Please log in.');
      setTimeout(() => {
        setIsRegister(false);
        setUsername('');
        setPassword('');
        setConfirmPassword('');
        setEmail('');
        setPhone('');
        setRole('DISPATCHER');
      }, 1500);
    } catch (err) {
      setError(err.message || 'Registration failed. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-bg">
      <div className="glow-orb orb-cyan" />
      <div className="glow-orb orb-blue" />
      
      <div className="glass-panel login-card" style={{ maxWidth: isRegister ? '450px' : '400px' }}>
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
            {isRegister ? 'Create a new dispatch account' : 'Enter dispatch credentials to access fleet operations.'}
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

        {success && (
          <div style={{ 
            display: 'flex', 
            alignItems: 'center', 
            gap: '10px', 
            padding: '12px', 
            borderRadius: '8px', 
            background: 'rgba(16, 185, 129, 0.1)', 
            border: '1px solid rgba(16, 185, 129, 0.2)',
            color: 'var(--accent-emerald)',
            fontSize: '13px',
            marginBottom: '20px'
          }}>
            <span>✓ {success}</span>
          </div>
        )}

        {!isRegister ? (
          // LOGIN FORM
          <form onSubmit={handleLoginSubmit}>
            <div className="input-group">
              <span className="input-label">Username</span>
              <div style={{ position: 'relative' }}>
                <input 
                  type="text" 
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  placeholder="Enter username" 
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

            <div style={{ 
              textAlign: 'center', 
              marginTop: '20px',
              paddingTop: '20px',
              borderTop: '1px solid var(--border-color)'
            }}>
              <span style={{ color: 'var(--text-secondary)', fontSize: '13px' }}>
                Don&apos;t have an account?{' '}
                <button
                  type="button"
                  onClick={() => {
                    setIsRegister(true);
                    setError('');
                    setSuccess('');
                  }}
                  style={{
                    background: 'none',
                    border: 'none',
                    color: 'var(--accent-cyan)',
                    cursor: 'pointer',
                    textDecoration: 'underline',
                    fontSize: '13px',
                    fontWeight: '600'
                  }}
                >
                  Register Here
                </button>
              </span>
            </div>
          </form>
        ) : (
          // REGISTER FORM
          <form onSubmit={handleRegisterSubmit}>
            <div className="input-group">
              <span className="input-label">Username</span>
              <div style={{ position: 'relative' }}>
                <input 
                  type="text" 
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  placeholder="Enter username" 
                  required 
                  style={{ paddingLeft: '40px' }}
                />
                <UserIcon size={16} style={{ position: 'absolute', left: '14px', top: '50%', transform: 'translateY(-50%)', color: 'var(--text-muted)' }} />
              </div>
            </div>

            <div className="input-group">
              <span className="input-label">Email Address</span>
              <div style={{ position: 'relative' }}>
                <input 
                  type="email" 
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  placeholder="Enter email address" 
                  required 
                  style={{ paddingLeft: '40px' }}
                />
                <Mail size={16} style={{ position: 'absolute', left: '14px', top: '50%', transform: 'translateY(-50%)', color: 'var(--text-muted)' }} />
              </div>
            </div>

            <div className="input-group">
              <span className="input-label">Phone Number</span>
              <div style={{ position: 'relative' }}>
                <input 
                  type="tel" 
                  value={phone}
                  onChange={(e) => setPhone(e.target.value)}
                  placeholder="Enter phone number" 
                  style={{ paddingLeft: '40px' }}
                />
                <Phone size={16} style={{ position: 'absolute', left: '14px', top: '50%', transform: 'translateY(-50%)', color: 'var(--text-muted)' }} />
              </div>
            </div>

            <div className="input-group">
              <span className="input-label">Role</span>
              <div style={{ position: 'relative' }}>
                <select 
                  value={role}
                  onChange={(e) => setRole(e.target.value)}
                  style={{ paddingLeft: '40px' }}
                  required
                >
                  <option value="DISPATCHER">Dispatcher</option>
                  <option value="ADMIN">Administrator</option>
                  <option value="OPERATOR">Operator</option>
                </select>
                <Briefcase size={16} style={{ position: 'absolute', left: '14px', top: '50%', transform: 'translateY(-50%)', color: 'var(--text-muted)', pointerEvents: 'none' }} />
              </div>
            </div>

            <div className="input-group">
              <span className="input-label">Password</span>
              <div style={{ position: 'relative' }}>
                <input 
                  type="password" 
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  placeholder="Enter password" 
                  required 
                  style={{ paddingLeft: '40px' }}
                />
                <Key size={16} style={{ position: 'absolute', left: '14px', top: '50%', transform: 'translateY(-50%)', color: 'var(--text-muted)' }} />
              </div>
            </div>

            <div className="input-group" style={{ marginBottom: '28px' }}>
              <span className="input-label">Confirm Password</span>
              <div style={{ position: 'relative' }}>
                <input 
                  type="password" 
                  value={confirmPassword}
                  onChange={(e) => setConfirmPassword(e.target.value)}
                  placeholder="Confirm password" 
                  required 
                  style={{ paddingLeft: '40px' }}
                />
                <Key size={16} style={{ position: 'absolute', left: '14px', top: '50%', transform: 'translateY(-50%)', color: 'var(--text-muted)' }} />
              </div>
            </div>

            <div className="input-group" style={{ marginBottom: '28px' }}>
              <span className="input-label">Confirm Password</span>
              <div style={{ position: 'relative' }}>
                <input 
                  type="password" 
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  placeholder="Enter password" 
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
              {loading ? 'Creating Account...' : 'Create Account'}
            </button>

            <div style={{ 
              textAlign: 'center', 
              marginTop: '20px',
              paddingTop: '20px',
              borderTop: '1px solid var(--border-color)'
            }}>
              <span style={{ color: 'var(--text-secondary)', fontSize: '13px' }}>
                Already have an account?{' '}
                <button
                  type="button"
                  onClick={() => {
                    setIsRegister(false);
                    setError('');
                    setSuccess('');
                    setUsername('');
                    setPassword('');
                    setConfirmPassword('');
                    setEmail('');
                    setPhone('');
                    setRole('DISPATCHER');
                  }}
                  style={{
                    background: 'none',
                    border: 'none',
                    color: 'var(--accent-cyan)',
                    cursor: 'pointer',
                    textDecoration: 'underline',
                    fontSize: '13px',
                    fontWeight: '600'
                  }}
                >
                  Sign In
                </button>
              </span>
            </div>
          </form>
        )}
      </div>
    </div>
  );
}
