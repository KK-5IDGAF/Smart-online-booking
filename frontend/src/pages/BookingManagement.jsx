import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { adminApi, bookingApi } from '../api/services';

export default function BookingManagement() {
  const queryClient = useQueryClient();
  const bookings = useQuery({ queryKey: ['admin-bookings'], queryFn: adminApi.bookings });
  const cancel = useMutation({
    mutationFn: bookingApi.cancel,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['admin-bookings'] }),
  });

  return (
    <section className="glass-card overflow-x-auto p-6">
      <h2 className="text-xl font-semibold">All bookings</h2>
      <table className="mt-4 w-full min-w-[800px] text-left text-sm">
        <thead>
          <tr className="text-slate-500">
            <th className="py-2">User</th>
            <th>Resource</th>
            <th>Date</th>
            <th>Window</th>
            <th>Status</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          {(bookings.data || []).map((booking) => (
            <tr key={booking.id} className="border-t border-slate-200/60 dark:border-slate-700">
              <td className="py-3">{booking.userName}</td>
              <td>{booking.resourceName}</td>
              <td>{booking.slotDate}</td>
              <td>
                {booking.startTime} - {booking.endTime}
              </td>
              <td>{booking.status}</td>
              <td>
                {booking.status !== 'CANCELLED' && (
                  <button className="rounded-lg bg-danger px-3 py-1 text-white" onClick={() => cancel.mutate(booking.id)}>
                    Cancel
                  </button>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </section>
  );
}
