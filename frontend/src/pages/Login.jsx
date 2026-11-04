import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function Login() {
  const { login } = useAuth();
  const navigate = useNavigate();
  const [form, setForm] = useState({ email: '', password: '' });
  const [error, setError] = useState('');

  const submit = async (event) => {
    event.preventDefault();
    setError('');
    try {
      await login(form);
      navigate('/app');
    } catch (err) {
      setError(err.response?.data?.message || 'Unable to sign in');
    }
  };

  return (
    <div className="flex min-h-screen items-center justify-center bg-gradient-to-br from-slate-100 to-blue-100 p-4 dark:from-slate-950 dark:to-slate-900">
      <form onSubmit={submit} className="glass-card w-full max-w-md p-8">
        <h1 className="text-2xl font-semibold">Welcome back</h1>
        <p className="mt-2 text-sm text-slate-500">Use seeded accounts or your own customer login.</p>
        {error && <p className="mt-4 rounded-xl bg-danger/10 px-3 py-2 text-sm text-danger">{error}</p>}
        <label className="mt-6 block text-sm">Email</label>
        <input
          className="mt-1 w-full rounded-xl border border-slate-200 bg-white/80 px-3 py-2 dark:border-slate-700 dark:bg-slate-800"
          type="email"
          value={form.email}
          onChange={(e) => setForm({ ...form, email: e.target.value })}
          required
        />
        <label className="mt-4 block text-sm">Password</label>
        <input
          className="mt-1 w-full rounded-xl border border-slate-200 bg-white/80 px-3 py-2 dark:border-slate-700 dark:bg-slate-800"
          type="password"
          value={form.password}
          onChange={(e) => setForm({ ...form, password: e.target.value })}
          required
        />
        <button className="mt-6 w-full rounded-xl bg-primary py-3 font-medium text-white">Login</button>
        <p className="mt-4 text-center text-sm">
          New here? <Link className="text-primary" to="/register">Register</Link>
        </p>
      </form>
    </div>
  );
}
