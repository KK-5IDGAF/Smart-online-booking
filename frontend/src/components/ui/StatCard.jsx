export default function StatCard({ label, value, accent = 'primary' }) {
  const accents = {
    primary: 'from-blue-500/20 to-blue-500/5',
    success: 'from-emerald-500/20 to-emerald-500/5',
    danger: 'from-rose-500/20 to-rose-500/5',
    slate: 'from-slate-500/20 to-slate-500/5',
  };

  return (
    <article className={`glass-card bg-gradient-to-br ${accents[accent]} p-6`}>
      <p className="text-sm text-slate-500 dark:text-slate-400">{label}</p>
      <p className="mt-3 text-3xl font-semibold">{value ?? 0}</p>
    </article>
  );
}
