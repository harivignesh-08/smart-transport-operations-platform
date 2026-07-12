import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { LogIn, AlertCircle, CheckCircle, Lock, User, Mail, Phone, Briefcase } from 'lucide-react'
import { authAPI } from '../services/api'
import { useAuth } from '../context/AuthContext'
import '../styles/auth.css'

export default function LoginPage() {
  const navigate = useNavigate()
  const { login } = useAuth()
  const [isRegister, setIsRegister] = useState(false)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')

  const [loginForm, setLoginForm] = useState({ username: '', password: '' })
  const [registerForm, setRegisterForm] = useState({
    username: '',
    email: '',
    phone: '',
    password: '',
    confirmPassword: '',
    role: 'OPERATOR',
  })

  const handleLogin = async (e) => {
    e.preventDefault()
    setError('')
    setLoading(true)

    try {
      const response = await authAPI.login(loginForm.username, loginForm.password)
      login(response.token, response.username, response.role || 'OPERATOR')
      navigate('/dashboard')
    } catch (err) {
      setError(err || 'Login failed')
    } finally {
      setLoading(false)
    }
  }

  const handleRegister = async (e) => {
    e.preventDefault()
    setError('')
    setSuccess('')

    if (registerForm.password !== registerForm.confirmPassword) {
      setError('Passwords do not match')
      return
    }

    if (registerForm.password.length < 6) {
      setError('Password must be at least 6 characters')
      return
    }

    setLoading(true)

    try {
      await authAPI.register({
        username: registerForm.username,
        email: registerForm.email,
        phone: registerForm.phone,
        password: registerForm.password,
        role: registerForm.role,
      })
      setSuccess('Account created! Please log in.')
      setTimeout(() => {
        setIsRegister(false)
        setRegisterForm({
          username: '',
          email: '',
          phone: '',
          password: '',
          confirmPassword: '',
          role: 'OPERATOR',
        })
      }, 2000)
    } catch (err) {
      setError(err || 'Registration failed')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="auth-container">
      <div className="auth-background" />
      <div className="auth-card">
        <div className="auth-header">
          <div className="auth-icon">
            <LogIn size={32} />
          </div>
          <h1>{isRegister ? 'Create Account' : 'Welcome to Openeye'}</h1>
          <p>{isRegister ? 'Join the fleet operations platform' : 'Smart Fleet Operations Platform'}</p>
        </div>

        {error && (
          <div className="alert alert-error">
            <AlertCircle size={16} />
            <span>{error}</span>
          </div>
        )}

        {success && (
          <div className="alert alert-success">
            <CheckCircle size={16} />
            <span>{success}</span>
          </div>
        )}

        {!isRegister ? (
          <form onSubmit={handleLogin} className="auth-form">
            <div className="form-group">
              <label>Username</label>
              <div className="input-wrapper">
                <User size={16} />
                <input
                  type="text"
                  placeholder="Enter username"
                  value={loginForm.username}
                  onChange={(e) => setLoginForm({ ...loginForm, username: e.target.value })}
                  required
                />
              </div>
            </div>

            <div className="form-group">
              <label>Password</label>
              <div className="input-wrapper">
                <Lock size={16} />
                <input
                  type="password"
                  placeholder="Enter password"
                  value={loginForm.password}
                  onChange={(e) => setLoginForm({ ...loginForm, password: e.target.value })}
                  required
                />
              </div>
            </div>

            <button type="submit" className="btn btn-primary btn-full" disabled={loading}>
              {loading ? 'Logging in...' : 'Login'}
            </button>

            <div className="auth-toggle">
              <span>Don&apos;t have an account?</span>
              <button
                type="button"
                className="link-btn"
                onClick={() => setIsRegister(true)}
              >
                Register
              </button>
            </div>
          </form>
        ) : (
          <form onSubmit={handleRegister} className="auth-form">
            <div className="form-group">
              <label>Username</label>
              <div className="input-wrapper">
                <User size={16} />
                <input
                  type="text"
                  placeholder="Choose username"
                  value={registerForm.username}
                  onChange={(e) => setRegisterForm({ ...registerForm, username: e.target.value })}
                  required
                />
              </div>
            </div>

            <div className="form-group">
              <label>Email</label>
              <div className="input-wrapper">
                <Mail size={16} />
                <input
                  type="email"
                  placeholder="Enter email"
                  value={registerForm.email}
                  onChange={(e) => setRegisterForm({ ...registerForm, email: e.target.value })}
                  required
                />
              </div>
            </div>

            <div className="form-group">
              <label>Phone</label>
              <div className="input-wrapper">
                <Phone size={16} />
                <input
                  type="tel"
                  placeholder="Enter phone number"
                  value={registerForm.phone}
                  onChange={(e) => setRegisterForm({ ...registerForm, phone: e.target.value })}
                />
              </div>
            </div>

            <div className="form-group">
              <label>Role</label>
              <div className="input-wrapper">
                <Briefcase size={16} />
                <select
                  value={registerForm.role}
                  onChange={(e) => setRegisterForm({ ...registerForm, role: e.target.value })}
                >
                  <option value="DISPATCHER">Dispatcher</option>
                  <option value="OPERATOR">Operator</option>
                  <option value="ADMIN">Administrator</option>
                </select>
              </div>
            </div>

            <div className="form-group">
              <label>Password</label>
              <div className="input-wrapper">
                <Lock size={16} />
                <input
                  type="password"
                  placeholder="Enter password"
                  value={registerForm.password}
                  onChange={(e) => setRegisterForm({ ...registerForm, password: e.target.value })}
                  required
                />
              </div>
            </div>

            <div className="form-group">
              <label>Confirm Password</label>
              <div className="input-wrapper">
                <Lock size={16} />
                <input
                  type="password"
                  placeholder="Confirm password"
                  value={registerForm.confirmPassword}
                  onChange={(e) => setRegisterForm({ ...registerForm, confirmPassword: e.target.value })}
                  required
                />
              </div>
            </div>

            <button type="submit" className="btn btn-primary btn-full" disabled={loading}>
              {loading ? 'Creating account...' : 'Create Account'}
            </button>

            <div className="auth-toggle">
              <span>Already have an account?</span>
              <button
                type="button"
                className="link-btn"
                onClick={() => setIsRegister(false)}
              >
                Login
              </button>
            </div>
          </form>
        )}
      </div>
    </div>
  )
}
