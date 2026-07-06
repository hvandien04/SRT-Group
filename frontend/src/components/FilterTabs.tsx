
interface FilterTabsProps {
  filter: 'all' | 'pending' | 'completed'
  onChange: (filter: 'all' | 'pending' | 'completed') => void
}

export default function FilterTabs({ filter, onChange }: FilterTabsProps) {
  const tabs = [
    { value: 'all' as const, label: 'Tất cả' },
    { value: 'pending' as const, label: 'Đang chờ' },
    { value: 'completed' as const, label: 'Hoàn thành' },
  ]

  return (
    <div className="mb-6 flex gap-2 rounded-xl bg-white/60 p-1.5 shadow-sm backdrop-blur-sm">
      {tabs.map((tab) => (
        <button
          key={tab.value}
          onClick={() => onChange(tab.value)}
          className={`flex-1 rounded-lg px-4 py-2.5 text-sm font-medium transition-all ${
            filter === tab.value
              ? 'bg-gradient-to-r from-indigo-600 to-purple-600 text-white shadow-md'
              : 'text-slate-600 hover:bg-white/80'
          }`}
        >
          {tab.label}
        </button>
      ))}
    </div>
  )
}
