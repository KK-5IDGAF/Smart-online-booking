/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{js,jsx}'],
  darkMode: 'class',
  theme: {
    extend: {
      colors: {
        primary: '#2563EB',
        secondary: '#1E293B',
        success: '#10B981',
        danger: '#EF4444',
      },
      boxShadow: {
        glass: '0 8px 32px rgba(15, 23, 42, 0.18)',
      },
    },
  },
  plugins: [],
};
