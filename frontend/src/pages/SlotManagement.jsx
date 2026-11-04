import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { useState } from 'react';
import { resourceApi, slotApi } from '../api/services';

const empty = { resourceId: '', slotDate: '', startTime: '', endTime: '', available: true };

export default function SlotManagement() {
  const queryClient = useQueryClient();
  const resources = useQuery({ queryKey: ['resources'], queryFn: () => resourceApi.list() });
  const slots = useQuery({ queryKey: ['slots'], queryFn: () => slotApi.list() });
  const [form, setForm] = useState(empty);
  const [editingId, setEditingId] = useState(null);

  const save = useMutation({
    mutationFn: () => {
      const payload = { ...form, resourceId: Number(form.resourceId) };
      return editingId ? slotApi.update(editingId, payload) : slotApi.create(payload);
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['slots'] });
      setForm(empty);
      setEditingId(null);
    },
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
        <h2 className="text-xl font-semibold">{editingId ? 'Update slot' : 'Create slot'}</h2>
        <label className="mt-4 block text-sm">Resource</label>
        <select
          className="mt-1 w-full rounded-xl border border-slate-200 bg-white/80 px-3 py-2 dark:border-slate-700 dark:bg-slate-800"
          value={form.resourceId}
          onChange={(e) => setForm({ ...form, resourceId: e.target.value })}
          required
        >
          <option value="">Select</option>
          {(resources.data || []).map((resource) => (
            <option key={resource.id} value={resource.id}>
              {resource.name}
            </option>
          ))}
        </select>
        <label className="mt-4 block text-sm">Date</label>
        <input
          type="date"
          className="mt-1 w-full rounded-xl border border-slate-200 bg-white/80 px-3 py-2 dark:border-slate-700 dark:bg-slate-800"
          value={form.slotDate}
          onChange={(e) => setForm({ ...form, slotDate: e.target.value })}
          required
        />
        <label className="mt-4 block text-sm">Start</label>
        <input
          type="time"
          className="mt-1 w-full rounded-xl border border-slate-200 bg-white/80 px-3 py-2 dark:border-slate-700 dark:bg-slate-800"
          value={form.startTime}
          onChange={(e) => setForm({ ...form, startTime: e.target.value })}
          required
        />
        <label className="mt-4 block text-sm">End</label>
        <input
          type="time"
          className="mt-1 w-full rounded-xl border border-slate-200 bg-white/80 px-3 py-2 dark:border-slate-700 dark:bg-slate-800"
          value={form.endTime}
          onChange={(e) => setForm({ ...form, endTime: e.target.value })}
          required
        />
        <label className="mt-4 flex items-center gap-2 text-sm">
          <input
            type="checkbox"
            checked={form.available}
            onChange={(e) => setForm({ ...form, available: e.target.checked })}
          />
          Available
        </label>
        <button className="mt-6 w-full rounded-xl bg-primary py-3 text-white">Save slot</button>
      </form>
      <section className="glass-card overflow-x-auto p-6">
        <h2 className="text-xl font-semibold">Slots</h2>
        <table className="mt-4 w-full min-w-[720px] text-left text-sm">
          <thead>
            <tr className="text-slate-500">
              <th className="py-2">Resource</th>
              <th>Date</th>
              <th>Window</th>
              <th>Status</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            {(slots.data || []).map((slot) => (
              <tr key={slot.id} className="border-t border-slate-200/60 dark:border-slate-700">
                <td className="py-3">{slot.resourceName}</td>
                <td>{slot.slotDate}</td>
                <td>
                  {slot.startTime} - {slot.endTime}
                </td>
                <td>{slot.available ? 'Open' : 'Taken'}</td>
                <td>
                  <button
                    className="rounded-lg bg-primary px-3 py-1 text-white"
                    onClick={() => {
                      setEditingId(slot.id);
                      setForm({
                        resourceId: String(slot.resourceId),
                        slotDate: slot.slotDate,
                        startTime: slot.startTime?.slice(0, 5),
                        endTime: slot.endTime?.slice(0, 5),
                        available: slot.available,
                      });
                    }}
                  >
                    Edit
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </section>
    </div>
  );
}
