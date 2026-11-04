import { Link } from 'react-router-dom';

export default function Landing() {
  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-950 via-slate-900 to-blue-950 text-white">
      <header className="mx-auto flex max-w-6xl items-center justify-between px-6 py-6">
        <span className="text-lg font-semibold tracking-wide">Smart Online Booking</span>
        <div className="space-x-3">
          <Link to="/login" className="rounded-xl px-4 py-2 text-sm hover:bg-white/10">
            Login
          </Link>
          <Link to="/register" className="rounded-xl bg-primary px-4 py-2 text-sm font-medium">
            Get started
          </Link>
        </div>
      </header>
      <main className="mx-auto grid max-w-6xl gap-10 px-6 py-20 lg:grid-cols-2">
        <section>
          <p className="text-sm uppercase tracking-[0.4em] text-blue-300">Reservations without collisions</p>
          <h1 className="mt-4 text-5xl font-semibold leading-tight">
            Book halls, courts, and rooms with a single source of truth.
          </h1>
          <p className="mt-6 max-w-xl text-slate-300">
            Customers reserve available slots. Staff manage inventory. Admins see live statistics.
            Double-booking is blocked at the database transaction layer.
          </p>
          <div className="mt-8 flex gap-4">
            <Link to="/register" className="rounded-2xl bg-primary px-6 py-3 font-medium">
              Create an account
            </Link>
            <Link to="/login" className="rounded-2xl border border-white/20 px-6 py-3">
              Sign in
            </Link>
          </div>
        </section>
        <section className="grid gap-4 sm:grid-cols-2">
          {['Role-based JWT access', 'Pessimistic slot locking', 'Live admin reports', 'Extensible notifications'].map(
            (item) => (
              <div key={item} className="glass-card border-white/10 bg-white/10 p-6 text-white">
                {item}
              </div>
            )
          )}
        </section>
      </main>
    </div>
  );
}
