import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { useState } from 'react';
import { resourceApi } from '../api/services';

const empty = { name: '', type: '', description: '', capacity: 1 };

export default function ResourceManagement() {
  const queryClient = useQueryClient();
  const resources = useQuery({ queryKey: ['resources'], queryFn: () => resourceApi.list() });
  const [form, setForm] = useState(empty);
  const [editingId, setEditingId] = useState(null);

  const save = useMutation({
    mutationFn: () => (editingId ? resourceApi.update(editingId, form) : resourceApi.create(form)),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['resources'] });
      setForm(empty);
      setEditingId(null);
    },
  });

  const remove = useMutation({
    mutationFn: resourceApi.remove,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['resources'] }),
  });

  return (
    <div className="grid gap-6 lg:grid-cols-[360px_1fr]">
      <form
        className="glass-card p-6"
        onSubmit={(e) => {
          e.preventDefault();
          save.mutate();
        }}
      >
        <h2 className="text-xl font-semibold">{editingId ? 'Update resource' : 'Create resource'}</h2>
        {['name', 'type', 'description'].map((field) => (
          <div key={field} className="mt-4">
            <label className="text-sm capitalize">{field}</label>
            <input
              className="mt-1 w-full rounded-xl border border-slate-200 bg-white/80 px-3 py-2 dark:border-slate-700 dark:bg-slate-800"
              value={form[field]}
              onChange={(e) => setForm({ ...form, [field]: e.target.value })}
              required={field !== 'description'}
            />
          </div>
        ))}
        <label className="mt-4 block text-sm">Capacity</label>
        <input
          type="number"
          min="1"
          className="mt-1 w-full rounded-xl border border-slate-200 bg-white/80 px-3 py-2 dark:border-slate-700 dark:bg-slate-800"
          value={form.capacity}
          onChange={(e) => setForm({ ...form, capacity: Number(e.target.value) })}
        />
        <button className="mt-6 w-full rounded-xl bg-primary py-3 text-white">Save</button>
      </form>
      <section className="glass-card p-6">
        <h2 className="text-xl font-semibold">All resources</h2>
        <div className="mt-4 space-y-3">
          {(resources.data || []).map((resource) => (
            <article key={resource.id} className="rounded-xl bg-white/60 p-4 dark:bg-slate-800/60">
              <div className="flex items-start justify-between gap-4">
                <div>
                  <p className="font-medium">{resource.name}</p>
                  <p className="text-sm text-slate-500">
                    {resource.type} · {resource.capacity} seats
                  </p>
                </div>
                <div className="flex gap-2">
                  <button
                    className="rounded-lg bg-primary px-3 py-1 text-white"
                    onClick={() => {
                      setEditingId(resource.id);
                      setForm(resource);
                    }}
                  >
                    Edit
                  </button>
                  <button className="rounded-lg bg-danger px-3 py-1 text-white" onClick={() => remove.mutate(resource.id)}>
                    Delete
                  </button>
                </div>
              </div>
            </article>
          ))}
        </div>
      </section>
    </div>
  );
}
