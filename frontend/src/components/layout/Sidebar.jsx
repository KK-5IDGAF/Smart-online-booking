import { NavLink } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';

const links = [
  { to: '/app/dashboard', label: 'Customer dashboard', roles: ['CUSTOMER', 'ADMIN', 'STAFF'] },
  { to: '/app/admin', label: 'Admin dashboard', roles: ['ADMIN'] },
  { to: '/app/resources', label: 'Resources', roles: ['ADMIN', 'STAFF'] },
  { to: '/app/slots', label: 'Slots', roles: ['ADMIN', 'STAFF'] },
  { to: '/app/bookings', label: 'Bookings', roles: ['ADMIN'] },
  { to: '/app/profile', label: 'Profile', roles: ['CUSTOMER', 'ADMIN', 'STAFF'] },
];

export default function Sidebar({ open, onClose }) {
  const { user, logout } = useAuth();

  return (
    <>
      {open && <div className="fixed inset-0 z-30 bg-slate-900/50 lg:hidden" onClick={onClose} />}
      <aside
        className={`fixed inset-y-0 left-0 z-40 w-72 transform border-r border-white/20 bg-secondary/95 p-6 text-white backdrop-blur-xl transition-transform lg:translate-x-0 ${
          open ? 'translate-x-0' : '-translate-x-full'
        }`}
      >
        <div className="mb-10">
          <p className="text-xs uppercase tracking-[0.3em] text-blue-200">Smart Booking</p>
          <h1 className="mt-2 text-2xl font-semibold">Control Center</h1>
        </div>
        <nav className="space-y-2">
          {links
            .filter((link) => link.roles.includes(user?.role))
            .map((link) => (
              <NavLink
                key={link.to}
                to={link.to}
                onClick={onClose}
                className={({ isActive }) =>
                  `block rounded-xl px-4 py-3 text-sm transition ${
                    isActive ? 'bg-primary text-white' : 'text-slate-200 hover:bg-white/10'
                  }`
                }
              >
                {link.label}
              </NavLink>
            ))}
        </nav>
        <button
          onClick={logout}
          className="mt-10 w-full rounded-xl border border-white/20 px-4 py-3 text-sm text-slate-200 hover:bg-white/10"
        >
          Sign out
        </button>
      </aside>
    </>
  );
}
