import React from 'react'
import { useAuth } from '../context/AuthContext'
import Sidebar from './Sidebar'
import '../styles/layout.css'

export default function Layout({ children }) {
  const { user, role } = useAuth()

  return (
    <div className="layout">
      <Sidebar />
      <main className="main-content">
        <header className="top-bar">
          <h1>Openeye</h1>
          <div className="user-info">
            <span className="user-name">{user}</span>
            <span className="user-role">{role}</span>
          </div>
        </header>
        <div className="content">
          {children}
        </div>
      </main>
    </div>
  )
}
