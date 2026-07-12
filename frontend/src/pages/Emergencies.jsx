import React, { useEffect, useState } from 'react'
import { AlertTriangle, Plus } from 'lucide-react'
import { emergencyAPI, trackingAPI } from '../services/api'
import { useAuth } from '../context/AuthContext'
import '../styles/emergencies.css'

export default function Emergencies() {
  const { role } = useAuth()
  const [emergencies, setEmergencies] = useState([])
  const [loading, setLoading] = useState(true)
  const [showForm, setShowForm] = useState(false)
  const [formData, setFormData] = useState({
    description: '',
    severity: 'MEDIUM',
    location: '',
  })

  useEffect(() => {
    loadEmergencies()
  }, [])

  const loadEmergencies = async () => {
    try {
      const data = await emergencyAPI.getAll()
      setEmergencies(Array.isArray(data) ? data : [])
    } catch (err) {
      console.error('Error loading emergencies:', err)
    } finally {
      setLoading(false)
    }
  }

  const handleReportEmergency = async (e) => {
    e.preventDefault()

    // Get current location if dispatcher
    let locationData = { description: formData.description, severity: formData.severity }

    if (role === 'DISPATCHER') {
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition((position) => {
          locationData.latitude = position.coords.latitude
          locationData.longitude = position.coords.longitude
        })
      }
    }

    try {
      await emergencyAPI.create(locationData)
      setFormData({ description: '', severity: 'MEDIUM', location: '' })
      setShowForm(false)
      loadEmergencies()
    } catch (err) {
      console.error('Error reporting emergency:', err)
    }
  }

  const severityColor = {
    LOW: '#10b981',
    MEDIUM: '#f59e0b',
    HIGH: '#ef4444',
  }

  return (
    <div className="page-container">
      <div className="page-header">
        <div>
          <h1>Emergencies</h1>
          <p>Incident management and alerts</p>
        </div>
        {(role === 'DISPATCHER' || role === 'OPERATOR') && (
          <button className="btn btn-primary" onClick={() => setShowForm(!showForm)}>
            <Plus size={18} />
            Report Emergency
          </button>
        )}
      </div>

      {showForm && (
        <div className="form-card">
          <h2>Report Emergency</h2>
          <form onSubmit={handleReportEmergency}>
            <div className="form-group">
              <label>Description</label>
              <textarea
                placeholder="Describe the emergency..."
                value={formData.description}
                onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                required
                rows={4}
              />
            </div>
            <div className="form-group">
              <label>Severity</label>
              <select
                value={formData.severity}
                onChange={(e) => setFormData({ ...formData, severity: e.target.value })}
              >
                <option value="LOW">Low</option>
                <option value="MEDIUM">Medium</option>
                <option value="HIGH">High</option>
              </select>
            </div>
            <div className="form-actions">
              <button type="submit" className="btn btn-primary">Report</button>
              <button type="button" className="btn btn-secondary" onClick={() => setShowForm(false)}>Cancel</button>
            </div>
          </form>
        </div>
      )}

      {loading ? (
        <div style={{ textAlign: 'center', padding: '40px' }}>Loading...</div>
      ) : emergencies.length === 0 ? (
        <div className="empty-state">
          <AlertTriangle size={48} />
          <h2>No emergencies</h2>
          <p>All clear - no active emergencies</p>
        </div>
      ) : (
        <div className="emergencies-list">
          {emergencies.map((emergency) => (
            <div key={emergency.id} className="emergency-card">
              <div className="emergency-header">
                <div className="emergency-title">
                  <AlertTriangle size={20} color={severityColor[emergency.severity]} />
                  <div>
                    <h3>Emergency #{emergency.id}</h3>
                    <p>{new Date(emergency.createdAt).toLocaleString()}</p>
                  </div>
                </div>
                <span
                  className="severity-badge"
                  style={{ borderColor: severityColor[emergency.severity] }}
                >
                  {emergency.severity}
                </span>
              </div>
              <p className="emergency-description">{emergency.description}</p>
              {emergency.latitude && emergency.longitude && (
                <div className="emergency-location">
                  <span>Location: {emergency.latitude.toFixed(4)}, {emergency.longitude.toFixed(4)}</span>
                </div>
              )}
              <div className="emergency-status">
                <span className={`status-badge status-${emergency.status?.toLowerCase()}`}>
                  {emergency.status}
                </span>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}
