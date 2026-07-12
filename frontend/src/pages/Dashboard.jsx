import React, { useEffect, useState } from 'react'
import { useAuth } from '../context/AuthContext'
import { Activity, Truck, Users, AlertTriangle, TrendingUp } from 'lucide-react'
import { vehicleAPI, driverAPI, tripAPI, emergencyAPI } from '../services/api'
import '../styles/dashboard.css'

export default function Dashboard() {
  const { role } = useAuth()
  const [stats, setStats] = useState({
    vehicles: 0,
    drivers: 0,
    trips: 0,
    emergencies: 0,
  })
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    async function loadStats() {
      try {
        const [vehicles, drivers, trips, emergencies] = await Promise.all([
          vehicleAPI.getAll(),
          driverAPI.getAll(),
          tripAPI.getAll(),
          emergencyAPI.getAll(),
        ])
        setStats({
          vehicles: Array.isArray(vehicles) ? vehicles.length : 0,
          drivers: Array.isArray(drivers) ? drivers.length : 0,
          trips: Array.isArray(trips) ? trips.length : 0,
          emergencies: Array.isArray(emergencies) ? emergencies.length : 0,
        })
      } catch (err) {
        console.error('Error loading stats:', err)
      } finally {
        setLoading(false)
      }
    }
    loadStats()
  }, [])

  const statCards = [
    { label: 'Active Vehicles', value: stats.vehicles, icon: Truck, color: 'blue' },
    { label: 'Drivers', value: stats.drivers, icon: Users, color: 'green' },
    { label: 'Active Trips', value: stats.trips, icon: Activity, color: 'cyan' },
    { label: 'Emergencies', value: stats.emergencies, icon: AlertTriangle, color: 'red' },
  ]

  const roleDescriptions = {
    DISPATCHER: 'Track location and respond to emergencies',
    OPERATOR: 'Manage vehicles, drivers, and trips',
    ADMIN: 'Full access to all platform features',
  }

  return (
    <div className="dashboard">
      <div className="dashboard-header">
        <div>
          <h1>Welcome to Openeye</h1>
          <p>{roleDescriptions[role]}</p>
        </div>
      </div>

      <div className="stats-grid">
        {statCards.map((card) => {
          const Icon = card.icon
          return (
            <div key={card.label} className={`stat-card stat-card-${card.color}`}>
              <div className="stat-icon">
                <Icon size={28} />
              </div>
              <div className="stat-content">
                <div className="stat-label">{card.label}</div>
                <div className="stat-value">{loading ? '-' : card.value}</div>
              </div>
            </div>
          )
        })}
      </div>

      <div className="dashboard-sections">
        {role === 'DISPATCHER' && <DispatcherSection />}
        {role === 'OPERATOR' && <OperatorSection />}
        {role === 'ADMIN' && <AdminSection />}
      </div>
    </div>
  )
}

function DispatcherSection() {
  return (
    <div className="dashboard-section">
      <h2>Quick Actions</h2>
      <div className="action-buttons">
        <button className="action-btn">
          <AlertTriangle size={20} />
          <span>Report Emergency</span>
        </button>
        <button className="action-btn">
          <Activity size={20} />
          <span>Share Location</span>
        </button>
      </div>
    </div>
  )
}

function OperatorSection() {
  return (
    <div className="dashboard-section">
      <h2>Recent Activities</h2>
      <p style={{ color: 'var(--text-secondary)' }}>No recent activities</p>
    </div>
  )
}

function AdminSection() {
  return (
    <div className="dashboard-section">
      <h2>System Overview</h2>
      <div style={{ color: 'var(--text-secondary)' }}>
        <p>All systems operational</p>
        <p style={{ fontSize: '12px', marginTop: '8px' }}>Last updated: {new Date().toLocaleString()}</p>
      </div>
    </div>
  )
}
