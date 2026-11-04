import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function Register() {
  const { register } = useAuth();
  const navigate = useNavigate();
  const [form, setForm] = useState({ name: '', email: '', password: '' });
  const [error, setError] = useState('');

  const submit = async (event) => {
    event.preventDefault();
    setError('');
    try {
      await register(form);
      navigate('/app');
    } catch (err) {
      setError(err.response?.data?.message || 'Unable to register');
    }
  };

  return (
    <div className="flex min-h-screen items-center justify-center bg-gradient-to-br from-slate-100 to-blue-100 p-4 dark:from-slate-950 dark:to-slate-900">
      <form onSubmit={submit} className="glass-card w-full max-w-md p-8">
        <h1 className="text-2xl font-semibold">Create your account</h1>
        {error && <p className="mt-4 rounded-xl bg-danger/10 px-3 py-2 text-sm text-danger">{error}</p>}
        <label className="mt-6 block text-sm">Name</label>
        <input
          className="mt-1 w-full rounded-xl border border-slate-200 bg-white/80 px-3 py-2 dark:border-slate-700 dark:bg-slate-800"
          value={form.name}
          onChange={(e) => setForm({ ...form, name: e.target.value })}
          required
        />
        <label className="mt-4 block text-sm">Email</label>
        <input
          className="mt-1 w-full rounded-xl border border-slate-200 bg-white/80 px-3 py-2 dark:border-slate-700 dark:bg-slate-800"
          type="email"
          value={form.email}
          onChange={(e) => setForm({ ...form, email: e.target.value })}
          required
        />
        <label className="mt-4 block text-sm">Password (min 8 characters)</label>
        <input
          className="mt-1 w-full rounded-xl border border-slate-200 bg-white/80 px-3 py-2 dark:border-slate-700 dark:bg-slate-800"
          type="password"
          minLength={8}
          value={form.password}
          onChange={(e) => setForm({ ...form, password: e.target.value })}
          required
        />
        <button className="mt-6 w-full rounded-xl bg-primary py-3 font-medium text-white">Register</button>
        <p className="mt-4 text-center text-sm">
          Already registered? <Link className="text-primary" to="/login">Login</Link>
        </p>
      </form>
    </div>
  );
}
