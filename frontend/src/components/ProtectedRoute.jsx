import React from 'react'
import { Navigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export default function ProtectedRoute({ children, requiredRoles = [] }) {
  const { isAuthenticated, role, loading } = useAuth()

  if (loading) {
    return <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', height: '100vh' }}>Loading...</div>
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" />
  }

  if (requiredRoles.length > 0 && !requiredRoles.includes(role)) {
    return <Navigate to="/unauthorized" />
  }

  return children
}
