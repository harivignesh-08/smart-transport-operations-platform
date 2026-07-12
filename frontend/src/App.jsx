import React from 'react'
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { AuthProvider } from './context/AuthContext'
import ProtectedRoute from './components/ProtectedRoute'
import Layout from './components/Layout'
import LoginPage from './pages/LoginPage'
import Dashboard from './pages/Dashboard'
import Vehicles from './pages/Vehicles'
import Drivers from './pages/Drivers'
import Location from './pages/Location'
import Emergencies from './pages/Emergencies'
import './App.css'

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/login" element={<LoginPage />} />

          <Route
            path="/dashboard"
            element={
              <ProtectedRoute>
                <Layout>
                  <Dashboard />
                </Layout>
              </ProtectedRoute>
            }
          />

          <Route
            path="/vehicles"
            element={
              <ProtectedRoute requiredRoles={['OPERATOR', 'ADMIN']}>
                <Layout>
                  <Vehicles />
                </Layout>
              </ProtectedRoute>
            }
          />

          <Route
            path="/drivers"
            element={
              <ProtectedRoute requiredRoles={['OPERATOR', 'ADMIN']}>
                <Layout>
                  <Drivers />
                </Layout>
              </ProtectedRoute>
            }
          />

          <Route
            path="/location"
            element={
              <ProtectedRoute requiredRoles={['DISPATCHER']}>
                <Layout>
                  <Location />
                </Layout>
              </ProtectedRoute>
            }
          />

          <Route
            path="/emergencies"
            element={
              <ProtectedRoute>
                <Layout>
                  <Emergencies />
                </Layout>
              </ProtectedRoute>
            }
          />

          <Route path="/" element={<Navigate to="/dashboard" />} />
          <Route path="*" element={<Navigate to="/dashboard" />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  )
}

export default App
