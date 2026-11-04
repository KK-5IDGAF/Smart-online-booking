import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { useMemo, useState } from 'react';
import { bookingApi, resourceApi, slotApi } from '../api/services';

export default function CustomerDashboard() {
  const queryClient = useQueryClient();
  const [search, setSearch] = useState('');
  const [message, setMessage] = useState('');
  const resourcesQuery = useQuery({ queryKey: ['resources', search], queryFn: () => resourceApi.list(search) });
  const slotsQuery = useQuery({ queryKey: ['slots-available'], queryFn: slotApi.available });
  const bookingsQuery = useQuery({ queryKey: ['my-bookings'], queryFn: bookingApi.mine });

  const bookMutation = useMutation({
    mutationFn: bookingApi.create,
    onSuccess: () => {
      setMessage('Booking confirmed.');
      queryClient.invalidateQueries({ queryKey: ['slots-available'] });
      queryClient.invalidateQueries({ queryKey: ['my-bookings'] });
    },
    onError: (error) => setMessage(error.response?.data?.message || 'Could not create booking'),
  });

  const cancelMutation = useMutation({
    mutationFn: bookingApi.cancel,
    onSuccess: () => {
      setMessage('Booking cancelled.');
      queryClient.invalidateQueries({ queryKey: ['slots-available'] });
      queryClient.invalidateQueries({ queryKey: ['my-bookings'] });
    },
  });

  const resourceNames = useMemo(
    () => new Map((resourcesQuery.data || []).map((item) => [item.id, item.name])),
    [resourcesQuery.data]
  );

  return (
    <div className="space-y-8">
      <section className="glass-card p-6">
        <h2 className="text-2xl font-semibold">Find a resource</h2>
        <input
          className="mt-4 w-full rounded-xl border border-slate-200 bg-white/80 px-4 py-3 dark:border-slate-700 dark:bg-slate-800"
          placeholder="Search by name or type"
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />
        <div className="mt-6 grid gap-4 md:grid-cols-3">
          {(resourcesQuery.data || []).map((resource) => (
            <article key={resource.id} className="rounded-2xl border border-white/20 bg-white/50 p-4 dark:bg-slate-800/50">
              <h3 className="font-semibold">{resource.name}</h3>
              <p className="text-sm text-slate-500">{resource.type} · capacity {resource.capacity}</p>
              <p className="mt-2 text-sm">{resource.description}</p>
            </article>
          ))}
        </div>
      </section>

      {message && <p className="rounded-xl bg-success/10 px-4 py-3 text-success">{message}</p>}

      <section className="glass-card p-6">
        <h2 className="text-2xl font-semibold">Available slots</h2>
        <div className="mt-4 overflow-x-auto">
          <table className="w-full min-w-[640px] text-left text-sm">
            <thead>
              <tr className="text-slate-500">
                <th className="py-2">Resource</th>
                <th>Date</th>
                <th>Start</th>
                <th>End</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {(slotsQuery.data || []).map((slot) => (
                <tr key={slot.id} className="border-t border-slate-200/60 dark:border-slate-700">
                  <td className="py-3">{slot.resourceName || resourceNames.get(slot.resourceId)}</td>
                  <td>{slot.slotDate}</td>
                  <td>{slot.startTime}</td>
                  <td>{slot.endTime}</td>
                  <td>
                    <button
                      className="rounded-lg bg-primary px-3 py-1.5 text-white"
                      onClick={() => bookMutation.mutate(slot.id)}
                    >
                      Book
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </section>

      <section className="glass-card p-6">
        <h2 className="text-2xl font-semibold">Booking history</h2>
        <div className="mt-4 space-y-3">
          {(bookingsQuery.data || []).map((booking) => (
            <div key={booking.id} className="flex flex-wrap items-center justify-between gap-3 rounded-xl bg-white/50 p-4 dark:bg-slate-800/50">
              <div>
                <p className="font-medium">{booking.resourceName}</p>
                <p className="text-sm text-slate-500">
                  {booking.slotDate} · {booking.startTime}-{booking.endTime} · {booking.status}
                </p>
              </div>
              {booking.status !== 'CANCELLED' && (
                <button className="rounded-lg bg-danger px-3 py-1.5 text-white" onClick={() => cancelMutation.mutate(booking.id)}>
                  Cancel
                </button>
              )}
            </div>
          ))}
        </div>
      </section>
    </div>
  );
}
