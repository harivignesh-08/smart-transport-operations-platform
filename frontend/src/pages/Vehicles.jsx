import React, { useEffect, useState } from 'react'
import { Truck, Plus, Trash2, Edit2 } from 'lucide-react'
import { vehicleAPI } from '../services/api'
import '../styles/table.css'

export default function Vehicles() {
  const [vehicles, setVehicles] = useState([])
  const [loading, setLoading] = useState(true)
  const [showForm, setShowForm] = useState(false)
  const [formData, setFormData] = useState({
    name: '',
    licensePlate: '',
    make: '',
    model: '',
    year: '',
    status: 'AVAILABLE',
  })

  useEffect(() => {
    loadVehicles()
  }, [])

  const loadVehicles = async () => {
    try {
      const data = await vehicleAPI.getAll()
      setVehicles(Array.isArray(data) ? data : [])
    } catch (err) {
      console.error('Error loading vehicles:', err)
    } finally {
      setLoading(false)
    }
  }

  const handleAddVehicle = async (e) => {
    e.preventDefault()
    try {
      await vehicleAPI.create(formData)
      setFormData({ name: '', licensePlate: '', make: '', model: '', year: '', status: 'AVAILABLE' })
      setShowForm(false)
      loadVehicles()
    } catch (err) {
      console.error('Error adding vehicle:', err)
    }
  }

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure?')) {
      try {
        await vehicleAPI.delete(id)
        loadVehicles()
      } catch (err) {
        console.error('Error deleting vehicle:', err)
      }
    }
  }

  return (
    <div className="page-container">
      <div className="page-header">
        <div>
          <h1>Vehicles</h1>
          <p>Manage your fleet</p>
        </div>
        <button className="btn btn-primary" onClick={() => setShowForm(!showForm)}>
          <Plus size={18} />
          Add Vehicle
        </button>
      </div>

      {showForm && (
        <div className="form-card">
          <h2>Add New Vehicle</h2>
          <form onSubmit={handleAddVehicle}>
            <div className="form-grid">
              <input
                type="text"
                placeholder="Vehicle Name"
                value={formData.name}
                onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                required
              />
              <input
                type="text"
                placeholder="License Plate"
                value={formData.licensePlate}
                onChange={(e) => setFormData({ ...formData, licensePlate: e.target.value })}
                required
              />
              <input
                type="text"
                placeholder="Make"
                value={formData.make}
                onChange={(e) => setFormData({ ...formData, make: e.target.value })}
              />
              <input
                type="text"
                placeholder="Model"
                value={formData.model}
                onChange={(e) => setFormData({ ...formData, model: e.target.value })}
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
      ) : vehicles.length === 0 ? (
        <div className="empty-state">
          <Truck size={48} />
          <h2>No vehicles</h2>
          <p>Add your first vehicle to get started</p>
        </div>
      ) : (
        <div className="table-container">
          <table className="data-table">
            <thead>
              <tr>
                <th>Name</th>
                <th>License Plate</th>
                <th>Make</th>
                <th>Model</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {vehicles.map((vehicle) => (
                <tr key={vehicle.id}>
                  <td>{vehicle.name}</td>
                  <td>{vehicle.licensePlate}</td>
                  <td>{vehicle.make}</td>
                  <td>{vehicle.model}</td>
                  <td>
                    <span className={`badge badge-${vehicle.status?.toLowerCase()}`}>
                      {vehicle.status}
                    </span>
                  </td>
                  <td>
                    <div className="action-icons">
                      <button className="icon-btn">
                        <Edit2 size={16} />
                      </button>
                      <button className="icon-btn danger" onClick={() => handleDelete(vehicle.id)}>
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
