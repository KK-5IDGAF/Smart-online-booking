import { useQuery } from '@tanstack/react-query';
import StatCard from '../components/ui/StatCard';
import { adminApi, resourceApi, slotApi } from '../api/services';

export default function AdminDashboard() {
  const stats = useQuery({ queryKey: ['admin-stats'], queryFn: adminApi.dashboard });
  const resources = useQuery({ queryKey: ['resources'], queryFn: () => resourceApi.list() });
  const slots = useQuery({ queryKey: ['slots'], queryFn: () => slotApi.list() });
  const bookings = useQuery({ queryKey: ['admin-bookings'], queryFn: adminApi.bookings });
  const users = useQuery({ queryKey: ['admin-users'], queryFn: adminApi.users });

  const data = stats.data || {};

  return (
    <div className="space-y-8">
      <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-4">
        <StatCard label="Users" value={data.totalUsers} />
        <StatCard label="Resources" value={data.totalResources} accent="slate" />
        <StatCard label="Active bookings" value={data.activeBookings} accent="success" />
        <StatCard label="Cancelled bookings" value={data.cancelledBookings} accent="danger" />
      </div>

      <section className="glass-card p-6">
        <h2 className="text-xl font-semibold">Users</h2>
        <Table
          headers={['Name', 'Email', 'Role']}
          rows={(users.data || []).map((item) => [item.name, item.email, item.role])}
        />
      </section>
      <section className="glass-card p-6">
        <h2 className="text-xl font-semibold">Resources</h2>
        <Table
          headers={['Name', 'Type', 'Capacity']}
          rows={(resources.data || []).map((item) => [item.name, item.type, item.capacity])}
        />
      </section>
      <section className="glass-card p-6">
        <h2 className="text-xl font-semibold">Slots</h2>
        <Table
          headers={['Resource', 'Date', 'Start', 'End', 'Available']}
          rows={(slots.data || []).map((item) => [
            item.resourceName,
            item.slotDate,
            item.startTime,
            item.endTime,
            item.available ? 'Yes' : 'No',
          ])}
        />
      </section>
      <section className="glass-card p-6">
        <h2 className="text-xl font-semibold">Bookings</h2>
        <Table
          headers={['User', 'Resource', 'Date', 'Status']}
          rows={(bookings.data || []).map((item) => [item.userName, item.resourceName, item.slotDate, item.status])}
        />
      </section>
    </div>
  );
}

function Table({ headers, rows }) {
  return (
    <div className="mt-4 overflow-x-auto">
      <table className="w-full min-w-[640px] text-left text-sm">
        <thead>
          <tr className="text-slate-500">
            {headers.map((header) => (
              <th key={header} className="py-2">
                {header}
              </th>
            ))}
          </tr>
        </thead>
        <tbody>
          {rows.map((row, index) => (
            <tr key={index} className="border-t border-slate-200/60 dark:border-slate-700">
              {row.map((cell, cellIndex) => (
                <td key={cellIndex} className="py-3">
                  {cell}
                </td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
