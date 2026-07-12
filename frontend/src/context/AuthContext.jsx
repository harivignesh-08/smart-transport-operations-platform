import React, { createContext, useState, useEffect } from 'react'

export const AuthContext = createContext()

export const AuthProvider = ({ children }) => {
  const [isAuthenticated, setIsAuthenticated] = useState(false)
  const [user, setUser] = useState(null)
  const [role, setRole] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const token = localStorage.getItem('token')
    const savedRole = localStorage.getItem('role')
    const savedUsername = localStorage.getItem('username')

    if (token && savedRole) {
      setIsAuthenticated(true)
      setUser(savedUsername)
      setRole(savedRole)
    }
    setLoading(false)
  }, [])

  const login = (token, username, userRole) => {
    localStorage.setItem('token', token)
    localStorage.setItem('username', username)
    localStorage.setItem('role', userRole)
    setIsAuthenticated(true)
    setUser(username)
    setRole(userRole)
  }

  const logout = () => {
    localStorage.removeItem('token')
    localStorage.removeItem('username')
    localStorage.removeItem('role')
    setIsAuthenticated(false)
    setUser(null)
    setRole(null)
  }

  return (
    <AuthContext.Provider value={{ isAuthenticated, user, role, loading, login, logout }}>
      {children}
    </AuthContext.Provider>
  )
}

export const useAuth = () => {
  const context = React.useContext(AuthContext)
  if (!context) {
    throw new Error('useAuth must be used within AuthProvider')
  }
  return context
}
