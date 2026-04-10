import { NavLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function Sidebar() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <aside className="sidebar">
      <div className="sidebar-logo">
        <h1>PlaceMentor</h1>
        <span>Adaptive Learning Platform</span>
      </div>

      <nav className="sidebar-nav">
        <NavLink to="/dashboard" className={({ isActive }) => isActive ? 'active' : ''}>
          <span className="nav-icon">📊</span>
          Dashboard
        </NavLink>
        <NavLink to="/test" className={({ isActive }) => isActive ? 'active' : ''}>
          <span className="nav-icon">📝</span>
          Take Test
        </NavLink>
        <NavLink to="/performance" className={({ isActive }) => isActive ? 'active' : ''}>
          <span className="nav-icon">📈</span>
          Performance
        </NavLink>
        <NavLink to="/roadmap" className={({ isActive }) => isActive ? 'active' : ''}>
          <span className="nav-icon">🗺️</span>
          Roadmap
        </NavLink>
      </nav>

      <div className="sidebar-footer">
        <div style={{ padding: '12px 16px', fontSize: '0.85rem', color: 'var(--text-secondary)', marginBottom: '8px' }}>
          <div style={{ fontWeight: 600, color: 'var(--text-primary)' }}>{user?.name}</div>
          <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>{user?.email}</div>
        </div>
        <button onClick={handleLogout} style={{ color: 'var(--danger)' }}>
          <span className="nav-icon">🚪</span>
          Logout
        </button>
      </div>
    </aside>
  );
}
