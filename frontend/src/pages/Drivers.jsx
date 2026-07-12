import React, { useEffect, useState } from 'react'
import { Users, Plus, Trash2, Edit2 } from 'lucide-react'
import { driverAPI } from '../services/api'
import '../styles/table.css'

export default function Drivers() {
  const [drivers, setDrivers] = useState([])
  const [loading, setLoading] = useState(true)
  const [showForm, setShowForm] = useState(false)
  const [formData, setFormData] = useState({
    name: '',
    licenseNumber: '',
    phone: '',
    email: '',
  })

  useEffect(() => {
    loadDrivers()
  }, [])

  const loadDrivers = async () => {
    try {
      const data = await driverAPI.getAll()
      setDrivers(Array.isArray(data) ? data : [])
    } catch (err) {
      console.error('Error loading drivers:', err)
    } finally {
      setLoading(false)
    }
  }

  const handleAddDriver = async (e) => {
    e.preventDefault()
    try {
      await driverAPI.create(formData)
      setFormData({ name: '', licenseNumber: '', phone: '', email: '' })
      setShowForm(false)
      loadDrivers()
    } catch (err) {
      console.error('Error adding driver:', err)
    }
  }

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure?')) {
      try {
        await driverAPI.delete(id)
        loadDrivers()
      } catch (err) {
        console.error('Error deleting driver:', err)
      }
    }
  }

  return (
    <div className="page-container">
      <div className="page-header">
        <div>
          <h1>Drivers</h1>
          <p>Manage your drivers</p>
        </div>
        <button className="btn btn-primary" onClick={() => setShowForm(!showForm)}>
          <Plus size={18} />
          Add Driver
        </button>
      </div>

      {showForm && (
        <div className="form-card">
          <h2>Add New Driver</h2>
          <form onSubmit={handleAddDriver}>
            <div className="form-grid">
              <input
                type="text"
                placeholder="Full Name"
                value={formData.name}
                onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                required
              />
              <input
                type="text"
                placeholder="License Number"
                value={formData.licenseNumber}
                onChange={(e) => setFormData({ ...formData, licenseNumber: e.target.value })}
                required
              />
              <input
                type="tel"
                placeholder="Phone"
                value={formData.phone}
                onChange={(e) => setFormData({ ...formData, phone: e.target.value })}
              />
              <input
                type="email"
                placeholder="Email"
                value={formData.email}
                onChange={(e) => setFormData({ ...formData, email: e.target.value })}
              />
            </div>
            <div className="form-actions">
              <button type="submit" className="btn btn-primary">Save</button>
              <button type="button" className="btn btn-secondary" onClick={() => setShowForm(false)}>Cancel</button>
            </div>
          </form>
        </div>
      )}

      {loading ? (
        <div style={{ textAlign: 'center', padding: '40px' }}>Loading...</div>
      ) : drivers.length === 0 ? (
        <div className="empty-state">
          <Users size={48} />
          <h2>No drivers</h2>
          <p>Add your first driver to get started</p>
        </div>
      ) : (
        <div className="table-container">
          <table className="data-table">
            <thead>
              <tr>
                <th>Name</th>
                <th>License Number</th>
                <th>Phone</th>
                <th>Email</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {drivers.map((driver) => (
                <tr key={driver.id}>
                  <td>{driver.name}</td>
                  <td>{driver.licenseNumber}</td>
                  <td>{driver.phone}</td>
                  <td>{driver.email}</td>
                  <td>
                    <div className="action-icons">
                      <button className="icon-btn">
                        <Edit2 size={16} />
                      </button>
                      <button className="icon-btn danger" onClick={() => handleDelete(driver.id)}>
                        <Trash2 size={16} />
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  )
}
