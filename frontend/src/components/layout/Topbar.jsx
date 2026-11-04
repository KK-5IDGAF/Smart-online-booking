import { useAuth } from '../../context/AuthContext';
import { useTheme } from '../../context/ThemeContext';

export default function Topbar({ onMenu }) {
  const { user } = useAuth();
  const { theme, toggleTheme } = useTheme();

  return (
    <header className="sticky top-0 z-20 flex items-center justify-between border-b border-white/30 bg-white/50 px-4 py-4 backdrop-blur-xl dark:border-white/10 dark:bg-slate-900/50 md:px-8">
      <button className="rounded-lg border border-slate-200 px-3 py-2 lg:hidden dark:border-slate-700" onClick={onMenu}>
        Menu
      </button>
      <div>
        <p className="text-sm text-slate-500 dark:text-slate-400">Signed in as</p>
        <p className="font-semibold">
          {user?.name} <span className="text-xs uppercase text-primary">{user?.role}</span>
        </p>
      </div>
      <button
        onClick={toggleTheme}
        className="rounded-xl bg-primary px-4 py-2 text-sm font-medium text-white shadow-lg shadow-blue-500/30"
      >
        {theme === 'dark' ? 'Light mode' : 'Dark mode'}
      </button>
    </header>
  );
}
