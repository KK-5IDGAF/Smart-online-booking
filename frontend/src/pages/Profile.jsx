import { useMutation, useQuery } from '@tanstack/react-query';
import { useEffect, useState } from 'react';
import { userApi } from '../api/services';

export default function Profile() {
  const profile = useQuery({ queryKey: ['profile'], queryFn: userApi.profile });
  const [name, setName] = useState('');
  const [saved, setSaved] = useState('');

  useEffect(() => {
    if (profile.data?.name) setName(profile.data.name);
  }, [profile.data]);

  const update = useMutation({
    mutationFn: () => userApi.updateProfile({ name }),
    onSuccess: () => setSaved('Profile updated'),
  });

  return (
    <section className="glass-card max-w-xl p-6">
      <h2 className="text-xl font-semibold">Profile</h2>
      <p className="mt-2 text-sm text-slate-500">{profile.data?.email}</p>
      <p className="text-sm uppercase text-primary">{profile.data?.role}</p>
      {saved && <p className="mt-4 text-success">{saved}</p>}
      <label className="mt-6 block text-sm">Display name</label>
      <input
        className="mt-1 w-full rounded-xl border border-slate-200 bg-white/80 px-3 py-2 dark:border-slate-700 dark:bg-slate-800"
        value={name}
        onChange={(e) => setName(e.target.value)}
      />
      <button className="mt-6 rounded-xl bg-primary px-5 py-3 text-white" onClick={() => update.mutate()}>
        Save changes
      </button>
    </section>
  );
}
