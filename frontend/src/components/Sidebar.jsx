import React, { useState } from 'react'
import { NavLink } from 'react-router-dom'
import { Menu, X, LogOut, MapPin, Package, Users, AlertTriangle, BarChart3, Activity } from 'lucide-react'
import { useAuth } from '../context/AuthContext'
import '../styles/sidebar.css'

export default function Sidebar() {
  const [isOpen, setIsOpen] = useState(false)
  const { role, logout } = useAuth()

  const menuItems = {
    DISPATCHER: [
      { label: 'Dashboard', path: '/dashboard', icon: Activity },
      { label: 'My Location', path: '/location', icon: MapPin },
      { label: 'Emergencies', path: '/emergencies', icon: AlertTriangle },
    ],
    OPERATOR: [
      { label: 'Dashboard', path: '/dashboard', icon: Activity },
      { label: 'Vehicles', path: '/vehicles', icon: Package },
      { label: 'Drivers', path: '/drivers', icon: Users },
      { label: 'Trips', path: '/trips', icon: MapPin },
      { label: 'Tracking', path: '/tracking', icon: MapPin },
      { label: 'Analytics', path: '/analytics', icon: BarChart3 },
      { label: 'Emergencies', path: '/emergencies', icon: AlertTriangle },
    ],
    ADMIN: [
      { label: 'Dashboard', path: '/dashboard', icon: Activity },
      { label: 'Vehicles', path: '/vehicles', icon: Package },
      { label: 'Drivers', path: '/drivers', icon: Users },
      { label: 'Trips', path: '/trips', icon: MapPin },
      { label: 'Tracking', path: '/tracking', icon: MapPin },
      { label: 'Users', path: '/users', icon: Users },
      { label: 'Analytics', path: '/analytics', icon: BarChart3 },
      { label: 'Emergencies', path: '/emergencies', icon: AlertTriangle },
    ],
  }

  const items = menuItems[role] || []

  return (
    <>
      <button className="sidebar-toggle" onClick={() => setIsOpen(!isOpen)}>
        {isOpen ? <X size={24} /> : <Menu size={24} />}
      </button>

      <aside className={`sidebar ${isOpen ? 'open' : ''}`}>
        <div className="sidebar-header">
          <h2>Openeye</h2>
        </div>

        <nav className="sidebar-nav">
          {items.map((item) => {
            const Icon = item.icon
            return (
              <NavLink
                key={item.path}
                to={item.path}
                className={({ isActive }) => `nav-link ${isActive ? 'active' : ''}`}
                onClick={() => setIsOpen(false)}
              >
                <Icon size={18} />
                <span>{item.label}</span>
              </NavLink>
            )
          })}
        </nav>

        <div className="sidebar-footer">
          <button className="logout-btn" onClick={() => {
            logout()
            setIsOpen(false)
          }}>
            <LogOut size={18} />
            <span>Logout</span>
          </button>
        </div>
      </aside>

      {isOpen && <div className="sidebar-overlay" onClick={() => setIsOpen(false)} />}
    </>
  )
}
