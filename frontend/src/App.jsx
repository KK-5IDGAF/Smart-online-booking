import { Navigate, Route, Routes } from 'react-router-dom';
import { useAuth } from './context/AuthContext';
import AppLayout from './components/layout/AppLayout';
import Landing from './pages/Landing';
import Login from './pages/Login';
import Register from './pages/Register';
import CustomerDashboard from './pages/CustomerDashboard';
import AdminDashboard from './pages/AdminDashboard';
import ResourceManagement from './pages/ResourceManagement';
import SlotManagement from './pages/SlotManagement';
import BookingManagement from './pages/BookingManagement';
import Profile from './pages/Profile';

function Protected({ children, roles }) {
  const { isAuthenticated, user } = useAuth();
  if (!isAuthenticated) return <Navigate to="/login" replace />;
  if (roles && !roles.includes(user?.role)) return <Navigate to="/app" replace />;
  return children;
}

function HomeRedirect() {
  const { user } = useAuth();
  if (user?.role === 'ADMIN') return <Navigate to="/app/admin" replace />;
  if (user?.role === 'STAFF') return <Navigate to="/app/resources" replace />;
  return <Navigate to="/app/dashboard" replace />;
}

export default function App() {
  return (
    <Routes>
      <Route path="/" element={<Landing />} />
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />
      <Route
        path="/app"
        element={
          <Protected>
            <AppLayout />
          </Protected>
        }
      >
        <Route index element={<HomeRedirect />} />
        <Route path="dashboard" element={<CustomerDashboard />} />
        <Route path="profile" element={<Profile />} />
        <Route
          path="admin"
          element={
            <Protected roles={['ADMIN']}>
              <AdminDashboard />
            </Protected>
          }
        />
        <Route
          path="resources"
          element={
            <Protected roles={['ADMIN', 'STAFF']}>
              <ResourceManagement />
            </Protected>
          }
        />
        <Route
          path="slots"
          element={
            <Protected roles={['ADMIN', 'STAFF']}>
              <SlotManagement />
            </Protected>
          }
        />
        <Route
          path="bookings"
          element={
            <Protected roles={['ADMIN']}>
              <BookingManagement />
            </Protected>
          }
        />
      </Route>
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}
