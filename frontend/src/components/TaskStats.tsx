import { TaskStats } from '../api/taskApi'

interface TaskStatsProps {
  stats: TaskStats
}

const statCards = [
  { key: 'total', label: 'Tổng cộng', valueKey: 'totalTasks' as const, color: 'from-slate-600 to-slate-800', bg: 'bg-slate-50' },
  { key: 'completed', label: 'Hoàn thành', valueKey: 'completedTasks' as const, color: 'from-emerald-500 to-teal-600', bg: 'bg-emerald-50' },
  { key: 'pending', label: 'Đang chờ', valueKey: 'pendingTasks' as const, color: 'from-amber-500 to-orange-500', bg: 'bg-amber-50' },
]

export default function TaskStatsComponent({ stats }: TaskStatsProps) {
  return (
    <div className="mb-8 grid grid-cols-1 gap-4 sm:grid-cols-3">
      {statCards.map((card) => (
        <div
          key={card.key}
          className={`rounded-2xl border border-white/80 ${card.bg} p-5 shadow-sm backdrop-blur-sm`}
        >
          <p className="text-sm font-medium text-slate-500">{card.label}</p>
          <p className={`mt-2 text-3xl font-bold bg-gradient-to-r ${card.color} bg-clip-text text-transparent`}>
            {stats[card.valueKey]}
          </p>
        </div>
      ))}
    </div>
  )
}
